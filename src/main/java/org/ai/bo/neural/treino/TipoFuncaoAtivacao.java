package org.ai.bo.neural.treino;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Edison
 */
public enum TipoFuncaoAtivacao {
    
    SigmoideLogistica(0,"Logística"),
    SigmoideTangenteHiperbolica(1,"Tangente Hiperbólica"),
    SigmoideDerivada(2,"Sigmóide Derivada"),
    Limiar(3,"Limiar"),
    LinearPorPartes(4,"Linear Por Partes");
        
    // <editor-fold defaultstate="collapsed" desc="Atributos">
    
    private int id;
    private String nome;
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Construtores">
    
    private TipoFuncaoAtivacao(int id,String nome) {
        this.id = id;
        this.nome = nome;
    }
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Propriedades">
    
    public int getId() {
        return id;
    }
    
    public String getNome(){
        return nome;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos">
    
    /**
     * Método para recuperar um objeto TipoFuncaoAtivacao
     * @param id
     * @return TipoFuncaoAtivacao
     */
    public static TipoFuncaoAtivacao find(int id){
        TipoFuncaoAtivacao result = null;
        for (TipoFuncaoAtivacao tipoFuncaoAtivacao : TipoFuncaoAtivacao.values()) {
            if (tipoFuncaoAtivacao.getId() == id){
                result = tipoFuncaoAtivacao;
            }
        }
        return result;
    }

    /**
     * Método para obter uma lista de TipoFuncaoAtivacao
     * @return
     */
    public static List<TipoFuncaoAtivacao> findAll(){
        List<TipoFuncaoAtivacao> tipoFuncaoAtivacoes = new ArrayList<TipoFuncaoAtivacao>();
        for (TipoFuncaoAtivacao tipoFuncaoAtivacao : TipoFuncaoAtivacao.values()) {
            tipoFuncaoAtivacoes.add(tipoFuncaoAtivacao);
        }
        return tipoFuncaoAtivacoes;
    }

    /**
     * Método para obter a quantidade de TipoFuncaoAtivacao
     * @return
     */
    public static int count(){
        return TipoFuncaoAtivacao.values().length;
    }
    
    // </editor-fold>
}
