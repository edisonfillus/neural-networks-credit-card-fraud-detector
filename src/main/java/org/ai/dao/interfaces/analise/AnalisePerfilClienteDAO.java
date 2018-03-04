package org.ai.dao.interfaces.analise;

import java.util.List;

import org.ai.bo.analise.AnalisePerfilCliente;

/**
 * Interface DAO para o objeto AnalisePerfilCliente
 * @author Edison Klafke Fillus
 * @author Jefferson de Souza Campos
 */
public interface AnalisePerfilClienteDAO {

     /**
     * Método para Inserir um objeto AnalisePerfilCliente
     * @param analisePerfilCliente AnalisePerfilCliente
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean createAnalisePerfilCliente(AnalisePerfilCliente analisePerfilCliente);
    
    /**
     * Método para Atualizar um objeto AnalisePerfilCliente
     * @param analisePerfilCliente AnalisePerfilCliente
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean updateAnalisePerfilCliente(AnalisePerfilCliente analisePerfilCliente);

    /**
     * Método para Excluir um objeto AnalisePerfilCliente
     * @param analisePerfilCliente AnalisePerfilCliente
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean deleteAnalisePerfilCliente(AnalisePerfilCliente analisePerfilCliente);

    /**
     * Método para localizar um objeto AnalisePerfilCliente
     * @param id
     * @return AnalisePerfilCliente
     */
    public AnalisePerfilCliente findAnalisePerfilCliente(int id);
    
     /**
     * Método para obter uma lista de objetos AnalisePerfilCliente
     * @return
     */
    public List<AnalisePerfilCliente> listAnalisePerfilCliente();

     /**
     * Método para obter a contagem de objetos AnalisePerfilCliente
     * @return
     */
    public int getAnalisePerfilClienteCount();

}
