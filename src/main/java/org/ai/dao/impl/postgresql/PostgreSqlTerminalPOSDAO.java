package org.ai.dao.impl.postgresql;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;

import org.ai.bo.ambiente.Cidade;
import org.ai.bo.ambiente.RamoAtividade;
import org.ai.bo.ambiente.TerminalPOS;
import org.ai.dao.factory.PostgreSqlDAOFactory;
import org.ai.dao.interfaces.ambiente.TerminalPOSDAO;

/**
 * Implementação do DAO do objeto TerminalPOS para o SGBD PostgreSql
 * @author Edison Klafke Fillus
 * @author Jefferson de Souza Campos
 */
public class PostgreSqlTerminalPOSDAO extends PostgreSqlDAOFactory implements TerminalPOSDAO {

    // <editor-fold defaultstate="collapsed" desc="Parâmetros">

    private static String CLASS_NAME = TerminalPOS.class.getSimpleName();
    private static String OBJECT_INSTANCE_NAME = TerminalPOS.class.getSimpleName().toLowerCase();
    private static String SELECT_ALL_JPA = "SELECT OBJECT(" + OBJECT_INSTANCE_NAME + ") FROM " + CLASS_NAME + " AS " + OBJECT_INSTANCE_NAME;
    private static String SELECT_COUNT_JPA = "SELECT COUNT(" + OBJECT_INSTANCE_NAME + ") FROM " + CLASS_NAME + " AS " + OBJECT_INSTANCE_NAME;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construtores">
    public PostgreSqlTerminalPOSDAO() {
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Métodos DAO CRUD">
    
    /**
     * Método para Inserir um objeto TerminalPOS
     * @param terminalPOS TerminalPOS
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean createTerminalPOS(TerminalPOS terminalPOS) {
        boolean created = false; 
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        try {
            logger.debug("Inserindo objeto " + CLASS_NAME + ": " + terminalPOS.toString());
            em.getTransaction().begin(); //Inicia Transação
            em.persist(terminalPOS); //Insere
            em.getTransaction().commit(); //Confirma Transação
            created = true;
            logger.info("Objeto " + CLASS_NAME + " Inserido: " + terminalPOS.toString());
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback(); //Se ocorrer erro, desfaz alterações da transação
            }
            logger.error("Erro ao inserir objeto" + CLASS_NAME + ": " + ex.getMessage() , ex);
        } finally {
            if (!isKeepAliveJPAEntityManager()) {  //Se não for para manter o EntityManager, fecha
                closeJPAEntityManager();
            }
        }
        return created;
    }

    /**
     * Método para Atualizar um objeto TerminalPOS
     * @param terminalPOS TerminalPOS
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean updateTerminalPOS(TerminalPOS terminalPOS) {
        boolean updated = false;
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        try {
            logger.debug("Atualizando objeto " + CLASS_NAME + ": " + terminalPOS.toString());
            em.getTransaction().begin(); //Inicia Transação
            em.merge(terminalPOS); //Atualiza
            em.getTransaction().commit(); //Confirma Transação
            updated = true;
            logger.info("Objeto " + CLASS_NAME + " Atualizado: " + terminalPOS.toString());
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();  //Se ocorrer erro, desfaz alterações da transação
            }
            logger.error("Erro ao atualizar objeto " + CLASS_NAME + ": " + ex.getMessage() , ex);
        } finally {
            if (!isKeepAliveJPAEntityManager()) {  //Se não for para manter o EntityManager, fecha
                closeJPAEntityManager();
            }
        }
        return updated;
    }

    /**
     * Método para Excluir um objeto TerminalPOS
     * @param terminalPOS TerminalPOS
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean deleteTerminalPOS(TerminalPOS terminalPOS) {
        boolean deleted = false;
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        int id = terminalPOS.getId();
        try {
            logger.debug("Excluindo objeto " + CLASS_NAME + ": " + terminalPOS.toString());
            terminalPOS = em.getReference(TerminalPOS.class, id); //Vefica se existe referência no banco
            em.getTransaction().begin(); //Inicia Transação
            em.remove(terminalPOS); //Exclui
            em.getTransaction().commit(); //Confirma Transação
            deleted = true;
            logger.info("Objeto " + CLASS_NAME + " excluido: " + terminalPOS.toString());
        } catch (EntityNotFoundException enfe) { 
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();  //Se ocorrer erro, desfaz alterações da transação
            }
            logger.warn("Não foi encontrado objeto " + CLASS_NAME + " com id: " + id, enfe);
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback(); //Se ocorrer erro, desfaz alterações da transação
            }
            logger.error("Erro ao excluir objeto "+ CLASS_NAME + ": " + ex.getMessage() , ex);
        } finally {
            if (!isKeepAliveJPAEntityManager()) {  //Se não for para manter o EntityManager, fecha
                closeJPAEntityManager();
            }
        }
        return deleted;
    }

    /**
     * Método para localizar um objeto TerminalPOS
     * @param id
     * @return TerminalPOS
     */
    public TerminalPOS findTerminalPOS(int id) {
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        TerminalPOS terminalPOS = em.find(TerminalPOS.class, id); //Localiza terminalPOS
        em.refresh(terminalPOS);
        if (!isKeepAliveJPAEntityManager()) {  //Se não for para manter o EntityManager, fecha
            closeJPAEntityManager();
        }
        return terminalPOS;
        
    }

    /**
     * Método para obter uma lista de objetos TerminalPOS
     * @return
     */
    public List<TerminalPOS> listTerminalPOS() {
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        Query q = em.createQuery(SELECT_ALL_JPA); //Cria a string de busca
        List<TerminalPOS> list = q.getResultList(); //Executa a busca e retorna a lista
        if (!isKeepAliveJPAEntityManager()) {  //Se não for para manter o EntityManager, fecha
            closeJPAEntityManager();
        }
        return list;
    }

    /**
     * Método para obter a contagem de objetos TerminalPOS
     * @return
     */
    public int getTerminalPOSCount() {
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        int count = 0;
        try {
            count = ((Long) em.createQuery(SELECT_COUNT_JPA).getSingleResult()).intValue(); //Executa a contagem
        } finally {
            if (!isKeepAliveJPAEntityManager()) {  //Se não for para manter o EntityManager, fecha
                closeJPAEntityManager();
            }
        }
        return count;
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos DAO Complementares">
    /**
     * Método para obter uma lista de objetos TerminalPOS de uma determinada cidade e determinado ramo de atividade
     * @param cidade
     * @param ramoAtividade
     * @return
     */
    public List<TerminalPOS> listTerminalPOSByCidadeAndRamoAtividade(Cidade cidade, RamoAtividade ramoAtividade) {
        List<TerminalPOS> list = null;
        try {
            logger.debug("Consultando lista de objetos " + CLASS_NAME);
            EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
            Query q = em.createQuery(SELECT_ALL_JPA + " WHERE " + OBJECT_INSTANCE_NAME +".estabelecimento.cidade = :cidade AND " + OBJECT_INSTANCE_NAME + ".estabelecimento.ramoAtividade = :ramoAtividade"); //Cria a string de busca
            q.setParameter("cidade", cidade);
            q.setParameter("ramoAtividade", ramoAtividade);
            list = q.getResultList(); //Executa a busca e retorna a lista
            logger.info(list.size() + " objetos " + CLASS_NAME + " listados.");
        } catch (Exception ex) {
            logger.error("Erro ao consultar lista de objetos " + CLASS_NAME + ": " + ex.getMessage(), ex);
        } finally {
            if (!isKeepAliveJPAEntityManager()) {  //Se não for para manter o EntityManager, fecha
                closeJPAEntityManager();
            }
        }
        return list;
    }

    /**
     * Método para obter uma lista de objetos TerminalPOS de uma determinada regiao e determinado ramo de atividade,
     * excluindo determinada cidade
     * @param cidade
     * @param ramoAtividade
     * @return
     */
    public List<TerminalPOS> listTerminalPOSByRegiaoAndRamoAtividadeExcluindoCidade(Cidade cidade, RamoAtividade ramoAtividade) {
        List<TerminalPOS> list = null;
        try {
            logger.debug("Consultando lista de objetos " + CLASS_NAME);
            EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
            Query q = em.createQuery(SELECT_ALL_JPA + " WHERE " + OBJECT_INSTANCE_NAME +".estabelecimento.cidade.regiao = :regiao AND " + OBJECT_INSTANCE_NAME + ".estabelecimento.ramoAtividade = :ramoAtividade AND " +  OBJECT_INSTANCE_NAME + ".estabelecimento.cidade <> :cidade"); //Cria a string de busca
            q.setParameter("regiao", cidade.getRegiao());
            q.setParameter("cidade", cidade);
            q.setParameter("ramoAtividade", ramoAtividade);
            list = q.getResultList(); //Executa a busca e retorna a lista
            logger.info(list.size() + " objetos " + CLASS_NAME + " listados.");
        } catch (Exception ex) {
            logger.error("Erro ao consultar lista de objetos " + CLASS_NAME + ": " + ex.getMessage(), ex);
        } finally {
            if (!isKeepAliveJPAEntityManager()) {  //Se não for para manter o EntityManager, fecha
                closeJPAEntityManager();
            }
        }
        return list;
    }

    /**
     * Método para obter uma lista de objetos TerminalPOS de um determinado estado e determinado ramo de atividade,
     * excluindo determinada regiao
     * @param cidade
     * @param ramoAtividade
     * @return
     */
    public List<TerminalPOS> listTerminalPOSByEstadoAndRamoAtividadeExcluindoRegiao(Cidade cidade, RamoAtividade ramoAtividade) {
        List<TerminalPOS> list = null;
        try {
            logger.debug("Consultando lista de objetos " + CLASS_NAME);
            EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
            Query q = em.createQuery(SELECT_ALL_JPA + " WHERE " + OBJECT_INSTANCE_NAME +".estabelecimento.cidade.estado = :estado AND " + OBJECT_INSTANCE_NAME + ".estabelecimento.ramoAtividade = :ramoAtividade AND " +  OBJECT_INSTANCE_NAME + ".estabelecimento.cidade.regiao != :regiao"); //Cria a string de busca
            q.setParameter("regiao", cidade.getRegiao());
            q.setParameter("estado", cidade.getEstado());
            q.setParameter("ramoAtividade", ramoAtividade);
            list = q.getResultList(); //Executa a busca e retorna a lista
            logger.info(list.size() + " objetos " + CLASS_NAME + " listados.");
        } catch (Exception ex) {
            logger.error("Erro ao consultar lista de objetos " + CLASS_NAME + ": " + ex.getMessage(), ex);
        } finally {
            if (!isKeepAliveJPAEntityManager()) {  //Se não for para manter o EntityManager, fecha
                closeJPAEntityManager();
            }
        }
        return list;
    }


     /**
     * Método para obter uma lista de objetos TerminalPOS de um determinado ramo de atividade,
     * excluindo determinado estado
     * @param cidade
     * @param ramoAtividade
     * @return
     */
    public List<TerminalPOS> listTerminalPOSByRamoAtividadeExcluindoEstado(Cidade cidade, RamoAtividade ramoAtividade) {
        List<TerminalPOS> list = null;
        try {
            logger.debug("Consultando lista de objetos " + CLASS_NAME);
            EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
            Query q = em.createQuery(SELECT_ALL_JPA + " WHERE " + OBJECT_INSTANCE_NAME +".estabelecimento.cidade.estado != :estado AND " + OBJECT_INSTANCE_NAME + ".estabelecimento.ramoAtividade = :ramoAtividade"); //Cria a string de busca
            q.setParameter("estado", cidade.getEstado());
            q.setParameter("ramoAtividade", ramoAtividade);
            list = q.getResultList(); //Executa a busca e retorna a lista
            logger.info(list.size() + " objetos " + CLASS_NAME + " listados.");
        } catch (Exception ex) {
            logger.error("Erro ao consultar lista de objetos " + CLASS_NAME + ": " + ex.getMessage(), ex);
        } finally {
            if (!isKeepAliveJPAEntityManager()) {  //Se não for para manter o EntityManager, fecha
                closeJPAEntityManager();
            }
        }
        return list;
    }



    // </editor-fold>
    
}
