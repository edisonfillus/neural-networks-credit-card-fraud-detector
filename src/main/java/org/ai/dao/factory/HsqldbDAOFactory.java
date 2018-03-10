package org.ai.dao.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.ai.dao.impl.hsqldb.HsqldbAlertaDAO;
import org.ai.dao.impl.hsqldb.HsqldbAnalistaDAO;
import org.ai.dao.impl.hsqldb.HsqldbBancoDAO;
import org.ai.dao.impl.hsqldb.HsqldbCartaoDAO;
import org.ai.dao.impl.hsqldb.HsqldbCidadeDAO;
import org.ai.dao.impl.hsqldb.HsqldbClienteDAO;
import org.ai.dao.impl.hsqldb.HsqldbEstabelecimentoDAO;
import org.ai.dao.impl.hsqldb.HsqldbEstadoDAO;
import org.ai.dao.impl.hsqldb.HsqldbItemDAO;
import org.ai.dao.impl.hsqldb.HsqldbRamoAtividadeDAO;
import org.ai.dao.impl.hsqldb.HsqldbRegiaoDAO;
import org.ai.dao.impl.hsqldb.HsqldbRespostaDAO;
import org.ai.dao.impl.hsqldb.HsqldbTerminalPOSDAO;
import org.ai.dao.impl.hsqldb.HsqldbTransacaoDAO;
import org.ai.dao.interfaces.ambiente.AlertaDAO;
import org.ai.dao.interfaces.ambiente.AnalistaDAO;
import org.ai.dao.interfaces.ambiente.BancoDAO;
import org.ai.dao.interfaces.ambiente.CartaoDAO;
import org.ai.dao.interfaces.ambiente.CidadeDAO;
import org.ai.dao.interfaces.ambiente.ClienteDAO;
import org.ai.dao.interfaces.ambiente.EstabelecimentoDAO;
import org.ai.dao.interfaces.ambiente.EstadoDAO;
import org.ai.dao.interfaces.ambiente.ItemDAO;
import org.ai.dao.interfaces.ambiente.RamoAtividadeDAO;
import org.ai.dao.interfaces.ambiente.RegiaoDAO;
import org.ai.dao.interfaces.ambiente.RespostaDAO;
import org.ai.dao.interfaces.ambiente.TerminalPOSDAO;
import org.ai.dao.interfaces.ambiente.TransacaoDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HsqldbDAOFactory {

    // <editor-fold defaultstate="collapsed" desc="Parâmetros">

    private static final String DBDRIVER = "org.hsqldb.jdbcDriver";
    private static final String DBURL = "jdbc:hsqldb:file:database/hsqldb";
    private static final String DBUSER = "sa";
    private static final String DBPASSWORD = "";
    private static final String PERSISTENCE_UNIT = "HSQLDB";

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Atributos">
    
    private static Connection connection = null; //Cache para conexões JDBC
    private static EntityManager entityManager = null;  
    private static boolean keepAliveJDBCConnection = false; //Flag Manter Conexão JDBC Ativa
    private static boolean keepAliveJPAEntityManager = false; //Flag Manter Entity Manager JPA Ativo
    protected static Logger logger = LogManager.getLogger("daoLogger"); //Log do DAO
    private static EntityManagerFactory emf = null;//Cache para Manager JPA
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Propriedades">
    /**
     * Verifica se a conexão JDBC deverá ser mantida ativa.
     * @return
     */
    public static boolean isKeepAliveJDBCConnection() {
        return keepAliveJDBCConnection;
    }

    /**
     * Seta a propriedade de manter a conexão JDBC ativa
     * @param keepAliveJDBCConnection 
     */
    public static void setKeepAliveJDBCConnection(boolean keepAliveJDBCConnection) {
        HsqldbDAOFactory.keepAliveJDBCConnection = keepAliveJDBCConnection;
    }


    /**
     * Verifica se o Entity Manager JPA deverá ser mantido Ativo
     * @return
     */
    public static boolean isKeepAliveJPAEntityManager() {
        return keepAliveJPAEntityManager;
    }

    /**
     * Seta a propriedade de manter o Entity Manager JPA Ativo
     * @param keepAliveJPAEntityManager
     */
    public static void setKeepAliveJPAEntityManager(boolean keepAliveJPAEntityManager) {
        HsqldbDAOFactory.keepAliveJPAEntityManager = keepAliveJPAEntityManager;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos Connection">


    /**
     * Método para criar um EntityManager (JPA) para o Hsqldb
     * @return
     */
    protected EntityManager getJPAEntityManager() {
        if( emf == null || !emf.isOpen( ) ) {
             emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        }
        if (entityManager == null || !entityManager.isOpen()){
            entityManager = emf.createEntityManager();
        }
        return entityManager;
    }
    
    

    /**
     * Método para criar conexões JDBC com o Hsqldb
     * @return
     */
    protected Connection getJDBCConnection() {
        if (connection == null) {
            try {
                Class.forName(DBDRIVER);
                connection = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD);
            } catch (ClassNotFoundException ex) {
                logger.error("Não foi encontrado o driver JDBC: " + DBDRIVER, ex);
            } catch (SQLException ex) {
                logger.error("Erro ao criar conexão",ex);
            }
        }
        return connection;
    }

    /**
     * Método para fechar conexão com o Hsqldb
     * @return
     */
    public static boolean closeJDBCConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException ex) {
                logger.error("Erro ao fechar conexão.",ex);
            }
        }
        return true;
    }
    
     /**
     * Método para fechar o EntityManager
     * @return
     */
    public static boolean closeJPAEntityManager() {
        if (entityManager != null) {
            entityManager.close();
            entityManager = null;
        }
        return true;
    }





    /**
     * Método para verificar se uma conexão continua ativa
     * @return
     */
    public static boolean isJDBCConnectionAlive() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    return true;
                }
            } catch (SQLException ex) {
                logger.error("Erro ao verificar conexão.",ex);
            }
        }
        return false;
    }

    



    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos Transaction">

    protected static void startJPATransaction(){
        if(entityManager != null){
            entityManager.getTransaction().begin();
        }
    }
    
    protected static void commitJPATransaction(){
        if(entityManager != null){
            entityManager.getTransaction().commit();
        }
    }
    
    

    
    
    
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos DAO">

    public static BancoDAO getBancoDAO(){
        return new HsqldbBancoDAO();
    }

    public static EstadoDAO getEstadoDAO(){
        return new HsqldbEstadoDAO();
    }

    public static RegiaoDAO getRegiaoDAO(){
        return new HsqldbRegiaoDAO();
    }

    public static CidadeDAO getCidadeDAO(){
        return new HsqldbCidadeDAO();
    }

    public static ClienteDAO getClienteDAO(){
        return new HsqldbClienteDAO();
    }

    public static RespostaDAO getRespostaDAO(){
        return new HsqldbRespostaDAO();
    }

    public static CartaoDAO getCartaoDAO(){
        return new HsqldbCartaoDAO();
    }

    public static RamoAtividadeDAO getRamoAtividadeDAO(){
        return new HsqldbRamoAtividadeDAO();
    }

    public static EstabelecimentoDAO getEstabelecimentoDAO(){
        return new HsqldbEstabelecimentoDAO();
    }

    public static TerminalPOSDAO getTerminalPOSDAO(){
        return new HsqldbTerminalPOSDAO();
    }

    public static AnalistaDAO getAnalistaDAO(){
        return new HsqldbAnalistaDAO();
    }

    public static AlertaDAO getAlertaDAO(){
        return new HsqldbAlertaDAO();
    }

    public static TransacaoDAO getTransacaoDAO(){
        return new HsqldbTransacaoDAO();
    }

    public static ItemDAO getItemDAO(){
        return new HsqldbItemDAO();
    }

    // </editor-fold>
}
