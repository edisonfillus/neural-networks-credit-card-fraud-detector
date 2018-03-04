/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ai.bo.analise;

public class CasoTeste {

    // <editor-fold defaultstate="collapsed" desc="Atributos">
    
    private int idCaso;
    private double desvioPadraoValorCompra;
    private double distanciaCidadeOrigem;
    private double quantidadeComprasDia;
    private double hora;
    private double[] saidaEsperada;
    private double[] saidaRede;

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Propriedades">
    public void setIdCaso(int idCaso) {
        this.idCaso = idCaso;
    }

    public int getIdCaso() {
        return idCaso;
    }

    public double getDesvioPadraoValorCompra() {
        return desvioPadraoValorCompra;
    }

    public void setDesvioPadraoValorCompra(double desvioPadraoValorCompra) {
        this.desvioPadraoValorCompra = desvioPadraoValorCompra;
    }

    public double getDistanciaCidadeOrigem() {
        return distanciaCidadeOrigem;
    }

    public void setDistanciaCidadeOrigem(double distanciaCidadeOrigem) {
        this.distanciaCidadeOrigem = distanciaCidadeOrigem;
    }

    public double getQuantidadeComprasDia() {
        return quantidadeComprasDia;
    }

    public void setQuantidadeComprasDia(double quantidadeComprasDia) {
        this.quantidadeComprasDia = quantidadeComprasDia;
    }

    public double getHora() {
        return hora;
    }

    public void setHora(double hora) {
        this.hora = hora;
    }

    public double[] getSaidaEsperada() {
        return saidaEsperada;
    }

    public void setSaidaEsperada(double[] saidaEsperada) {
        this.saidaEsperada = saidaEsperada;
    }

    public double[] getSaidaRede() {
        return saidaRede;
    }

    public void setSaidaRede(double[] saidaRede) {
        this.saidaRede = saidaRede;
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Contrutores">
    public CasoTeste() {
    }

    public CasoTeste(double pDesvio, double pDistancia, double pQuantidade, double pHora, double[] pEsperado) {
        desvioPadraoValorCompra = pDesvio;
        distanciaCidadeOrigem = pDistancia;
        quantidadeComprasDia = pQuantidade;
        saidaEsperada = pEsperado;
        hora = pHora;

    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Métodos">
    public double[] transformarVetorEntrada() {
        double[] retorno = new double[4];
        retorno[0] = getDesvioPadraoValorCompra();
        retorno[1] = getDistanciaCidadeOrigem();
        retorno[2] = getQuantidadeComprasDia();
        retorno[3] = getHora();
        return retorno;
    }
    // </editor-fold>
    
}
