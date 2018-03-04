package org.ai.dao.impl.db4o;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import java.util.ArrayList;
import java.util.List;

import org.ai.bo.analise.AnaliseTransacao;
import org.ai.dao.factory.Db4oDAOFactory;
import org.ai.dao.interfaces.analise.AnaliseTransacaoDAO;

/**
 * Implementação do DAO do objeto AnaliseTransacao para o SGBD Db4o
 * @author Edison Klafke Fillus
 * @author Jefferson de Souza Campos
 */
public class Db4oAnaliseTransacaoDAO extends Db4oDAOFactory implements AnaliseTransacaoDAO {

    // <editor-fold defaultstate="collapsed" desc="Atributos">
    
    private static String CLASS_NAME = AnaliseTransacao.class.getSimpleName();
    private static String OBJECT_INSTANCE_NAME = AnaliseTransacao.class.getSimpleName().toLowerCase();

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Construtores">
    
    public Db4oAnaliseTransacaoDAO() {
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos DAO CRUD">
    
    /**
     * Método para Inserir um objeto AnaliseTransacao
     * @param analiseTransacao AnaliseTransacao
     * @return <code>true</code> Se executado com sucesso
     */
    @Override
    public boolean createAnaliseTransacao(AnaliseTransacao analiseTransacao) {
        boolean created = false;
        ObjectContainer connection = super.getConnection(); //Recupera o ObjectContainer
        try {
            logger.debug("Inserindo objeto " + CLASS_NAME + ": " + analiseTransacao.toString());
            connection.store(analiseTransacao); //Insere
            created = true;
            logger.info("Objeto " + CLASS_NAME + " Inserido: " + analiseTransacao.toString());
        } catch (Exception ex) {
            if (!connection.ext().isClosed()) {
                connection.rollback(); //Se ocorrer erro, desfaz alterações da transação
            }
            logger.error("Erro ao inserir objeto" + CLASS_NAME + ": " + ex.getMessage(), ex);
        } finally {
            if (!isKeepAliveConnection()) {  //Se não for para manter o ObjetContainer, fecha
                closeConnection();
            }
        }
        return created;
    }

    /**
     * Método para Atualizar um objeto AnaliseTransacao
     * @param analiseTransacao AnaliseTransacao
     * @return <code>true</code> Se executado com sucesso
     */
    @Override
    public boolean updateAnaliseTransacao(AnaliseTransacao analiseTransacao) {
        boolean updated = false;
        ObjectContainer connection = super.getConnection();//Recupera o ObjectContainer
        connection.ext().configure().updateDepth(4);
        try {
            logger.debug("Atualizando objeto " + CLASS_NAME + ": " + analiseTransacao.toString());
            connection.store(analiseTransacao);//Atualiza
            updated = true;
            logger.info("Objeto " + CLASS_NAME + " Atualizado: " + analiseTransacao.toString());
        } catch (Exception ex) {
            if (!connection.ext().isClosed()) {
                connection.rollback(); //Se ocorrer erro, desfaz alterações da transação
            }
            logger.error("Erro ao atualizar objeto " + CLASS_NAME + ": " + ex.getMessage(), ex);
        } finally {
            if (!isKeepAliveConnection()) {  //Se não for para manter o ObjetContainer, fecha
                closeConnection();
            }
        }
        return updated;
    }

    /**
     * Método para Excluir um objeto AnaliseTransacao
     * @return <code>true</code> Se executado com sucesso
     */
    @Override
    public boolean deleteAnaliseTransacao(AnaliseTransacao analiseTransacao) {
        boolean deleted = false;
        ObjectContainer connection = super.getConnection(); //Recupera o ObjectContainer
        connection.ext().configure().objectClass(AnaliseTransacao.class.getName()).cascadeOnDelete(true);
        try {
            logger.debug("Excluindo objeto " + CLASS_NAME + ": " + analiseTransacao.toString());
            connection.delete(analiseTransacao); //Exclui
            deleted = true;
            logger.info("Objeto " + CLASS_NAME + " excluido: " + analiseTransacao.toString());
        } catch (Exception ex) {
            if (!connection.ext().isClosed()) {
                connection.rollback(); //Se ocorrer erro, desfaz alterações da transação
            }
            logger.error("Erro ao excluir objeto " + CLASS_NAME + ": " + ex.getMessage(), ex);
        } finally {
            if (!isKeepAliveConnection()) {  //Se não for para manter o ObjetContainer, fecha
                closeConnection();
            }
        }
        return deleted;
    }

    /**
     * Método para localizar um objeto AnaliseTransacao
     * @param id
     * @return AnaliseTransacao
     */
    @Override
    public AnaliseTransacao findAnaliseTransacao(final int id) {
        AnaliseTransacao rede = new AnaliseTransacao();
        ObjectContainer connection = super.getConnection(); //Recupera o ObjectContainer
        ObjectSet objectSet = connection.query(new Predicate<AnaliseTransacao>() {
            @Override
            public boolean match(AnaliseTransacao result) {
                return result.getId() == id;
            }
        });
        if (objectSet.hasNext()) {
            rede = (AnaliseTransacao) objectSet.next();
        }
        if (!isKeepAliveConnection()) {  //Se não for para manter o ObjetContainer, fecha
            closeConnection();
        }
        return rede;
    }

    /**
     * Método para obter uma lista de objetos AnaliseTransacao
     * @return
     */
    @Override
    public List<AnaliseTransacao> listAnaliseTransacao() {
        ObjectContainer connection = super.getConnection(); //Recupera o ObjectContainer
        List<AnaliseTransacao> resultSet = connection.query(AnaliseTransacao.class);

        List<AnaliseTransacao> redes = new ArrayList<AnaliseTransacao>();

        for (AnaliseTransacao rede : resultSet) {
            redes.add(rede);
        }
        if (!isKeepAliveConnection()) {  //Se não for para manter o ObjetContainer, fecha
            closeConnection();
        }
        return redes;
    }

    /**
     * Método para obter a contagem de objetos AnaliseTransacao
     * @return
     */
    @Override
    public int getAnaliseTransacaoCount() {
        int count = 0;
        ObjectContainer connection = super.getConnection(); //Recupera o ObjectContainer
        //TODO: Verificar como fazer consulta mais eficiente
        List<AnaliseTransacao> resultSet = connection.query(AnaliseTransacao.class);
        count = resultSet.size();
        if (!isKeepAliveConnection()) {  //Se não for para manter o ObjetContainer, fecha
            closeConnection();
        }
        return count;
    }    // </editor-fold>
}
