package org.ai.dao.interfaces.ambiente;

import java.util.List;

import org.ai.bo.ambiente.Item;

/**
 * Interface DAO para o objeto Item
 * @author Edison Klafke Fillus
 * @author Jefferson de Souza Campos
 */
public interface ItemDAO {

     /**
     * Método para Inserir um objeto Item
     * @param item Item
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean createItem(Item item);
    
    /**
     * Método para Atualizar um objeto Item
     * @param item Item
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean updateItem(Item item);

    /**
     * Método para Excluir um objeto Item
     * @param item Item
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean deleteItem(Item item);

    /**
     * Método para localizar um objeto Item
     * @param id
     * @return Item
     */
    public Item findItem(int id);
    
     /**
     * Método para obter uma lista de objetos Item
     * @return
     */
    public List<Item> listItem();

     /**
     * Método para obter a contagem de objetos Item
     * @return
     */
    public int getItemCount();

}
