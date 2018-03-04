package org.ai.bo.analise;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe para abstrair as classes casos para utilizacao pela rede neural
 * @author Edison
 */
public abstract class Caso implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    // <editor-fold defaultstate="collapsed" desc="Atributos">
    
    private int id;
    private List<Double> entradas;
    private List<Double> saidasEsperadas;
    private List<Double> saidasRede;

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Propriedades">
    
    public List<Double> getEntradas() {
        return entradas;
    }

    public void setEntradas(List<Double> entradas) {
        this.entradas = entradas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Double> getSaidasEsperadas() {
        return saidasEsperadas;
    }

    public void setSaidasEsperadas(List<Double> saidasEsperadas) {
        this.saidasEsperadas = saidasEsperadas;
    }

    public List<Double> getSaidasRede() {
        return saidasRede;
    }

    public void setSaidasRede(List<Double> saidasRede) {
        this.saidasRede = saidasRede;
    }

    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Construtores">
    
    public Caso() {
    }
    
    public Caso(double desvio, double quantidade, double horario, double localidade, double resposta){
        List<Double> teste  = new ArrayList<Double>();
        teste.add(desvio); //Desvio Valor Compra
        teste.add(quantidade); //Quantidade
        teste.add(horario); //Localidade
        teste.add(localidade); //Horário
        this.entradas = teste;
        List<Double> saidas = new ArrayList<Double>();
        saidas.add(resposta);
        this.saidasEsperadas = saidas;
    }
    
    // </editor-fold>
    
}
