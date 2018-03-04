package org.ai.dao.impl.postgresql;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;

import org.ai.bo.ambiente.Regiao;
import org.ai.dao.factory.PostgreSqlDAOFactory;
import org.ai.dao.interfaces.ambiente.RegiaoDAO;

/**
 * Implementação do DAO do objeto Regiao para o SGBD PostgreSql
 * @author Edison Klafke Fillus
 * @author Jefferson de Souza Campos
 */
public class PostgreSqlRegiaoDAO extends PostgreSqlDAOFactory implements RegiaoDAO {

    // <editor-fold defaultstate="collapsed" desc="Parâmetros">

    private static String CLASS_NAME = Regiao.class.getSimpleName();
    private static String OBJECT_INSTANCE_NAME = Regiao.class.getSimpleName().toLowerCase();
    private static String SELECT_ALL_JPA = "SELECT OBJECT(" + OBJECT_INSTANCE_NAME + ") FROM " + CLASS_NAME + " AS " + OBJECT_INSTANCE_NAME;
    private static String SELECT_COUNT_JPA = "SELECT COUNT(" + OBJECT_INSTANCE_NAME + ") FROM " + CLASS_NAME + " AS " + OBJECT_INSTANCE_NAME;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construtores">
    public PostgreSqlRegiaoDAO() {
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Métodos DAO CRUD">
    
    /**
     * Método para Inserir um objeto Regiao
     * @param regiao Regiao
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean createRegiao(Regiao regiao) {
        boolean created = false; 
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        try {
            logger.debug("Inserindo objeto " + CLASS_NAME + ": " + regiao.toString());
            em.getTransaction().begin(); //Inicia Transação
            em.persist(regiao); //Insere
            em.getTransaction().commit(); //Confirma Transação
            created = true;
            logger.info("Objeto " + CLASS_NAME + " Inserido: " + regiao.toString());
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
     * Método para Atualizar um objeto Regiao
     * @param regiao Regiao
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean updateRegiao(Regiao regiao) {
        boolean updated = false;
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        try {
            logger.debug("Atualizando objeto " + CLASS_NAME + ": " + regiao.toString());
            em.getTransaction().begin(); //Inicia Transação
            em.merge(regiao); //Atualiza
            em.getTransaction().commit(); //Confirma Transação
            updated = true;
            logger.info("Objeto " + CLASS_NAME + " Atualizado: " + regiao.toString());
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
     * Método para Excluir um objeto Regiao
     * @param regiao Regiao
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean deleteRegiao(Regiao regiao) {
        boolean deleted = false;
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        int id = regiao.getId();
        try {
            logger.debug("Excluindo objeto " + CLASS_NAME + ": " + regiao.toString());
            regiao = em.getReference(Regiao.class, id); //Vefica se existe referência no banco
            em.getTransaction().begin(); //Inicia Transação
            em.remove(regiao); //Exclui
            em.getTransaction().commit(); //Confirma Transação
            deleted = true;
            logger.info("Objeto " + CLASS_NAME + " excluido: " + regiao.toString());
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
     * Método para localizar um objeto Regiao
     * @param id
     * @return Regiao
     */
    public Regiao findRegiao(int id) {
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        Regiao regiao = em.find(Regiao.class, id); //Localiza regiao
        em.refresh(regiao);
        if (!isKeepAliveJPAEntityManager()) {  //Se não for para manter o EntityManager, fecha
            closeJPAEntityManager();
        }
        return regiao;
        
    }

    /**
     * Método para obter uma lista de objetos Regiao
     * @return
     */
    public List<Regiao> listRegiao() {
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        Query q = em.createQuery(SELECT_ALL_JPA); //Cria a string de busca
        List<Regiao> list = q.getResultList(); //Executa a busca e retorna a lista
        if (!isKeepAliveJPAEntityManager()) {  //Se não for para manter o EntityManager, fecha
            closeJPAEntityManager();
        }
        return list;
    }

    /**
     * Método para obter a contagem de objetos Regiao
     * @return
     */
    public int getRegiaoCount() {
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