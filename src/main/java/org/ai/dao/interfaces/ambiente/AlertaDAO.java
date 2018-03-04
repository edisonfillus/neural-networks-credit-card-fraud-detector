package org.ai.dao.interfaces.ambiente;

import java.util.List;

import org.ai.bo.ambiente.Alerta;

/**
 * Interface DAO para o objeto Alerta
 * @author Edison Klafke Fillus
 * @author Jefferson de Souza Campos
 */
public interface AlertaDAO {

     /**
     * Método para Inserir um objeto Alerta
     * @param alerta Alerta
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean createAlerta(Alerta alerta);
    
    /**
     * Método para Atualizar um objeto Alerta
     * @param alerta Alerta
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean updateAlerta(Alerta alerta);

    /**
     * Método para Excluir um objeto Alerta
     * @param alerta Alerta
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean deleteAlerta(Alerta alerta);

    /**
     * Método para localizar um objeto Alerta
     * @param id
     * @return Alerta
     */
    public Alerta findAlerta(int id);
    
     /**
     * Método para obter uma lista de objetos Alerta
     * @return
     */
    public List<Alerta> listAlerta();

     /**
     * Método para obter a contagem de objetos Alerta
     * @return
     */
    public int getAlertaCount();

}