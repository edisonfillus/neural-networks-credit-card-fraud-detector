package org.ai.agentes.listeners;

/**
 * Interface que deverá ser implementada por listener para receber eventos de treinamento
 *
 */
public interface TreinamentoRedeNeuralListener {
    
    /**
     * Método disparado quando o treinamento é iniciado
     */
    public void treinamentoIniciado();
    
    /**
     * Método disparado quando o progresso do treinamento é atualizado
     */
    public void progressoAtualizado();

    
    /**
     * Método disparado quando o treinamento é finalizado
     */
    public void treinamentoFinalizado();
}
