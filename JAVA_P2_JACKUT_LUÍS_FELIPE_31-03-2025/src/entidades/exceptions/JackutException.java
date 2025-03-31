package entidades.exceptions;

/**
 * Classe base para todas as exce��es do sistema Jackut
 */
public abstract class JackutException extends RuntimeException {
    public JackutException(String message) {
        super(message);
    }

    public JackutException(String message, Throwable cause) {
        super(message, cause);
    }
}