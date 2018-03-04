package org.ai.dao.interfaces.ambiente;

import java.util.List;

import org.ai.bo.ambiente.Analista;

/**
 * Interface DAO para o objeto Analista
 * @author Edison Klafke Fillus
 * @author Jefferson de Souza Campos
 */
public interface AnalistaDAO {

     /**
     * Método para Inserir um objeto Analista
     * @param analista Analista
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean createAnalista(Analista analista);
    
    /**
     * Método para Atualizar um objeto Analista
     * @param analista Analista
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean updateAnalista(Analista analista);

    /**
     * Método para Excluir um objeto Analista
     * @param analista Analista
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean deleteAnalista(Analista analista);

    /**
     * Método para localizar um objeto Analista
     * @param id
     * @return Analista
     */
    public Analista findAnalista(int id);
    
     /**
     * Método para obter uma lista de objetos Analista
     * @return
     */
    public List<Analista> listAnalista();

     /**
     * Método para obter a contagem de objetos Analista
     * @return
     */
    public int getAnalistaCount();

}

