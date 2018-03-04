package org.ai.bo.analise;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.ai.bo.ambiente.StatusTransacao;
import org.ai.bo.ambiente.TerminalPOS;
import org.ai.bo.ambiente.Transacao;

/**
 *
 * @author Edison
 */
public class CasoCliente extends Caso {
    
    private static final long serialVersionUID = 1L;
    
    // <editor-fold defaultstate="collapsed" desc="Atributos">
    
    private int idCaso;
    private double desvioValorCompra;
    private double distanciaCidadeOrigem;
    private double desvioQtdComprasDia;
    private double hora;
    private double percComprasLocalidade;
    private double percComprasRamo;
    private double percFraudesAmbiente;
   

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Propriedades">
    
    public void setIdCaso(int idCaso) {
        this.idCaso = idCaso;
    }

    public int getIdCaso() {
        return idCaso;
    }

    public double getDesvioPadraoValorCompra() {
        return desvioValorCompra;
    }

    public void setDesvioPadraoValorCompra(double desvioPadraoValorCompra) {
        this.desvioValorCompra = desvioPadraoValorCompra;
    }

    public double getDistanciaCidadeOrigem() {
        return distanciaCidadeOrigem;
    }

    public void setDistanciaCidadeOrigem(double distanciaCidadeOrigem) {
        this.distanciaCidadeOrigem = distanciaCidadeOrigem;
    }

    public double getQuantidadeComprasDia() {
        return desvioQtdComprasDia;
    }

    public void setQuantidadeComprasDia(double quantidadeComprasDia) {
        this.desvioQtdComprasDia = quantidadeComprasDia;
    }

    public double getHora() {
        return hora;
    }

    public void setHora(double hora) {
        this.hora = hora;
    }

    public double getDesvioQtdComprasDia() {
        return desvioQtdComprasDia;
    }

    public void setDesvioQtdComprasDia(double desvioQtdComprasDia) {
        this.desvioQtdComprasDia = desvioQtdComprasDia;
    }

    public double getPercComprasLocalidade() {
        return percComprasLocalidade;
    }

    public void setPercComprasLocalidade(double percComprasLocalidade) {
        this.percComprasLocalidade = percComprasLocalidade;
    }

    public double getPercComprasRamo() {
        return percComprasRamo;
    }

    public void setPercComprasRamo(double percComprasRamo) {
        this.percComprasRamo = percComprasRamo;
    }



   
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Contrutores">

    public CasoCliente(Transacao transacao) {
        calcularDistanciaCidadeOrigem(transacao);
        calcularHoraCompra(transacao);
        calcularDesvioQuantidadeValorComprasDia(transacao);
        calcularPercComprasLocalidade(transacao);
        calcularPercComprasRamoAtividade(transacao);
        calcularPercFraudesAmbiente(transacao);
        definirEntradasRede();
        definirSaidasEsperadas(transacao);
    }
    
