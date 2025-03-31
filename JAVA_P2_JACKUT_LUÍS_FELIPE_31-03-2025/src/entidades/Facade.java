package entidades;

import java.util.*;
import entidades.exceptions.*;

/**
 * Classe Facade que representa a interface simplificada para intera��o com o sistema.
 * Centraliza as opera��es relacionadas a usu�rios, perfis, amizades e mensagens.
 */
public class Facade {
    private final Sistema sistema;

    /**
     * Constr�i uma nova inst�ncia da Facade, inicializando o sistema interno.
     */
    public Facade() {
        this.sistema = new Sistema();
    }

    // US1 - User Account Management

    /**
     * Cria um novo usu�rio no sistema.
     *
     * @param login Identificador �nico do usu�rio
     * @param senha Senha do usu�rio
     * @param nome Nome completo do usu�rio
     * @throws IllegalArgumentException Se o login, senha forem inv�lidos ou se o usu�rio j� existir
     */
    public void criarUsuario(String login, String senha, String nome) {
        try {
            Usuario novoUsuario = new Usuario(login, senha, nome);
            sistema.adicionarUsuario(novoUsuario);
        } catch (LoginInvalidoException e) {
            throw new IllegalArgumentException("Login inv�lido.");
        } catch (SenhaInvalidaException e) {
            throw new IllegalArgumentException("Senha inv�lida.");
        } catch (UsuarioJaExisteException e) {
            throw new IllegalArgumentException("Conta com esse nome j� existe.");
        }
    }

    /**
     * Abre uma nova sess�o para o usu�rio.
     *
     * @param login Identificador do usu�rio
     * @param senha Senha do usu�rio
     * @return ID da sess�o criada
     * @throws IllegalArgumentException Se o login ou senha forem inv�lidos
     */
    public String abrirSessao(String login, String senha) {
        try {
            return sistema.abrirSessao(login, senha);
        } catch (LoginOuSenhaInvalidosException e) {
            throw new IllegalArgumentException("Login ou senha inv�lidos.");
        }
    }

    /**
     * Obt�m um atributo espec�fico de um usu�rio.
     *
     * @param login Identificador do usu�rio
     * @param atributo Nome do atributo a ser recuperado
     * @return Valor do atributo solicitado
     * @throws IllegalArgumentException Se o usu�rio n�o existir ou o atributo n�o estiver preenchido
     */
    public String getAtributoUsuario(String login, String atributo) {
        Usuario usuario = sistema.getUsuario(login);
        if (usuario == null) {
            throw new IllegalArgumentException("Usu�rio n�o cadastrado.");
        }

        if ("nome".equals(atributo)) {
            return usuario.getNome();
        }

        if (!usuario.possuiAtributo(atributo)) {
            throw new IllegalArgumentException("Atributo n�o preenchido.");
        }
        return usuario.getAtributo(atributo);
    }

    // US2 - Profile Management

    /**
     * Edita um atributo do perfil do usu�rio atualmente logado.
     *
     * @param idSessao ID da sess�o ativa
     * @param atributo Nome do atributo a ser editado
     * @param valor Novo valor para o atributo
     * @throws IllegalArgumentException Se a sess�o for inv�lida ou o usu�rio n�o existir
     */
    public void editarPerfil(String idSessao, String atributo, String valor) {
        if (idSessao == null || idSessao.isEmpty()) {
            throw new IllegalArgumentException("Usu�rio n�o cadastrado.");
        }

        String login = sistema.getLoginPorSessao(idSessao);
        Usuario usuario = sistema.getUsuario(login);

        if (usuario == null) {
            throw new IllegalArgumentException("Usu�rio n�o cadastrado.");
        }

        usuario.editarAtributo(atributo, valor);
    }

    // US3 - Friendship Management

