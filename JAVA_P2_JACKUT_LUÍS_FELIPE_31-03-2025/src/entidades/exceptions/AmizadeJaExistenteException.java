package entidades.exceptions;

public class AmizadeJaExistenteException extends JackutException {
    public AmizadeJaExistenteException() {
        super("Usuário já está adicionado como amigo.");
    }
}