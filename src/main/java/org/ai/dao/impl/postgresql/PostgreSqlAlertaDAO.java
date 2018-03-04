package org.ai.dao.impl.postgresql;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;

import org.ai.bo.ambiente.Alerta;
import org.ai.dao.factory.PostgreSqlDAOFactory;
import org.ai.dao.interfaces.ambiente.AlertaDAO;

/**
 * Implementação do DAO do objeto Alerta para o SGBD PostgreSql
 * @author Edison Klafke Fillus
 * @author Jefferson de Souza Campos
 */
public class PostgreSqlAlertaDAO extends PostgreSqlDAOFactory implements AlertaDAO {

    // <editor-fold defaultstate="collapsed" desc="Parâmetros">

    private static String CLASS_NAME = Alerta.class.getSimpleName();
    private static String OBJECT_INSTANCE_NAME = Alerta.class.getSimpleName().toLowerCase();
    private static String SELECT_ALL_JPA = "SELECT OBJECT(" + OBJECT_INSTANCE_NAME + ") FROM " + CLASS_NAME + " AS " + OBJECT_INSTANCE_NAME;
    private static String SELECT_COUNT_JPA = "SELECT COUNT(" + OBJECT_INSTANCE_NAME + ") FROM " + CLASS_NAME + " AS " + OBJECT_INSTANCE_NAME;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construtores">
    public PostgreSqlAlertaDAO() {
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Métodos DAO CRUD">
    
    /**
     * Método para Inserir um objeto Alerta
     * @param alerta Alerta
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean createAlerta(Alerta alerta) {
        boolean created = false; 
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        try {
            logger.debug("Inserindo objeto " + CLASS_NAME + ": " + alerta.toString());
            em.getTransaction().begin(); //Inicia Transação
            em.persist(alerta); //Insere
            em.getTransaction().commit(); //Confirma Transação
            created = true;
            logger.info("Objeto " + CLASS_NAME + " Inserido: " + alerta.toString());
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
     * Método para Atualizar um objeto Alerta
     * @param alerta Alerta
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean updateAlerta(Alerta alerta) {
        boolean updated = false;
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        try {
            logger.debug("Atualizando objeto " + CLASS_NAME + ": " + alerta.toString());
            em.getTransaction().begin(); //Inicia Transação
            em.merge(alerta); //Atualiza
            em.getTransaction().commit(); //Confirma Transação
            updated = true;
            logger.info("Objeto " + CLASS_NAME + " Atualizado: " + alerta.toString());
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
     * Método para Excluir um objeto Alerta
     * @param alerta Alerta
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean deleteAlerta(Alerta alerta) {
        boolean deleted = false;
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        int id = alerta.getId();
        try {
            logger.debug("Excluindo objeto " + CLASS_NAME + ": " + alerta.toString());
            alerta = em.getReference(Alerta.class, id); //Vefica se existe referência no banco
            em.getTransaction().begin(); //Inicia Transação
            em.remove(alerta); //Exclui
            em.getTransaction().commit(); //Confirma Transação
            deleted = true;
            logger.info("Objeto " + CLASS_NAME + " excluido: " + alerta.toString());
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
     * Método para localizar um objeto Alerta
     * @param id
     * @return Alerta
     */
    public Alerta findAlerta(int id) {
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        Alerta alerta = em.find(Alerta.class, id); //Localiza alerta
        em.refresh(alerta);
        if (!isKeepAliveJPAEntityManager()) {  //Se não for para manter o EntityManager, fecha
            closeJPAEntityManager();
        }
        return alerta;
        
    }

    /**
     * Método para obter uma lista de objetos Alerta
     * @return
     */
    public List<Alerta> listAlerta() {
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        Query q = em.createQuery(SELECT_ALL_JPA); //Cria a string de busca
        List<Alerta> list = q.getResultList(); //Executa a busca e retorna a lista
        if (!isKeepAliveJPAEntityManager()) {  //Se não for para manter o EntityManager, fecha
            closeJPAEntityManager();
        }
        return list;
    }

    /**
     * Método para obter a contagem de objetos Alerta
     * @return
     */
    public int getAlertaCount() {
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

