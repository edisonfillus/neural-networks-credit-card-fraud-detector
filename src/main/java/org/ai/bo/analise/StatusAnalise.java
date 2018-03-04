/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ai.bo.analise;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Edison
 */
public enum StatusAnalise {
    
    NAO_ANALISADA(0,"Não Analisada"),
    ANALISANDO(1,"Analisando"),
    ANALISADA(2,"Analisada");
    
    
    // <editor-fold defaultstate="collapsed" desc="Atributos">

    private int id;
    private String nome;

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Construtores">
    
    private StatusAnalise(int id, String nome) {
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
     * Método para recuperar um objeto StatusAnalise
     * @param id
     * @return StatusAnalise
     */
    public static StatusAnalise find(int id){
        StatusAnalise result = null;
        for (StatusAnalise statusAnalise : StatusAnalise.values()) {
            if (statusAnalise.getId() == id){
                result = statusAnalise;
            }
        }
        return result;
    }

    /**
     * Método para obter uma lista de StatusAnalise
     * @return
     */
    public static List<StatusAnalise> findAll(){
        List<StatusAnalise> statusAnalises = new ArrayList<StatusAnalise>();
        for (StatusAnalise statusAnalise : StatusAnalise.values()) {
            statusAnalises.add(statusAnalise);
        }
        return statusAnalises;
    }

    /**
     * Método para obter a quantidade de StatusAnalise
     * @return
     */
    public static int count(){
        return StatusAnalise.values().length;
    }
    
    // </editor-fold>
}
