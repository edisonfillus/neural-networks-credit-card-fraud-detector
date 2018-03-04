package org.ai.bo.ambiente;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

import org.ai.dao.factory.HsqldbDAOFactory;

@Entity
@Table(name="ANALISTA")
/**
 *
 * @author Edison
 */
public class Analista implements Serializable {

    public static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Atributos">

    @Id
    @Basic(optional=false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="ID_ANALISTA")
    private int id;

    @Basic(optional=false)
    @Column(name="NOME_ANALISTA")
    private String nome;

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
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construtores">
    
    public Analista() {
    }

    public Analista(String nome) {
        this.nome = nome;
    }



    public Analista(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos Persistência">
    
     /**
     * Método para criar o objeto no local de persistência
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean create(){
        return HsqldbDAOFactory.getAnalistaDAO().createAnalista(this);
    }

    /**
     * Método para atualizar o objeto no local de persistência
     * @return
     */
    public boolean update(){
        return HsqldbDAOFactory.getAnalistaDAO().updateAnalista(this);
    }

    /**
     * Método para excluir o objeto no local de persistência
     * @return
     */
    public boolean delete(){
        return HsqldbDAOFactory.getAnalistaDAO().deleteAnalista(this);
    }

    /**
     * Método para localizar um objeto Analista no local de persitência
     * @param id
     * @return Analista
     */
    public static Analista find(int id){
        return HsqldbDAOFactory.getAnalistaDAO().findAnalista(id);
    }

    /**
     * Método para obter uma lista de Analista do local de persistência
     * @return
     */
    public static List<Analista> findAll(){
        return HsqldbDAOFactory.getAnalistaDAO().listAnalista();
    }

    /**
     * Método para obter a quantidade de Analista do local de persistência
     * @return
     */
    public static int count(){
        return HsqldbDAOFactory.getAnalistaDAO().getAnalistaCount();
    }
    
    // </editor-fold>
    
}