    /**
     * Adiciona um amigo para o usu�rio atualmente logado.
     *
     * @param idSessao ID da sess�o ativa
     * @param amigo Login do usu�rio a ser adicionado como amigo
     * @throws IllegalArgumentException Se a sess�o for inv�lida, usu�rio n�o existir,
     *         tentar adicionar a si mesmo, ou se a amizade j� existir
     */
    public void adicionarAmigo(String idSessao, String amigo) {
        if (idSessao == null || idSessao.isEmpty()) {
            throw new IllegalArgumentException("Usu�rio n�o cadastrado.");
        }

        String login = sistema.getLoginPorSessao(idSessao);

        if (login.equals(amigo)) {
            throw new IllegalArgumentException("Usu�rio n�o pode adicionar a si mesmo como amigo.");
        }

        Usuario usuario = sistema.getUsuario(login);
        Usuario amigoUsuario = sistema.getUsuario(amigo);

        if (amigoUsuario == null) {
            throw new IllegalArgumentException("Usu�rio n�o cadastrado.");
        }

        if (usuario.ehAmigo(amigo)) {
            throw new IllegalArgumentException("Usu�rio j� est� adicionado como amigo.");
        }

        if (usuario.temConvitePendente(amigo)) {
            throw new IllegalArgumentException("Usu�rio j� est� adicionado como amigo, esperando aceita��o do convite.");
        }

        if (amigoUsuario.temConvitePendente(login)) {
            // Aceita��o m�tua
            usuario.adicionarAmizade(amigo, true);
            amigoUsuario.adicionarAmizade(login, true);
        } else {
            // Envia convite
            usuario.adicionarAmizade(amigo, false);
        }
    }

    /**
     * Verifica se dois usu�rios s�o amigos.
     *
     * @param login1 Login do primeiro usu�rio
     * @param login2 Login do segundo usu�rio
     * @return true se os usu�rios s�o amigos, false caso contr�rio
     */
    public boolean ehAmigo(String login1, String login2) {
        Usuario usuario = sistema.getUsuario(login1);
        return usuario != null && usuario.ehAmigo(login2);
    }

    /**
     * Obt�m a lista de amigos de um usu�rio no formato JSON.
     *
     * @param login Login do usu�rio
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
     * Envia um recado para outro usu�rio.
     *
     * @param idSessao ID da sess�o do remetente
     * @param destinatario Login do usu�rio destinat�rio
     * @param recado Conte�do da mensagem
     * @throws IllegalArgumentException Se a sess�o for inv�lida, destinat�rio n�o existir,
     *         ou tentar enviar mensagem para si mesmo
     */
    public void enviarRecado(String idSessao, String destinatario, String recado) {
        String remetente = sistema.getLoginPorSessao(idSessao);

        if (remetente.equals(destinatario)) {
            throw new IllegalArgumentException("Usu�rio n�o pode enviar recado para si mesmo.");
        }

        Usuario usuarioDestino = sistema.getUsuario(destinatario);
        if (usuarioDestino == null) {
            throw new IllegalArgumentException("Usu�rio n�o cadastrado.");
        }

        usuarioDestino.receberRecado(remetente, recado);
    }

    /**
     * L� o pr�ximo recado n�o lido do usu�rio.
     *
     * @param idSessao ID da sess�o do usu�rio
     * @return Conte�do do recado
     * @throws IllegalArgumentException Se n�o houver recados para ler
     */
    public String lerRecado(String idSessao) {
        String login = sistema.getLoginPorSessao(idSessao);
        Usuario usuario = sistema.getUsuario(login);

        String recado = usuario.lerRecado();
        if (recado == null) {
            throw new IllegalArgumentException("N�o h� recados.");
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
     * Encerra o sistema, realizando opera��es de limpeza necess�rias.
     */
    public void encerrarSistema() {
        sistema.encerrarSistema();
    }

    /**
     * Valida se um usu�rio pode adicionar outro como amigo.
     *
     * @param login Login do usu�rio que est� adicionando
     * @param amigo Login do usu�rio a ser adicionado como amigo
     * @throws IllegalArgumentException Se o usu�rio tentar adicionar a si mesmo ou
     *         se o amigo n�o estiver cadastrado
     */
    private void validarAdicaoAmigo(String login, String amigo) {
        if (login.equals(amigo)) {
            throw new IllegalArgumentException("Usu�rio n�o pode adicionar a si mesmo como amigo.");
        }

        if (sistema.getUsuario(amigo) == null) {
            throw new IllegalArgumentException("Usu�rio n�o cadastrado.");
        }
    }
}