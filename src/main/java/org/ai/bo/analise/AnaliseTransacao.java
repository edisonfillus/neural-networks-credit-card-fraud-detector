/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ai.bo.analise;

import java.util.Calendar;
import java.util.List;

import org.ai.bo.ambiente.Transacao;
import org.ai.bo.neural.rede.RedeNeural;
import org.ai.dao.factory.Db4oDAOFactory;

/**
 *
 * @author Edison
 */
public class AnaliseTransacao {

    // <editor-fold defaultstate="collapsed" desc="Atributos">

    private int id;
    private int pontuacao;
    private Calendar dhUltimaAnalise;
    private StatusAnalise status;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Propriedades">

    public Calendar getDhUltimaAnalise() {
        return dhUltimaAnalise;
    }

    public void setDhUltimaAnalise(Calendar dhUltimaAnalise) {
        this.dhUltimaAnalise = dhUltimaAnalise;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    public StatusAnalise getStatus() {
        return status;
    }

    public void setStatus(StatusAnalise status) {
        this.status = status;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Contrutores">

    public AnaliseTransacao() {
        id = 0;
        pontuacao = 0;
        status = StatusAnalise.NAO_ANALISADA;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Métodos">

    public void analisarTransacao(Transacao transacao) {
        status = StatusAnalise.ANALISANDO;
        analiseRedeNeuralGlobal(transacao);
        dhUltimaAnalise = Calendar.getInstance();
        status = StatusAnalise.ANALISADA;
    }

    private void analiseRedeNeuralGlobal(Transacao transacao) {
        RedeNeural rede = RedeNeural.find(RedeNeural.GLOBAL_CLIENTES);
        Caso caso = new CasoCliente(transacao);
        rede.PreparaCamadaEntradaParaPulsar(caso);
        rede.Pulsar();
        pontuacao = (int) (rede.getSaidas().get(0) * 1000);
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos Persistência">
    /**
     * Método para criar o objeto no local de persistência
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean create() {
        return Db4oDAOFactory.getAnaliseTransacaoDAO().createAnaliseTransacao(this);
    }

    /**
     * Método para atualizar o objeto no local de persistência
     * @return
     */
    public boolean update() {
        return Db4oDAOFactory.getAnaliseTransacaoDAO().updateAnaliseTransacao(this);
    }

    /**
     * Método para excluir o objeto no local de persistência
     * @return
     */
    public boolean delete() {
        return Db4oDAOFactory.getAnaliseTransacaoDAO().deleteAnaliseTransacao(this);
    }

    /**
     * Método para localizar um objeto AnaliseTransacao no local de persitência
     * @param id
     * @return AnaliseTransacao
     */
    public static AnaliseTransacao find(int id) {
        return Db4oDAOFactory.getAnaliseTransacaoDAO().findAnaliseTransacao(id);
    }

    /**
     * Método para obter uma lista de AnaliseTransacao do local de persistência
     * @return
     */
    public static List<AnaliseTransacao> findAll() {
        return Db4oDAOFactory.getAnaliseTransacaoDAO().listAnaliseTransacao();
    }

    /**
     * Método para obter a quantidade de AnaliseTransacao do local de persistência
     * @return
     */
    public static int count() {
        return Db4oDAOFactory.getAnaliseTransacaoDAO().getAnaliseTransacaoCount();
    }    // </editor-fold>
    
}
