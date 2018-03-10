package org.ai.dao.factory;

import com.db4o.*;


import org.ai.dao.impl.db4o.Db4oAnalisePerfilClienteDAO;
import org.ai.dao.impl.db4o.Db4oAnaliseTransacaoDAO;
import org.ai.dao.impl.db4o.Db4oRedeNeuralDAO;
import org.ai.dao.interfaces.analise.AnalisePerfilClienteDAO;
import org.ai.dao.interfaces.analise.AnaliseTransacaoDAO;
import org.ai.dao.interfaces.neural.RedeNeuralDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Db4oDAOFactory {

    // <editor-fold defaultstate="collapsed" desc="Parâmetros">
    
    private static final String HOST_NAME = "localhost";
    private static final int PORT = 4488;
    private static final String USER = "bd4o";
    private static final String PASSWORD = "bd4o";
    private static final String FILE = "database/bd.db4o";

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Atributos">
    
    private static ObjectContainer connection;
    private static boolean keepAliveConnection = false;
    protected static Logger logger = LogManager.getLogger("daoLogger"); //Log do DAO
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos Connection">
    
    /**
     * Método para criar conexões com o Db4o
     * @return
     */
    protected ObjectContainer getConnection() {
        if (connection == null){
            connection = Db4o.openFile(FILE);
        }
        return connection;
        //return Db4o.openClient(HOST_NAME, PORT, USER, PASSWORD);
    }
    
     /**
     * Método para fechar conexão com o Db4o
     * @return
     */
    public static boolean closeConnection() {
        if (connection != null){
            connection.close();
            connection = null;
        }
        return true;
    }
    
    /**
     * Método para verificar se uma conexão continua ativa
     * @return
     */
    public static boolean isConnectionAlive(){
        if(!connection.ext().isClosed()){
            return true;
        } else {
            return false;
        }       
    }
    
    /**
     * Método para verificar se o factory deve manter a conexão ativa
     * @return
     */
    public static boolean isKeepAliveConnection() {
        return keepAliveConnection;
    }
    
    
    /**
     * Método para definir se a conexão deverá manter-se ativa
     * @param keepAliveConnection
     */
    public static void setKeepAliveConnection(boolean keepAliveConnection) {
        Db4oDAOFactory.keepAliveConnection = keepAliveConnection;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos DAO">

    /**
     * Método para retornar um DAO de RedeNeural para Db4o
     * @return
     */
    public static RedeNeuralDAO getRedeNeuralDAO() {
        return new Db4oRedeNeuralDAO();
    }
    
     /**
     * Método para retornar um DAO de AnalisePerfilCliente para Db4o
     * @return
     */
    public static AnalisePerfilClienteDAO getAnalisePerfilClienteDAO() {
        return new Db4oAnalisePerfilClienteDAO();
    }
    
     /**
     * Método para retornar um DAO de AnaliseTransacao para Db4o
     * @return
     */
    public static AnaliseTransacaoDAO getAnaliseTransacaoDAO() {
        return new Db4oAnaliseTransacaoDAO();
    }
    
    
    
    // </editor-fold>
   
}
