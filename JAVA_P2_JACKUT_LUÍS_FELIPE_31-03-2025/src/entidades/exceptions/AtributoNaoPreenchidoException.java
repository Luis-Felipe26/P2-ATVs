package entidades.exceptions;

public class AtributoNaoPreenchidoException extends JackutException {
    public AtributoNaoPreenchidoException() {
        super("Atributo n�o preenchido.");
    }
}