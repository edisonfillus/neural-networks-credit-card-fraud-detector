package org.ai.agentes.listeners;

/**
 * Interface que deverá ser implementada por listener para receber eventos de analise de perfil de cliente
 *
 */
public interface AnaliseListener {
    
    /**
     * Método disparado quando a analise é iniciada
     */
    public void analiseIniciada();
    
    /**
     * Método disparado quando o progresso da analise é atualizada
     */
    public void progressoAtualizado();
    
    /**
     * Método disparado quando a analise é finalizada
     */
    public void analiseFinalizada();
}
