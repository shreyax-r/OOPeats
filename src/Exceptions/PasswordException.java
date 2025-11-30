package Exceptions;

// Custom Exception for password validation
public class PasswordException extends Exception {
    public PasswordException(String message) {
        super(message);
    }
}

