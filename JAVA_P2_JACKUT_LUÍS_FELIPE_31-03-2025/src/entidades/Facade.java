package entidades;

import java.util.*;
import entidades.exceptions.*;

/**
 * Classe Facade que representa a interface simplificada para interação com o sistema.
 * Centraliza as operações relacionadas a usuários, perfis, amizades e mensagens.
 */
public class Facade {
    private final Sistema sistema;

    /**
     * Constrói uma nova instância da Facade, inicializando o sistema interno.
     */
    public Facade() {
        this.sistema = new Sistema();
    }

    // US1 - User Account Management

    /**
     * Cria um novo usuário no sistema.
     *
     * @param login Identificador único do usuário
     * @param senha Senha do usuário
     * @param nome Nome completo do usuário
     * @throws IllegalArgumentException Se o login, senha forem inválidos ou se o usuário já existir
     */
    public void criarUsuario(String login, String senha, String nome) {
        try {
            Usuario novoUsuario = new Usuario(login, senha, nome);
            sistema.adicionarUsuario(novoUsuario);
        } catch (LoginInvalidoException e) {
            throw new IllegalArgumentException("Login inválido.");
        } catch (SenhaInvalidaException e) {
            throw new IllegalArgumentException("Senha inválida.");
        } catch (UsuarioJaExisteException e) {
            throw new IllegalArgumentException("Conta com esse nome já existe.");
        }
    }

    /**
     * Abre uma nova sessão para o usuário.
     *
     * @param login Identificador do usuário
     * @param senha Senha do usuário
     * @return ID da sessão criada
     * @throws IllegalArgumentException Se o login ou senha forem inválidos
     */
    public String abrirSessao(String login, String senha) {
        try {
            return sistema.abrirSessao(login, senha);
        } catch (LoginOuSenhaInvalidosException e) {
            throw new IllegalArgumentException("Login ou senha inválidos.");
        }
    }

    /**
     * Obtém um atributo específico de um usuário.
     *
     * @param login Identificador do usuário
     * @param atributo Nome do atributo a ser recuperado
     * @return Valor do atributo solicitado
     * @throws IllegalArgumentException Se o usuário não existir ou o atributo não estiver preenchido
     */
    public String getAtributoUsuario(String login, String atributo) {
        Usuario usuario = sistema.getUsuario(login);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não cadastrado.");
        }

        if ("nome".equals(atributo)) {
            return usuario.getNome();
        }

