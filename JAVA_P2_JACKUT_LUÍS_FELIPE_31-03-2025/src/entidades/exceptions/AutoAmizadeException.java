package entidades.exceptions;

public class AutoAmizadeException extends JackutException {
    public AutoAmizadeException() {
        super("Usu�rio n�o pode adicionar a si mesmo como amigo.");
    }
}


