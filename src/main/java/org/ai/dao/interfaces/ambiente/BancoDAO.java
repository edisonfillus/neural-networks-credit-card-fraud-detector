package org.ai.dao.interfaces.ambiente;

import java.util.List;

import org.ai.bo.ambiente.Banco;

/**
 * Interface DAO para o objeto Banco
 * @author Edison Klafke Fillus
 * @author Jefferson de Souza Campos
 */
public interface BancoDAO {

     /**
     * Método para Inserir um objeto Banco
     * @param banco Banco
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean createBanco(Banco banco);
    
    /**
     * Método para Atualizar um objeto Banco
     * @param banco Banco
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean updateBanco(Banco banco);

    /**
     * Método para Excluir um objeto Banco
     * @param banco Banco
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean deleteBanco(Banco banco);

    /**
     * Método para localizar um objeto Banco
     * @param id
     * @return Banco
     */
    public Banco findBanco(int id);
    
     /**
     * Método para obter uma lista de objetos Banco
     * @return
     */
    public List<Banco> listBanco();

     /**
     * Método para obter a contagem de objetos Banco
     * @return
     */
    public int getBancoCount();

}
