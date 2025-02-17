package exceptions;

// Kullanıcı zaten var exception
public class UserAlreadyExistsException extends UserException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
