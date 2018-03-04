package org.ai.bo.ambiente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;

import org.ai.bo.analise.AnaliseTransacao;
import org.ai.dao.factory.HsqldbDAOFactory;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.LazyLoader;

@Entity
@Table(name = "TRANSACAO", schema = "public")
/**
 *
 * @author Edison
 */
public class Transacao implements Serializable {

    public static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Atributos">
    private int id;
    private StatusTransacao status;
    private TerminalPOS terminalPOS;
    private Cartao cartao;
    private Resposta resposta;
    private Calendar dhTransacao;
    private Long tempoResposta;
    private String codAutorizacao;
    private int idOrdem;
    private double valorTotal;
    private Alerta alerta;
    private List<Item> itens;
    private boolean flagTreinamento;
    private boolean flagTeste;
    private AnaliseTransacao analiseTransacao;
    private int pontuacao;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Propriedades">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_TRANSACAO")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Enumerated(value = EnumType.ORDINAL)
    @Basic(optional = false)
    @Column(name = "COD_STATUS_TRANSACAO")
    public StatusTransacao getStatus() {
        return status;
    }

    public void setStatus(StatusTransacao status) {
        this.status = status;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TERMINAL_POS", referencedColumnName = "ID_TERMINAL_POS")
    public TerminalPOS getTerminalPOS() {
        return terminalPOS;
    }

    public void setTerminalPOS(TerminalPOS terminalPOS) {
        this.terminalPOS = terminalPOS;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "NUM_CARTAO", referencedColumnName = "NUM_CARTAO")
    public Cartao getCartao() {
        return cartao;
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "COD_RESPOSTA", referencedColumnName = "COD_RESPOSTA", nullable = true)
    public Resposta getResposta() {
        return resposta;
    }

    public void setResposta(Resposta resposta) {
        this.resposta = resposta;
    }

    @Basic(optional = true)
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "DH_TRANSACAO", nullable = true)
    public Calendar getDhTransacao() {
        return dhTransacao;
    }

    public void setDhTransacao(Calendar dhTransacao) {
        this.dhTransacao = dhTransacao;
    }

    @Basic(optional = true)
    @Column(name = "TEMPO_RESPOSTA", nullable = true)
    public Long getTempoResposta() {
        return tempoResposta;
    }

    public void setTempoResposta(Long tempoResposta) {
        this.tempoResposta = tempoResposta;
    }

    @Basic(optional = true)
    @Column(name = "COD_AUTORIZACAO", nullable = true)
    public String getCodAutorizacao() {
        return codAutorizacao;
    }

    public void setCodAutorizacao(String codAutorizacao) {
        this.codAutorizacao = codAutorizacao;
    }

    @OneToOne(mappedBy = "transacao", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public Alerta getAlerta() {
        return alerta;
    }

    public void setAlerta(Alerta alerta) {
        this.alerta = alerta;
    }

    @Basic(optional = false)
    @Column(name = "ID_ORDEM", nullable = false)
    public int getIdOrdem() {
        return idOrdem;
    }

    public void setIdOrdem(int idOrdem) {
        this.idOrdem = idOrdem;
    }

    @Basic(optional = false)
    @Column(name = "VALOR_TOTAL", nullable = false)
    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    @OneToMany(mappedBy = "transacao", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    public List<Item> getItens() {
        return itens;
    }

    public void setItens(List<Item> itens) {
        this.itens = itens;
    }

    @Basic(optional = false)
    @Column(name = "FLAG_TREINAMENTO")
    public boolean isFlagTreinamento() {
        return flagTreinamento;
    }

    public void setFlagTreinamento(boolean flagTreinamento) {
        this.flagTreinamento = flagTreinamento;
    }

    @Basic(optional = false)
    @Column(name = "FLAG_TESTE")
    public boolean isFlagTeste() {
        return flagTeste;
    }

    public void setFlagTeste(boolean flagTeste) {
        this.flagTeste = flagTeste;
    }

    @Basic(optional = false)
    @Column(name = "PONTUACAO")
    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    @Transient
    public AnaliseTransacao getAnaliseTransacao() {
        if (analiseTransacao == null) {
            analiseTransacao = loadAnaliseTransacaoLazy();
        }
        return analiseTransacao;
    }

    public void setAnaliseTransacao(AnaliseTransacao analiseTransacao) {
        this.analiseTransacao = analiseTransacao;
    }





    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construtores">
    public Transacao() {
        itens = new ArrayList<Item>();
        status = StatusTransacao.CADASTRANDO;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Métodos Processamento">
    public void processar() {
        status = StatusTransacao.PROCESSANDO;
        Calendar dhInicio = Calendar.getInstance();
        if (cartao.isBloqueado()) {
            resposta = Resposta.find(Resposta.CARTAO_BLOQUEADO);
        } else if (cartao.getDataExpiracao().before(Calendar.getInstance())) {
            resposta = Resposta.find(Resposta.CARTAO_EXPIRADO);
        } else {
            resposta = Resposta.find(Resposta.APROVADA);
            codAutorizacao = UUID.randomUUID().toString().substring(0, 7).toUpperCase();
        }
        tempoResposta = Calendar.getInstance().getTimeInMillis() - dhInicio.getTimeInMillis();
        //Se a data não foi predefinida, utiliza a data de inicio da transação
        if (dhTransacao == null) {
            dhTransacao = dhInicio;
        }
        status = StatusTransacao.PROCESSADA;
    }

    @Override
    public String toString() {
        return Integer.toString(id);
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Métodos Análise">
    /**
     * Método para analisar a transacao
     */
    public void analisar() {
        status = StatusTransacao.ANALISANDO;
        analiseTransacao.analisarTransacao(this);
        this.pontuacao = analiseTransacao.getPontuacao();
        analiseTransacao.update();
        //Se alerta diferente de fraude, exclui
        if (alerta != null && alerta.getStatus() != StatusAlerta.FRAUDE_CONFIRMADA) {
            alerta.delete();
            alerta = null;
        } else {
            status = StatusTransacao.FRAUDE;
        }

        if (alerta == null) { //Se era fraude, não altera
            if (analiseTransacao.getPontuacao() > 500) {//Se pontuação alta e não era fraude, marca como suspeita
                status = StatusTransacao.SUSPEITA;
                alerta = new Alerta();
                alerta.setTransacao(this);
                alerta.setDhGeracao(Calendar.getInstance());
                alerta.setStatus(StatusAlerta.ATIVO);
            } else {//Se pontuação baixa e não era fraude, marca como OK
                status = StatusTransacao.ANALISE_OK;
            }
        }

        this.update();
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Métodos Persistência">
    /**
     * Método para criar o objeto no local de persistência
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean create() {
        if (!itens.isEmpty()) {
            for (Item item : itens) {
                item.setTransacao(this);
            }
        }
        return HsqldbDAOFactory.getTransacaoDAO().createTransacao(this);
    }

    /**
     * Método para atualizar o objeto no local de persistência
     * @return
     */
    public boolean update() {
        return HsqldbDAOFactory.getTransacaoDAO().updateTransacao(this);
    }

    /**
     * Método para excluir o objeto no local de persistência
     * @return
     */
    public boolean delete() {
        return HsqldbDAOFactory.getTransacaoDAO().deleteTransacao(this);
    }

    /**
     * Método para localizar um objeto Transacao no local de persitência
     * @param id
     * @return Transacao
     */
    public static Transacao find(int id) {
        return HsqldbDAOFactory.getTransacaoDAO().findTransacao(id);
    }

    /**
     * Método para obter uma lista de Transacao do local de persistência
     * @return
     */
    public static List<Transacao> findAll() {
        return HsqldbDAOFactory.getTransacaoDAO().listTransacao();
    }

    /**
     * Método para obter uma lista de Transacao do local de persistência somente marcadas para treinamento
     * @return
     */
    public static List<Transacao> findOnlyTreimento() {
        return HsqldbDAOFactory.getTransacaoDAO().listTransacaoOnlyTreinamento();
    }

    /**
     * Método para obter a quantidade de Transacao do local de persistência
     * @return
     */
    public static int count() {
        return HsqldbDAOFactory.getTransacaoDAO().getTransacaoCount();
    }

    /**
     * Método para obter a quantidade de Transacao do local de persistência
     * @return
     */
    public static int countOnlyTreinamento() {
        return HsqldbDAOFactory.getTransacaoDAO().getCountTransacaoSelecionadaTreinamento();
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Métodos Lazy Load">
    private AnaliseTransacao loadAnaliseTransacaoLazy() {
        return (AnaliseTransacao) Enhancer.create(AnaliseTransacao.class, new LazyLoader() {

            public Object loadObject() throws Exception {
                AnaliseTransacao analise = AnaliseTransacao.find(id);
                if (analise.getId() == 0 || analise == null) {
                    analise = new AnaliseTransacao();
                    analise.setId(id);
                }
                return analise;
            }
        });
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Métodos Sincronização">
    @PostLoad
    private void postLoad() {
        this.analiseTransacao = loadAnaliseTransacaoLazy();
    }

    @PostRemove
    private void postRemove() {
        analiseTransacao.delete();
    }

    @PostUpdate
    private void postUpdate() {
        analiseTransacao.update();
    }
    // </editor-fold>
}

