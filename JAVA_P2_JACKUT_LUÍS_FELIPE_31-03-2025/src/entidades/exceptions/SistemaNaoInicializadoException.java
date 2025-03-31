package entidades.exceptions;

public class SistemaNaoInicializadoException extends JackutException {
    public SistemaNaoInicializadoException() {
        super("Sistema não foi inicializado corretamente.");
    }
}

