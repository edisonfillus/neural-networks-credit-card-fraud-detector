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
@Table(name="RAMO_ATIVIDADE", schema="public")

/**
 *
 * @author Edison
 */
public class RamoAtividade implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // <editor-fold defaultstate="collapsed" desc="Atributos">
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_RAMO_ATIVIDADE")
    private int id;

    @Basic(optional = false)
    @Column(name = "NOME_RAMO_ATIVIDADE")
    private String nome;
    
    @OneToMany(mappedBy = "ramoAtividade", fetch = FetchType.LAZY)
    private List<Estabelecimento> estabelecimentos;


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

    public List<Estabelecimento> getEstabelecimentos() {
        return estabelecimentos;
    }

    public void setEstabelecimentos(List<Estabelecimento> estabelecimentos) {
        this.estabelecimentos = estabelecimentos;
    }
    
    
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construtores">
    
    public RamoAtividade() {
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos Persistência">
    
     /**
     * Método para criar o objeto no local de persistência
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean create(){
        return HsqldbDAOFactory.getRamoAtividadeDAO().createRamoAtividade(this);
    }

    /**
     * Método para atualizar o objeto no local de persistência
     * @return
     */
    public boolean update(){
        return HsqldbDAOFactory.getRamoAtividadeDAO().updateRamoAtividade(this);
    }

    /**
     * Método para excluir o objeto no local de persistência
     * @return
     */
    public boolean delete(){
        return HsqldbDAOFactory.getRamoAtividadeDAO().deleteRamoAtividade(this);
    }

    /**
     * Método para localizar um objeto RamoAtividade no local de persitência
     * @param id
     * @return RamoAtividade
     */
    public static RamoAtividade find(int id){
        return HsqldbDAOFactory.getRamoAtividadeDAO().findRamoAtividade(id);
    }

    /**
     * Método para obter uma lista de RamoAtividade do local de persistência
     * @return
     */
    public static List<RamoAtividade> findAll(){
        return HsqldbDAOFactory.getRamoAtividadeDAO().listRamoAtividade();
    }

    /**
     * Método para obter a quantidade de RamoAtividade do local de persistência
     * @return
     */
    public static int count(){
        return HsqldbDAOFactory.getRamoAtividadeDAO().getRamoAtividadeCount();
    }
    
    // </editor-fold>
    
    @Override
    public String toString(){
        return nome;
    }

}
