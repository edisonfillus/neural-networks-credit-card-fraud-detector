/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ai.dao.impl.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ai.bo.analise.CasoTeste;
import org.ai.dao.factory.PostgreSqlDAOFactory;
import org.ai.dao.interfaces.analise.CasoDAO;

/**
 *
 * @author Edison
 */
public class PostgreSqlCasoDAO extends PostgreSqlDAOFactory implements CasoDAO {

    // <editor-fold defaultstate="collapsed" desc="Parâmetros">
    
    private static String DBTABLE = "CASO";
    private static String SEQUENCE_NAME = "seq_caso";
    private static String FIELDS_INSERT = "desvio_valor_compra, distancia_cidade_origem, quantidade_compras_dia, hora_compra, saida_esperada";
    private static String FIELDS_SELECT = "id_caso, " + FIELDS_INSERT;
    private static String INSERT_SQL = "INSERT INTO " + DBTABLE + " ( " + FIELDS_INSERT + " ) " + " VALUES ( ?, ?, ?, ?, ?)";
    private static String SELECT_SQL = "SELECT " + FIELDS_SELECT + " FROM " + DBTABLE;
    private static String UPDATE_SQL = "UPDATE " + DBTABLE + " set desvio_valor_compra = ?, distancia_cidade_origem = ?, quantidade_compras_dia = ?, hora_compra = ?, saida_esperada = ? " +
            "WHERE id_caso = ? ";
    private static String DELETE_SQL = "DELETE FROM " + DBTABLE + " WHERE id_caso = ? ";
    private static String LASTID_SQL = "SELECT last_value FROM \"" + SEQUENCE_NAME + "\"";

    // </editor-fold>
            
    // <editor-fold defaultstate="collapsed" desc="Contrutores">
    
    public PostgreSqlCasoDAO() {
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos DAO CRUD">

    public int createCaso(CasoTeste caso) {
        Connection connection = super.getJDBCConnection();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(INSERT_SQL);
            int i = 1;
            statement.setDouble(i++, caso.getDesvioPadraoValorCompra());
            statement.setDouble(i++, caso.getDistanciaCidadeOrigem());
            statement.setDouble(i++, caso.getQuantidadeComprasDia());
            statement.setDouble(i++, caso.getHora());
            statement.setDouble(i++, caso.getSaidaEsperada()[0]);
            int numRows = statement.executeUpdate();

            //Recupera o ID
            if (numRows == 1) {
                statement.clearParameters();
                statement = connection.prepareStatement(LASTID_SQL);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    caso.setIdCaso(rs.getInt(1));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PostgreSqlCasoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (!isKeepAliveJDBCConnection()) {  //Se não for para manter a conexão, fecha
                closeJDBCConnection();
            }
        }

        return caso.getIdCaso();

    }

    public boolean deleteCaso(CasoTeste caso) {
        boolean result = false;
        Connection connection = super.getJDBCConnection();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(DELETE_SQL);
            statement.setInt(1, caso.getIdCaso());
            int numRows = statement.executeUpdate();
            if (numRows == 1) {
                result = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(PostgreSqlCasoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (!isKeepAliveJDBCConnection()) {  //Se não for para manter a conexão, fecha
                closeJDBCConnection();
            }
        }
        return result;
    }

    public CasoTeste findCaso(int idCaso) {
        CasoTeste caso = null;
        Connection connection = super.getJDBCConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(SELECT_SQL + " WHERE id_caso = ?");
            statement.setInt(1, idCaso);
            rs = statement.executeQuery();
            if (rs.next()) {
                caso = new CasoTeste();
                caso.setIdCaso(idCaso);
                caso.setDesvioPadraoValorCompra(rs.getDouble("desvio_valor_compra"));
                caso.setDistanciaCidadeOrigem(rs.getDouble("distancia_cidade_origem"));
                caso.setQuantidadeComprasDia(rs.getDouble("quantidade_compras_dia"));
                caso.setHora(rs.getDouble("hora_compra"));
                caso.setSaidaEsperada(new double[]{rs.getDouble("saida_esperada")});
            }
        } catch (SQLException ex) {
            Logger.getLogger(PostgreSqlCasoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (!isKeepAliveJDBCConnection()) {  //Se não for para manter a conexão, fecha
                closeJDBCConnection();
            }
        }
        return caso;

    }
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Métodos DAO Complementares">
    
    public List<CasoTeste> listCaso() {
        List<CasoTeste> casos = new ArrayList<CasoTeste>();
        Connection connection = super.getJDBCConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(SELECT_SQL);
            rs = statement.executeQuery();
            while (rs.next()) {
                CasoTeste caso = new CasoTeste();
                caso.setIdCaso(rs.getInt("id_caso"));
                caso.setDesvioPadraoValorCompra(rs.getDouble("desvio_valor_compra"));
                caso.setDistanciaCidadeOrigem(rs.getDouble("distancia_cidade_origem"));
                caso.setQuantidadeComprasDia(rs.getDouble("quantidade_compras_dia"));
                caso.setHora(rs.getDouble("hora_compra"));
                caso.setSaidaEsperada(new double[]{rs.getDouble("saida_esperada")});
                casos.add(caso);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PostgreSqlCasoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (!isKeepAliveJDBCConnection()) {  //Se não for para manter a conexão, fecha
                closeJDBCConnection();
            }
        }
        return casos;

    }
    
    // </editor-fold>
}
