package application;


/**
 * Custom exception for handling the absence of digits in a password.
 * This exception is thrown when a password does not contain a numeric character.
 */
public  class NoDigitException extends Exception {
    private static final long serialVersionUID = 1L;

    public NoDigitException(String message) {
        super(message);  // Pass the error message to the parent class (Exception)
    }
}