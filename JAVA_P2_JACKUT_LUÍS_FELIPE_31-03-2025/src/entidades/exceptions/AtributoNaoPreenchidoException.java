package entidades.exceptions;

public class AtributoNaoPreenchidoException extends JackutException {
    public AtributoNaoPreenchidoException() {
        super("Atributo não preenchido.");
    }
}