/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ai.bo.neural.treino;

import java.io.Serializable;

/**
 *
 * @author Edison
 */
public class EstatisticaTreinamento implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int epoca;
    private double erroQuadraticoMedio;
    private double taxaAprendizagem;
    private double variacaoTaxaAprendizagem;

    public int getEpoca() {
        return epoca;
    }

    public void setEpoca(int epoca) {
        this.epoca = epoca;
    }

    public double getErroQuadraticoMedio() {
        return erroQuadraticoMedio;
    }

    public void setErroQuadraticoMedio(double erroQuadraticoMedio) {
        this.erroQuadraticoMedio = erroQuadraticoMedio;
    }

    public double getTaxaAprendizagem() {
        return taxaAprendizagem;
    }

    public void setTaxaAprendizagem(double taxaAprendizagem) {
        this.taxaAprendizagem = taxaAprendizagem;
    }

    public double getVariacaoTaxaAprendizagem() {
        return variacaoTaxaAprendizagem;
    }

    public void setVariacaoTaxaAprendizagem(double variacaoTaxaAprendizagem) {
        this.variacaoTaxaAprendizagem = variacaoTaxaAprendizagem;
    }



    public EstatisticaTreinamento() {
    }

    
    
    
}
