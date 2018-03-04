package org.ai.dao.impl.hsqldb;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;

import org.ai.bo.ambiente.Item;
import org.ai.dao.factory.HsqldbDAOFactory;
import org.ai.dao.interfaces.ambiente.ItemDAO;

/**
 * Implementação do DAO do objeto Item para o SGBD Hsqldb
 * @author Edison Klafke Fillus
 * @author Jefferson de Souza Campos
 */
public class HsqldbItemDAO extends HsqldbDAOFactory implements ItemDAO {

    // <editor-fold defaultstate="collapsed" desc="Parâmetros">

    private static String CLASS_NAME = Item.class.getSimpleName();
    private static String OBJECT_INSTANCE_NAME = Item.class.getSimpleName().toLowerCase();
    private static String SELECT_ALL_JPA = "SELECT OBJECT(" + OBJECT_INSTANCE_NAME + ") FROM " + CLASS_NAME + " AS " + OBJECT_INSTANCE_NAME;
    private static String SELECT_COUNT_JPA = "SELECT COUNT(" + OBJECT_INSTANCE_NAME + ") FROM " + CLASS_NAME + " AS " + OBJECT_INSTANCE_NAME;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construtores">
    public HsqldbItemDAO() {
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Métodos DAO CRUD">
    
    /**
     * Método para Inserir um objeto Item
     * @param item Item
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean createItem(Item item) {
        boolean created = false; 
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        try {
            logger.debug("Inserindo objeto " + CLASS_NAME + ": " + item.toString());
            em.getTransaction().begin(); //Inicia Transação
            em.persist(item); //Insere
            em.getTransaction().commit(); //Confirma Transação
            created = true;
            logger.info("Objeto " + CLASS_NAME + " Inserido: " + item.toString());
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
     * Método para Atualizar um objeto Item
     * @param item Item
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean updateItem(Item item) {
        boolean updated = false;
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        try {
            logger.debug("Atualizando objeto " + CLASS_NAME + ": " + item.toString());
            em.getTransaction().begin(); //Inicia Transação
            em.merge(item); //Atualiza
            em.getTransaction().commit(); //Confirma Transação
            updated = true;
            logger.info("Objeto " + CLASS_NAME + " Atualizado: " + item.toString());
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
     * Método para Excluir um objeto Item
     * @param item Item
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean deleteItem(Item item) {
        boolean deleted = false;
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        int id = item.getId();
        try {
            logger.debug("Excluindo objeto " + CLASS_NAME + ": " + item.toString());
            item = em.getReference(Item.class, id); //Vefica se existe referência no banco
            em.getTransaction().begin(); //Inicia Transação
            em.remove(item); //Exclui
            em.getTransaction().commit(); //Confirma Transação
            deleted = true;
            logger.info("Objeto " + CLASS_NAME + " excluido: " + item.toString());
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
     * Método para localizar um objeto Item
     * @param id
     * @return Item
     */
    public Item findItem(int id) {
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        Item item = em.find(Item.class, id); //Localiza item
        em.refresh(item);
        if (!isKeepAliveJPAEntityManager()) {  //Se não for para manter o EntityManager, fecha
            closeJPAEntityManager();
        }
        return item;
        
    }

    /**
     * Método para obter uma lista de objetos Item
     * @return
     */
    public List<Item> listItem() {
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        Query q = em.createQuery(SELECT_ALL_JPA); //Cria a string de busca
        List<Item> list = q.getResultList(); //Executa a busca e retorna a lista
        if (!isKeepAliveJPAEntityManager()) {  //Se não for para manter o EntityManager, fecha
            closeJPAEntityManager();
        }
        return list;
    }

    /**
     * Método para obter a contagem de objetos Item
     * @return
     */
    public int getItemCount() {
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
