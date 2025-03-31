package entidades.exceptions;

public class UsuarioNaoCadastradoException extends JackutException {
    public UsuarioNaoCadastradoException() {
        super("Usuário não cadastrado.");
    }
}

