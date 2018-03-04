package org.ai.dao.interfaces.ambiente;

import java.util.List;

import org.ai.bo.ambiente.Resposta;

/**
 * Interface DAO para o objeto Resposta
 * @author Edison Klafke Fillus
 * @author Jefferson de Souza Campos
 */
public interface RespostaDAO {

     /**
     * Método para Inserir um objeto Resposta
     * @param resposta Resposta
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean createResposta(Resposta resposta);
    
    /**
     * Método para Atualizar um objeto Resposta
     * @param resposta Resposta
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean updateResposta(Resposta resposta);

    /**
     * Método para Excluir um objeto Resposta
     * @param resposta Resposta
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean deleteResposta(Resposta resposta);

    /**
     * Método para localizar um objeto Resposta
     * @param id
     * @return Resposta
     */
    public Resposta findResposta(int id);
    
     /**
     * Método para obter uma lista de objetos Resposta
     * @return
     */
    public List<Resposta> listResposta();

     /**
     * Método para obter a contagem de objetos Resposta
     * @return
     */
    public int getRespostaCount();

}