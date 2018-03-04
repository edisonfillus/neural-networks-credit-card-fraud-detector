package org.ai.bo.ambiente;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Edison
 */
public enum StatusAlerta {

    ATIVO(0,"Ativo"),
    FALSO_POSITIVO(1,"Falso Positivo"),
    FRAUDE_CONFIRMADA(2,"Fraude Confirmada"),
    CANCELADO(3,"Cancelado");

    // <editor-fold defaultstate="collapsed" desc="Atributos">

    private int id;
    private String nome;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Contrutor">

    private StatusAlerta(int id,String nome) {
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
     * Método para recuperar um objeto StatusAlerta
     * @param id
     * @return StatusAlerta
     */
    public static StatusAlerta find(int id){
        StatusAlerta result = null;
        for (StatusAlerta statusAlerta : StatusAlerta.values()) {
            if (statusAlerta.getId() == id){
                result = statusAlerta;
            }
        }
        return result;
    }

    /**
     * Método para obter uma lista de StatusAlerta
     * @return
     */
    public static List<StatusAlerta> findAll(){
        List<StatusAlerta> listStatusAlerta = new ArrayList<StatusAlerta>();
        for (StatusAlerta statusAlerta : StatusAlerta.values()) {
            listStatusAlerta.add(statusAlerta);
        }
        return listStatusAlerta;
    }

    /**
     * Método para obter a quantidade de StatusAlerta
     * @return
     */
    public static int count(){
        return StatusAlerta.values().length;
    }
    
    // </editor-fold>
    
}
