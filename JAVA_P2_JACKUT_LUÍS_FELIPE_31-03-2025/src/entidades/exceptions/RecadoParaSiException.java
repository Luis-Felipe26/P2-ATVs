package entidades.exceptions;

public class RecadoParaSiException extends JackutException {
    public RecadoParaSiException() {
        super("Usuário não pode enviar recado para si mesmo.");
    }
}
