package org.ai.bo.ambiente;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Edison
 */
public enum StatusTransacao {

    CADASTRANDO(0,"CADASTRANDO"),
    PROCESSANDO(1,"PROCESSANDO"),
    PROCESSADA(2,"PROCESSADA"),
    ANALISANDO(3,"ANALISANDO"),
    ANALISE_OK(4,"ANALISE OK"),
    SUSPEITA(5,"SUSPEITA"),
    FALSO_POSITIVO(6,"FALSO POSITIVO"),
    FRAUDE(7,"FRAUDE"),
    FALSO_NEGATIVO(8,"FALSO NEGATIVO");
    
    
    // <editor-fold defaultstate="collapsed" desc="Atributos">

    private int id;
    private String nome;

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Construtores">
    
    private StatusTransacao(int id, String nome) {
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
     * Método para recuperar um objeto StatusTransacao
     * @param id
     * @return StatusTransacao
     */
    public static StatusTransacao find(int id){
        StatusTransacao result = null;
        for (StatusTransacao statusTransacao : StatusTransacao.values()) {
            if (statusTransacao.getId() == id){
                result = statusTransacao;
            }
        }
        return result;
    }

    /**
     * Método para obter uma lista de StatusTransacao
     * @return
     */
    public static List<StatusTransacao> findAll(){
        List<StatusTransacao> statusTransacoes = new ArrayList<StatusTransacao>();
        for (StatusTransacao statusTransacao : StatusTransacao.values()) {
            statusTransacoes.add(statusTransacao);
        }
        return statusTransacoes;
    }

    /**
     * Método para obter a quantidade de StatusTransacao
     * @return
     */
    public static int count(){
        return StatusTransacao.values().length;
    }
    
    // </editor-fold>
    
}
