package entidades.exceptions;

public class SessaoInvalidaException extends JackutException {
    public SessaoInvalidaException() {
        super("Sess�o inv�lida.");
    }
}