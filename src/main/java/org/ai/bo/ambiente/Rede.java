package org.ai.bo.ambiente;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Edison
 */
public enum Rede {
    
    REDECARD(0,"Redecard"),
    VISANET(1,"Visanet"),
    AMEX(2,"Amex");

    // <editor-fold defaultstate="collapsed" desc="Atributos">

    private int id;
    private String nome;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construtores">

    private Rede(int id, String nome) {
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
     * Método para recuperar um objeto Rede
     * @param id
     * @return Rede
     */
    public static Rede find(int id){
        Rede result = null;
        for (Rede rede : Rede.values()) {
            if (rede.getId() == id){
                result = rede;
            }
        }
        return result;
    }

    /**
     * Método para obter uma lista de Rede
     * @return
     */
    public static List<Rede> findAll(){
        List<Rede> redes = new ArrayList<Rede>();
        for (Rede rede : Rede.values()) {
            redes.add(rede);
        }
        return redes;
    }

    /**
     * Método para obter a quantidade de Rede
     * @return
     */
    public static int count(){
        return Rede.values().length;
    }
    
    // </editor-fold>
    
}
