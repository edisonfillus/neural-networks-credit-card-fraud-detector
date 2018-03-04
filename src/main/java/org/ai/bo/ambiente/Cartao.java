/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ai.bo.ambiente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.ai.dao.factory.HsqldbDAOFactory;



@Entity
@Table(name="CARTAO",schema="public")
/**
 *
 * @author Edison
 */
public class Cartao implements Serializable {
    
    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Atributos">
    
    private String numCartao;
    private Banco banco;
    private Bandeira bandeira;
    private Cliente cliente;
    private int codSeguranca;
    private Double limite;
    private Double saldo;
    private Calendar dataAtivacao;
    private Calendar dataExpiracao;
    private boolean bloqueado;
    private List<Transacao> transacoes;
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Propriedades">
    
    @Id
    @Basic(optional = false)
    @Column(name = "NUM_CARTAO")
    public String getNumCartao() {
        return numCartao;
    }

    public void setNumCartao(String numCartao) {
        this.numCartao = numCartao;
    }

    @JoinColumn(name = "COD_BANCO", referencedColumnName = "COD_BANCO")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    @Enumerated(value = EnumType.ORDINAL)
    @Basic(optional = false)
    @Column(name = "ID_BANDEIRA")
    public Bandeira getBandeira() {
        return bandeira;
    }

    public void setBandeira(Bandeira bandeira) {
        this.bandeira = bandeira;
    }

    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID_CLIENTE")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Basic(optional = true)
    @Column(name = "COD_SEGURANCA")
    public int getCodSeguranca() {
        return codSeguranca;
    }

    public void setCodSeguranca(int codSeguranca) {
        this.codSeguranca = codSeguranca;
    }

    @Basic(optional = false)
    @Column(name = "LIMITE_CARTAO")
    public Double getLimite() {
        return limite;
    }

    public void setLimite(Double limite) {
        this.limite = limite;
    }

    @Basic(optional = false)
    @Column(name = "FLAG_BLOQUEADO")
    public boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }


    @Basic(optional=false)
    @Temporal(value = TemporalType.DATE)
    @Column(name = "DATA_ATIVACAO")
    public Calendar getDataAtivacao() {
        return dataAtivacao;
    }

    public void setDataAtivacao(Calendar dataAtivacao) {
        this.dataAtivacao = dataAtivacao;
    }



    @Basic(optional = false)
    @Temporal(value = TemporalType.DATE)
    @Column(name = "DATA_EXPIRACAO")
    public Calendar getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(Calendar dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    
    @OneToMany(mappedBy = "cartao", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderBy(value="dhTransacao DESC")
    public List<Transacao> getTransacoes() {
        return transacoes;
    }

    public void setTransacoes(List<Transacao> transacoes) {
        this.transacoes = transacoes;
    }

    @Transient
    public Double getSaldo() {
        if (saldo == null){
            saldo = limite;
            for (Transacao transacao : transacoes) {
                Calendar dtTransacao = transacao.getDhTransacao();
                Calendar dtAtual = Calendar.getInstance();
                if(dtTransacao.get(Calendar.YEAR) == dtAtual.get(Calendar.YEAR) && dtTransacao.get(Calendar.MONTH) == dtAtual.get(Calendar.MONTH)){
                    saldo -= transacao.getValorTotal();
                }
            }
        }
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }


    

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Construtores">
    
    public Cartao() {
        this.transacoes = new ArrayList<Transacao>();
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos Persistência">
    
     /**
     * Método para criar o objeto no local de persistência
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean create(){
        return HsqldbDAOFactory.getCartaoDAO().createCartao(this);
    }

    /**
     * Método para atualizar o objeto no local de persistência
     * @return
     */
    public boolean update(){
        return HsqldbDAOFactory.getCartaoDAO().updateCartao(this);
    }

    /**
     * Método para excluir o objeto no local de persistência
     * @return
     */
    public boolean delete(){
        return HsqldbDAOFactory.getCartaoDAO().deleteCartao(this);
    }

    /**
     * Método para localizar um objeto Cartao no local de persitência
     * @param num
     * @return Cartao
     */
    public static Cartao find(String num){
        return HsqldbDAOFactory.getCartaoDAO().findCartao(num);
    }

    /**
     * Método para obter uma lista de Cartao do local de persistência
     * @return
     */
    public static List<Cartao> findAll(){
        return HsqldbDAOFactory.getCartaoDAO().listCartao();
    }

    /**
     * Método para obter a quantidade de Cartao do local de persistência
     * @return
     */
    public static int count(){
        return HsqldbDAOFactory.getCartaoDAO().getCartaoCount();
    }
    
    // </editor-fold>


    
    @Override
    public String toString(){
        return "Cartao[numCartao="+ numCartao
                + ",cliente=" + cliente.getNome()
                + "]";
    }
    
}
