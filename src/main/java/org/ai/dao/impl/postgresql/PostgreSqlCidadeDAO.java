package org.ai.dao.impl.postgresql;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;

import org.ai.bo.ambiente.Cidade;
import org.ai.dao.factory.PostgreSqlDAOFactory;
import org.ai.dao.interfaces.ambiente.CidadeDAO;

/**
 * Implementação do DAO do objeto Cidade para o SGBD PostgreSql
 * @author Edison Klafke Fillus
 * @author Jefferson de Souza Campos
 */
public class PostgreSqlCidadeDAO extends PostgreSqlDAOFactory implements CidadeDAO {

    // <editor-fold defaultstate="collapsed" desc="Parâmetros">

    private static String CLASS_NAME = Cidade.class.getSimpleName();
    private static String OBJECT_INSTANCE_NAME = Cidade.class.getSimpleName().toLowerCase();
    private static String SELECT_ALL_JPA = "SELECT OBJECT(" + OBJECT_INSTANCE_NAME + ") FROM " + CLASS_NAME + " AS " + OBJECT_INSTANCE_NAME;
    private static String SELECT_COUNT_JPA = "SELECT COUNT(" + OBJECT_INSTANCE_NAME + ") FROM " + CLASS_NAME + " AS " + OBJECT_INSTANCE_NAME;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construtores">
    public PostgreSqlCidadeDAO() {
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Métodos DAO CRUD">
    
    /**
     * Método para Inserir um objeto Cidade
     * @param cidade Cidade
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean createCidade(Cidade cidade) {
        boolean created = false; 
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        try {
            logger.debug("Inserindo objeto " + CLASS_NAME + ": " + cidade.toString());
            em.getTransaction().begin(); //Inicia Transação
            em.persist(cidade); //Insere
            em.getTransaction().commit(); //Confirma Transação
            created = true;
            logger.info("Objeto " + CLASS_NAME + " Inserido: " + cidade.toString());
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
     * Método para Atualizar um objeto Cidade
     * @param cidade Cidade
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean updateCidade(Cidade cidade) {
        boolean updated = false;
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        try {
            logger.debug("Atualizando objeto " + CLASS_NAME + ": " + cidade.toString());
            em.getTransaction().begin(); //Inicia Transação
            em.merge(cidade); //Atualiza
            em.getTransaction().commit(); //Confirma Transação
            updated = true;
            logger.info("Objeto " + CLASS_NAME + " Atualizado: " + cidade.toString());
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
     * Método para Excluir um objeto Cidade
     * @param cidade Cidade
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean deleteCidade(Cidade cidade) {
        boolean deleted = false;
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        int id = cidade.getId();
        try {
            logger.debug("Excluindo objeto " + CLASS_NAME + ": " + cidade.toString());
            cidade = em.getReference(Cidade.class, id); //Vefica se existe referência no banco
            em.getTransaction().begin(); //Inicia Transação
            em.remove(cidade); //Exclui
            em.getTransaction().commit(); //Confirma Transação
            deleted = true;
            logger.info("Objeto " + CLASS_NAME + " excluido: " + cidade.toString());
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
     * Método para localizar um objeto Cidade
     * @param id
     * @return Cidade
     */
    public Cidade findCidade(int id) {
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        Cidade cidade = em.find(Cidade.class, id); //Localiza cidade
        em.refresh(cidade);
        if (!isKeepAliveJPAEntityManager()) {  //Se não for para manter o EntityManager, fecha
            closeJPAEntityManager();
        }
        return cidade;
        
    }

    /**
     * Método para obter uma lista de objetos Cidade
     * @return
     */
    public List<Cidade> listCidade() {
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        Query q = em.createQuery(SELECT_ALL_JPA); //Cria a string de busca
        List<Cidade> list = q.getResultList(); //Executa a busca e retorna a lista
        if (!isKeepAliveJPAEntityManager()) {  //Se não for para manter o EntityManager, fecha
            closeJPAEntityManager();
        }
        return list;
    }

    /**
     * Método para obter a contagem de objetos Cidade
     * @return
     */
    public int getCidadeCount() {
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
