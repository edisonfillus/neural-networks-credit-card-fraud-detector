package org.ai.dao.interfaces.ambiente;

import java.util.List;

import org.ai.bo.ambiente.Estado;

/**
 * Interface DAO para o objeto Estado
 * @author Edison Klafke Fillus
 * @author Jefferson de Souza Campos
 */
public interface EstadoDAO {

     /**
     * Método para Inserir um objeto Estado
     * @param estado Estado
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean createEstado(Estado estado);
    
    /**
     * Método para Atualizar um objeto Estado
     * @param estado Estado
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean updateEstado(Estado estado);

    /**
     * Método para Excluir um objeto Estado
     * @param estado Estado
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean deleteEstado(Estado estado);

    /**
     * Método para localizar um objeto Estado
     * @param id
     * @return Estado
     */
    public Estado findEstado(int id);
    
     /**
     * Método para obter uma lista de objetos Estado
     * @return
     */
    public List<Estado> listEstado();

     /**
     * Método para obter a contagem de objetos Estado
     * @return
     */
    public int getEstadoCount();

}
