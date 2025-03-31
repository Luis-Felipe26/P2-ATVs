package entidades;

import java.io.*;
import java.util.Map;
import entidades.exceptions.*;

/**
 * Classe respons�vel pela persist�ncia dos dados do sistema Jackut.
 * Oferece opera��es para salvar, carregar e limpar dados de usu�rios,
 * com suporte a backup autom�tico e recupera��o em caso de falhas.
 *
 * <p>Os dados s�o armazenados em um arquivo serializado no diret�rio 'dados/',
 * com um arquivo de backup criado automaticamente antes de cada opera��o de salvamento.</p>
 */
public class Persistencia {
    private static final String ARQUIVO_DADOS = "dados_jackut.ser";
    private static final String DIRETORIO_DADOS = "dados";
    private static final String BACKUP_SUFFIX = "_bkp";

    /**
     * Salva o mapa de usu�rios no arquivo de dados.
     *
     * <p>Antes de salvar, cria um backup do arquivo atual. Se ocorrer um erro durante o salvamento,
     * tenta restaurar o backup automaticamente.</p>
     *
     * @param usuarios Mapa de usu�rios a ser salvo
     * @throws PersistenciaException Se ocorrer um erro durante a opera��o de salvamento
     */
    public static void salvarDados(Map<String, Usuario> usuarios) throws PersistenciaException {
        criarDiretorioSeNaoExistir();
        fazerBackup(); // Cria backup antes de sobrescrever

        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(getCaminhoCompleto()))) {
            out.writeObject(usuarios);
        } catch (IOException e) {
            restaurarBackup(); // Tenta restaurar se falhar
            throw new PersistenciaException(
                    "Falha ao salvar dados dos usu�rios",
                    "arquivo de dados",
                    e.getMessage(),
                    e
            );
        }
    }

    /**
     * Carrega os dados de usu�rios do arquivo de persist�ncia.
     *
     * <p>Se o arquivo n�o existir, retorna um novo mapa vazio. Verifica a integridade
     * dos dados carregados, garantindo que seja uma inst�ncia de Map&lt;String, Usuario&gt;.</p>
     *
     * @return Mapa contendo os usu�rios carregados
     * @throws PersistenciaException Se ocorrer um erro durante o carregamento ou se os dados estiverem corrompidos
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Usuario> carregarDados() throws PersistenciaException {
        File arquivo = new File(getCaminhoCompleto());
        if (!arquivo.exists()) {
            return new java.util.HashMap<>();
        }

        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(getCaminhoCompleto()))) {
            Object obj = in.readObject();

            if (!(obj instanceof Map)) {
                throw new PersistenciaException(
                        "Formato de dados inv�lido",
                        "arquivo de dados",
                        "O arquivo n�o cont�m um mapa de usu�rios v�lido"
                );
            }

            return (Map<String, Usuario>) obj;
        } catch (FileNotFoundException e) {
            return new java.util.HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new PersistenciaException(
                    "Falha ao carregar dados dos usu�rios",
                    "arquivo de dados",
                    e.getMessage(),
                    e
            );
        }
    }

    /**
     * Remove permanentemente todos os dados persistidos, incluindo o arquivo principal e o backup.
     *
     * @throws PersistenciaException Se ocorrer um erro durante a remo��o dos arquivos
     */
    public static void limparDados() throws PersistenciaException {
        File arquivo = new File(getCaminhoCompleto());
        File backup = new File(getCaminhoBackup());

        boolean sucesso = true;
        if (arquivo.exists()) {
            sucesso = arquivo.delete() && sucesso;
        }
        if (backup.exists()) {
            sucesso = backup.delete() && sucesso;
        }

        if (!sucesso) {
            throw new PersistenciaException(
                    "Falha ao limpar dados persistentes",
                    "arquivo de dados/backup",
                    "N�o foi poss�vel remover os arquivos de dados"
            );
        }
    }

    // M�todos auxiliares privados

    /**
     * Cria o diret�rio de dados se ele n�o existir.
     */
    private static void criarDiretorioSeNaoExistir() {
        File dir = new File(DIRETORIO_DADOS);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Cria um backup do arquivo de dados atual.
     */
    private static void fazerBackup() {
        File original = new File(getCaminhoCompleto());
        File backup = new File(getCaminhoBackup());

        if (original.exists()) {
            original.renameTo(backup);
        }
    }

    /**
     * Restaura o backup como arquivo principal de dados.
     */
    private static void restaurarBackup() {
        File original = new File(getCaminhoCompleto());
        File backup = new File(getCaminhoBackup());

        if (backup.exists()) {
            backup.renameTo(original);
        }
    }

    /**
     * Obt�m o caminho completo para o arquivo de dados principal.
     *
     * @return String com o caminho absoluto do arquivo de dados
     */
    private static String getCaminhoCompleto() {
        return DIRETORIO_DADOS + File.separator + ARQUIVO_DADOS;
    }

    /**
     * Obt�m o caminho completo para o arquivo de backup.
     *
     * @return String com o caminho absoluto do arquivo de backup
     */
    private static String getCaminhoBackup() {
        return DIRETORIO_DADOS + File.separator + ARQUIVO_DADOS + BACKUP_SUFFIX;
    }
}

