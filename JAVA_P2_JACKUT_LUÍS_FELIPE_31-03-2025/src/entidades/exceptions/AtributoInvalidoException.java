package entidades.exceptions;

public class AtributoInvalidoException extends JackutException {
    public AtributoInvalidoException(String atributo) {
        super("Atributo inv�lido: " + atributo);
    }
}