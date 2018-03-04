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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.ai.dao.factory.HsqldbDAOFactory;


@Entity
@Table(name = "CIDADE" , schema="public")
/**
 *
 * @author Edison
 */
public class Cidade implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // <editor-fold defaultstate="collapsed" desc="Atributos">
    
   
    private int id;
    private String nome;
    private Estado estado;
    private Regiao regiao;
    private List<Estabelecimento> estabelecimentos;



    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Propriedades">
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_CIDADE")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "NOME_CIDADE")
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @JoinColumn(name = "ID_ESTADO", referencedColumnName = "ID_ESTADO")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    @JoinColumn(name = "ID_REGIAO", referencedColumnName = "ID_REGIAO")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    public Regiao getRegiao() {
        return regiao;
    }

    public void setRegiao(Regiao regiao) {
        this.regiao = regiao;
    }

    @OneToMany(mappedBy = "cidade", fetch = FetchType.LAZY)
    public List<Estabelecimento> getEstabelecimentos() {
        return estabelecimentos;
    }

    public void setEstabelecimentos(List<Estabelecimento> estabelecimentos) {
        this.estabelecimentos = estabelecimentos;
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Construtores">
    
    public Cidade() {
    }

    public Cidade(int id, String nome, Estado estado, Regiao regiao) {
        this.id = id;
        this.nome = nome;
        this.estado = estado;
        this.regiao = regiao;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos Persistência">
    
     /**
     * Método para criar o objeto no local de persistência
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean create(){
        return HsqldbDAOFactory.getCidadeDAO().createCidade(this);
    }

    /**
     * Método para atualizar o objeto no local de persistência
     * @return
     */
    public boolean update(){
        return HsqldbDAOFactory.getCidadeDAO().updateCidade(this);
    }

    /**
     * Método para excluir o objeto no local de persistência
     * @return
     */
    public boolean delete(){
        return HsqldbDAOFactory.getCidadeDAO().deleteCidade(this);
    }

    /**
     * Método para localizar um objeto Cidade no local de persitência
     * @param id
     * @return Cidade
     */
    public static Cidade find(int id){
        return HsqldbDAOFactory.getCidadeDAO().findCidade(id);
    }

    /**
     * Método para obter uma lista de Cidade do local de persistência
     * @return
     */
    public static List<Cidade> findAll(){
        return HsqldbDAOFactory.getCidadeDAO().listCidade();
    }

    /**
     * Método para obter a quantidade de Cidade do local de persistência
     * @return
     */
    public static int count(){
        return HsqldbDAOFactory.getCidadeDAO().getCidadeCount();
    }
    
    // </editor-fold>
    
}
