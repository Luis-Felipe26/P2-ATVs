package entidades.exceptions;

public class OperacaoNaoPermitidaException extends JackutException {
    public OperacaoNaoPermitidaException() {
        super("Operação não permitida no estado atual do sistema.");
    }
}
