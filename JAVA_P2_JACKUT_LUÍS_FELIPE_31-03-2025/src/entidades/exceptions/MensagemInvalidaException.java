package entidades.exceptions;

public class MensagemInvalidaException extends JackutException {
    public MensagemInvalidaException() {
        super("Mensagem n�o pode ser vazia.");
    }
}