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
public class TaxaAprendizadoAdaptativa implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private double taxaAprendizado;
    private double erroQuadraticoMedio;

    public double getErroQuadraticoMedio() {
        return erroQuadraticoMedio;
    }

    public void setErroQuadraticoMedio(double erroQuadraticoMedio) {
        this.erroQuadraticoMedio = erroQuadraticoMedio;
    }

    public double getTaxaAprendizado() {
        return taxaAprendizado;
    }

    public void setTaxaAprendizado(double taxaAprendizado) {
        this.taxaAprendizado = taxaAprendizado;
    }
    
    public TaxaAprendizadoAdaptativa() {
    }

    public TaxaAprendizadoAdaptativa(double erroQuadraticoMedio, double taxaAprendizado) {
        this.taxaAprendizado = taxaAprendizado;
        this.erroQuadraticoMedio = erroQuadraticoMedio;
    }

    
    
}
