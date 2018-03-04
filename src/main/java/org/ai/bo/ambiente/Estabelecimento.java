package org.ai.bo.ambiente;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import javax.persistence.*;

import org.ai.dao.factory.HsqldbDAOFactory;


@Entity
@Table(name="ESTABELECIMENTO",schema="public")
/**
 *
 * @author Edison
 */
public class Estabelecimento implements Serializable {
    
    public static final long serialVersionUID = 1L;
    
    // <editor-fold defaultstate="collapsed" desc="Atributos">
    
    private int id;
    private String nome;
    private Calendar dataAfiliacao;
    private Cidade cidade;
    private RamoAtividade ramoAtividade;
    private List<TerminalPOS> terminaisPOS;

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Propriedades">
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Basic(optional=false)
    @Column(name="ID_ESTABELECIMENTO")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    @Basic(optional = false)
    @Column(name = "NOME_ESTABELECIMENTO")
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }



    @Basic(optional = false)
    @Temporal(value = TemporalType.DATE)
    @Column(name = "DATA_AFILIACAO")
    public Calendar getDataAfiliacao() {
        return dataAfiliacao;
    }

    public void setDataAfiliacao(Calendar dataAfiliacao) {
        this.dataAfiliacao = dataAfiliacao;
    }



    @JoinColumn(name = "ID_CIDADE", referencedColumnName = "ID_CIDADE")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }



    @JoinColumn(name = "ID_RAMO_ATIVIDADE", referencedColumnName = "ID_RAMO_ATIVIDADE")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    public RamoAtividade getRamoAtividade() {
        return ramoAtividade;
    }

    public void setRamoAtividade(RamoAtividade ramoAtividade) {
        this.ramoAtividade = ramoAtividade;
    }



    @OneToMany(mappedBy = "estabelecimento", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    public List<TerminalPOS> getTerminaisPOS() {
        return terminaisPOS;
    }

    public void setTerminaisPOS(List<TerminalPOS> terminaisPOS) {
        this.terminaisPOS = terminaisPOS;
    }


    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construtores">
    
    public Estabelecimento() {
    }

    public Estabelecimento(int id, String nome, Calendar dataAfiliacao, Cidade cidade, RamoAtividade ramoAtividade) {
        this.id = id;
        this.nome = nome;
        this.dataAfiliacao = dataAfiliacao;
        this.cidade = cidade;
        this.ramoAtividade = ramoAtividade;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos">
    
    public int getCountTransacoes(){
        int total = 0;
        for (TerminalPOS terminalPOS : terminaisPOS) {
            total += terminalPOS.getTransacoes().size();
        }
        return total;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos Persistência">
    
     /**
     * Método para criar o objeto no local de persistência
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean create(){
        return HsqldbDAOFactory.getEstabelecimentoDAO().createEstabelecimento(this);
    }

    /**
     * Método para atualizar o objeto no local de persistência
     * @return
     */
    public boolean update(){
        return HsqldbDAOFactory.getEstabelecimentoDAO().updateEstabelecimento(this);
    }

    /**
     * Método para excluir o objeto no local de persistência
     * @return
     */
    public boolean delete(){
        return HsqldbDAOFactory.getEstabelecimentoDAO().deleteEstabelecimento(this);
    }

    /**
     * Método para localizar um objeto Estabelecimento no local de persitência
     * @param id
     * @return Estabelecimento
     */
    public static Estabelecimento find(int id){
        return HsqldbDAOFactory.getEstabelecimentoDAO().findEstabelecimento(id);
    }

    /**
     * Método para obter uma lista de Estabelecimento do local de persistência
     * @return
     */
    public static List<Estabelecimento> findAll(){
        return HsqldbDAOFactory.getEstabelecimentoDAO().listEstabelecimento();
    }

    /**
     * Método para obter a quantidade de Estabelecimento do local de persistência
     * @return
     */
    public static int count(){
        return HsqldbDAOFactory.getEstabelecimentoDAO().getEstabelecimentoCount();
    }
    
    // </editor-fold>

}
