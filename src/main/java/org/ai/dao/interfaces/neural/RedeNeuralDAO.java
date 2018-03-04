package org.ai.dao.interfaces.neural;

import java.util.List;

import org.ai.bo.neural.rede.RedeNeural;

/**
 * Interface DAO para o objeto RedeNeural
 * @author Edison Klafke Fillus
 * @author Jefferson de Souza Campos
 */
public interface RedeNeuralDAO {

     /**
     * Método para Inserir um objeto RedeNeural
     * @param redeNeural RedeNeural
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean createRedeNeural(RedeNeural redeNeural);
    
    /**
     * Método para Atualizar um objeto RedeNeural
     * @param redeNeural RedeNeural
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean updateRedeNeural(RedeNeural redeNeural);

    /**
     * Método para Excluir um objeto RedeNeural
     * @param redeNeural RedeNeural
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean deleteRedeNeural(RedeNeural redeNeural);

    /**
     * Método para localizar um objeto RedeNeural
     * @param id
     * @return RedeNeural
     */
    public RedeNeural findRedeNeural(int id);
    
     /**
     * Método para obter uma lista de objetos RedeNeural
     * @return
     */
    public List<RedeNeural> listRedeNeural();

     /**
     * Método para obter a contagem de objetos RedeNeural
     * @return
     */
    public int getRedeNeuralCount();

}