package entidades.exceptions;

public class SemRecadosException extends JackutException {
    public SemRecadosException() {
        super("N�o h� recados.");
    }
}