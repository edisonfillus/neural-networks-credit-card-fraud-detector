package org.ai.bo.ambiente;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

import org.ai.dao.factory.HsqldbDAOFactory;


@Entity
@Table(name="TERMINAL_POS",schema="public")
/**
 *
 * @author Edison
 */
public class TerminalPOS implements Serializable {

    public static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Atributos">

    private int id;
    private Estabelecimento estabelecimento;
    private Rede rede;
    private List<Transacao> transacoes;
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Propriedades">
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Basic(optional=false)
    @Column(name="ID_TERMINAL_POS")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    @JoinColumn(name = "ID_ESTABELECIMENTO", referencedColumnName = "ID_ESTABELECIMENTO")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    public Estabelecimento getEstabelecimento() {
        return estabelecimento;
    }

    public void setEstabelecimento(Estabelecimento estabelecimento) {
        this.estabelecimento = estabelecimento;
    }



    @Enumerated(value = EnumType.ORDINAL)
    @Basic(optional = false)
    @Column(name = "ID_REDE")
    public Rede getRede() {
        return rede;
    }

    public void setRede(Rede rede) {
        this.rede = rede;
    }



    @OneToMany(mappedBy = "terminalPOS", fetch = FetchType.LAZY,cascade=CascadeType.ALL)
    public List<Transacao> getTransacoes() {
        return transacoes;
    }

    public void setTransacoes(List<Transacao> transacoes) {
        this.transacoes = transacoes;
    }

    
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construtores">
    
    public TerminalPOS() {
    }

    public TerminalPOS(int id, Estabelecimento estabelecimento) {
        this.id = id;
        this.estabelecimento = estabelecimento;
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos Persistência">
    
     /**
     * Método para criar o objeto no local de persistência
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean create(){
        return HsqldbDAOFactory.getTerminalPOSDAO().createTerminalPOS(this);
    }

    /**
     * Método para atualizar o objeto no local de persistência
     * @return
     */
    public boolean update(){
        return HsqldbDAOFactory.getTerminalPOSDAO().updateTerminalPOS(this);
    }

    /**
     * Método para excluir o objeto no local de persistência
     * @return
     */
    public boolean delete(){
        return HsqldbDAOFactory.getTerminalPOSDAO().deleteTerminalPOS(this);
    }

    /**
     * Método para localizar um objeto TerminalPOS no local de persitência
     * @param id
     * @return TerminalPOS
     */
    public static TerminalPOS find(int id){
        return HsqldbDAOFactory.getTerminalPOSDAO().findTerminalPOS(id);
    }

    /**
     * Método para obter uma lista de TerminalPOS do local de persistência
     * @return
     */
    public static List<TerminalPOS> findAll(){
        return HsqldbDAOFactory.getTerminalPOSDAO().listTerminalPOS();
    }

    /**
     * Método para obter a quantidade de TerminalPOS do local de persistência
     * @return
     */
    public static int count(){
        return HsqldbDAOFactory.getTerminalPOSDAO().getTerminalPOSCount();
    }

    /**
     * Método para obter uma lista de TerminalPOS de uma determinada cidade e determinado ramo de atividade
     * @param cidade
     * @param ramoAtividade
     * @return
     */
    public static List<TerminalPOS> findByCidadeAndRamoAtividade(Cidade cidade, RamoAtividade ramoAtividade) {
        return HsqldbDAOFactory.getTerminalPOSDAO().listTerminalPOSByCidadeAndRamoAtividade(cidade, ramoAtividade);
    }

    /**
     * Método para obter uma lista de TerminalPOS de uma determinada regiao e determinado ramo de atividade,
     * excluindo determinada cidade
     * @param cidade
     * @param ramoAtividade
     * @return
     */
    public static List<TerminalPOS> findByRegiaoAndRamoAtividadeExcluindoCidade(Cidade cidade, RamoAtividade ramoAtividade) {
        return HsqldbDAOFactory.getTerminalPOSDAO().listTerminalPOSByRegiaoAndRamoAtividadeExcluindoCidade(cidade, ramoAtividade);
    }

    /**
     * Método para obter uma lista de objetos TerminalPOS de um determinado estado e determinado ramo de atividade,
     * excluindo determinada regiao
     * @param cidade
     * @param ramoAtividade
     * @return
     */
    public static List<TerminalPOS> findByEstadoAndRamoAtividadeExcluindoRegiao(Cidade cidade, RamoAtividade ramoAtividade) {
        return HsqldbDAOFactory.getTerminalPOSDAO().listTerminalPOSByEstadoAndRamoAtividadeExcluindoRegiao(cidade, ramoAtividade);
    }

     /**
     * Método para obter uma lista de objetos TerminalPOS de um determinado ramo de atividade,
     * excluindo determinado estado
     * @param cidade
     * @param ramoAtividade
     * @return
     */
    public static List<TerminalPOS> findByRamoAtividadeExcluindoEstado(Cidade cidade, RamoAtividade ramoAtividade){
        return HsqldbDAOFactory.getTerminalPOSDAO().listTerminalPOSByRamoAtividadeExcluindoEstado(cidade, ramoAtividade);
    }




    // </editor-fold>
    
}

