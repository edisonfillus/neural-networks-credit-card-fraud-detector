package org.ai.bo.neural.rede;

import java.io.Serializable;
import java.util.ArrayList;

import org.ai.bo.neural.treino.TipoFuncaoAtivacao;

/**
 * Classe Camada de Neurônios de uma Rede Neural
 * @author Edison
 */
public class Camada implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // <editor-fold defaultstate="collapsed" desc="Atributos">
    private int id;
    private ArrayList<Neuronio> neuronios;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Propriedades">

    public int getId () {
        return id;
    }
    
    public void setId (int val) {
        this.id = val;
    }
    
    public ArrayList<Neuronio> getNeuronios () {
        return neuronios;
    }

    public void setNeuronios (ArrayList<Neuronio> val) {
        this.neuronios = val;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construtores">

    /**
     * Construtor de Camada Neural
     */
    public Camada () {
        neuronios = new ArrayList<Neuronio>();
    }
    
    
    /**
     * Contrutor de Camada Neural com o numero de neurônios e com bias
     * @param numNeuronios Número de Neurônios
     * @param bias 
     */
    public Camada (int numNeuronios, boolean bias) {
        neuronios = new ArrayList<Neuronio>();
        for (int i = 0; i < numNeuronios; i++) {
            if (bias){
                neuronios.add(i,new Neuronio(Math.random())); //Com bias
            } else {
                neuronios.add(i,new Neuronio()); //Sem bias
            }
        }
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos">
    
    /**
     * Método para Aplicar o Aprendizado a todos os Neurônios da Camada
     */
    public void AplicarAprendizado () {
        for (Neuronio neuronio : neuronios) {
            neuronio.AplicarAprendizado();
        }
    }

    /**
     * Método para Pulsar todos os Neurônios da Camada
     * @param tipoFuncaoAtivacao 
     */
    public void Pulsar (TipoFuncaoAtivacao tipoFuncaoAtivacao) {
        for (Neuronio neuronio : neuronios) {
            neuronio.Pulsar(tipoFuncaoAtivacao);
        }
    }

    /**
     * Método para Reiniciar o Aprendizado de todos os Neurônios da Camada
     */
    public void ReiniciarAprendizado () {
        for (Neuronio neuronio : neuronios) {
            neuronio.ReiniciarAprendizado();
        }
    }

    // </editor-fold>
    
}

