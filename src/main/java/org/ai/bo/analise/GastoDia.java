package org.ai.bo.analise;

/**
 *
 * @author Edison
 */
public class GastoDia{

    public int dia;
    public int mes;
    public int ano;
    public int qtd;
    public double valor;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GastoDia other = (GastoDia) obj;
        if (this.dia != other.dia) {
            return false;
        }
        if (this.mes != other.mes) {
            return false;
        }
        if (this.ano != other.ano) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.dia;
        hash = 59 * hash + this.mes;
        hash = 59 * hash + this.ano;
        hash = 59 * hash + this.qtd;
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.valor) ^ (Double.doubleToLongBits(this.valor) >>> 32));
        return hash;
    }







}
