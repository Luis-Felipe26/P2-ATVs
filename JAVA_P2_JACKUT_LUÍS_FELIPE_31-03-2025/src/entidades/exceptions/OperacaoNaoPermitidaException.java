package entidades.exceptions;

public class OperacaoNaoPermitidaException extends JackutException {
    public OperacaoNaoPermitidaException() {
        super("Opera��o n�o permitida no estado atual do sistema.");
    }
}
