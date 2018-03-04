package org.ai.dao.interfaces.analise;

import java.util.List;

import org.ai.bo.analise.AnaliseTransacao;

/**
 * Interface DAO para o objeto AnaliseTransacao
 * @author Edison Klafke Fillus
 * @author Jefferson de Souza Campos
 */
public interface AnaliseTransacaoDAO {

     /**
     * Método para Inserir um objeto AnaliseTransacao
     * @param analiseTransacao AnaliseTransacao
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean createAnaliseTransacao(AnaliseTransacao analiseTransacao);
    
    /**
     * Método para Atualizar um objeto AnaliseTransacao
     * @param analiseTransacao AnaliseTransacao
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean updateAnaliseTransacao(AnaliseTransacao analiseTransacao);

    /**
     * Método para Excluir um objeto AnaliseTransacao
     * @param analiseTransacao AnaliseTransacao
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean deleteAnaliseTransacao(AnaliseTransacao analiseTransacao);

    /**
     * Método para localizar um objeto AnaliseTransacao
     * @param id
     * @return AnaliseTransacao
     */
    public AnaliseTransacao findAnaliseTransacao(int id);
    
     /**
     * Método para obter uma lista de objetos AnaliseTransacao
     * @return
     */
    public List<AnaliseTransacao> listAnaliseTransacao();

     /**
     * Método para obter a contagem de objetos AnaliseTransacao
     * @return
     */
    public int getAnaliseTransacaoCount();

}
