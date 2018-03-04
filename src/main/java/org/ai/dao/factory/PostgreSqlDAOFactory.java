package org.ai.dao.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.ai.dao.impl.postgresql.*;
import org.ai.dao.interfaces.*;
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
import org.ai.dao.interfaces.analise.CasoDAO;
import org.apache.log4j.Logger;

public class PostgreSqlDAOFactory {

    // <editor-fold defaultstate="collapsed" desc="Parâmetros">

    private static final String DBDRIVER = "org.postgresql.Driver";
    private static final String DBURL = "jdbc:postgresql://nbsrv:5432/tcc";
    private static final String DBUSER = "postgres";
    private static final String DBPASSWORD = "123456";
    private static final String PERSISTENCE_UNIT = "PostgreSQL";

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Atributos">
    
    private static Connection connection = null; //Cache para conexões JDBC
    private static EntityManager entityManager = null;  //Cache para Manager JPA
    private static boolean keepAliveJDBCConnection = false; //Flag Manter Conexão JDBC Ativa
    private static boolean keepAliveJPAEntityManager = false; //Flag Manter Entity Manager JPA Ativo
    protected static Logger logger = Logger.getLogger("daoLogger"); //Log do DAO
    
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
        PostgreSqlDAOFactory.keepAliveJDBCConnection = keepAliveJDBCConnection;
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
        PostgreSqlDAOFactory.keepAliveJPAEntityManager = keepAliveJPAEntityManager;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos Connection">


    /**
     * Método para criar um EntityManager (JPA) para o PostgreSQL
     * @return
     */
    protected EntityManager getJPAEntityManager() {
        if (entityManager == null) {
             EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
             entityManager = emf.createEntityManager();
        }
        return entityManager;
    }
    
    

    /**
     * Método para criar conexões JDBC com o PostgreSQL
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
     * Método para fechar conexão com o PostgreSQL
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

    protected void startJPATransaction(){
        if(entityManager != null){
            entityManager.getTransaction().begin();
        }
    }
    
    protected void commitJPATransaction(){
        if(entityManager != null){
            entityManager.getTransaction().commit();
        }
    }
    
    

    
    
    
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos DAO">

    public static CasoDAO getCasoDAO() {
        return new PostgreSqlCasoDAO();
    }

    public static BancoDAO getBancoDAO(){
        return new PostgreSqlBancoDAO();
    }

    public static EstadoDAO getEstadoDAO(){
        return new PostgreSqlEstadoDAO();
    }

    public static RegiaoDAO getRegiaoDAO(){
        return new PostgreSqlRegiaoDAO();
    }

    public static CidadeDAO getCidadeDAO(){
        return new PostgreSqlCidadeDAO();
    }

    public static ClienteDAO getClienteDAO(){
        return new PostgreSqlClienteDAO();
    }

    public static RespostaDAO getRespostaDAO(){
        return new PostgreSqlRespostaDAO();
    }

    public static CartaoDAO getCartaoDAO(){
        return new PostgreSqlCartaoDAO();
    }

    public static RamoAtividadeDAO getRamoAtividadeDAO(){
        return new PostgreSqlRamoAtividadeDAO();
    }

    public static EstabelecimentoDAO getEstabelecimentoDAO(){
        return new PostgreSqlEstabelecimentoDAO();
    }

    public static TerminalPOSDAO getTerminalPOSDAO(){
        return new PostgreSqlTerminalPOSDAO();
    }

    public static AnalistaDAO getAnalistaDAO(){
        return new PostgreSqlAnalistaDAO();
    }

    public static AlertaDAO getAlertaDAO(){
        return new PostgreSqlAlertaDAO();
    }

    public static TransacaoDAO getTransacaoDAO(){
        return new PostgreSqlTransacaoDAO();
    }

    public static ItemDAO getItemDAO(){
        return new PostgreSqlItemDAO();
    }

    // </editor-fold>
}
