package entidades.exceptions;

public class LoginOuSenhaInvalidosException extends JackutException {
    public LoginOuSenhaInvalidosException() {
        super("Login ou senha inv�lidos.");
    }
}