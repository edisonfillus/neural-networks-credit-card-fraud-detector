package org.ai.agentes;

import java.util.ArrayList;
import java.util.List;

import org.ai.agentes.listeners.AnaliseListener;
import org.ai.bo.ambiente.Transacao;

/**
 * Classe Agente Analisador de Transações
 * @author Edison
 */
public class AnalisadorTransacao implements Runnable {

    // <editor-fold defaultstate="collapsed" desc="Atributos">
    
    private List<Transacao> transacoes;
    private int totalTransacoes;
    private int totalTransacoesAnalisadas;


    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Propriedades">

    public int getTotalTransacoes() {
        return totalTransacoes;
    }

    public int getTotalTransacoesAnalisadas() {
        return totalTransacoesAnalisadas;
    }
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construtores">
    
    public AnalisadorTransacao(List<Transacao> transacoes) {
        this.transacoes = transacoes;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Eventos">

    private List<AnaliseListener> listenersAnalise = new ArrayList<AnaliseListener>();

    /**
     * Adiciona um Listener para receber eventos da analise de transacao
     * @param listener
     */
    public void addAnaliseListener(AnaliseListener listener) {
        listenersAnalise.add(listener);
    }

    /**
     * Remove um Listener para deixar de receber eventos da analise de transacao
     * @param listener
     */
    public void removeAnaliseListener(AnaliseListener listener) {
        listenersAnalise.remove(listener);
    }

    /**
     * Notifica os Listeners com o progresso da análise
     * @param index
     */
    private void notifyProgressoAnaliseAtualizado() {
        for (AnaliseListener listener : listenersAnalise) {
             listener.progressoAtualizado();
        }
    }

     /**
     * Notifica os Listeners que a analise foi iniciada
     * @param index
     */
    private void notifyAnaliseInicializada() {
        for (AnaliseListener listener : listenersAnalise) {
             listener.analiseIniciada();
        }
    }

     /**
     * Notifica os Listeners que a analise foi finalizada
     * @param index
     */
    private void notifyAnaliseFinalizada() {
        for (AnaliseListener listener : listenersAnalise) {
             listener.analiseFinalizada();
        }
    }




    // </editor-fold>
        
    // <editor-fold defaultstate="collapsed" desc="Métodos">
    
    public void run() {
        notifyAnaliseInicializada();
        totalTransacoes = transacoes.size();
        totalTransacoesAnalisadas = 0;
        for (Transacao transacao : transacoes) {
            transacao.analisar();
            totalTransacoesAnalisadas++;
            notifyProgressoAnaliseAtualizado();
        }
        notifyAnaliseFinalizada();
    }
    
    // </editor-fold>

}
