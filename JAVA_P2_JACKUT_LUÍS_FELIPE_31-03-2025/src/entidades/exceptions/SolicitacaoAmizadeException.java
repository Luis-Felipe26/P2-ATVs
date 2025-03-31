package entidades.exceptions;

public class SolicitacaoAmizadeException extends JackutException {
    public SolicitacaoAmizadeException() {
        super("Erro ao processar solicitação de amizade.");
    }
}