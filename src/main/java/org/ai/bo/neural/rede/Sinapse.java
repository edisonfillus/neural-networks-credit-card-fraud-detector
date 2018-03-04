package org.ai.bo.neural.rede;

import java.io.Serializable;

/**
 * Classe Sinapse de um Neurônio
 * @author Edison
 */
public class Sinapse implements Serializable{

    private static final long serialVersionUID = 1L;
    
    // <editor-fold defaultstate="collapsed" desc="Atributos">

    private double delta;
    private double peso;
    private double ultimoDelta;
    private Neuronio neuronioEntrada;
        
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Propriedades">
    
    public double getDelta () {
        return delta;
    }

    public void setDelta (double val) {
        this.delta = val;
    }

    public double getPeso () {
        return peso;
    }

    public void setPeso (double val) {
        this.peso = val;
    }

    public double getUltimoDelta () {
        return ultimoDelta;
    }

    public void setUltimoDelta (double val) {
        this.ultimoDelta = val;
    }
    
    public Neuronio getNeuronioEntrada () {
        return neuronioEntrada;
    }

    public void setNeuronioEntrada (Neuronio val) {
        this.neuronioEntrada = val;
    }

    // </editor-fold> 
    
    // <editor-fold defaultstate="collapsed" desc="Contrutores">
    public Sinapse (double pPeso, Neuronio pNeuronioEntrada) {
        peso = pPeso;
        ultimoDelta = 0;
        delta = 0;
        neuronioEntrada = pNeuronioEntrada;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos">

    /**
     * Aplica a Troca de Peso na Sinapse
     */
    public void AplicarTrocaPeso () {
        ultimoDelta = delta;
        peso += delta;
    }

    /**
     * Reinicia a Troca de Peso
     */
    public void ReiniciarTrocaPeso () {
        delta = 0;
    }

    // </editor-fold>
    
}

