package org.ai.dao.impl.hsqldb;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;

import org.ai.bo.ambiente.Cliente;
import org.ai.dao.factory.HsqldbDAOFactory;
import org.ai.dao.interfaces.ambiente.ClienteDAO;

/**
 * Implementação do DAO do objeto Cliente para o SGBD Hsqldb
 * @author Edison Klafke Fillus
 * @author Jefferson de Souza Campos
 */
public class HsqldbClienteDAO extends HsqldbDAOFactory implements ClienteDAO {

    // <editor-fold defaultstate="collapsed" desc="Parâmetros">
    private static String CLASS_NAME = Cliente.class.getSimpleName();
    private static String OBJECT_INSTANCE_NAME = Cliente.class.getSimpleName().toLowerCase();
    private static String SELECT_ALL_JPA = "SELECT OBJECT(" + OBJECT_INSTANCE_NAME + ") FROM " + CLASS_NAME + " AS " + OBJECT_INSTANCE_NAME;
    private static String SELECT_COUNT_JPA = "SELECT COUNT(" + OBJECT_INSTANCE_NAME + ") FROM " + CLASS_NAME + " AS " + OBJECT_INSTANCE_NAME;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construtores">
    public HsqldbClienteDAO() {
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Métodos DAO CRUD">
    /**
     * Método para Inserir um objeto Cliente
     * @param cliente Cliente
     * @return <code>true</code> Se executado com sucesso
     */
    @Override
    public boolean createCliente(Cliente cliente) {
        boolean created = false;
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        try {
            logger.debug("Inserindo objeto " + CLASS_NAME + ": " + cliente.toString());
            em.getTransaction().begin(); //Inicia Transação
            em.persist(cliente); //Insere
            em.getTransaction().commit(); //Confirma Transação
            created = true;
            logger.info("Objeto " + CLASS_NAME + " Inserido: " + cliente.toString());
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback(); //Se ocorrer erro, desfaz alterações da transação
            }
            logger.error("Erro ao inserir objeto" + CLASS_NAME + ": " + ex.getMessage(), ex);
        } finally {
            if (!isKeepAliveJPAEntityManager()) {  //Se não for para manter o EntityManager, fecha
                closeJPAEntityManager();
            }
        }
        return created;
    }

    /**
     * Método para Atualizar um objeto Cliente
     * @param cliente Cliente
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean updateCliente(Cliente cliente) {
        boolean updated = false;
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        try {
            logger.debug("Atualizando objeto " + CLASS_NAME + ": " + cliente.toString());
            em.getTransaction().begin(); //Inicia Transação
            em.merge(cliente); //Atualiza
            em.getTransaction().commit(); //Confirma Transação
            updated = true;
            logger.info("Objeto " + CLASS_NAME + " Atualizado: " + cliente.toString());
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();  //Se ocorrer erro, desfaz alterações da transação
            }
            logger.error("Erro ao atualizar objeto " + CLASS_NAME + ": " + ex.getMessage(), ex);
        } finally {
            if (!isKeepAliveJPAEntityManager()) {  //Se não for para manter o EntityManager, fecha
                closeJPAEntityManager();
            }
        }
        return updated;
    }

    /**
     * Método para Excluir um objeto Cliente
     * @param cliente Cliente
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean deleteCliente(Cliente cliente) {
        boolean deleted = false;
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        int id = cliente.getId();
        try {
            logger.debug("Excluindo objeto " + CLASS_NAME + ": " + cliente.toString());
            cliente = em.getReference(Cliente.class, id); //Vefica se existe referência no banco
            em.getTransaction().begin(); //Inicia Transação
            em.remove(cliente); //Exclui
            em.getTransaction().commit(); //Confirma Transação
            deleted = true;
            logger.info("Objeto " + CLASS_NAME + " excluido: " + cliente.toString());
        } catch (EntityNotFoundException enfe) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();  //Se ocorrer erro, desfaz alterações da transação
            }
            logger.warn("Não foi encontrado objeto " + CLASS_NAME + " com id: " + id, enfe);
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback(); //Se ocorrer erro, desfaz alterações da transação
            }
            logger.error("Erro ao excluir objeto " + CLASS_NAME + ": " + ex.getMessage(), ex);
        } finally {
            if (!isKeepAliveJPAEntityManager()) {  //Se não for para manter o EntityManager, fecha
                closeJPAEntityManager();
            }
        }
        return deleted;
    }

    /**
     * Método para localizar um objeto Cliente
     * @param id
     * @return Cliente
     */
    public Cliente findCliente(int id) {
        Cliente cliente = null;
        try {
            logger.debug("Consultando objeto " + CLASS_NAME + " com id: " + id);
            EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
            cliente = em.find(Cliente.class, id); //Localiza cliente
            em.refresh(cliente);
            logger.info("Objeto " + CLASS_NAME + " consultado: " + cliente.toString());
        } catch (Exception ex) {
            logger.error("Erro ao consultar objeto " + CLASS_NAME + ": " + ex.getMessage(), ex);
        } finally {
            if (!isKeepAliveJPAEntityManager()) {  //Se não for para manter o EntityManager, fecha
                closeJPAEntityManager();
            }
        }
        return cliente;

    }

    /**
     * Método para obter uma lista de objetos Cliente
     * @return
     */
    public List<Cliente> listCliente() {
        List<Cliente> list = null;
        try {
            logger.debug("Consultando lista de objetos " + CLASS_NAME);
            EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
            Query q = em.createQuery(SELECT_ALL_JPA); //Cria a string de busca
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
     * Método para obter a contagem de objetos Cliente
     * @return
     */
    public int getClienteCount() {
        int count = 0;
        try {
            logger.debug("Consultando quantidade de objetos " + CLASS_NAME);
            EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
            count = ((Long) em.createQuery(SELECT_COUNT_JPA).getSingleResult()).intValue(); //Executa a contagem
            logger.info(count + " objetos " + CLASS_NAME + " contados.");
        } catch (Exception ex) {
            logger.error("Erro ao consultar quantidade de objetos " + CLASS_NAME + ": " + ex.getMessage(), ex);
        } finally {
            if (!isKeepAliveJPAEntityManager()) {  //Se não for para manter o EntityManager, fecha
                closeJPAEntityManager();
            }
        }
        return count;
    }
    // </editor-fold>
}
