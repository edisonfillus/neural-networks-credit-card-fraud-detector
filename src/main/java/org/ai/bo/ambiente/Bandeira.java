package org.ai.bo.ambiente;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Edison
 */
public enum Bandeira {
    
    MASTERCARD(0,"Mastercard"),
    VISA(1,"Visa"),
    AMERICAN_EXPRESS(2,"American Express");
    
    // <editor-fold defaultstate="collapsed" desc="Atributos">
    
    private int id;
    private String nome;
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Construtores">
    
    private Bandeira(int id,String nome) {
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
     * Método para recuperar um objeto Bandeira
     * @param id
     * @return Bandeira
     */
    public static Bandeira find(int id){
        Bandeira result = null;
        for (Bandeira bandeira : Bandeira.values()) {
            if (bandeira.getId() == id){
                result = bandeira;
            }
        }
        return result;
    }

    /**
     * Método para obter uma lista de Bandeira
     * @return
     */
    public static List<Bandeira> findAll(){
        List<Bandeira> bandeiras = new ArrayList<Bandeira>();
        for (Bandeira bandeira : Bandeira.values()) {
            bandeiras.add(bandeira);
        }
        return bandeiras;
    }

    /**
     * Método para obter a quantidade de Bandeira
     * @return
     */
    public static int count(){
        return Bandeira.values().length;
    }
    
    // </editor-fold>
}
