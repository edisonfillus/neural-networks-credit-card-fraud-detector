package org.ai.dao.impl.hsqldb;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;

import org.ai.bo.ambiente.Cidade;
import org.ai.bo.ambiente.Cliente;
import org.ai.bo.ambiente.RamoAtividade;
import org.ai.bo.ambiente.Transacao;
import org.ai.dao.factory.HsqldbDAOFactory;
import org.ai.dao.interfaces.ambiente.TransacaoDAO;

/**
 * Implementação do DAO do objeto Transacao para o SGBD Hsqldb
 * @author Edison Klafke Fillus
 * @author Jefferson de Souza Campos
 */
public class HsqldbTransacaoDAO extends HsqldbDAOFactory implements TransacaoDAO {

    // <editor-fold defaultstate="collapsed" desc="Parâmetros">
    private static String CLASS_NAME = Transacao.class.getSimpleName();
    private static String OBJECT_INSTANCE_NAME = Transacao.class.getSimpleName().toLowerCase();
    private static String SELECT_ALL_JPA = "SELECT OBJECT(" + OBJECT_INSTANCE_NAME + ") FROM " + CLASS_NAME + " AS " + OBJECT_INSTANCE_NAME;
    private static String SELECT_COUNT_JPA = "SELECT COUNT(" + OBJECT_INSTANCE_NAME + ") FROM " + CLASS_NAME + " AS " + OBJECT_INSTANCE_NAME;
    private static String SELECT_ONLY_TREINAMENTO_JPA = SELECT_ALL_JPA + " WHERE " + OBJECT_INSTANCE_NAME + ".flagTreinamento = true";
    private static String SELECT_COUNT_ONLY_TREINAMENTO_JPA = SELECT_COUNT_JPA + " WHERE " + OBJECT_INSTANCE_NAME + ".flagTreinamento = true";


    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construtores">
    public HsqldbTransacaoDAO() {
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Métodos DAO CRUD">
    /**
     * Método para Inserir um objeto Transacao
     * @param transacao Transacao
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean createTransacao(Transacao transacao) {
        boolean created = false;
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        try {
            logger.debug("Inserindo objeto " + CLASS_NAME + ": " + transacao.toString());
            em.getTransaction().begin(); //Inicia Transação
            em.persist(transacao); //Insere
            em.getTransaction().commit(); //Confirma Transação
            created = true;
            logger.info("Objeto " + CLASS_NAME + " Inserido: " + transacao.toString());
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
     * Método para Atualizar um objeto Transacao
     * @param transacao Transacao
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean updateTransacao(Transacao transacao) {
        boolean updated = false;
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        try {
            logger.debug("Atualizando objeto " + CLASS_NAME + ": " + transacao.toString());
            em.getTransaction().begin(); //Inicia Transação
            em.merge(transacao); //Atualiza
            em.getTransaction().commit(); //Confirma Transação
            updated = true;
            logger.info("Objeto " + CLASS_NAME + " Atualizado: " + transacao.toString());
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
     * Método para Excluir um objeto Transacao
     * @param transacao Transacao
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean deleteTransacao(Transacao transacao) {
        boolean deleted = false;
        EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
        int id = transacao.getId();
        try {
            logger.debug("Excluindo objeto " + CLASS_NAME + ": " + transacao.toString());
            transacao = em.getReference(Transacao.class, id); //Vefica se existe referência no banco
            em.getTransaction().begin(); //Inicia Transação
            em.remove(transacao); //Exclui
            em.getTransaction().commit(); //Confirma Transação
            deleted = true;
            logger.info("Objeto " + CLASS_NAME + " excluido: " + transacao.toString());
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
     * Método para localizar um objeto Transacao
     * @param id
     * @return Transacao
     */
    public Transacao findTransacao(int id) {
        Transacao transacao = null;
        try {
            logger.debug("Consultando objeto " + CLASS_NAME + " com id: " + id);
            EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
            transacao = em.find(Transacao.class, id); //Localiza transacao
            em.refresh(transacao);
            logger.info("Objeto " + CLASS_NAME + " consultado: " + transacao.toString());
        } catch (Exception ex) {
            logger.error("Erro ao consultar objeto " + CLASS_NAME + ": " + ex.getMessage(), ex);
        } finally {
            if (!isKeepAliveJPAEntityManager()) {  //Se não for para manter o EntityManager, fecha
                closeJPAEntityManager();
            }
        }
        return transacao;

    }

    /**
     * Método para obter uma lista de objetos Transacao
     * @return
     */
    public List<Transacao> listTransacao() {
        List<Transacao> list = null;
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
     * Método para obter a contagem de objetos Transacao
     * @return
     */
    public int getTransacaoCount() {
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

    // <editor-fold defaultstate="collapsed" desc="Métodos DAO Complementares">
    /**
     * Método para obter uma lista de objetos Transacao somente marcadas para treinamento
     * @return
     */
    public List<Transacao> listTransacaoOnlyTreinamento() {
        List<Transacao> list = null;
        try {
            logger.debug("Consultando lista de objetos " + CLASS_NAME);
            EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
            Query q = em.createQuery(SELECT_ONLY_TREINAMENTO_JPA); //Cria a string de busca
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
     * Método para obter a contagem de objetos Transacao somente marcadas para treinamento
     * @return
     */
    public int getCountTransacaoSelecionadaTreinamento() {
        int count = 0;
        try {
            logger.debug("Consultando quantidade de objetos " + CLASS_NAME);
            EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
            count = ((Long) em.createQuery(SELECT_COUNT_ONLY_TREINAMENTO_JPA).getSingleResult()).intValue(); //Executa a contagem
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

    /**
     * Método para retornar o total de transões de um cliente
     * @param cliente
     * @return
     */
    public int getCountTransacaoCliente(Cliente cliente) {
        int count = 0;
        try {
            logger.debug("Consultando quantidade de objetos " + CLASS_NAME + " do cliente " + cliente.getNome());
            EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
            Query query = em.createQuery(SELECT_COUNT_JPA + " WHERE " + OBJECT_INSTANCE_NAME + ".cartao.cliente = :cliente ");
            query.setParameter("cliente", cliente);
            count = ((Integer) query.getSingleResult()); //Executa a contagem
            logger.info(count + " objetos " + CLASS_NAME + " contados para o cliente " + cliente.getNome());
        } catch (Exception ex) {
            logger.error("Erro ao consultar quantidade de objetos " + CLASS_NAME + ": " + ex.getMessage(), ex);
        } finally {
            if (!isKeepAliveJPAEntityManager()) {  //Se não for para manter o EntityManager, fecha
                closeJPAEntityManager();
            }
        }
        return count;
    }

    /**
     * Método para retornar o total de transões de um cliente feita em determinado ramo de atividade
     * @param cliente
     * @param ramo
     * @return
     */
    public int getCountTransacaoClienteNoRamoAtividade(Cliente cliente, RamoAtividade ramo) {
        int count = 0;
        try {
            logger.debug("Consultando quantidade de objetos " + CLASS_NAME + " do cliente " + cliente.getNome() + "no ramo de atividade " + ramo.getNome());
            EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
            Query query = em.createQuery(SELECT_COUNT_JPA + " WHERE " + OBJECT_INSTANCE_NAME + ".cartao.cliente = :cliente AND " + OBJECT_INSTANCE_NAME + ".terminalPos.estabelecimento.ramoAtividade = :ramo");
            query.setParameter("cliente", cliente);
            query.setParameter("ramo", ramo);
            count = ((Integer) query.getSingleResult()); //Executa a contagem
            logger.info(count + " objetos " + CLASS_NAME + " contados para o cliente " + cliente.getNome() + " no ramo de atividade " + ramo.getNome());
        } catch (Exception ex) {
            logger.error("Erro ao consultar quantidade de objetos " + CLASS_NAME + ": " + ex.getMessage(), ex);
        } finally {
            if (!isKeepAliveJPAEntityManager()) {  //Se não for para manter o EntityManager, fecha
                closeJPAEntityManager();
            }
        }
        return count;
    }

    /**
     * Método para retornar o total de transões de um cliente feita em determinado ramo de atividade
     * @param cliente
     * @param cidade
     * @return
     */
    public int getCountTransacaoClienteNaCidade(Cliente cliente, Cidade cidade) {
        int count = 0;
        try {
            logger.debug("Consultando quantidade de objetos " + CLASS_NAME + " do cliente " + cliente.getNome() + "na cidade " + cidade.getNome());
            EntityManager em = super.getJPAEntityManager(); //Recupera o EntityManager
            Query query = em.createQuery(SELECT_COUNT_JPA + " WHERE " + OBJECT_INSTANCE_NAME + ".cartao.cliente = :cliente AND " + OBJECT_INSTANCE_NAME + ".terminalPos.estabelecimento.cidade = :cidade");
            query.setParameter("cliente", cliente);
            query.setParameter("cidade", cidade);
            count = ((Integer) query.getSingleResult()); //Executa a contagem
            logger.info(count + " objetos " + CLASS_NAME + " contados para o cliente " + cliente.getNome() + " na cidade " + cidade.getNome());
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


 