    public CasoCliente(){
        
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos Cálculo">
    
    public void calcularDistanciaCidadeOrigem(Transacao transacao){
        
        if(transacao.getCartao().getCliente().getCidade().getId() == transacao.getTerminalPOS().getEstabelecimento().getCidade().getId()){
            distanciaCidadeOrigem = 0.00F; //Dentro da Cidade
        } else if (transacao.getCartao().getCliente().getCidade().getRegiao().getId() == transacao.getTerminalPOS().getEstabelecimento().getCidade().getRegiao().getId()){
            distanciaCidadeOrigem = 0.25F; //Dentro da Região
        } else if (transacao.getCartao().getCliente().getCidade().getEstado().getId() == transacao.getTerminalPOS().getEstabelecimento().getCidade().getEstado().getId()){
            distanciaCidadeOrigem = 0.50F; //Dentro do Estado
        } else {
            distanciaCidadeOrigem = 1.00F; //Fora do Estado
        }
    }


    public void calcularHoraCompra(Transacao transacao){
        if(transacao.getDhTransacao().get(Calendar.HOUR_OF_DAY) >= 6 && transacao.getDhTransacao().get(Calendar.HOUR_OF_DAY) < 12){ 
            hora = 0.25F; //Dia
        } else if(transacao.getDhTransacao().get(Calendar.HOUR_OF_DAY) >= 12 && transacao.getDhTransacao().get(Calendar.HOUR_OF_DAY) < 18){
            hora = 0.50F; //Tarde
        } else if(transacao.getDhTransacao().get(Calendar.HOUR_OF_DAY) >= 18 && transacao.getDhTransacao().get(Calendar.HOUR_OF_DAY) < 24){
            hora = 0.75F; //Noite
        } else {
            hora = 1.00F; //Madrugada
        }   
    }
    
    public void calcularDesvioQuantidadeValorComprasDia(Transacao transacao){
        int qtd = 0;
        double gastoDia = 0.0D;
        for (Transacao transacaoAnterior : transacao.getCartao().getTransacoes()) {
            if (transacaoAnterior.getDhTransacao().get(Calendar.DATE) == transacao.getDhTransacao().get(Calendar.DATE)
              &&transacaoAnterior.getDhTransacao().get(Calendar.MONTH) == transacao.getDhTransacao().get(Calendar.MONTH)
              &&transacaoAnterior.getDhTransacao().get(Calendar.YEAR) == transacao.getDhTransacao().get(Calendar.YEAR)) {
                qtd++;
                gastoDia += transacaoAnterior.getValorTotal();
            }
        }
        desvioValorCompra = gastoDia / transacao.getCartao().getCliente().getAnalisePerfil().getMediaValorComprasDiaria();
        desvioQtdComprasDia = qtd / transacao.getCartao().getCliente().getAnalisePerfil().getMediaComprasEfetuadasDiarias();
    }

    public void calcularPercComprasRamoAtividade(Transacao transacao){
        Integer qtdRamo = transacao.getCartao().getCliente().getAnalisePerfil().getCountComprasEfetuadasRamo().get(transacao.getTerminalPOS().getEstabelecimento().getRamoAtividade().getId());
        if(qtdRamo == null){
            percComprasRamo = 0.0D;
        } else {
            percComprasRamo = (float)qtdRamo / (float)transacao.getCartao().getCliente().getAnalisePerfil().getCountComprasEfetuadasTotal();
        }
    }

    public void calcularPercComprasLocalidade(Transacao transacao){
        int qtd = 0;
        for (Transacao transacaoAnterior : transacao.getCartao().getTransacoes()) {
            if (transacaoAnterior.getTerminalPOS().getEstabelecimento().getCidade().getId() == transacao.getTerminalPOS().getEstabelecimento().getCidade().getId()) {
                qtd++;
            }
        }
        percComprasLocalidade = (float)qtd / (float)transacao.getCartao().getCliente().getAnalisePerfil().getCountComprasEfetuadasTotal();

    }

    private void calcularPercFraudesAmbiente(Transacao transacao){
        List<TerminalPOS> terminais = TerminalPOS.findByCidadeAndRamoAtividade(transacao.getTerminalPOS().getEstabelecimento().getCidade(), transacao.getTerminalPOS().getEstabelecimento().getRamoAtividade());
        List<Transacao> transacoesAmbiente = new ArrayList<Transacao>();
        for (TerminalPOS terminalPOS : terminais) {
            transacoesAmbiente.addAll(terminalPOS.getTransacoes());
        }
        int qtdFraudes = 0;
        for (Transacao transacaoAtual : transacoesAmbiente) {
            if(transacaoAtual.getStatus() == StatusTransacao.FRAUDE || transacaoAtual.getStatus() == StatusTransacao.FALSO_NEGATIVO){
                qtdFraudes++;
            }
        }
        percFraudesAmbiente = (float)qtdFraudes / (float) transacoesAmbiente.size();
    }



    public void definirSaidasEsperadas(Transacao transacao) {
        List<Double> saidas = new ArrayList<Double>();
        double risco = 0.00D;
        if(transacao.getStatus() == StatusTransacao.FRAUDE || transacao.getStatus() == StatusTransacao.FALSO_POSITIVO){
            risco += 1.0F;
        }
//        risco += distanciaCidadeOrigem/8; //max 20%
//        risco += desvioQtdComprasDia /5; //2,5% por compra. 10compras = 25%
//        if (desvioPadraoValorCompra > 1){
//            risco += (desvioPadraoValorCompra -1)/20; // 4.00 -> 0.26
//        }
//
//        if (risco > 1.00D){
//            risco = 1.00D;
//        }
        saidas.add(risco);
        super.setSaidasEsperadas(saidas);
        
    }
    
    public void definirEntradasRede(){
        List<Double> entradas = new ArrayList<Double>();
        entradas.add(desvioValorCompra);
        entradas.add(distanciaCidadeOrigem);
        entradas.add(desvioQtdComprasDia);
        entradas.add(hora);
        entradas.add(percComprasLocalidade);
        entradas.add(percComprasRamo);
        entradas.add(percFraudesAmbiente);
        super.setEntradas(entradas);
    }
    
    public static List<String> getLabels(){
        List<String> labels = new ArrayList<String>();
        labels.add("valor");    //Desvio do gasto diário
        labels.add("dist");     //Distância
        labels.add("qtd");      //Desvio da quantidade diária
        labels.add("hora");     //Horário
        labels.add("% local");  //% compras localidade
        labels.add("% ramo");   //% compras ramo
        labels.add("% fraud");  //% fraudes no ambiente
        return labels;
    }
    
    // </editor-fold>
   
}
