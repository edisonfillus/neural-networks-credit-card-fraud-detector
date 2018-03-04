package org.ai.bo.neural.treino;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ai.bo.analise.Caso;

/**
 * Classe DadosTreinamento
 * @author Edison Klafke Fillus
 * @author Jefferson de Souza Campos
 */
public class DadosTreinamento implements Serializable, Cloneable{
    
    private static final long serialVersionUID = 1L;
    
    // <editor-fold defaultstate="collapsed" desc="Atributos">
    
    private Calendar dhUltimoTreinamento;
    private List<Caso> listCasosTreinamento;
    private List<Caso> listCasosTeste;
    private List<String> labels;
    private List<EstatisticaTreinamento> estatisticasTreinamento;
    private List<TaxaAprendizadoAdaptativa> taxaAprendizagemAdaptativa;
    private TipoTaxaAprendizagem tipoTaxaAprendizagem;
    private TipoCriterioParada tipoCriterioParada;
    private TipoFuncaoAtivacao tipoFuncaoAtivacao;
    private TipoTreino tipoTreino;

    private double taxaAprendizado;
    private double taxaAprendizadoMaxima;
    private double indiceAcertosTestes;
    private double indiceAcertosTreinamento;
    private double erroQuadraticoMedio;
    private double erroQuadraticoMedioTestes;
    private double erroAceitavel;
    private int maxEpocas;
    private int reinicio;
    private int epocas;
    private long tempoDecorrido;

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Contrutores">
    
