package entidades.exceptions;

public class SemRecadosException extends JackutException {
    public SemRecadosException() {
        super("Não há recados.");
    }
}