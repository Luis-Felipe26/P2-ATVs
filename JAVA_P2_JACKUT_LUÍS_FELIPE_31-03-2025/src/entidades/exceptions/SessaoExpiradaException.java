package entidades.exceptions;

public class SessaoExpiradaException extends JackutException {
    public SessaoExpiradaException() {
        super("Sess�o expirada.");
    }
}