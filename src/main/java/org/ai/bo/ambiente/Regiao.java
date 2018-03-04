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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.ai.dao.factory.HsqldbDAOFactory;


@Entity
@Table(name="REGIAO" , schema="public")
/**
 *
 * @author Edison
 */
public class Regiao implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Atributos">

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_REGIAO")
    private int id;

    @Basic(optional = false)
    @Column(name = "NOME_REGIAO")
    private String nome;

    @OneToMany(mappedBy = "regiao", fetch = FetchType.LAZY)
    private List<Cidade> cidades;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Propriedades">
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Cidade> getCidades() {
        return cidades;
    }

    public void setCidades(List<Cidade> cidades) {
        this.cidades = cidades;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construtores">
    public Regiao() {
    }
    // </editor-fold>
   
    // <editor-fold defaultstate="collapsed" desc="Métodos Persistência">
    
     /**
     * Método para criar o objeto no local de persistência
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean create(){
        return HsqldbDAOFactory.getRegiaoDAO().createRegiao(this);
    }

    /**
     * Método para atualizar o objeto no local de persistência
     * @return
     */
    public boolean update(){
        return HsqldbDAOFactory.getRegiaoDAO().updateRegiao(this);
    }

    /**
     * Método para excluir o objeto no local de persistência
     * @return
     */
    public boolean delete(){
        return HsqldbDAOFactory.getRegiaoDAO().deleteRegiao(this);
    }

    /**
     * Método para localizar um objeto Regiao no local de persitência
     * @param id
     * @return Regiao
     */
    public static Regiao find(int id){
        return HsqldbDAOFactory.getRegiaoDAO().findRegiao(id);
    }

    /**
     * Método para obter uma lista de Regiao do local de persistência
     * @return
     */
    public static List<Regiao> findAll(){
        return HsqldbDAOFactory.getRegiaoDAO().listRegiao();
    }

    /**
     * Método para obter a quantidade de Regiao do local de persistência
     * @return
     */
    public static int count(){
        return HsqldbDAOFactory.getRegiaoDAO().getRegiaoCount();
    }
    
    // </editor-fold>
    
}