    public DadosTreinamento() {
        listCasosTreinamento = new ArrayList<Caso>();
        listCasosTeste = new ArrayList<Caso>();
        labels = new ArrayList<String>();
        estatisticasTreinamento = new ArrayList<EstatisticaTreinamento>();
        taxaAprendizagemAdaptativa = new ArrayList<TaxaAprendizadoAdaptativa>();
        tipoFuncaoAtivacao = TipoFuncaoAtivacao.SigmoideLogistica;
        tipoTreino = tipoTreino.BackPropagation;
        tipoTaxaAprendizagem = TipoTaxaAprendizagem.FIXA;
        tipoCriterioParada = TipoCriterioParada.ERRO_ACEITAVEL;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Propriedades">

    public Calendar getDhUltimoTreinamento() {
        return dhUltimoTreinamento;
    }

    public void setDhUltimoTreinamento(Calendar dhUltimoTreinamento) {
        this.dhUltimoTreinamento = dhUltimoTreinamento;
    }

    public int getEpocas() {
        return epocas;
    }

    public void setEpocas(int epocas) {
        this.epocas = epocas;
    }

    public double getErroQuadraticoMedio() {
        return erroQuadraticoMedio;
    }

    public void setErroQuadraticoMedio(double erroQuadraticoMedio) {
        this.erroQuadraticoMedio = erroQuadraticoMedio;
    }

    public List<Caso> getListCasosTreinamento() {
        return listCasosTreinamento;
    }

    public void setListCasosTreinamento(List<Caso> listCasosTreinamento) {
        this.listCasosTreinamento = listCasosTreinamento;
    }

    public double getTaxaAprendizado() {
        return taxaAprendizado;
    }

    public void setTaxaAprendizado(double taxaAprendizado) {
        this.taxaAprendizado = taxaAprendizado;
    }

    public long getTempoDecorrido() {
        return tempoDecorrido;
    }

    public void setTempoDecorrido(long tempoDecorrido) {
        this.tempoDecorrido = tempoDecorrido;
    }

    public double getErroAceitavel() {
        return erroAceitavel;
    }

    public void setErroAceitavel(double erroAceitavel) {
        this.erroAceitavel = erroAceitavel;
    }

    public List<EstatisticaTreinamento> getEstatisticasTreinamento() {
        return estatisticasTreinamento;
    }

    public void setEstatisticasTreinamento(List<EstatisticaTreinamento> estatisticasTreinamento) {
        this.estatisticasTreinamento = estatisticasTreinamento;
    }

    public List<TaxaAprendizadoAdaptativa> getTaxaAprendizagemAdaptativa() {
        return taxaAprendizagemAdaptativa;
    }

    public void setTaxaAprendizagemAdaptativa(List<TaxaAprendizadoAdaptativa> taxaAprendizagemAdaptativa) {
        this.taxaAprendizagemAdaptativa = taxaAprendizagemAdaptativa;
    }

    public TipoFuncaoAtivacao getTipoFuncaoAtivacao() {
        return tipoFuncaoAtivacao;
    }

    public void setTipoFuncaoAtivacao(TipoFuncaoAtivacao tipoFuncaoAtivacao) {
        this.tipoFuncaoAtivacao = tipoFuncaoAtivacao;
    }

    public TipoTreino getTipoTreino() {
        return tipoTreino;
    }

    public void setTipoTreino(TipoTreino tipoTreino) {
        this.tipoTreino = tipoTreino;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public TipoCriterioParada getTipoCriterioParada() {
        return tipoCriterioParada;
    }

    public void setTipoCriterioParada(TipoCriterioParada tipoCriterioParada) {
        this.tipoCriterioParada = tipoCriterioParada;
    }

    public TipoTaxaAprendizagem getTipoTaxaAprendizagem() {
        return tipoTaxaAprendizagem;
    }

    public void setTipoTaxaAprendizagem(TipoTaxaAprendizagem tipoTaxaAprendizagem) {
        this.tipoTaxaAprendizagem = tipoTaxaAprendizagem;
    }

    public double getTaxaAprendizadoMaxima() {
        return taxaAprendizadoMaxima;
    }

    public void setTaxaAprendizadoMaxima(double taxaAprendizadoMaxima) {
        this.taxaAprendizadoMaxima = taxaAprendizadoMaxima;
    }

    public int getMaxEpocas() {
        return maxEpocas;
    }

    public void setMaxEpocas(int maxEpocas) {
        this.maxEpocas = maxEpocas;
    }

    public List<Caso> getListCasosTeste() {
        return listCasosTeste;
    }

    public void setListCasosTeste(List<Caso> listCasosTeste) {
        this.listCasosTeste = listCasosTeste;
    }

    public double getErroQuadraticoMedioTestes() {
        return erroQuadraticoMedioTestes;
    }

    public void setErroQuadraticoMedioTestes(double erroQuadraticoMedioTestes) {
        this.erroQuadraticoMedioTestes = erroQuadraticoMedioTestes;
    }

    public double getIndiceAcertosTestes() {
        return indiceAcertosTestes;
    }

    public void setIndiceAcertosTestes(double indiceAcertosTestes) {
        this.indiceAcertosTestes = indiceAcertosTestes;
    }

    public double getIndiceAcertosTreinamento() {
        return indiceAcertosTreinamento;
    }

    public void setIndiceAcertosTreinamento(double indiceAcertosTreinamento) {
        this.indiceAcertosTreinamento = indiceAcertosTreinamento;
    }

    public int getReinicio() {
        return reinicio;
    }

    public void setReinicio(int reinicio) {
        this.reinicio = reinicio;
    }




    
    
   

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos">
    
    /**
     * Recupera a taxa de aprendizado atual baseada no erro quadrático médio
     * Se a Rede não utiliza taxa de aprendizado adaptativa, retorna o valor de taxa de aprendizado
     * @return
     */
    public double getTaxaAprendizagemAtual(){
        if (tipoTaxaAprendizagem == tipoTaxaAprendizagem.ADAPTATIVA){
            for (int i = 0; i < taxaAprendizagemAdaptativa.size(); i++) {
                if (erroQuadraticoMedio > taxaAprendizagemAdaptativa.get(i).getErroQuadraticoMedio()){
                    return taxaAprendizagemAdaptativa.get(i).getTaxaAprendizado();
                }
            }
            //Se erro quadrático menor que todos, define a ultima taxa
            return taxaAprendizagemAdaptativa.get(taxaAprendizagemAdaptativa.size() - 1).getTaxaAprendizado();
        } else {
            //Não utiliza taxa de aprendizagem adaptativa
            return taxaAprendizado;
        } 
    }

     public void transfereInformacoes(DadosTreinamento clone){
        clone.setDhUltimoTreinamento(dhUltimoTreinamento);
        clone.setEpocas(epocas);
        clone.setErroAceitavel(erroAceitavel);
        clone.setErroQuadraticoMedio(erroQuadraticoMedio);
        clone.setTaxaAprendizado(taxaAprendizado);
        clone.setTipoTaxaAprendizagem(tipoTaxaAprendizagem);
        clone.setTipoCriterioParada(tipoCriterioParada);
        clone.setTempoDecorrido(tempoDecorrido);
        clone.setTipoFuncaoAtivacao(tipoFuncaoAtivacao);
        clone.setTipoTreino(tipoTreino);
        clone.setEstatisticasTreinamento(estatisticasTreinamento);
        clone.setLabels(labels);
        clone.setListCasosTreinamento(listCasosTreinamento);
        clone.setListCasosTeste(listCasosTeste);
    }

    @Override
    public DadosTreinamento clone(){
        ObjectOutputStream oos = null;
        DadosTreinamento clone  = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(100);
            oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            byte[] buf = baos.toByteArray();
            oos.close();
            // deserializa
            ByteArrayInputStream bais = new ByteArrayInputStream(buf);
            ObjectInputStream ois = new ObjectInputStream(bais);
            clone = (DadosTreinamento) ois.readObject();
            ois.close();
        } catch (IOException ex) {
            Logger.getLogger(DadosTreinamento.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DadosTreinamento.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                oos.close();
            } catch (IOException ex) {
                Logger.getLogger(DadosTreinamento.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return clone;
    }
    
    // </editor-fold>
    
}
