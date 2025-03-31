package entidades.exceptions;

/**
 * Classe base para todas as exceções do sistema Jackut
 */
public abstract class JackutException extends RuntimeException {
    public JackutException(String message) {
        super(message);
    }

    public JackutException(String message, Throwable cause) {
        super(message, cause);
    }
}