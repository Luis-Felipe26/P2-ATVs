package entidades.exceptions;

public class SistemaNaoInicializadoException extends JackutException {
    public SistemaNaoInicializadoException() {
        super("Sistema n�o foi inicializado corretamente.");
    }
}

