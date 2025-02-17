package services;

import models.User;
import java.sql.*;
import java.util.*;
import exceptions.*;
import database.Database;
import database.DataConnect;
import dto.CreateUserDTO;
import dto.UpdateUserDTO;
import dto.GetUserListDto;

public class UserService {

    // Tüm Kullanıcıları Getir (READ - GET ALL) 
    public static List<GetUserListDto> getAllUsers() throws DatabaseException {
        // Kullanıcı listesini tutacak bir liste oluşturur
        List<GetUserListDto> users = new ArrayList<>();
        String sql = "SELECT id, name, surname, email, gender, phone, address FROM users WHERE deleted = FALSE";

        // Veritabanından kullanıcıları çeker ve listeye ekler
        try (Connection conn = DataConnect.getConnection();
            /* Aşağıdaki kod satırının açıklaması:
            1. SQL sorgusunu çalıştırır
            2. Sonuç kümesini döndürür
            3. Sonuç kümesindeki her bir satırı dolaşır
            4. Her bir satırı GetUserListDto nesnesine dönüştürür
            5. Nesneyi listeye ekler */

            // PreparedStatement, SQL sorgularını çalıştırır
             PreparedStatement stmt = conn.prepareStatement(sql);
            // ResultSet, veritabanından alınan verileri tutar
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(new GetUserListDto(
                    UUID.fromString(rs.getString("id")),
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getString("email"),
                    rs.getString("gender"),
                    rs.getString("phone"),
                    rs.getString("address")
                ));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Veritabanı hatası: " + e.getMessage());
        }
        //System.out.println("users: " +users);
        return users;
    }

