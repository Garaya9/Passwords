package application;


/**
 * Custom exception for handling the absence of uppercase alphabetic characters in a password.
 * This exception is thrown when a password does not contain an uppercase letter.
 */
public  class NoUpperAlphaException extends Exception {
    private static final long serialVersionUID = 1L;

    public NoUpperAlphaException(String message) {
        super(message);  // Pass the error message to the parent class (Exception)
    }
}
