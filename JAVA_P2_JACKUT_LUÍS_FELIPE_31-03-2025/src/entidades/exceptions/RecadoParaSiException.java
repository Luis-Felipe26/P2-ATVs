package entidades.exceptions;

public class RecadoParaSiException extends JackutException {
    public RecadoParaSiException() {
        super("Usu�rio n�o pode enviar recado para si mesmo.");
    }
}
