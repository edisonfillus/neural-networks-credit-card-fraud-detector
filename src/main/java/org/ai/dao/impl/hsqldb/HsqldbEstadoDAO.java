package org.ai.dao.impl.hsqldb;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;

import org.ai.bo.ambiente.Estado;
import org.ai.dao.factory.HsqldbDAOFactory;
import org.ai.dao.interfaces.ambiente.EstadoDAO;

/**
 * Implementação do DAO do objeto Estado para o SGBD Hsqldb
 * @author Edison Klafke Fillus
 * @author Jefferson de Souza Campos
 */
public class HsqldbEstadoDAO extends HsqldbDAOFactory implements EstadoDAO {

    // <editor-fold defaultstate="collapsed" desc="Parâmetros">

    private static String CLASS_NAME = Estado.class.getSimpleName();
    private static String OBJECT_INSTANCE_NAME = Estado.class.getSimpleName().toLowerCase();
    private static String SELECT_ALL_JPA = "SELECT OBJECT(" + OBJECT_INSTANCE_NAME + ") FROM " + CLASS_NAME + " AS " + OBJECT_INSTANCE_NAME;
    private static String SELECT_COUNT_JPA = "SELECT COUNT(" + OBJECT_INSTANCE_NAME + ") FROM " + CLASS_NAME + " AS " + OBJECT_INSTANCE_NAME;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construtores">
    public HsqldbEstadoDAO() {
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Métodos DAO CRUD">
    
    /**
     * Método para Inserir um objeto Estado
     * @param estado Estado
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean createEstado(Estado estado) {
        boolean created = false; 
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        try {
            logger.debug("Inserindo objeto " + CLASS_NAME + ": " + estado.toString());
            em.getTransaction().begin(); //Inicia Transação
            em.persist(estado); //Insere
            em.getTransaction().commit(); //Confirma Transação
            created = true;
            logger.info("Objeto " + CLASS_NAME + " Inserido: " + estado.toString());
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
     * Método para Atualizar um objeto Estado
     * @param estado Estado
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean updateEstado(Estado estado) {
        boolean updated = false;
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        try {
            logger.debug("Atualizando objeto " + CLASS_NAME + ": " + estado.toString());
            em.getTransaction().begin(); //Inicia Transação
            em.merge(estado); //Atualiza
            em.getTransaction().commit(); //Confirma Transação
            updated = true;
            logger.info("Objeto " + CLASS_NAME + " Atualizado: " + estado.toString());
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
     * Método para Excluir um objeto Estado
     * @param estado Estado
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean deleteEstado(Estado estado) {
        boolean deleted = false;
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        int id = estado.getId();
        try {
            logger.debug("Excluindo objeto " + CLASS_NAME + ": " + estado.toString());
            estado = em.getReference(Estado.class, id); //Vefica se existe referência no banco
            em.getTransaction().begin(); //Inicia Transação
            em.remove(estado); //Exclui
            em.getTransaction().commit(); //Confirma Transação
            deleted = true;
            logger.info("Objeto " + CLASS_NAME + " excluido: " + estado.toString());
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
     * Método para localizar um objeto Estado
     * @param id
     * @return Estado
     */
    public Estado findEstado(int id) {
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        Estado estado = em.find(Estado.class, id); //Localiza estado
        em.refresh(estado);
        if (!isKeepAliveJPAEntityManager()) {  //Se não for para manter o EntityManager, fecha
            closeJPAEntityManager();
        }
        return estado;
        
    }

    /**
     * Método para obter uma lista de objetos Estado
     * @return
     */
    public List<Estado> listEstado() {
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        Query q = em.createQuery(SELECT_ALL_JPA); //Cria a string de busca
        List<Estado> list = q.getResultList(); //Executa a busca e retorna a lista
        if (!isKeepAliveJPAEntityManager()) {  //Se não for para manter o EntityManager, fecha
            closeJPAEntityManager();
        }
        return list;
    }

    /**
     * Método para obter a contagem de objetos Estado
     * @return
     */
    public int getEstadoCount() {
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
    
}
