package org.ai.bo.neural.rede;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Edison
 */
public enum StatusRedeNeural {
    
    INICIADA(0,"INICIADA"),
    ARQUITETURA_CRIADA(1,"ARQUITETURA CRIADA"),
    PRONTA_TREINAMENTO(2,"PRONTA PARA TREINAMENTO"),
    TREINANDO(3,"TREINANDO"),
    TREINADA(4,"TREINADA");
   
    // <editor-fold defaultstate="collapsed" desc="Atributos">

    private int id;
    private String nome;

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Construtores">
    
    private StatusRedeNeural(int id, String nome) {
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
     * Método para recuperar um objeto StatusRedeNeural
     * @param id
     * @return StatusRedeNeural
     */
    public static StatusRedeNeural find(int id){
        StatusRedeNeural result = null;
        for (StatusRedeNeural statusRedeNeural : StatusRedeNeural.values()) {
            if (statusRedeNeural.getId() == id){
                result = statusRedeNeural;
            }
        }
        return result;
    }

    /**
     * Método para obter uma lista de StatusRedeNeural
     * @return
     */
    public static List<StatusRedeNeural> findAll(){
        List<StatusRedeNeural> statusRedes = new ArrayList<StatusRedeNeural>();
        for (StatusRedeNeural statusRedeNeural : StatusRedeNeural.values()) {
            statusRedes.add(statusRedeNeural);
        }
        return statusRedes;
    }

    /**
     * Método para obter a quantidade de StatusRedeNeural
     * @return
     */
    public static int count(){
        return StatusRedeNeural.values().length;
    }
    
    // </editor-fold>
    
}
