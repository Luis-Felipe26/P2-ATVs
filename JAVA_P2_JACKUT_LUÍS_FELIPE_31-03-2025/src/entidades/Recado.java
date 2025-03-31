package entidades;

import java.io.Serial;
import java.io.Serializable;

/**
 * Classe que representa um recado/mensagem no sistema.
 * Contém o conteúdo da mensagem e implementa Serializable para permitir
 * a serialização dos objetos para armazenamento persistente.
 */
public class Recado implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * O conteúdo textual da mensagem.
     * Armazenado como final para garantir imutabilidade após criação.
     */
    private final String mensagem;

    /**
     * Constrói um novo recado com remetente e mensagem.
     *
     * @param remetente Login/nome do usuário que enviou o recado
     * @param mensagem Conteúdo textual do recado (não pode ser nulo ou vazio)
     * @throws IllegalArgumentException Se a mensagem for nula ou vazia
     */
    public Recado(String remetente, String mensagem) {
        if (mensagem == null || mensagem.trim().isEmpty()) {
            throw new IllegalArgumentException("Mensagem não pode ser vazia");
        }
        this.mensagem = mensagem;
    }

    /**
     * Obtém o conteúdo da mensagem.
     *
     * @return String com o texto completo da mensagem
     */
    public String getMensagem() {
        return mensagem;
    }

    /**
     * Retorna uma representação em string do recado.
     * Neste caso, retorna apenas o conteúdo da mensagem.
     *
     * @return String contendo o texto da mensagem
     */
    @Override
    public String toString() {
        return mensagem;
    }
}