package models;

import java.util.UUID;
import core.utilities.JsonHelper;

public class User {
    private UUID id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String gender;
    private String phone;
    private String address;

    public User(UUID id, String name, String surname, String email, String password, String gender, String phone, String address) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    // JSON dönüşümü artık JsonHelper kullanılarak
    public String toJson() {
        return JsonHelper.toJson(this);
    }
}
