$(document).ready(function() {
    // Kullanıcı ekleme formu
    $('#userForm').on('submit', function(event) {
        event.preventDefault();

        const userData = {
            name: $('#name').val(),
            surname: $('#surname').val(),
            email: $('#email').val(),
            password: $('#password').val(),
            gender: $('#gender').val(),
            phone: $('#phone').val(),
            address: $('#address').val()
        };

        $.ajax({
            url: 'http://localhost:8080/api/v1/users/create',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(userData),
            success: function(response) {
                alert('Kullanıcı başarıyla eklendi');
                $('#userForm')[0].reset();
                loadUsers();
            },
            error: function(xhr, status, error) {
                const errorResponse = JSON.parse(xhr.responseText);
                alert('Hata: ' + errorResponse.error);
            }
        });
    });

    // Kullanıcıları yükleme fonksiyonu
    function loadUsers() {
        $.ajax({
            url: 'http://localhost:8080/api/v1/users/getAll',
            type: 'GET',
            success: function(users) {
                const userTableBody = $('#userTable tbody');
                userTableBody.empty();
                users.forEach(user => {
                    userTableBody.append(`
                        <tr>
                            <td>${user.name}</td>
                            <td>${user.surname}</td>
                            <td>${user.email}</td>
                            <td>${user.gender}</td>
                            <td>${user.phone}</td>
                            <td>${user.address}</td>
                            <td>
                                <button class="btn btn-warning btn-sm" onclick="editUser('${user.id}')">Güncelle</button>
                                <button class="btn btn-danger btn-sm" onclick="deleteUser('${user.id}')">Sil</button>
                            </td>
                        </tr>
                    `);
                });
            },
            error: function(xhr, status, error) {
                const errorResponse = JSON.parse(xhr.responseText);
                alert('Hata: ' + errorResponse.error);
            }
        });
    }

    // Kullanıcı güncelleme fonksiyonu
    window.editUser = function(userId) {
        const userData = {
            name: prompt("Yeni Ad:"),
            surname: prompt("Yeni Soyad:"),
            email: prompt("Yeni Email:"),
            gender: prompt("Yeni Cinsiyet:"),
            phone: prompt("Yeni Telefon:"),
            address: prompt("Yeni Adres:")
        };

        $.ajax({
            url: `http://localhost:8080/api/v1/users/update/${userId}`,
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(userData),
            success: function(response) {
                alert(response.message);
                loadUsers();
            },
            error: function(xhr, status, error) {
                const errorResponse = JSON.parse(xhr.responseText);
                alert('Hata: ' + errorResponse.error);
            }
        });
    };

    // Kullanıcı silme fonksiyonu
    window.deleteUser = function(userId) {
        $.ajax({
            url: `http://localhost:8080/api/v1/users/delete/${userId}`,
            type: 'DELETE',
            success: function(response) {
                alert(response.message);
                loadUsers();
            },
            error: function(xhr, status, error) {
                const errorResponse = JSON.parse(xhr.responseText);
                alert('Hata: ' + errorResponse.error);
                loadUsers();
            }
        });
    };



    loadUsers();
});