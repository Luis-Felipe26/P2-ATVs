package entidades.exceptions;

public class AmizadePendenteException extends JackutException {
    public AmizadePendenteException() {
        super("Usu�rio j� est� adicionado como amigo, esperando aceita��o do convite.");
    }
}