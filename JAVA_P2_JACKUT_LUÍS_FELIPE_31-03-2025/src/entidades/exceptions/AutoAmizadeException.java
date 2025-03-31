package entidades.exceptions;

public class AutoAmizadeException extends JackutException {
    public AutoAmizadeException() {
        super("Usuário não pode adicionar a si mesmo como amigo.");
    }
}


