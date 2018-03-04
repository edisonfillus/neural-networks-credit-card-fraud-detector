package org.ai.bo.ambiente;

import java.io.Serializable;
import javax.persistence.*;




@Entity
@Table(name="ITEM",schema="public")
/**
 *
 * @author Edison
 */
public class Item implements Serializable {

    public static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Atributos">


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Basic(optional=false)
    @Column(name="ID_ITEM")
    private int id;
    
    @Id
    @JoinColumn(name="ID_TRANSACAO",referencedColumnName="ID_TRANSACAO")
    @ManyToOne(optional=false,fetch=FetchType.LAZY)
    private Transacao transacao;

    @Basic(optional=false)
    @Column(name="NOME_ITEM")
    private String nome;

    @Basic(optional=false)
    @Column(name="QUANTIDADE")
    private int quantidade;

    @Basic(optional=false)
    @Column(name="VALOR")
    private Double valor;

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Propriedades">
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Transacao getTransacao() {
        return transacao;
    }

    public void setTransacao(Transacao transacao) {
        this.transacao = transacao;
    }

   
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Construtores">
   
    public Item() {
    }

   
    // </editor-fold>
    
}
