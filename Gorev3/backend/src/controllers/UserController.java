package controllers;

import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import dto.GetUserByIdDTO;
import dto.CreateUserDTO;
import dto.UpdateUserDTO;
import dto.GetUserListDto;
import models.User;
import exceptions.*;
import services.UserService;
import core.utilities.JsonHelper;
import core.utilities.RequestHelper;

public class UserController {

    // Kullanıcı işlemlerini yöneten sınıf
    public void handleGetAllUsers(HttpExchange exchange) throws IOException {

        // RequestHelper sınıfı ile gelen isteği işler
        RequestHelper.handleRequest(exchange, "GET", () -> {
            // UserService sınıfı ile tüm kullanıcıları alır
            List<GetUserListDto> users = UserService.getAllUsers();
            // Eğer kullanıcılar boş ise UserNotFoundException hatası fırlatılır
            if (users.isEmpty()) throw new UserNotFoundException("Kullanıcılar bulunamadı!");
            // Kullanıcılar listesi JSON formatına dönüştürülür
            String jsonResponse = JsonHelper.toJson(users);
            //System.out.println("JSON Response: " + jsonResponse);
            return jsonResponse;
        });
    }

    // Kullanıcı oluşturma işlemini yöneten sınıf
    public void handleCreateUser(HttpExchange exchange) throws IOException {
        // RequestHelper sınıfı ile gelen isteği işler
        RequestHelper.handleRequest(exchange, "POST", () -> {
            // Request body okunur
            String requestBody = readRequestBody(exchange);
            // Request body JSON formatından CreateUserDTO nesnesine dönüştürülür
            CreateUserDTO dto = JsonHelper.fromJson(requestBody, CreateUserDTO.class);
            // UserService sınıfı ile kullanıcı oluşturulur
            if (UserService.addUser(dto)) {
                // Eğer kullanıcı başarıyla eklenirse başarılı mesajı döndürülür
                return "{\"message\":\"Kullanıcı başarıyla eklendi\"}";
            }
            // Eğer kullanıcı eklenemezse DatabaseException hatası fırlatılır
            throw new DatabaseException("Kullanıcı eklenemedi");
        });
    }

    // Kullanıcı güncelleme işlemini yöneten sınıf
    public void handleUpdateUser(HttpExchange exchange) throws IOException {
        // RequestHelper sınıfı ile gelen isteği işler
        RequestHelper.handleRequest(exchange, "PUT", () -> {
            String userId = getUserIdFromPath(exchange);
            String requestBody = readRequestBody(exchange);
            // Request body JSON formatından UpdateUserDTO nesnesine dönüştürülür
            UpdateUserDTO dto = JsonHelper.fromJson(requestBody, UpdateUserDTO.class);
            // UserService sınıfı ile kullanıcı güncellenir
            if (UserService.updateUser(UUID.fromString(userId), dto)) {
                // Eğer kullanıcı başarıyla güncellenirse başarılı mesajı döndürülür
                return "{\"message\":\"Kullanıcı başarıyla güncellendi\"}";
            }
            // Eğer kullanıcı güncellenemezse DatabaseException hatası fırlatılır
            throw new DatabaseException("Kullanıcı güncellenemedi");
        });
    }

    // Kullanıcı detaylarını getiren sınıf
    public void handleGetUserById(HttpExchange exchange) throws IOException {
        // RequestHelper sınıfı ile gelen isteği işler
        RequestHelper.handleRequest(exchange, "GET", () -> {
            // Kullanıcı ID'si alınır
            String userId = getUserIdFromPath(exchange);
            // UserService sınıfı ile kullanıcı ID'sine göre kullanıcı getirilir
            User user = UserService.getUserById(UUID.fromString(userId));
            // Eğer kullanıcı bulunamazsa UserNotFoundException hatası fırlatılır
            if (user == null) throw new UserNotFoundException("Kullanıcı bulunamadı: " + userId);
            // Kullanıcı DTO nesnesine dönüştürülür
            GetUserByIdDTO userDto = GetUserByIdDTO.fromUser(user);
            // Kullanıcı DTO nesnesi JSON formatına dönüştürülür
            return JsonHelper.toJson(userDto);
        });
    }

    // Kullanıcı silme işlemini yöneten sınıf
    public void handleDeleteUser(HttpExchange exchange) throws IOException {
        RequestHelper.handleRequest(exchange, "DELETE", () -> {
            // Kullanıcı ID'si alınır
            String userId = getUserIdFromPath(exchange);
            // UserService sınıfı ile kullanıcı silinir
            if (UserService.deleteUser(UUID.fromString(userId)))
                return "{\"message\":\"Kullanıcı başarıyla silindi\"}";
            throw new DatabaseException("Kullanıcı silinemedi");
        });
    }

    // Kullanıcı geri yükleme işlemini yöneten sınıf
    public void handleRestoreUser(HttpExchange exchange) throws IOException {
        RequestHelper.handleRequest(exchange, "PATCH", () -> {
            String userId = getUserIdFromPath(exchange);
            // UserService sınıfı ile kullanıcı geri yüklenir
            if (UserService.restoreUser(UUID.fromString(userId)))
                return "{\"message\":\"Kullanıcı başarıyla geri yüklendi\"}";
            throw new DatabaseException("Kullanıcı geri yüklenemedi");
        });
    }

    // Kullanıcı ID'sini alır
    private String getUserIdFromPath(HttpExchange exchange) {
        // Request URI'dan kullanıcı ID'si alınır
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        // Kullanıcı ID'si döndürülür
        return pathParts[pathParts.length - 1];
    }

    // Request body okuma işlemini yöneten sınıf
    private String readRequestBody(HttpExchange exchange) throws IOException {
        // Request body okunur ve string olarak döndürülür
        return new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining("\n"));
    }
}
