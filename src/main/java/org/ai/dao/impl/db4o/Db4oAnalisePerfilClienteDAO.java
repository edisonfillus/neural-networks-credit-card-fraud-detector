package org.ai.dao.impl.db4o;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import java.util.ArrayList;
import java.util.List;

import org.ai.bo.analise.AnalisePerfilCliente;
import org.ai.dao.factory.Db4oDAOFactory;
import org.ai.dao.interfaces.analise.AnalisePerfilClienteDAO;

/**
 * Implementação do DAO do objeto AnalisePerfilCliente para o SGBD Db4o
 * @author Edison Klafke Fillus
 * @author Jefferson de Souza Campos
 */
public class Db4oAnalisePerfilClienteDAO extends Db4oDAOFactory implements AnalisePerfilClienteDAO {

    // <editor-fold defaultstate="collapsed" desc="Atributos">
    
    private static String CLASS_NAME = AnalisePerfilCliente.class.getSimpleName();
    private static String OBJECT_INSTANCE_NAME = AnalisePerfilCliente.class.getSimpleName().toLowerCase();

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Construtores">
    
    public Db4oAnalisePerfilClienteDAO() {
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos DAO CRUD">
    
    /**
     * Método para Inserir um objeto AnalisePerfilCliente
     * @param analisePerfilCliente AnalisePerfilCliente
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean createAnalisePerfilCliente(AnalisePerfilCliente analisePerfilCliente) {
        boolean created = false;
        ObjectContainer connection = super.getConnection(); //Recupera o ObjectContainer
        try {
            logger.debug("Inserindo objeto " + CLASS_NAME + ": " + analisePerfilCliente.toString());
            connection.store(analisePerfilCliente); //Insere
            created = true;
            logger.info("Objeto " + CLASS_NAME + " Inserido: " + analisePerfilCliente.toString());
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
     * Método para Atualizar um objeto AnalisePerfilCliente
     * @param analisePerfilCliente AnalisePerfilCliente
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean updateAnalisePerfilCliente(AnalisePerfilCliente analisePerfilCliente) {
        boolean updated = false;
        ObjectContainer connection = super.getConnection();//Recupera o ObjectContainer
        connection.ext().configure().updateDepth(4);
        try {
            logger.debug("Atualizando objeto " + CLASS_NAME + ": " + analisePerfilCliente.toString());
            connection.store(analisePerfilCliente);//Atualiza
            updated = true;
            logger.info("Objeto " + CLASS_NAME + " Atualizado: " + analisePerfilCliente.toString());
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
     * Método para Excluir um objeto AnalisePerfilCliente
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean deleteAnalisePerfilCliente(AnalisePerfilCliente analisePerfilCliente) {
        boolean deleted = false;
        ObjectContainer connection = super.getConnection(); //Recupera o ObjectContainer
        connection.ext().configure().objectClass(AnalisePerfilCliente.class.getName()).cascadeOnDelete(true);
        try {
            logger.debug("Excluindo objeto " + CLASS_NAME + ": " + analisePerfilCliente.toString());
            connection.delete(analisePerfilCliente); //Exclui
            deleted = true;
            logger.info("Objeto " + CLASS_NAME + " excluido: " + analisePerfilCliente.toString());
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
     * Método para localizar um objeto AnalisePerfilCliente
     * @param id
     * @return AnalisePerfilCliente
     */
    public AnalisePerfilCliente findAnalisePerfilCliente(final int id) {
        AnalisePerfilCliente rede = new AnalisePerfilCliente();
        ObjectContainer connection = super.getConnection(); //Recupera o ObjectContainer
        ObjectSet objectSet = connection.query(new Predicate<AnalisePerfilCliente>() {
            @Override
            public boolean match(AnalisePerfilCliente result) {
                return result.getId() == id;
            }
        });
        if (objectSet.hasNext()) {
            rede = (AnalisePerfilCliente) objectSet.next();
        }
        if (!isKeepAliveConnection()) {  //Se não for para manter o ObjetContainer, fecha
            closeConnection();
        }
        return rede;
    }

    /**
     * Método para obter uma lista de objetos AnalisePerfilCliente
     * @return
     */
    public List<AnalisePerfilCliente> listAnalisePerfilCliente() {
        ObjectContainer connection = super.getConnection(); //Recupera o ObjectContainer
        List<AnalisePerfilCliente> resultSet = connection.query(AnalisePerfilCliente.class);

        List<AnalisePerfilCliente> redes = new ArrayList<AnalisePerfilCliente>();

        for (AnalisePerfilCliente rede : resultSet) {
            redes.add(rede);
        }
        if (!isKeepAliveConnection()) {  //Se não for para manter o ObjetContainer, fecha
            closeConnection();
        }
        return redes;
    }

    /**
     * Método para obter a contagem de objetos AnalisePerfilCliente
     * @return
     */
    public int getAnalisePerfilClienteCount() {
        int count = 0;
        ObjectContainer connection = super.getConnection(); //Recupera o ObjectContainer
        //TODO: Verificar como fazer consulta mais eficiente
        List<AnalisePerfilCliente> resultSet = connection.query(AnalisePerfilCliente.class);
        count = resultSet.size();
        if (!isKeepAliveConnection()) {  //Se não for para manter o ObjetContainer, fecha
            closeConnection();
        }
        return count;
    }    // </editor-fold>
}
