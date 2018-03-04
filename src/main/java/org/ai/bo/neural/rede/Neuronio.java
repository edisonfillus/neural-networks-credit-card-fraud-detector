package org.ai.bo.neural.rede;

import java.io.Serializable;
import java.util.ArrayList;

import org.ai.bo.neural.*;
import org.ai.bo.neural.treino.TipoFuncaoAtivacao;

/**
 * Classe Neurônio
 * @author Edison
 */
public class Neuronio implements Serializable{

    private static final long serialVersionUID = 1L;
    
    // <editor-fold defaultstate="collapsed" desc="Atributos">
   
    private int id;
    private Sinapse bias;
    private double erroLocal;
    private double ultimoErroLocal;
    private double sinalErro;
    private double saida;
    private double erroQuadratico;
    
    private ArrayList<Sinapse> sinapses;

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Propriedades">
    public Sinapse getBias() {
        return bias;
    }

    public void setBias(Sinapse val) {
        this.bias = val;
    }

    public double getErroLocal() {
        return erroLocal;
    }

    public void setErroLocal(double val) {
        this.erroLocal = val;
    }

    public int getId() {
        return id;
    }

    public void setId(int val) {
        this.id = val;
    }

    public double getSaida() {
        return saida;
    }

    public void setSaida(double val) {
        this.saida = val;
    }

    public ArrayList<Sinapse> getSinapses() {
        return sinapses;
    }

    public void setSinapses(ArrayList<Sinapse> val) {
        this.sinapses = val;
    }

    public double getUltimoErroLocal() {
        return ultimoErroLocal;
    }

    public void setUltimoErroLocal(double val) {
        this.ultimoErroLocal = val;
    }

    public double getSinalErro() {
        return sinalErro;
    }

    public void setSinalErro(double sinalErro) {
        this.sinalErro = sinalErro;
    }

    public double getErroQuadratico() {
        return erroQuadratico;
    }

    public void setErroQuadratico(double erroQuadratico) {
        this.erroQuadratico = erroQuadratico;
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Construtores">
    /**
     * Contrutor de Neurônio com Bias
     * @param pBias Valor Inicial do Bias
     */
    public Neuronio(double pBias) {
        bias = new Sinapse(pBias, null);
        erroLocal = 0;
        sinapses = new ArrayList<Sinapse>();
    }

    /**
     * Construtor de Neurônio sem Bias
     */
    public Neuronio() {
        bias = null;
        erroLocal = 0;
        sinapses = new ArrayList<Sinapse>();
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos">
    /**
     * Metodo para pulsar um Neurônio. <br>
     * Ele multiplica todas as saidas dos neuronios de entrada pelo peso de suas ligações(sinapses), junto ao bias(se houver), formando o somador.
     * Após isto, o resultado do somador é passado pela função de ativação para definir a saida do neurônio
     * @param tipoFuncaoAtivacao
     */
    public void Pulsar(TipoFuncaoAtivacao tipoFuncaoAtivacao) {
        this.saida = 0;
        for (Sinapse sinapse : sinapses) { //Somador
            saida += sinapse.getNeuronioEntrada().getSaida() * sinapse.getPeso(); //Valor de Entrada * Peso
        }
        if (bias != null) {
            saida += bias.getPeso(); //Bias
        }
        saida = new FuncaoAtivacao(tipoFuncaoAtivacao).Executar(saida); //Função de Ativação   
    }

    /**
     * Metodo para Aplicar o Aprendizado
     */
    public void AplicarAprendizado() {
        for (Sinapse sinapse : sinapses) {
            sinapse.AplicarTrocaPeso();
        }
        if (bias != null) {
            bias.AplicarTrocaPeso();
        }
    }

    /**
     * Método para Zerar as Informações de Aprendizado
     */
    public void ReiniciarAprendizado() {
        for (Sinapse sinapse : sinapses) {
            sinapse.ReiniciarTrocaPeso();
        }
        if (bias != null) {
            bias.ReiniciarTrocaPeso();
        }
    }

    /**
     * Método para obter a sinapse na qual o neurônio de entrada está conectado
     * @param pNeuronioEntrada
     * @return <code>Sinapse</code> na qual o neurônio de entrada está conectado
     * @see Sinapse
     */
    public Sinapse getSinapseEntrada(Neuronio pNeuronioEntrada) {
        for (Sinapse sinapse : sinapses) {
            if (sinapse.getNeuronioEntrada().equals(pNeuronioEntrada)) {
                return sinapse;
            }
        }
        return null;
    }
    // </editor-fold>
}

