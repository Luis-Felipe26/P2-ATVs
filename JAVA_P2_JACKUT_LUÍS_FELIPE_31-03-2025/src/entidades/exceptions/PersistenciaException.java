package entidades.exceptions;

public class PersistenciaException extends JackutException {
    private final String operacao;
    private final String recurso;

    public PersistenciaException(String operacao, String recurso, String mensagem) {
        super(formatarMensagem(operacao, recurso, mensagem));
        this.operacao = operacao;
        this.recurso = recurso;
    }

    public PersistenciaException(String operacao, String recurso, String mensagem, Throwable causa) {
        super(formatarMensagem(operacao, recurso, mensagem), causa);
        this.operacao = operacao;
        this.recurso = recurso;
    }

    private static String formatarMensagem(String operacao, String recurso, String mensagem) {
        return String.format("[%s] Falha ao %s no recurso %s: %s",
                PersistenciaException.class.getSimpleName(), operacao, recurso, mensagem);
    }

    public String getOperacao() { return operacao; }
    public String getRecurso() { return recurso; }
}