package entidades;

import java.io.*;
import java.util.*;
import entidades.exceptions.*;

/**
 * Classe que representa um usuário do sistema, contendo informações pessoais,
 * atributos de perfil, relacionamentos de amizade e sistema de mensagens.
 * Implementa Serializable para permitir serialização dos objetos.
 */
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String login;
    private final String senha;
    private final String nome;
    private final Map<String, String> atributos;
    private final Queue<Recado> recados;
    private final Map<String, Boolean> amigos; // true=amigo confirmado, false=convite pendente
    private final LinkedHashSet<String> ordemAmizade; // Mantém a ordem cronológica das amizades confirmadas

    /**
     * Constrói um novo usuário com informações básicas.
     *
     * @param login Identificador único do usuário (não pode ser vazio)
     * @param senha Senha do usuário (não pode ser vazia)
     * @param nome Nome completo do usuário (pode ser vazio)
     * @throws LoginInvalidoException Se o login for nulo ou vazio
     * @throws SenhaInvalidaException Se a senha for nula ou vazia
     */
    public Usuario(String login, String senha, String nome) {
        if (login == null || login.trim().isEmpty()) {
            throw new LoginInvalidoException();
        }
        if (senha == null || senha.trim().isEmpty()) {
            throw new SenhaInvalidaException();
        }
        this.login = login;
        this.senha = senha;
        this.nome = nome != null ? nome : "";
        this.atributos = new HashMap<>();
        this.recados = new LinkedList<>();
        this.amigos = new HashMap<>();
        this.ordemAmizade = new LinkedHashSet<>();
    }

    // Métodos de amizade

    /**
     * Adiciona uma nova amizade ou convite de amizade.
     *
     * @param amigo Login do usuário amigo
     * @param confirmada true para amizade confirmada, false para convite pendente
     */
    public void adicionarAmizade(String amigo, boolean confirmada) {
        if (confirmada) {
            amigos.put(amigo, true);
            ordemAmizade.add(amigo); // Adiciona na ordem de confirmação
        } else {
            if (!amigos.containsKey(amigo)) {
                amigos.put(amigo, false);
            }
        }
    }

    /**
     * Verifica se um usuário é amigo confirmado.
     *
     * @param amigo Login do usuário a verificar
     * @return true se for amigo confirmado, false caso contrário
     */
    public boolean ehAmigo(String amigo) {
        return amigos.containsKey(amigo) && amigos.get(amigo);
    }

    /**
     * Verifica se existe um convite de amizade pendente de/para um usuário.
     *
     * @param amigo Login do usuário a verificar
     * @return true se houver convite pendente, false caso contrário
     */
    public boolean temConvitePendente(String amigo) {
        return amigos.containsKey(amigo) && !amigos.get(amigo);
    }

    /**
     * Obtém a lista de amigos confirmados em ordem cronológica de confirmação.
     *
     * @return Conjunto ordenado de logins dos amigos
     */
    public LinkedHashSet<String> getAmigosOrdenados() {
        LinkedHashSet<String> amigosConfirmados = new LinkedHashSet<>();
        // Retorna na ordem de confirmação
        for (String amigo : ordemAmizade) {
            if (amigos.get(amigo)) {
                amigosConfirmados.add(amigo);
            }
        }
        return amigosConfirmados;
    }

    // Métodos de recados

    /**
     * Recebe um novo recado de outro usuário.
     *
     * @param remetente Login do usuário remetente
     * @param mensagem Conteúdo da mensagem
     * @throws MensagemInvalidaException Se a mensagem for nula ou vazia
     */
    public void receberRecado(String remetente, String mensagem) {
        if (mensagem == null || mensagem.trim().isEmpty()) {
            throw new MensagemInvalidaException();
        }
        this.recados.add(new Recado(remetente, mensagem));
    }

    /**
     * Lê o próximo recado não lido da fila.
     *
     * @return String formatada com o recado ou null se não houver recados
     */
    public String lerRecado() {
        Recado recado = recados.poll();
        return recado != null ? recado.toString() : null;
    }

    // Métodos de perfil

    /**
     * Edita ou remove um atributo do perfil do usuário.
     *
     * @param atributo Nome do atributo a ser modificado
     * @param valor Novo valor do atributo (se vazio ou nulo, remove o atributo)
     */
    public void editarAtributo(String atributo, String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            atributos.remove(atributo);
        } else {
            atributos.put(atributo, valor);
        }
    }

    /**
     * Verifica se o usuário possui um determinado atributo de perfil.
     *
     * @param atributo Nome do atributo a verificar
     * @return true se o atributo existir, false caso contrário
     */
    public boolean possuiAtributo(String atributo) {
        return atributos.containsKey(atributo);
    }

    /**
     * Obtém o valor de um atributo de perfil.
     *
     * @param atributo Nome do atributo
     * @return Valor do atributo ou null se não existir
     */
    public String getAtributo(String atributo) {
        return atributos.get(atributo);
    }

    // Getters

    /**
     * Obtém o nome completo do usuário.
     *
     * @return Nome do usuário
     */
    public String getNome() {
        return nome;
    }

    /**
     * Verifica se a senha fornecida corresponde à senha do usuário.
     *
     * @param senha Senha a ser verificada
     * @return true se as senhas coincidirem, false caso contrário
     */
    public boolean verificarSenha(String senha) {
        return this.senha.equals(senha);
    }

    /**
     * Obtém o login do usuário.
     *
     * @return Login do usuário
     */
    public String getLogin() {
        return login;
    }

    // Métodos de serialização

    /**
     * Método personalizado para serialização do objeto.
     *
     * @param out Stream de saída para escrita do objeto
     * @throws IOException Se ocorrer um erro de I/O durante a serialização
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    /**
     * Método personalizado para desserialização do objeto.
     *
     * @param in Stream de entrada para leitura do objeto
     * @throws IOException Se ocorrer um erro de I/O durante a desserialização
     * @throws ClassNotFoundException Se a classe do objeto serializado não for encontrada
     * @throws InvalidObjectException Se os dados do usuário forem inválidos após desserialização
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        // Validação após desserialização
        if (login == null || login.trim().isEmpty() ||
                senha == null || senha.trim().isEmpty()) {
            throw new InvalidObjectException("Dados do usuário inválidos após desserialização");
        }
    }
}