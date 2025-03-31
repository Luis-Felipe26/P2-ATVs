package entidades.exceptions;

public class SessaoInvalidaException extends JackutException {
    public SessaoInvalidaException() {
        super("Sessão inválida.");
    }
}