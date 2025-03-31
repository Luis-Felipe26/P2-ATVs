package entidades.exceptions;

public class SenhaInvalidaException extends JackutException {
    public SenhaInvalidaException() {
        super("Senha inválida.");
    }
}