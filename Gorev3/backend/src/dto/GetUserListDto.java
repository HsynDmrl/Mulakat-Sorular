package dto;

import models.User;
import java.util.UUID;

public class GetUserListDto {
    private UUID id;
    private String name;
    private String surname;
    private String email;
    private String gender;
    private String phone;
    private String address;

    public GetUserListDto(UUID id, String name, String surname, String email, String gender, String phone, String address) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
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

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public static GetUserListDto fromUser(User user) {
        return new GetUserListDto(
            user.getId(),
            user.getName(),
            user.getSurname(),
            user.getEmail(),
            user.getGender(),
            user.getPhone(),
            user.getAddress()
        );
    }

    @Override
    public String toString() {
        return "GetUserListDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}