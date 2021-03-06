package org.ai.bo.neural.rede;

import org.ai.bo.neural.*;
import org.ai.bo.neural.treino.TipoFuncaoAtivacao;

/**
 * Classe Função de Ativação
 * @author Edison
 */
public class FuncaoAtivacao {

    // <editor-fold defaultstate="collapsed" desc="Atributos">

    private double angulo;
    private TipoFuncaoAtivacao tipoFuncao;

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Propriedades">
    public double getAngulo() {
        return angulo;
    }

    public void setAngulo(double val) {
        this.angulo = val;
    }

    public TipoFuncaoAtivacao getTipoFuncao() {
        return tipoFuncao;
    }

    public void setTipoFuncao(TipoFuncaoAtivacao val) {
        this.tipoFuncao = val;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construtores">
    /**
     * Construtor de Função de Ativação. <br>
     * Angulo fixo em 1
     * @param pTipoFuncao
     */
    public FuncaoAtivacao(TipoFuncaoAtivacao pTipoFuncao) {
        tipoFuncao = pTipoFuncao;
        angulo = 1.0;
    }

    /**
     * Construtor de Função de Ativação
     * @param pTipoFuncao
     * @param pAngulo
     */
    public FuncaoAtivacao(TipoFuncaoAtivacao pTipoFuncao, double pAngulo) {
        tipoFuncao = pTipoFuncao;
        angulo = pAngulo;
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos">
    /**
     * Função para Executar o Cálculo da Função de Ativação
     * @param pValor
     * @return Resultado da Função de Ativação
     */
    public double Executar(double pValor) {
        double result = 0.0;
        switch (tipoFuncao) {
            case SigmoideLogistica:
                result = FuncaoAtivacaoLogistica(pValor);
                break;
            case SigmoideTangenteHiperbolica:
                result = FuncaoAtivacaoTangenteHiperbolica(pValor);
                break;
            case Limiar:
                result = FuncaoAtivacaoLimiar(pValor);
                break;
            case LinearPorPartes:
                result = FuncaoAtivacaoLinearPorPartes(pValor);
                break;
            case SigmoideDerivada:
                result = FuncaoAtivacaoSigmoideDerivada(pValor);
                break;
        }
        return result;
    }

    // </editor-fold>           

    // <editor-fold defaultstate="collapsed" desc="Funções de Ativação">
    /**
     * Função de Ativação Sigmóide
     * @param valor
     * @return Intervalo entre 0 e 1
     */
    private double FuncaoAtivacaoLogistica(double valor) {
        return 1 / (1 + Math.exp(-valor * angulo));
    }

    /**
     * Função de ativação Tangente Hiperbólica
     * @param valor
     * @return Intervalor entre -1 e 1
     */
    private double FuncaoAtivacaoTangenteHiperbolica(double valor) {
        return Math.tanh(valor * angulo);
    }

    /**
     * Função de ativação Limiar
     * @param valor
     * @return 1 se o valor for positivo <br>
     * @return 0 se o valor for negativo
     */
    private double FuncaoAtivacaoLimiar(double valor) {
        return (valor > 0.0) ? 1.0 : 0.0;
    }

    /**
     * Função de ativação Linear por partes
     * @param valor
     * @return 1 Se o valor acima de 0.5 <br>
     * @return (<code>valor</code> + 0.5) Se o valor estiver entre -0.5 e 0.5 <br>
     * @return 0 Se o valor abaixo de -0.5
     */
    private double FuncaoAtivacaoLinearPorPartes(double valor) {
        double result;
        if (valor > 0.5) {
            result = 1.0;
        } else if (valor < -0.5) {
            result = 0.0;
        } else {
            result = valor + 0.5;
        }
        return result;
    }

    /**
     * Função de Ativação Sigmoide Derivada
     * @param valor
     * @return
     */
    private double FuncaoAtivacaoSigmoideDerivada(double valor) {
        return valor * (1.0F - valor);
    }
    // </editor-fold>
    
}