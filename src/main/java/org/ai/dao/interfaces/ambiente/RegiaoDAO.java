package org.ai.dao.interfaces.ambiente;

import java.util.List;

import org.ai.bo.ambiente.Regiao;

/**
 * Interface DAO para o objeto Regiao
 * @author Edison Klafke Fillus
 * @author Jefferson de Souza Campos
 */
public interface RegiaoDAO {

     /**
     * Método para Inserir um objeto Regiao
     * @param regiao Regiao
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean createRegiao(Regiao regiao);
    
    /**
     * Método para Atualizar um objeto Regiao
     * @param regiao Regiao
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean updateRegiao(Regiao regiao);

    /**
     * Método para Excluir um objeto Regiao
     * @param regiao Regiao
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean deleteRegiao(Regiao regiao);

    /**
     * Método para localizar um objeto Regiao
     * @param id
     * @return Regiao
     */
    public Regiao findRegiao(int id);
    
     /**
     * Método para obter uma lista de objetos Regiao
     * @return
     */
    public List<Regiao> listRegiao();

     /**
     * Método para obter a contagem de objetos Regiao
     * @return
     */
    public int getRegiaoCount();

}
