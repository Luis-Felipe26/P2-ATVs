package entidades;

import java.util.*;
import java.io.*;
import entidades.exceptions.*;

/**
 * Classe que representa o núcleo do sistema Jackut, responsável por:
 * - Gerenciamento de usuários e sessões
 * - Persistência de dados
 * - Controle do estado do sistema
 *
 * Implementa funcionalidades de autenticação, armazenamento e recuperação de dados.
 */
public class Sistema {
    private static final String ARQUIVO_DADOS = "dados_jackut.ser";
    private Map<String, Usuario> usuarios;
    private Map<String, String> sessoes; // idSessao -> login

    /**
     * Constrói uma nova instância do sistema, carregando os dados persistentes.
     * Se não existirem dados salvos, inicia com estruturas vazias.
     */
    public Sistema() {
        carregarDados();
    }

    // US1 - Gerenciamento de usuários

    /**
     * Adiciona um novo usuário ao sistema.
     *
     * @param usuario Objeto Usuario a ser adicionado
     * @throws UsuarioJaExisteException Se já existir um usuário com o mesmo login
     */
    public void adicionarUsuario(Usuario usuario) {
        if (usuarios.containsKey(usuario.getLogin())) {
            throw new UsuarioJaExisteException();
        }
        usuarios.put(usuario.getLogin(), usuario);
    }

    /**
     * Obtém um usuário pelo seu login.
     *
     * @param login Identificador do usuário
     * @return Objeto Usuario correspondente ou null se não existir
     */
    public Usuario getUsuario(String login) {
        return usuarios.get(login);
    }

    /**
     * Abre uma nova sessão para um usuário autenticado.
     *
     * @param login Identificador do usuário
     * @param senha Senha do usuário
     * @return ID único da sessão criada
     * @throws LoginOuSenhaInvalidosException Se as credenciais forem inválidas
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
     * Verifica se uma sessão é válida (existe e está ativa).
     *
     * @param idSessao ID da sessão a ser verificada
     * @return true se a sessão for válida, false caso contrário
     */
    public boolean validarSessao(String idSessao) {
        return sessoes.containsKey(idSessao);
    }

    /**
     * Obtém o login do usuário associado a uma sessão.
     *
     * @param idSessao ID da sessão
     * @return Login do usuário associado à sessão
     * @throws IllegalArgumentException Se a sessão for inválida
     */
    public String getLoginPorSessao(String idSessao) {
        if (idSessao == null || idSessao.isEmpty() || !validarSessao(idSessao)) {
            throw new IllegalArgumentException("Sessão inválida.");
        }
        return sessoes.get(idSessao);
    }

    // Persistência

    /**
     * Carrega os dados persistentes do sistema a partir do arquivo de armazenamento.
     * Se o arquivo não existir, inicia com estruturas vazias.
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
     * Apenas os usuários são persistidos, as sessões são temporárias.
     * Erros durante o salvamento são registrados no console.
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
     * Zera o sistema, removendo todos os usuários e sessões,
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

    // Métodos auxiliares

    /**
     * Verifica se um usuário existe no sistema.
     *
     * @param login Identificador do usuário a verificar
     * @return true se o usuário existir, false caso contrário
     */
    public boolean existeUsuario(String login) {
        return usuarios.containsKey(login);
    }

    /**
     * Obtém uma coleção com todos os usuários do sistema.
     *
     * @return Coleção não modificável de usuários
     */
    public Collection<Usuario> getTodosUsuarios() {
        return new ArrayList<>(usuarios.values());
    }
}