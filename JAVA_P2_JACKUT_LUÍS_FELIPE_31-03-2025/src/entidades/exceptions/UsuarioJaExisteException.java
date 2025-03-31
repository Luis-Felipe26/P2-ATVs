package entidades.exceptions;

public class UsuarioJaExisteException extends JackutException {
    public UsuarioJaExisteException() {
        super("Conta com esse nome já existe.");
    }
}