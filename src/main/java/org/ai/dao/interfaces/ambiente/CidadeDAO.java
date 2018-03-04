package org.ai.dao.interfaces.ambiente;

import java.util.List;

import org.ai.bo.ambiente.Cidade;

/**
 * Interface DAO para o objeto Cidade
 * @author Edison Klafke Fillus
 * @author Jefferson de Souza Campos
 */
public interface CidadeDAO {

     /**
     * Método para Inserir um objeto Cidade
     * @param cidade Cidade
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean createCidade(Cidade cidade);
    
    /**
     * Método para Atualizar um objeto Cidade
     * @param cidade Cidade
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean updateCidade(Cidade cidade);

    /**
     * Método para Excluir um objeto Cidade
     * @param cidade Cidade
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean deleteCidade(Cidade cidade);

    /**
     * Método para localizar um objeto Cidade
     * @param id
     * @return Cidade
     */
    public Cidade findCidade(int id);
    
     /**
     * Método para obter uma lista de objetos Cidade
     * @return
     */
    public List<Cidade> listCidade();

     /**
     * Método para obter a contagem de objetos Cidade
     * @return
     */
    public int getCidadeCount();

}
