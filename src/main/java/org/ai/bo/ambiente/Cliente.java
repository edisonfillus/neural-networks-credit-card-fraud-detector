package org.ai.bo.ambiente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.ai.bo.analise.AnalisePerfilCliente;
import org.ai.bo.analise.CasoCliente;
import org.ai.bo.analise.TipoCaso;
import org.ai.bo.neural.rede.RedeNeural;
import org.ai.dao.factory.HsqldbDAOFactory;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.LazyLoader;

@Entity
@Table(name = "CLIENTE", schema = "public")
/**
 * Classe Cliente
 * @author Edison Klafke Fillus
 * @author Jefferson de Souza Campos
 */
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;    
    
    // <editor-fold defaultstate="collapsed" desc="Atributos">

    private int id;
    private String nome;
    private int idade;
    private String cpf;
    private Double renda;
    private Cidade cidade;
    private List<Cartao> cartoes;
    private RedeNeural redeNeural;
    private AnalisePerfilCliente analisePerfil;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Propriedades">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CLIENTE")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic(optional = false)
    @Column(name = "NOME_CLIENTE")
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Basic(optional = false)
    @Column(name = "IDADE")
    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    @Basic(optional = false)
    @Column(name = "RENDA")
    public Double getRenda() {
        return renda;
    }

    public void setRenda(Double renda) {
        this.renda = renda;
    }

    @JoinColumn(name = "ID_CIDADE", referencedColumnName = "ID_CIDADE")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<Cartao> getCartoes() {
        return cartoes;
    }

    public void setCartoes(List<Cartao> cartoes) {
        this.cartoes = cartoes;
    }

    @Basic(optional = true)
    @Column(name = "CPF")
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Transient
    public RedeNeural getRedeNeural() {
        return redeNeural;
    }

    public void setRedeNeural(RedeNeural redeNeural) {
        this.redeNeural = redeNeural;
    }

    @Transient
    public AnalisePerfilCliente getAnalisePerfil() {
        return analisePerfil;
    }

    public void setAnalisePerfil(AnalisePerfilCliente analisePerfil) {
        this.analisePerfil = analisePerfil;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Contrutores">
    public Cliente() {
        this.cartoes = new ArrayList<Cartao>();
    }

    public Cliente(int id, String nome, int idade, double renda, Cidade cidade) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.renda = renda;
        this.cidade = cidade;
        this.cartoes = new ArrayList<Cartao>();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos Análise">

    /**
     * Método para analisar o perfil do cliente e carregar os casos de treinamento
     */
    public void analisarPerfil() {
        analisePerfil.analisarPerfil(this);
        analisePerfil.update();
        redeNeural.getDadosTreinamento().setListCasosTreinamento(analisePerfil.recuperarCasosTreinamento(this,TipoCaso.TODOS));
        redeNeural.getDadosTreinamento().setLabels(CasoCliente.getLabels());
        redeNeural.update();
    }
    

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos Persistência">
    /**
     * Método para criar o objeto no local de persistência
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean create() {
        if (!cartoes.isEmpty()) {
            for (Cartao cartao : cartoes) {
                cartao.setCliente(this);
            }
        }
        return HsqldbDAOFactory.getClienteDAO().createCliente(this);
    }

    /**
     * Método para atualizar o objeto no local de persistência
     * @return
     */
    public boolean update() {
        if (!cartoes.isEmpty()) {
            for (Cartao cartao : cartoes) {
                cartao.setCliente(this);
            }
        }
        return HsqldbDAOFactory.getClienteDAO().updateCliente(this);
    }

    /**
     * Método para excluir o objeto no local de persistência
     * @return
     */
    public boolean delete() {
        return HsqldbDAOFactory.getClienteDAO().deleteCliente(this);
    }

    /**
     * Método para localizar um objeto Cliente no local de persistência
     * @param id
     * @return Cliente
     */
    public static Cliente find(final int id) {
        return HsqldbDAOFactory.getClienteDAO().findCliente(id);
    }
    
    /**
     * Método para obter uma lista de Cliente do local de persistência
     * @return
     */
    public static List<Cliente> findAll() {
        return HsqldbDAOFactory.getClienteDAO().listCliente();
    }

    /**
     * Método para obter a quantidade de Cliente do local de persistência
     * @return
     */
    public static int count() {
        return HsqldbDAOFactory.getClienteDAO().getClienteCount();
    }    
    
    
    
    
    
    // </editor-fold>
         
    // <editor-fold defaultstate="collapsed" desc="Métodos Sincronização">
    
    @PostLoad
    private void postLoad(){
        this.redeNeural = loadRedeNeuralLazy();
        this.analisePerfil = loadAnalisePerfilLazy();
    }
    
    @PostRemove
    private void postRemove(){
        redeNeural.delete();
        analisePerfil.delete();
    }
    
    @PostUpdate
    private void postUpdate(){
        redeNeural.update();
        analisePerfil.update();
    }
    
    // </editor-fold>
         
    // <editor-fold defaultstate="collapsed" desc="Métodos Lazy Load">
    
    private RedeNeural loadRedeNeuralLazy(){
        return (RedeNeural) Enhancer.create(RedeNeural.class, new LazyLoader() {
            public Object loadObject() throws Exception {
                RedeNeural rede = RedeNeural.find(Integer.parseInt(id + "0"));
                if (rede.getId() == 0 || rede == null) {
                    rede = new RedeNeural();
                    rede.setId(Integer.parseInt(id + "0"));
                    rede.setNome("Cliente: " + nome);
                }
                return rede;
            }
        });
    }
    
    
    private AnalisePerfilCliente loadAnalisePerfilLazy(){
        return (AnalisePerfilCliente) Enhancer.create(AnalisePerfilCliente.class, new LazyLoader() {
            public Object loadObject() throws Exception {
                AnalisePerfilCliente perfil = AnalisePerfilCliente.find(id);
                if (perfil.getId() == 0 || perfil == null) {
                    perfil = new AnalisePerfilCliente();
                    perfil.setId(id);
                }
                return perfil;
            }
        });
    }
    
    // </editor-fold>
    
    @Override
    public String toString(){
        return nome;
    }
    
}
