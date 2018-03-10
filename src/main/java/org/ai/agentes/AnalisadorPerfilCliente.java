package org.ai.agentes;

import java.util.ArrayList;
import java.util.List;

import org.ai.agentes.listeners.AnaliseListener;
import org.ai.bo.ambiente.Cliente;

/**
 * Classe Agente Analisador de Perfil de Cliente
 * @author Edison
 */
public class AnalisadorPerfilCliente implements Runnable {

    // <editor-fold defaultstate="collapsed" desc="Atributos">
    
    private List<Cliente> clientes;
    private int totalClientes;
    private int totalClientesAnalisados;
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Propriedades">
    
    public int getTotalClientes() {
        return totalClientes;
    }

    public int getTotalClientesAnalisados() {
        return totalClientesAnalisados;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Construtores">
    
    public AnalisadorPerfilCliente(List<Cliente> clientes) {
        this.clientes = clientes;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Eventos">
    
    private List<AnaliseListener> listenersAnalise = new ArrayList<AnaliseListener>();

    /**
     * Adiciona um Listener para receber eventos da analise de perfil
     * @param listener
     */
    public void addAnaliseListener(AnaliseListener listener) {
        listenersAnalise.add(listener);
    }

    /**
     * Remove um Listener para deixar de receber eventos da analise de perfil
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
        totalClientes = clientes.size();
        totalClientesAnalisados = 0;
        for (Cliente cliente : clientes) {
            cliente.analisarPerfil();
            totalClientesAnalisados++;
            notifyProgressoAnaliseAtualizado();
        }
        notifyAnaliseFinalizada();
    }
    
    // </editor-fold>

}
