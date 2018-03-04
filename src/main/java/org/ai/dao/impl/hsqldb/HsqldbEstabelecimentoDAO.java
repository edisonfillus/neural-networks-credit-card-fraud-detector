package org.ai.dao.impl.hsqldb;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;

import org.ai.bo.ambiente.Estabelecimento;
import org.ai.dao.factory.HsqldbDAOFactory;
import org.ai.dao.interfaces.ambiente.EstabelecimentoDAO;

/**
 * Implementação do DAO do objeto Estabelecimento para o SGBD Hsqldb
 * @author Edison Klafke Fillus
 * @author Jefferson de Souza Campos
 */
public class HsqldbEstabelecimentoDAO extends HsqldbDAOFactory implements EstabelecimentoDAO {

    // <editor-fold defaultstate="collapsed" desc="Parâmetros">

    private static String CLASS_NAME = Estabelecimento.class.getSimpleName();
    private static String OBJECT_INSTANCE_NAME = Estabelecimento.class.getSimpleName().toLowerCase();
    private static String SELECT_ALL_JPA = "SELECT OBJECT(" + OBJECT_INSTANCE_NAME + ") FROM " + CLASS_NAME + " AS " + OBJECT_INSTANCE_NAME;
    private static String SELECT_COUNT_JPA = "SELECT COUNT(" + OBJECT_INSTANCE_NAME + ") FROM " + CLASS_NAME + " AS " + OBJECT_INSTANCE_NAME;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construtores">
    public HsqldbEstabelecimentoDAO() {
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Métodos DAO CRUD">
    
    /**
     * Método para Inserir um objeto Estabelecimento
     * @param estabelecimento Estabelecimento
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean createEstabelecimento(Estabelecimento estabelecimento) {
        boolean created = false; 
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        try {
            logger.debug("Inserindo objeto " + CLASS_NAME + ": " + estabelecimento.toString());
            em.getTransaction().begin(); //Inicia Transação
            em.persist(estabelecimento); //Insere
            em.getTransaction().commit(); //Confirma Transação
            created = true;
            logger.info("Objeto " + CLASS_NAME + " Inserido: " + estabelecimento.toString());
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
     * Método para Atualizar um objeto Estabelecimento
     * @param estabelecimento Estabelecimento
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean updateEstabelecimento(Estabelecimento estabelecimento) {
        boolean updated = false;
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        try {
            logger.debug("Atualizando objeto " + CLASS_NAME + ": " + estabelecimento.toString());
            em.getTransaction().begin(); //Inicia Transação
            em.merge(estabelecimento); //Atualiza
            em.getTransaction().commit(); //Confirma Transação
            updated = true;
            logger.info("Objeto " + CLASS_NAME + " Atualizado: " + estabelecimento.toString());
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
     * Método para Excluir um objeto Estabelecimento
     * @param estabelecimento Estabelecimento
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean deleteEstabelecimento(Estabelecimento estabelecimento) {
        boolean deleted = false;
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        int id = estabelecimento.getId();
        try {
            logger.debug("Excluindo objeto " + CLASS_NAME + ": " + estabelecimento.toString());
            estabelecimento = em.getReference(Estabelecimento.class, id); //Vefica se existe referência no banco
            em.getTransaction().begin(); //Inicia Transação
            em.remove(estabelecimento); //Exclui
            em.getTransaction().commit(); //Confirma Transação
            deleted = true;
            logger.info("Objeto " + CLASS_NAME + " excluido: " + estabelecimento.toString());
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
     * Método para localizar um objeto Estabelecimento
     * @param id
     * @return Estabelecimento
     */
    public Estabelecimento findEstabelecimento(int id) {
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        Estabelecimento estabelecimento = em.find(Estabelecimento.class, id); //Localiza estabelecimento
        em.refresh(estabelecimento);
        if (!isKeepAliveJPAEntityManager()) {  //Se não for para manter o EntityManager, fecha
            closeJPAEntityManager();
        }
        return estabelecimento;
        
    }

    /**
     * Método para obter uma lista de objetos Estabelecimento
     * @return
     */
    public List<Estabelecimento> listEstabelecimento() {
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        Query q = em.createQuery(SELECT_ALL_JPA); //Cria a string de busca
        List<Estabelecimento> list = q.getResultList(); //Executa a busca e retorna a lista
        if (!isKeepAliveJPAEntityManager()) {  //Se não for para manter o EntityManager, fecha
            closeJPAEntityManager();
        }
        return list;
    }

    /**
     * Método para obter a contagem de objetos Estabelecimento
     * @return
     */
    public int getEstabelecimentoCount() {
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

