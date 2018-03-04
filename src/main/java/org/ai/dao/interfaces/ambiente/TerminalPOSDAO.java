package org.ai.dao.interfaces.ambiente;

import java.util.List;

import org.ai.bo.ambiente.Cidade;
import org.ai.bo.ambiente.RamoAtividade;
import org.ai.bo.ambiente.TerminalPOS;

/**
 * Interface DAO para o objeto TerminalPOS
 * @author Edison Klafke Fillus
 * @author Jefferson de Souza Campos
 */
public interface TerminalPOSDAO {

     /**
     * Método para Inserir um objeto TerminalPOS
     * @param terminalPOS TerminalPOS
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean createTerminalPOS(TerminalPOS terminalPOS);
    
    /**
     * Método para Atualizar um objeto TerminalPOS
     * @param terminalPOS TerminalPOS
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean updateTerminalPOS(TerminalPOS terminalPOS);

    /**
     * Método para Excluir um objeto TerminalPOS
     * @param terminalPOS TerminalPOS
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean deleteTerminalPOS(TerminalPOS terminalPOS);

    /**
     * Método para localizar um objeto TerminalPOS
     * @param id
     * @return TerminalPOS
     */
    public TerminalPOS findTerminalPOS(int id);
    
     /**
     * Método para obter uma lista de objetos TerminalPOS
     * @return
     */
    public List<TerminalPOS> listTerminalPOS();

     /**
     * Método para obter a contagem de objetos TerminalPOS
     * @return
     */
    public int getTerminalPOSCount();
    
    /**
     * Método para obter uma lista de objetos TerminalPOS de uma determinada cidade e determinado ramo de atividade
     * @param cidade
     * @param ramoAtividade
     * @return
     */
    public List<TerminalPOS> listTerminalPOSByCidadeAndRamoAtividade(Cidade cidade, RamoAtividade ramoAtividade);

    /**
     * Método para obter uma lista de objetos TerminalPOS de uma determinada regiao e determinado ramo de atividade,
     * excluindo determinada cidade
     * @param cidade
     * @param ramoAtividade
     * @return
     */
    public List<TerminalPOS> listTerminalPOSByRegiaoAndRamoAtividadeExcluindoCidade(Cidade cidade, RamoAtividade ramoAtividade);


    /**
     * Método para obter uma lista de objetos TerminalPOS de um determinado estado e determinado ramo de atividade,
     * excluindo determinada regiao
     * @param cidade
     * @param ramoAtividade
     * @return
     */
    public List<TerminalPOS> listTerminalPOSByEstadoAndRamoAtividadeExcluindoRegiao(Cidade cidade, RamoAtividade ramoAtividade);

     /**
     * Método para obter uma lista de objetos TerminalPOS de um determinado ramo de atividade,
     * excluindo determinado estado
     * @param cidade
     * @param ramoAtividade
     * @return
     */
    public List<TerminalPOS> listTerminalPOSByRamoAtividadeExcluindoEstado(Cidade cidade, RamoAtividade ramoAtividade);


}