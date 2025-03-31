package entidades.exceptions;

public class MensagemInvalidaException extends JackutException {
    public MensagemInvalidaException() {
        super("Mensagem não pode ser vazia.");
    }
}