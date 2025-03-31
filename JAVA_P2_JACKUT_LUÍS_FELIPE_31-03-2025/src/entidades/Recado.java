package entidades;

import java.io.Serial;
import java.io.Serializable;

/**
 * Classe que representa um recado/mensagem no sistema.
 * Cont�m o conte�do da mensagem e implementa Serializable para permitir
 * a serializa��o dos objetos para armazenamento persistente.
 */
public class Recado implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * O conte�do textual da mensagem.
     * Armazenado como final para garantir imutabilidade ap�s cria��o.
     */
    private final String mensagem;

    /**
     * Constr�i um novo recado com remetente e mensagem.
     *
     * @param remetente Login/nome do usu�rio que enviou o recado
     * @param mensagem Conte�do textual do recado (n�o pode ser nulo ou vazio)
     * @throws IllegalArgumentException Se a mensagem for nula ou vazia
     */
    public Recado(String remetente, String mensagem) {
        if (mensagem == null || mensagem.trim().isEmpty()) {
            throw new IllegalArgumentException("Mensagem n�o pode ser vazia");
        }
        this.mensagem = mensagem;
    }

    /**
     * Obt�m o conte�do da mensagem.
     *
     * @return String com o texto completo da mensagem
     */
    public String getMensagem() {
        return mensagem;
    }

    /**
     * Retorna uma representa��o em string do recado.
     * Neste caso, retorna apenas o conte�do da mensagem.
     *
     * @return String contendo o texto da mensagem
     */
    @Override
    public String toString() {
        return mensagem;
    }
}