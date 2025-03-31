package entidades;

import java.io.*;
import java.util.*;
import entidades.exceptions.*;

/**
 * Classe que representa um usu�rio do sistema, contendo informa��es pessoais,
 * atributos de perfil, relacionamentos de amizade e sistema de mensagens.
 * Implementa Serializable para permitir serializa��o dos objetos.
 */
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String login;
    private final String senha;
    private final String nome;
    private final Map<String, String> atributos;
    private final Queue<Recado> recados;
    private final Map<String, Boolean> amigos; // true=amigo confirmado, false=convite pendente
    private final LinkedHashSet<String> ordemAmizade; // Mant�m a ordem cronol�gica das amizades confirmadas

    /**
     * Constr�i um novo usu�rio com informa��es b�sicas.
     *
     * @param login Identificador �nico do usu�rio (n�o pode ser vazio)
     * @param senha Senha do usu�rio (n�o pode ser vazia)
     * @param nome Nome completo do usu�rio (pode ser vazio)
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

    // M�todos de amizade

    /**
     * Adiciona uma nova amizade ou convite de amizade.
     *
     * @param amigo Login do usu�rio amigo
     * @param confirmada true para amizade confirmada, false para convite pendente
     */
    public void adicionarAmizade(String amigo, boolean confirmada) {
        if (confirmada) {
            amigos.put(amigo, true);
            ordemAmizade.add(amigo); // Adiciona na ordem de confirma��o
        } else {
            if (!amigos.containsKey(amigo)) {
                amigos.put(amigo, false);
            }
        }
    }

    /**
     * Verifica se um usu�rio � amigo confirmado.
     *
     * @param amigo Login do usu�rio a verificar
     * @return true se for amigo confirmado, false caso contr�rio
     */
    public boolean ehAmigo(String amigo) {
        return amigos.containsKey(amigo) && amigos.get(amigo);
    }

    /**
     * Verifica se existe um convite de amizade pendente de/para um usu�rio.
     *
     * @param amigo Login do usu�rio a verificar
     * @return true se houver convite pendente, false caso contr�rio
     */
    public boolean temConvitePendente(String amigo) {
        return amigos.containsKey(amigo) && !amigos.get(amigo);
    }

    /**
     * Obt�m a lista de amigos confirmados em ordem cronol�gica de confirma��o.
     *
     * @return Conjunto ordenado de logins dos amigos
     */
    public LinkedHashSet<String> getAmigosOrdenados() {
        LinkedHashSet<String> amigosConfirmados = new LinkedHashSet<>();
        // Retorna na ordem de confirma��o
        for (String amigo : ordemAmizade) {
            if (amigos.get(amigo)) {
                amigosConfirmados.add(amigo);
            }
        }
        return amigosConfirmados;
    }

    // M�todos de recados

    /**
     * Recebe um novo recado de outro usu�rio.
     *
     * @param remetente Login do usu�rio remetente
     * @param mensagem Conte�do da mensagem
     * @throws MensagemInvalidaException Se a mensagem for nula ou vazia
     */
    public void receberRecado(String remetente, String mensagem) {
        if (mensagem == null || mensagem.trim().isEmpty()) {
            throw new MensagemInvalidaException();
        }
        this.recados.add(new Recado(remetente, mensagem));
    }

    /**
     * L� o pr�ximo recado n�o lido da fila.
     *
     * @return String formatada com o recado ou null se n�o houver recados
     */
    public String lerRecado() {
        Recado recado = recados.poll();
        return recado != null ? recado.toString() : null;
    }

    // M�todos de perfil

    /**
     * Edita ou remove um atributo do perfil do usu�rio.
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
     * Verifica se o usu�rio possui um determinado atributo de perfil.
     *
     * @param atributo Nome do atributo a verificar
     * @return true se o atributo existir, false caso contr�rio
     */
    public boolean possuiAtributo(String atributo) {
        return atributos.containsKey(atributo);
    }

    /**
     * Obt�m o valor de um atributo de perfil.
     *
     * @param atributo Nome do atributo
     * @return Valor do atributo ou null se n�o existir
     */
    public String getAtributo(String atributo) {
        return atributos.get(atributo);
    }

    // Getters

    /**
     * Obt�m o nome completo do usu�rio.
     *
     * @return Nome do usu�rio
     */
    public String getNome() {
        return nome;
    }

    /**
     * Verifica se a senha fornecida corresponde � senha do usu�rio.
     *
     * @param senha Senha a ser verificada
     * @return true se as senhas coincidirem, false caso contr�rio
     */
    public boolean verificarSenha(String senha) {
        return this.senha.equals(senha);
    }

    /**
     * Obt�m o login do usu�rio.
     *
     * @return Login do usu�rio
     */
    public String getLogin() {
        return login;
    }

    // M�todos de serializa��o

    /**
     * M�todo personalizado para serializa��o do objeto.
     *
     * @param out Stream de sa�da para escrita do objeto
     * @throws IOException Se ocorrer um erro de I/O durante a serializa��o
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    /**
     * M�todo personalizado para desserializa��o do objeto.
     *
     * @param in Stream de entrada para leitura do objeto
     * @throws IOException Se ocorrer um erro de I/O durante a desserializa��o
     * @throws ClassNotFoundException Se a classe do objeto serializado n�o for encontrada
     * @throws InvalidObjectException Se os dados do usu�rio forem inv�lidos ap�s desserializa��o
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        // Valida��o ap�s desserializa��o
        if (login == null || login.trim().isEmpty() ||
                senha == null || senha.trim().isEmpty()) {
            throw new InvalidObjectException("Dados do usu�rio inv�lidos ap�s desserializa��o");
        }
    }
}