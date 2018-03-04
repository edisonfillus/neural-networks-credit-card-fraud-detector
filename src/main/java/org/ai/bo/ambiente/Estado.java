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
@Table(name = "ESTADO" , schema="public")

/**
 *
 * @author Edison
 */
public class Estado implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // <editor-fold defaultstate="collapsed" desc="Atributos">

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_ESTADO")
    private int id;
    
    @Basic(optional = false)
    @Column(name = "NOME_ESTADO")
    private String nome;
    
    @Basic(optional = false)
    @Column(name = "SIGLA_ESTADO")
    private String sigla;
    
    @OneToMany(mappedBy = "estado", fetch = FetchType.LAZY)
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
    
    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }
    
    public List<Cidade> getCidades() {
        return cidades;
    }

    public void setCidades(List<Cidade> cidades) {
        this.cidades = cidades;
    }
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Contrutores">
    
    public Estado() {
    }

    public Estado(int id, String nome) {
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
        return HsqldbDAOFactory.getEstadoDAO().createEstado(this);
    }

    /**
     * Método para atualizar o objeto no local de persistência
     * @return
     */
    public boolean update(){
        return HsqldbDAOFactory.getEstadoDAO().updateEstado(this);
    }

    /**
     * Método para excluir o objeto no local de persistência
     * @return
     */
    public boolean delete(){
        return HsqldbDAOFactory.getEstadoDAO().deleteEstado(this);
    }

    /**
     * Método para localizar um objeto Estado no local de persitência
     * @param id
     * @return Estado
     */
    public static Estado find(int id){
        return HsqldbDAOFactory.getEstadoDAO().findEstado(id);
    }

    /**
     * Método para obter uma lista de Estado do local de persistência
     * @return
     */
    public static List<Estado> findAll(){
        return HsqldbDAOFactory.getEstadoDAO().listEstado();
    }

    /**
     * Método para obter a quantidade de Estado do local de persistência
     * @return
     */
    public static int count(){
        return HsqldbDAOFactory.getEstadoDAO().getEstadoCount();
    }
    
    // </editor-fold>
    
}
