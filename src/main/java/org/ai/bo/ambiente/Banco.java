/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ai.bo.ambiente;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.ai.dao.factory.HsqldbDAOFactory;

@Entity
@Table(name = "BANCO")
/**
 *
 * @author Edison
 */
public class Banco implements Serializable {

    public static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Atritutos">
    
    @Id
    @Basic(optional = false)
    @Column(name = "COD_BANCO")
    private int codBanco;
    
    @Basic(optional = false)
    @Column(name = "NOME_BANCO")
    private String nome;
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Propriedades">
    
    public int getCodBanco() {
        return codBanco;
    }

    public void setCodBanco(int cod) {
        this.codBanco = cod;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Construtores">
    
    public Banco() {
    }

    public Banco(int codBanco, String nome) {
        this.codBanco = codBanco;
        this.nome = nome;
    }
    
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Métodos Persistência">
    
     /**
     * Método para criar o objeto no local de persistência
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean create(){
        return HsqldbDAOFactory.getBancoDAO().createBanco(this);
    }

    /**
     * Método para atualizar o objeto no local de persistência
     * @return
     */
    public boolean update(){
        return HsqldbDAOFactory.getBancoDAO().updateBanco(this);
    }

    /**
     * Método para excluir o objeto no local de persistência
     * @return
     */
    public boolean delete(){
        return HsqldbDAOFactory.getBancoDAO().deleteBanco(this);
    }

    /**
     * Método para localizar um objeto Banco no local de persitência
     * @param id
     * @return Banco
     */
    public static Banco find(int id){
        return HsqldbDAOFactory.getBancoDAO().findBanco(id);
    }

    /**
     * Método para obter uma lista de Banco do local de persistência
     * @return
     */
    public static List<Banco> findAll(){
        return HsqldbDAOFactory.getBancoDAO().listBanco();
    }

    /**
     * Método para obter a quantidade de Banco do local de persistência
     * @return
     */
    public static int count(){
        return HsqldbDAOFactory.getBancoDAO().getBancoCount();
    }
    
    // </editor-fold>
    
}
