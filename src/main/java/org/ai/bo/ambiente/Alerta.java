package org.ai.bo.ambiente;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import javax.persistence.*;

import org.ai.dao.factory.HsqldbDAOFactory;

@Entity
@Table(name = "ALERTA")
/**
 *
 * @author Edison
 */
public class Alerta implements Serializable {

    public static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Atributos">

    private int id;
    private Transacao transacao;
    private StatusAlerta status;
    private Analista analista;
    private Calendar dhGeracao;
    private Calendar dhConfirmacao;
    private String descricaoMotivo;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Propriedades">
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Basic(optional=false)
    @Column(name="ID_ALERTA")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JoinColumn(name = "ID_TRANSACAO", referencedColumnName = "ID_TRANSACAO")
    //@PrimaryKeyJoinColumn(name = "ID_TRANSACAO", referencedColumnName = "ID_TRANSACAO")
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    public Transacao getTransacao() {
        return transacao;
    }

    public void setTransacao(Transacao transacao) {
        this.transacao = transacao;
    }

    @Enumerated(value = EnumType.ORDINAL)
    @Basic(optional = false)
    @Column(name = "COD_STATUS_ALERTA")
    public StatusAlerta getStatus() {
        return status;
    }

    public void setStatus(StatusAlerta status) {
        this.status = status;
    }

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ANALISTA", referencedColumnName = "ID_ANALISTA",nullable=true)
    public Analista getAnalista() {
        return analista;
    }

    public void setAnalista(Analista analista) {
        this.analista = analista;
    }

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "DH_GERACAO")
    public Calendar getDhGeracao() {
        return dhGeracao;
    }

    public void setDhGeracao(Calendar dhGeracao) {
        this.dhGeracao = dhGeracao;
    }

    
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "DH_CONFIRMACAO")
    public Calendar getDhConfirmacao() {
        return dhConfirmacao;
    }

    public void setDhConfirmacao(Calendar dhConfirmacao) {
        this.dhConfirmacao = dhConfirmacao;
    }

    @Basic(optional = true)
    @Column(name = "DESCRICAO_MOTIVO")
    public String getDescricaoMotivo() {
        return descricaoMotivo;
    }

    public void setDescricaoMotivo(String descricaoMotivo) {
        this.descricaoMotivo = descricaoMotivo;
    }

    

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construtores">

    public Alerta() {

    }
    
    // </editor-fold>
    
    public void confirmarFraude(){
        status = StatusAlerta.FRAUDE_CONFIRMADA;
        dhConfirmacao = Calendar.getInstance();
        transacao.setStatus(StatusTransacao.FRAUDE);
    }
    
    public void falsoPositivo(){
        status = StatusAlerta.FALSO_POSITIVO;
        dhConfirmacao = Calendar.getInstance();
        transacao.setStatus(StatusTransacao.FALSO_POSITIVO);
    }
    
    public void cancelarAlerta(){
        status = StatusAlerta.CANCELADO;
        dhConfirmacao = Calendar.getInstance();
        transacao.setStatus(StatusTransacao.ANALISE_OK);
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="Métodos Persistência">
    
     /**
     * Método para criar o objeto no local de persistência
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean create(){
        return HsqldbDAOFactory.getAlertaDAO().createAlerta(this);
    }

    /**
     * Método para atualizar o objeto no local de persistência
     * @return
     */
    public boolean update(){
        return HsqldbDAOFactory.getAlertaDAO().updateAlerta(this);
    }

    /**
     * Método para excluir o objeto no local de persistência
     * @return
     */
    public boolean delete(){
        return HsqldbDAOFactory.getAlertaDAO().deleteAlerta(this);
    }

    /**
     * Método para localizar um objeto Alerta no local de persitência
     * @param id
     * @return Alerta
     */
    public static Alerta find(int id){
        return HsqldbDAOFactory.getAlertaDAO().findAlerta(id);
    }

    /**
     * Método para obter uma lista de Alerta do local de persistência
     * @return
     */
    public static List<Alerta> findAll(){
        return HsqldbDAOFactory.getAlertaDAO().listAlerta();
    }

    /**
     * Método para obter a quantidade de Alerta do local de persistência
     * @return
     */
    public static int count(){
        return HsqldbDAOFactory.getAlertaDAO().getAlertaCount();
    }
    
    // </editor-fold>
    
}
