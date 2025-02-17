package dto;

public class CreateUserDTO {
    private String name;
    private String surname;
    private String email;
    private String password;
    private String gender;
    private String phone;
    private String address;

    public CreateUserDTO() {}

    public CreateUserDTO(String name, String surname, String email, String password, String gender, String phone, String address) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
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
}