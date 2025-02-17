package dto;

public class UpdateUserDTO {
    private String name;
    private String surname;
    private String email;
    private String gender;
    private String phone;
    private String address;

    public UpdateUserDTO() {}

    public UpdateUserDTO(String name, String surname, String email, String gender, String phone, String address) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
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
}