package org.ai.forms.redesneurais.rn3d.listeners;

/**
 * Interface que deverá ser implementada por listener para receber o indice do neurônio selecionado
 *
 */
public interface NeuronioSelecionadoListener {
    
    /**
     * Método disparado quando um neurônio é selecionado
     *
     * @param index indice selecionado. -1 se nenhum foi selecionado
     */
    public void neuronioSelecionado(int index);
}