    // Kullanıcı Ekleme (CREATE)
    public static boolean addUser(CreateUserDTO dto) throws InvalidInputException, DatabaseException, UserAlreadyExistsException {
        // Eksik veya geçersiz giriş verisi varsa InvalidInputException hatası fırlatılır
        if (dto.getName() == null || dto.getSurname() == null || dto.getEmail() == null || dto.getPassword() == null) {
            throw new InvalidInputException("Eksik veya geçersiz giriş verisi!");
        }

        String sql = "INSERT INTO users (id, name, surname, email, password, gender, phone, address) VALUES (gen_random_uuid(), ?, ?, ?, ?, ?, ?, ?)";
        /* Aşağıdaki kod satırının açıklaması:
        1. SQL sorgusunu çalıştırır
        2. Parametreleri set eder
        3. Sorguyu çalıştırır
        4. Eğer sorgu başarılı ise true döndürür
        5. Eğer sorgu başarısız ise UserAlreadyExistsException hatası fırlatılır
        6. Eğer veritabanı hatası oluşursa DatabaseException hatası fırlatılır */
        try (Connection conn = DataConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dto.getName());
            stmt.setString(2, dto.getSurname());
            stmt.setString(3, dto.getEmail());
            stmt.setString(4, dto.getPassword());
            stmt.setString(5, dto.getGender());
            stmt.setString(6, dto.getPhone());
            stmt.setString(7, dto.getAddress());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new UserAlreadyExistsException("Bu e-posta adresi zaten kayıtlı: " + dto.getEmail());
            }
            throw new DatabaseException("Veritabanı hatası: " + e.getMessage());
        }
    }

    // ID'ye Göre Kullanıcı Getir (READ - GET BY ID)
    public static User getUserById(UUID id) throws UserNotFoundException, DatabaseException {
        String sql = "SELECT id, name, surname, email, gender, phone, address FROM users WHERE id = ? AND deleted = FALSE";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DataConnect.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setObject(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                    UUID.fromString(rs.getString("id")),
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getString("email"),
                    "",
                    rs.getString("gender"),
                    rs.getString("phone"),
                    rs.getString("address")
                );
            } else {
                throw new UserNotFoundException("Kullanıcı bulunamadı: " + id);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Veritabanı hatası: " + e.getMessage());
        } finally {
            DataConnect.closeResources(conn, stmt, rs);
        }
    }

    // Kullanıcı Güncelleme (UPDATE)
    public static boolean updateUser(UUID id, UpdateUserDTO dto) 
        throws DatabaseException, UserNotFoundException, UserAlreadyExistsException {

        String sqlCheckEmail = "SELECT id FROM users WHERE email = ? AND id != ? AND deleted = FALSE";
        String sqlUpdate = "UPDATE users SET name = ?, surname = ?, email = ?, gender = ?, phone = ?, address = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ? AND deleted = FALSE";

        try (Connection conn = DataConnect.getConnection();
            PreparedStatement stmtCheckEmail = conn.prepareStatement(sqlCheckEmail);
            PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {

            stmtCheckEmail.setString(1, dto.getEmail());
            stmtCheckEmail.setObject(2, id);
            try (ResultSet rs = stmtCheckEmail.executeQuery()) {
                if (rs.next()) {
                    throw new UserAlreadyExistsException("Bu e-posta adresi zaten kayıtlı: " + dto.getEmail());
                }
            }

            // Kullanıcı bilgilerini güncelle
            stmtUpdate.setString(1, dto.getName());
            stmtUpdate.setString(2, dto.getSurname());
            stmtUpdate.setString(3, dto.getEmail());
            stmtUpdate.setString(4, dto.getGender());
            stmtUpdate.setString(5, dto.getPhone());
            stmtUpdate.setString(6, dto.getAddress());
            stmtUpdate.setObject(7, id);

            int affectedRows = stmtUpdate.executeUpdate();
            if (affectedRows > 0) {
                return true;
            } else {
                throw new UserNotFoundException("Kullanıcı bulunamadı: " + id);
            }

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new UserAlreadyExistsException("Bu e-posta adresi zaten kayıtlı: " + dto.getEmail());
            }
            throw new DatabaseException("Veritabanı hatası: " + e.getMessage());
        }
    }

    // Kullanıcı Silme (DELETE - Soft Delete)
    public static boolean deleteUser(UUID id) throws DatabaseException, UserNotFoundException {
        String sqlDelete = "UPDATE users SET deleted = TRUE WHERE id = ?";

        return updateDeletedStatus(id, sqlDelete, "Kullanıcı zaten silinmiş");
    }

    // Kullanıcıyı Geri Yükleme
    public static boolean restoreUser(UUID id) throws DatabaseException, UserNotFoundException {
        String sqlRestore = "UPDATE users SET deleted = FALSE WHERE id = ?";

        return updateDeletedStatus(id, sqlRestore, "Kullanıcı zaten aktif");
    }

    // Silme veya geri yükleme işlemi için ortak metot
    private static boolean updateDeletedStatus(UUID id, String sql, String errorMessage) throws DatabaseException, UserNotFoundException {
        String sqlCheck = "SELECT deleted FROM users WHERE id = ?";
        /* Aşağıdaki kod satırının açıklaması:
        1. Kullanıcıyı kontrol eder
        2. Kullanıcı zaten silinmişse veya aktifse hata fırlatır
        3. Kullanıcıyı siler veya geri yükler */
        Connection conn = null;
        PreparedStatement stmtCheck = null;
        PreparedStatement stmtUpdate = null;
        ResultSet rs = null;

        try {
            conn = DataConnect.getConnection();
            stmtCheck = conn.prepareStatement(sqlCheck);
            stmtCheck.setObject(1, id);
            rs = stmtCheck.executeQuery();

            if (rs.next()) {
                boolean isDeleted = rs.getBoolean("deleted");
                if ((sql.contains("FALSE") && !isDeleted) || (sql.contains("TRUE") && isDeleted)) {
                    throw new UserNotFoundException(errorMessage + ": " + id);
                }
            } else {
                throw new UserNotFoundException("Kullanıcı bulunamadı: " + id);
            }

            stmtUpdate = conn.prepareStatement(sql);
            stmtUpdate.setObject(1, id);
            return stmtUpdate.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DatabaseException("Veritabanı hatası: " + e.getMessage());
        } finally {
            DataConnect.closeResources(conn, stmtCheck, rs);
            DataConnect.closeResources(null, stmtUpdate, null);
        }
    }
}
