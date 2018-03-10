package org.ai.dao.impl.db4o;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import java.util.ArrayList;
import java.util.List;

import org.ai.bo.neural.rede.RedeNeural;
import org.ai.dao.factory.Db4oDAOFactory;
import org.ai.dao.interfaces.neural.RedeNeuralDAO;

/**
 * Implementação do DAO do objeto RedeNeural para o SGBD Db4o
 * @author Edison Klafke Fillus
 * @author Jefferson de Souza Campos
 */
public class Db4oRedeNeuralDAO extends Db4oDAOFactory implements RedeNeuralDAO {

    // <editor-fold defaultstate="collapsed" desc="Atributos">
    
    private static String CLASS_NAME = RedeNeural.class.getSimpleName();
    private static String OBJECT_INSTANCE_NAME = RedeNeural.class.getSimpleName().toLowerCase();

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Construtores">
    
    public Db4oRedeNeuralDAO() {
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos DAO CRUD">
    
    /**
     * Método para Inserir um objeto Rede Neural
     * @param redeNeural RedeNeural
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean createRedeNeural(RedeNeural redeNeural) {
        boolean created = false;
        ObjectContainer connection = super.getConnection(); //Recupera o ObjectContainer
        try {
            logger.debug("Inserindo objeto " + CLASS_NAME + ": " + redeNeural.toString());
            connection.store(redeNeural); //Insere
            created = true;
            logger.info("Objeto " + CLASS_NAME + " Inserido: " + redeNeural.toString());
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
     * Método para Atualizar um objeto RedeNeural
     * @param redeNeural RedeNeural
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean updateRedeNeural(RedeNeural redeNeural) {
        boolean updated = false;
        ObjectContainer connection = super.getConnection();//Recupera o ObjectContainer
        connection.ext().configure().updateDepth(8);
        connection.ext().configure().objectClass(RedeNeural.class.getName()).cascadeOnUpdate(true);
        connection.ext().configure().objectClass(RedeNeural.class.getName()).cascadeOnDelete(true);
        try {
            logger.debug("Atualizando objeto " + CLASS_NAME + ": " + redeNeural.toString());
            connection.store(redeNeural);//Atualiza
            updated = true;
            logger.info("Objeto " + CLASS_NAME + " Atualizado: " + redeNeural.toString());
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
     * Método para Excluir um objeto RedeNeural
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean deleteRedeNeural(RedeNeural redeNeural) {
        boolean deleted = false;
        ObjectContainer connection = super.getConnection(); //Recupera o ObjectContainer
        connection.ext().configure().objectClass(RedeNeural.class.getName()).cascadeOnDelete(true);
        try {
            logger.debug("Excluindo objeto " + CLASS_NAME + ": " + redeNeural.toString());
            connection.delete(redeNeural); //Exclui
            deleted = true;
            logger.info("Objeto " + CLASS_NAME + " excluido: " + redeNeural.toString());
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
     * Método para localizar um objeto RedeNeural
     * @param id
     * @return RedeNeural
     */
    public RedeNeural findRedeNeural(final int id) {
        RedeNeural rede = new RedeNeural();
        ObjectContainer connection = super.getConnection(); //Recupera o ObjectContainer
        ObjectSet objectSet = connection.query(new Predicate<RedeNeural>() {
            @Override
            public boolean match(RedeNeural result) {
                return result.getId() == id;
            }
        });
        if (objectSet.hasNext()) {
            rede = (RedeNeural) objectSet.next();
            connection.activate(rede, 8); //Ativa a classe até o nivel 8
        }
        if (!isKeepAliveConnection()) {  //Se não for para manter o ObjetContainer, fecha
            closeConnection();
        }
        return rede;
    }

    /**
     * Método para obter uma lista de objetos RedeNeural
     * @return
     */
    public List<RedeNeural> listRedeNeural() {
        ObjectContainer connection = super.getConnection(); //Recupera o ObjectContainer
        List<RedeNeural> resultSet = connection.query(RedeNeural.class);

        List<RedeNeural> redes = new ArrayList<RedeNeural>();

        for (RedeNeural rede : resultSet) {
            connection.activate(rede, 8);//Ativa a classe até o nivel 8
            redes.add(rede);
        }
        if (!isKeepAliveConnection()) {  //Se não for para manter o ObjetContainer, fecha
            closeConnection();
        }
        return redes;
    }

    /**
     * Método para obter a contagem de objetos RedeNeural
     * @return
     */
    public int getRedeNeuralCount() {
        int count = 0;
        ObjectContainer connection = super.getConnection(); //Recupera o ObjectContainer
        //TODO: Verificar como fazer consulta mais eficiente
        List<RedeNeural> resultSet = connection.query(RedeNeural.class);
        count = resultSet.size();
        if (!isKeepAliveConnection()) {  //Se não for para manter o ObjetContainer, fecha
            closeConnection();
        }
        return count;
    }    // </editor-fold>
}
