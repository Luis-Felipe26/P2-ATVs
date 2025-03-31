package entidades.exceptions;

public class AmizadePendenteException extends JackutException {
    public AmizadePendenteException() {
        super("Usuário já está adicionado como amigo, esperando aceitação do convite.");
    }
}