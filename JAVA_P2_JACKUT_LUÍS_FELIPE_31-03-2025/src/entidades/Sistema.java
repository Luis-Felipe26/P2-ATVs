package entidades;

import java.util.*;
import java.io.*;
import entidades.exceptions.*;

/**
 * Classe que representa o n�cleo do sistema Jackut, respons�vel por:
 * - Gerenciamento de usu�rios e sess�es
 * - Persist�ncia de dados
 * - Controle do estado do sistema
 *
 * Implementa funcionalidades de autentica��o, armazenamento e recupera��o de dados.
 */
public class Sistema {
    private static final String ARQUIVO_DADOS = "dados_jackut.ser";
    private Map<String, Usuario> usuarios;
    private Map<String, String> sessoes; // idSessao -> login

    /**
     * Constr�i uma nova inst�ncia do sistema, carregando os dados persistentes.
     * Se n�o existirem dados salvos, inicia com estruturas vazias.
     */
    public Sistema() {
        carregarDados();
    }

    // US1 - Gerenciamento de usu�rios

    /**
     * Adiciona um novo usu�rio ao sistema.
     *
     * @param usuario Objeto Usuario a ser adicionado
     * @throws UsuarioJaExisteException Se j� existir um usu�rio com o mesmo login
     */
    public void adicionarUsuario(Usuario usuario) {
        if (usuarios.containsKey(usuario.getLogin())) {
            throw new UsuarioJaExisteException();
        }
        usuarios.put(usuario.getLogin(), usuario);
    }

    /**
     * Obt�m um usu�rio pelo seu login.
     *
     * @param login Identificador do usu�rio
     * @return Objeto Usuario correspondente ou null se n�o existir
     */
    public Usuario getUsuario(String login) {
        return usuarios.get(login);
    }

    /**
     * Abre uma nova sess�o para um usu�rio autenticado.
     *
     * @param login Identificador do usu�rio
     * @param senha Senha do usu�rio
     * @return ID �nico da sess�o criada
     * @throws LoginOuSenhaInvalidosException Se as credenciais forem inv�lidas
     */
    public String abrirSessao(String login, String senha) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null || !usuario.verificarSenha(senha)) {
            throw new LoginOuSenhaInvalidosException();
        }

        String idSessao = UUID.randomUUID().toString();
        sessoes.put(idSessao, login);
        return idSessao;
    }

    /**
     * Verifica se uma sess�o � v�lida (existe e est� ativa).
     *
     * @param idSessao ID da sess�o a ser verificada
     * @return true se a sess�o for v�lida, false caso contr�rio
     */
    public boolean validarSessao(String idSessao) {
        return sessoes.containsKey(idSessao);
    }

    /**
     * Obt�m o login do usu�rio associado a uma sess�o.
     *
     * @param idSessao ID da sess�o
     * @return Login do usu�rio associado � sess�o
     * @throws IllegalArgumentException Se a sess�o for inv�lida
     */
    public String getLoginPorSessao(String idSessao) {
        if (idSessao == null || idSessao.isEmpty() || !validarSessao(idSessao)) {
            throw new IllegalArgumentException("Sess�o inv�lida.");
        }
        return sessoes.get(idSessao);
    }

    // Persist�ncia

    /**
     * Carrega os dados persistentes do sistema a partir do arquivo de armazenamento.
     * Se o arquivo n�o existir, inicia com estruturas vazias.
     * Trata erros de leitura inicializando com estruturas vazias.
     */
    private void carregarDados() {
        try {
            File arquivo = new File(ARQUIVO_DADOS);
            if (arquivo.exists()) {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(ARQUIVO_DADOS));
                this.usuarios = (Map<String, Usuario>) in.readObject();
                in.close();
            } else {
                this.usuarios = new HashMap<>();
            }
            this.sessoes = new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar dados: " + e.getMessage());
            this.usuarios = new HashMap<>();
            this.sessoes = new HashMap<>();
        }
    }

    /**
     * Salva os dados do sistema no arquivo de armazenamento.
     * Apenas os usu�rios s�o persistidos, as sess�es s�o tempor�rias.
     * Erros durante o salvamento s�o registrados no console.
     */
    public void salvarDados() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARQUIVO_DADOS));
            out.writeObject(usuarios);
            out.close();
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    // Controle do sistema

    /**
     * Zera o sistema, removendo todos os usu�rios e sess�es,
     * e deletando o arquivo de dados persistente.
     */
    public void zerarSistema() {
        usuarios.clear();
        sessoes.clear();
        new File(ARQUIVO_DADOS).delete();
    }

    /**
     * Encerra o sistema adequadamente, salvando os dados atuais.
     */
    public void encerrarSistema() {
        salvarDados();
    }

    // M�todos auxiliares

    /**
     * Verifica se um usu�rio existe no sistema.
     *
     * @param login Identificador do usu�rio a verificar
     * @return true se o usu�rio existir, false caso contr�rio
     */
    public boolean existeUsuario(String login) {
        return usuarios.containsKey(login);
    }

    /**
     * Obt�m uma cole��o com todos os usu�rios do sistema.
     *
     * @return Cole��o n�o modific�vel de usu�rios
     */
    public Collection<Usuario> getTodosUsuarios() {
        return new ArrayList<>(usuarios.values());
    }
}