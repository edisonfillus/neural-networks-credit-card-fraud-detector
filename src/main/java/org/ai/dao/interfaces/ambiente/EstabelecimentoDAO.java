package org.ai.dao.interfaces.ambiente;

import java.util.List;

import org.ai.bo.ambiente.Estabelecimento;

/**
 * Interface DAO para o objeto Estabelecimento
 * @author Edison Klafke Fillus
 * @author Jefferson de Souza Campos
 */
public interface EstabelecimentoDAO {

     /**
     * Método para Inserir um objeto Estabelecimento
     * @param estabelecimento Estabelecimento
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean createEstabelecimento(Estabelecimento estabelecimento);
    
    /**
     * Método para Atualizar um objeto Estabelecimento
     * @param estabelecimento Estabelecimento
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean updateEstabelecimento(Estabelecimento estabelecimento);

    /**
     * Método para Excluir um objeto Estabelecimento
     * @param estabelecimento Estabelecimento
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean deleteEstabelecimento(Estabelecimento estabelecimento);

    /**
     * Método para localizar um objeto Estabelecimento
     * @param id
     * @return Estabelecimento
     */
    public Estabelecimento findEstabelecimento(int id);
    
     /**
     * Método para obter uma lista de objetos Estabelecimento
     * @return
     */
    public List<Estabelecimento> listEstabelecimento();

     /**
     * Método para obter a contagem de objetos Estabelecimento
     * @return
     */
    public int getEstabelecimentoCount();

}
