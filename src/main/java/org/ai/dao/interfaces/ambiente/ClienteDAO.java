package org.ai.dao.interfaces.ambiente;

import java.util.List;

import org.ai.bo.ambiente.Cliente;

/**
 * Interface DAO para o objeto Cliente
 * @author Edison Klafke Fillus
 * @author Jefferson de Souza Campos
 */
public interface ClienteDAO {

     /**
     * Método para Inserir um objeto Cliente
     * @param cliente Cliente
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean createCliente(Cliente cliente);
    
    /**
     * Método para Atualizar um objeto Cliente
     * @param cliente Cliente
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean updateCliente(Cliente cliente);

    /**
     * Método para Excluir um objeto Cliente
     * @param cliente Cliente
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean deleteCliente(Cliente cliente);

    /**
     * Método para localizar um objeto Cliente
     * @param id
     * @return Cliente
     */
    public Cliente findCliente(int id);
    
     /**
     * Método para obter uma lista de objetos Cliente
     * @return
     */
    public List<Cliente> listCliente();

     /**
     * Método para obter a contagem de objetos Cliente
     * @return
     */
    public int getClienteCount();
    
}
