package org.ai.dao.interfaces.ambiente;

import java.util.List;

import org.ai.bo.ambiente.Cartao;

/**
 * Interface DAO para o objeto Cartao
 * @author Edison Klafke Fillus
 * @author Jefferson de Souza Campos
 */
public interface CartaoDAO {

     /**
     * Método para Inserir um objeto Cartao
     * @param cartao Cartao
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean createCartao(Cartao cartao);
    
    /**
     * Método para Atualizar um objeto Cartao
     * @param cartao Cartao
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean updateCartao(Cartao cartao);

    /**
     * Método para Excluir um objeto Cartao
     * @param cartao Cartao
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean deleteCartao(Cartao cartao);

    /**
     * Método para localizar um objeto Cartao
     * @param num
     * @return Cartao
     */
    public Cartao findCartao(String num);
    
     /**
     * Método para obter uma lista de objetos Cartao
     * @return
     */
    public List<Cartao> listCartao();

     /**
     * Método para obter a contagem de objetos Cartao
     * @return
     */
    public int getCartaoCount();

}
