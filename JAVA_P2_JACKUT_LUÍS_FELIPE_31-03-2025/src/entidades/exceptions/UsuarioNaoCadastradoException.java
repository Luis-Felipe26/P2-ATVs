package entidades.exceptions;

public class UsuarioNaoCadastradoException extends JackutException {
    public UsuarioNaoCadastradoException() {
        super("Usu�rio n�o cadastrado.");
    }
}