        if (!usuario.possuiAtributo(atributo)) {
            throw new IllegalArgumentException("Atributo não preenchido.");
        }
        return usuario.getAtributo(atributo);
    }

    // US2 - Profile Management

    /**
     * Edita um atributo do perfil do usuário atualmente logado.
     *
     * @param idSessao ID da sessão ativa
     * @param atributo Nome do atributo a ser editado
     * @param valor Novo valor para o atributo
     * @throws IllegalArgumentException Se a sessão for inválida ou o usuário não existir
     */
    public void editarPerfil(String idSessao, String atributo, String valor) {
        if (idSessao == null || idSessao.isEmpty()) {
            throw new IllegalArgumentException("Usuário não cadastrado.");
        }

        String login = sistema.getLoginPorSessao(idSessao);
        Usuario usuario = sistema.getUsuario(login);

        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não cadastrado.");
        }

        usuario.editarAtributo(atributo, valor);
    }

    // US3 - Friendship Management

    /**
     * Adiciona um amigo para o usuário atualmente logado.
     *
     * @param idSessao ID da sessão ativa
     * @param amigo Login do usuário a ser adicionado como amigo
     * @throws IllegalArgumentException Se a sessão for inválida, usuário não existir,
     *         tentar adicionar a si mesmo, ou se a amizade já existir
     */
    public void adicionarAmigo(String idSessao, String amigo) {
        if (idSessao == null || idSessao.isEmpty()) {
            throw new IllegalArgumentException("Usuário não cadastrado.");
        }

        String login = sistema.getLoginPorSessao(idSessao);

        if (login.equals(amigo)) {
            throw new IllegalArgumentException("Usuário não pode adicionar a si mesmo como amigo.");
        }

        Usuario usuario = sistema.getUsuario(login);
        Usuario amigoUsuario = sistema.getUsuario(amigo);

        if (amigoUsuario == null) {
            throw new IllegalArgumentException("Usuário não cadastrado.");
        }

        if (usuario.ehAmigo(amigo)) {
            throw new IllegalArgumentException("Usuário já está adicionado como amigo.");
        }

        if (usuario.temConvitePendente(amigo)) {
            throw new IllegalArgumentException("Usuário já está adicionado como amigo, esperando aceitação do convite.");
        }

        if (amigoUsuario.temConvitePendente(login)) {
            // Aceitação mútua
            usuario.adicionarAmizade(amigo, true);
            amigoUsuario.adicionarAmizade(login, true);
        } else {
            // Envia convite
            usuario.adicionarAmizade(amigo, false);
        }
    }

    /**
     * Verifica se dois usuários são amigos.
     *
     * @param login1 Login do primeiro usuário
     * @param login2 Login do segundo usuário
     * @return true se os usuários são amigos, false caso contrário
     */
    public boolean ehAmigo(String login1, String login2) {
        Usuario usuario = sistema.getUsuario(login1);
        return usuario != null && usuario.ehAmigo(login2);
    }

    /**
     * Obtém a lista de amigos de um usuário no formato JSON.
     *
     * @param login Login do usuário
     * @return String no formato JSON contendo a lista de amigos ordenados
     */
    public String getAmigos(String login) {
        Usuario usuario = sistema.getUsuario(login);
        if (usuario == null) {
            return "{}";
        }

        LinkedHashSet<String> amigos = usuario.getAmigosOrdenados();
        if (amigos.isEmpty()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder("{");
        Iterator<String> it = amigos.iterator();
        sb.append(it.next());
        while (it.hasNext()) {
            sb.append(",").append(it.next());
        }
        sb.append("}");
        return sb.toString();
    }

    // US4 - Message System

    /**
     * Envia um recado para outro usuário.
     *
     * @param idSessao ID da sessão do remetente
     * @param destinatario Login do usuário destinatário
     * @param recado Conteúdo da mensagem
     * @throws IllegalArgumentException Se a sessão for inválida, destinatário não existir,
     *         ou tentar enviar mensagem para si mesmo
     */
    public void enviarRecado(String idSessao, String destinatario, String recado) {
        String remetente = sistema.getLoginPorSessao(idSessao);

        if (remetente.equals(destinatario)) {
            throw new IllegalArgumentException("Usuário não pode enviar recado para si mesmo.");
        }

        Usuario usuarioDestino = sistema.getUsuario(destinatario);
        if (usuarioDestino == null) {
            throw new IllegalArgumentException("Usuário não cadastrado.");
        }

        usuarioDestino.receberRecado(remetente, recado);
    }

    /**
     * Lê o próximo recado não lido do usuário.
     *
     * @param idSessao ID da sessão do usuário
     * @return Conteúdo do recado
     * @throws IllegalArgumentException Se não houver recados para ler
     */
    public String lerRecado(String idSessao) {
        String login = sistema.getLoginPorSessao(idSessao);
        Usuario usuario = sistema.getUsuario(login);

        String recado = usuario.lerRecado();
        if (recado == null) {
            throw new IllegalArgumentException("Não há recados.");
        }
        return recado;
    }

    // System Control

    /**
     * Reinicia o sistema, removendo todos os dados.
     */
    public void zerarSistema() {
        sistema.zerarSistema();
    }

    /**
     * Encerra o sistema, realizando operações de limpeza necessárias.
     */
    public void encerrarSistema() {
        sistema.encerrarSistema();
    }

    /**
     * Valida se um usuário pode adicionar outro como amigo.
     *
     * @param login Login do usuário que está adicionando
     * @param amigo Login do usuário a ser adicionado como amigo
     * @throws IllegalArgumentException Se o usuário tentar adicionar a si mesmo ou
     *         se o amigo não estiver cadastrado
     */
    private void validarAdicaoAmigo(String login, String amigo) {
        if (login.equals(amigo)) {
            throw new IllegalArgumentException("Usuário não pode adicionar a si mesmo como amigo.");
        }

        if (sistema.getUsuario(amigo) == null) {
            throw new IllegalArgumentException("Usuário não cadastrado.");
        }
    }
}