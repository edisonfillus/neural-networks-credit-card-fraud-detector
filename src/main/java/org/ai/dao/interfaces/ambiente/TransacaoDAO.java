package org.ai.dao.interfaces.ambiente;

import java.util.List;

import org.ai.bo.ambiente.Transacao;

/**
 * Interface DAO para o objeto Transacao
 * @author Edison Klafke Fillus
 * @author Jefferson de Souza Campos
 */
public interface TransacaoDAO {

     /**
     * Método para Inserir um objeto Transacao
     * @param transacao Transacao
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean createTransacao(Transacao transacao);
    
    /**
     * Método para Atualizar um objeto Transacao
     * @param transacao Transacao
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean updateTransacao(Transacao transacao);

    /**
     * Método para Excluir um objeto Transacao
     * @param transacao Transacao
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean deleteTransacao(Transacao transacao);

    /**
     * Método para localizar um objeto Transacao
     * @param id
     * @return Transacao
     */
    public Transacao findTransacao(int id);
    
     /**
     * Método para obter uma lista de objetos Transacao
     * @return
     */
    public List<Transacao> listTransacao();

     /**
     * Método para obter a contagem de objetos Transacao
     * @return
     */
    public int getTransacaoCount();
    
     /**
     * Método para obter uma lista de objetos Transacao somente marcadas para treinamento
     * @return
     */
    public List<Transacao> listTransacaoOnlyTreinamento();
    
     /**
     * Método para obter a contagem de objetos Transacao somente marcadas para treinamento
     * @return
     */
    public int getCountTransacaoSelecionadaTreinamento() ;

}
