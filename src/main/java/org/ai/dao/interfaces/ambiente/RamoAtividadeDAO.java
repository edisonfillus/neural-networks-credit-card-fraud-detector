package org.ai.dao.interfaces.ambiente;

import java.util.List;

import org.ai.bo.ambiente.RamoAtividade;

/**
 * Interface DAO para o objeto RamoAtividade
 * @author Edison Klafke Fillus
 * @author Jefferson de Souza Campos
 */
public interface RamoAtividadeDAO {

     /**
     * Método para Inserir um objeto RamoAtividade
     * @param ramoAtividade RamoAtividade
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean createRamoAtividade(RamoAtividade ramoAtividade);
    
    /**
     * Método para Atualizar um objeto RamoAtividade
     * @param ramoAtividade RamoAtividade
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean updateRamoAtividade(RamoAtividade ramoAtividade);

    /**
     * Método para Excluir um objeto RamoAtividade
     * @param ramoAtividade RamoAtividade
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean deleteRamoAtividade(RamoAtividade ramoAtividade);

    /**
     * Método para localizar um objeto RamoAtividade
     * @param id
     * @return RamoAtividade
     */
    public RamoAtividade findRamoAtividade(int id);
    
     /**
     * Método para obter uma lista de objetos RamoAtividade
     * @return
     */
    public List<RamoAtividade> listRamoAtividade();

     /**
     * Método para obter a contagem de objetos RamoAtividade
     * @return
     */
    public int getRamoAtividadeCount();

}