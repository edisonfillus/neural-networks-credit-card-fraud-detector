package org.ai.bo.analise;

import com.db4o.collections.MapEntry4;
import java.lang.String;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ai.bo.ambiente.Cartao;
import org.ai.bo.ambiente.Cliente;
import org.ai.bo.ambiente.RamoAtividade;
import org.ai.bo.ambiente.StatusTransacao;
import org.ai.bo.ambiente.Transacao;
import org.ai.dao.factory.Db4oDAOFactory;

import oracle.toplink.essentials.internal.queryframework.MapContainerPolicy;

/**
 *
 * @author Edison
 */
public class AnalisePerfilCliente {
    
    // <editor-fold defaultstate="collapsed" desc="Atributos">
    
    private int id;
    private double mediaValorCompras;
    private Calendar dhUltimaAnalise;
    private StatusAnalise status;
    private int countComprasEfetuadasTotal;
    private int countComprasEfetuadasCidade;
    private int countComprasEfetuadasRegiao;
    private int countComprasEfetuadasEstado;
    private int countComprasEfetuadasForaEstado;
    private double mediaComprasEfetuadasDiarias;
    private double mediaValorComprasDiaria;
    private double valorTotalGasto;
    private int countDiasOcorreuCompras;
    private Map<Integer,Integer> countComprasEfetuadasRamo;
    private List<GastoDia> gastosDiarios;
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Propriedades">
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Calendar getDhUltimaAnalise() {
        return dhUltimaAnalise;
    }

    public void setDhUltimaAnalise(Calendar dhUltimaAnalise) {
        this.dhUltimaAnalise = dhUltimaAnalise;
    }

    public StatusAnalise getStatus() {
        return status;
    }

    public void setStatus(StatusAnalise status) {
        this.status = status;
    }

    public int getCountComprasEfetuadasCidade() {
        return countComprasEfetuadasCidade;
    }

    public void setCountComprasEfetuadasCidade(int countComprasEfetuadasCidade) {
        this.countComprasEfetuadasCidade = countComprasEfetuadasCidade;
    }

    public int getCountComprasEfetuadasEstado() {
        return countComprasEfetuadasEstado;
    }

    public void setCountComprasEfetuadasEstado(int countComprasEfetuadasEstado) {
        this.countComprasEfetuadasEstado = countComprasEfetuadasEstado;
    }

    public int getCountComprasEfetuadasForaEstado() {
        return countComprasEfetuadasForaEstado;
    }

    public void setCountComprasEfetuadasForaEstado(int countComprasEfetuadasForaEstado) {
        this.countComprasEfetuadasForaEstado = countComprasEfetuadasForaEstado;
    }

    public Map<Integer, Integer> getCountComprasEfetuadasRamo() {
        return countComprasEfetuadasRamo;
    }

    public void setCountComprasEfetuadasRamo(Map<Integer, Integer> countComprasEfetuadasRamo) {
        this.countComprasEfetuadasRamo = countComprasEfetuadasRamo;
    }

    public int getCountComprasEfetuadasRegiao() {
        return countComprasEfetuadasRegiao;
    }

    public void setCountComprasEfetuadasRegiao(int countComprasEfetuadasRegiao) {
        this.countComprasEfetuadasRegiao = countComprasEfetuadasRegiao;
    }

    public int getCountComprasEfetuadasTotal() {
        return countComprasEfetuadasTotal;
    }

    public void setCountComprasEfetuadasTotal(int countComprasEfetuadasTotal) {
        this.countComprasEfetuadasTotal = countComprasEfetuadasTotal;
    }

    public double getMediaValorCompras() {
        return mediaValorCompras;
    }

    public void setMediaValorCompras(double mediaValorCompras) {
        this.mediaValorCompras = mediaValorCompras;
    }

    public double getValorTotalGasto() {
        return valorTotalGasto;
    }

    public void setValorTotalGasto(double valorTotalGasto) {
        this.valorTotalGasto = valorTotalGasto;
    }

    public int getCountDiasOcorreuCompras() {
        return countDiasOcorreuCompras;
    }

    public void setCountDiasOcorreuCompras(int countDiasOcorreuCompras) {
        this.countDiasOcorreuCompras = countDiasOcorreuCompras;
    }

    public List<GastoDia> getGastosDiarios() {
        return gastosDiarios;
    }

    public void setGastosDiarios(List<GastoDia> gastosDiarios) {
        this.gastosDiarios = gastosDiarios;
    }

    public double getMediaComprasEfetuadasDiarias() {
        return mediaComprasEfetuadasDiarias;
    }

    public void setMediaComprasEfetuadasDiarias(double mediaComprasEfetuadasDiarias) {
        this.mediaComprasEfetuadasDiarias = mediaComprasEfetuadasDiarias;
    }

    public double getMediaValorComprasDiaria() {
        return mediaValorComprasDiaria;
    }

    public void setMediaValorComprasDiaria(double mediaValorComprasDiaria) {
        this.mediaValorComprasDiaria = mediaValorComprasDiaria;
    }
    

    
    
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Construtor">

   
    public AnalisePerfilCliente() {
        id = 0;
        mediaValorCompras = 0.00D;
        countComprasEfetuadasTotal = 0;
        gastosDiarios = new ArrayList<GastoDia>();
        countComprasEfetuadasRamo = new HashMap<Integer,Integer>();
        status = StatusAnalise.NAO_ANALISADA;
    }
    
    
    
    // </editor-fold>
            
    // <editor-fold defaultstate="collapsed" desc="Métodos">
    
    public void analisarPerfil(Cliente cliente){
        status = StatusAnalise.ANALISANDO;
        efetuarCalculos(cliente);
        dhUltimaAnalise = Calendar.getInstance();
        status = StatusAnalise.ANALISADA;
    }

    /**
     * Efetua os calculos para identificar o perfil do cliente
     * OBS: Transações fraudulentas estão sendo ignoradas
     * @param cliente
     */
    private void efetuarCalculos(Cliente cliente) {
        
        valorTotalGasto = 0.00D;
        countComprasEfetuadasCidade = 0;
        countComprasEfetuadasEstado = 0;
        countComprasEfetuadasForaEstado = 0;
        countComprasEfetuadasRamo = new HashMap<Integer, Integer>();
        countComprasEfetuadasRegiao = 0;
        countComprasEfetuadasTotal = 0;
        countDiasOcorreuCompras = 0;
        gastosDiarios = new ArrayList<GastoDia>();
        mediaComprasEfetuadasDiarias = 0;
        mediaValorCompras = 0;
        mediaValorComprasDiaria = 0;

        for (Cartao cartao : cliente.getCartoes()) {
            for (Transacao transacao : cartao.getTransacoes()) {
                if(transacao.getStatus() != StatusTransacao.FRAUDE){
                    valorTotalGasto += transacao.getValorTotal(); //Incrementa o valor total
                    countComprasEfetuadasTotal++; //Incrementa o valor da transação
                    if(transacao.getTerminalPOS().getEstabelecimento().getCidade().getId() == cliente.getCidade().getId()){
                        countComprasEfetuadasCidade++;
                    } else if (transacao.getTerminalPOS().getEstabelecimento().getCidade().getRegiao().getId() == cliente.getCidade().getRegiao().getId()){
                        countComprasEfetuadasRegiao++;
                    } else if (transacao.getTerminalPOS().getEstabelecimento().getCidade().getEstado().getId() == cliente.getCidade().getEstado().getId()){
                        countComprasEfetuadasEstado++;
                    } else {
                        countComprasEfetuadasForaEstado++;
                    }
                    if(countComprasEfetuadasRamo.containsKey(transacao.getTerminalPOS().getEstabelecimento().getRamoAtividade().getId())){
                        int countComprasRamo = countComprasEfetuadasRamo.get(transacao.getTerminalPOS().getEstabelecimento().getRamoAtividade().getId());
                        countComprasRamo++;
                        countComprasEfetuadasRamo.put(transacao.getTerminalPOS().getEstabelecimento().getRamoAtividade().getId(), countComprasRamo);
                    } else {
                        countComprasEfetuadasRamo.put(transacao.getTerminalPOS().getEstabelecimento().getRamoAtividade().getId(), 1);
                    }
                    //Adiciona a estatistica diaria
                    GastoDia gastos = new GastoDia();
                    gastos.ano = transacao.getDhTransacao().get(Calendar.YEAR);
                    gastos.mes = transacao.getDhTransacao().get(Calendar.MONTH) + 1;
                    gastos.dia = transacao.getDhTransacao().get(Calendar.DAY_OF_MONTH);
                    if(gastosDiarios.contains(gastos)){
                        gastos = gastosDiarios.get(gastosDiarios.indexOf(gastos));
                        gastos.qtd++;
                        gastos.valor += transacao.getValorTotal();
                    } else {
                        gastos.qtd = 1;
                        gastos.valor = transacao.getValorTotal();
                        gastosDiarios.add(gastos);
                    }



                }
            }
        }
        //Calcula a média total
        if (valorTotalGasto == 0.00D){
            mediaValorCompras = 0.00D;
        } else {
            mediaValorCompras = valorTotalGasto / countComprasEfetuadasTotal;
        }
        //Calcula média diária
        countDiasOcorreuCompras = gastosDiarios.size();
        
        mediaComprasEfetuadasDiarias = (double)countComprasEfetuadasTotal / (double)countDiasOcorreuCompras;
        mediaValorComprasDiaria = valorTotalGasto / countDiasOcorreuCompras;

    }

    /**
     * Recupera os casos para treinamento da rede neural
     * OBS: Somente são retornados os casos marcados para treinamento
     * @param cliente
     * @param tipo
     * @return
     */
    public List<Caso> recuperarCasosTreinamento(Cliente cliente, TipoCaso tipo) {
        List<Caso> casosTreinamento = new ArrayList<Caso>();
        for (Cartao cartao : cliente.getCartoes()) {
            for (Transacao transacao : cartao.getTransacoes()) {
                switch (tipo){
                    case TODOS: {
                        casosTreinamento.add(new CasoCliente(transacao));
                        break;
                    }
                    case TREINAMENTO: {
                        if (transacao.isFlagTreinamento()){
                            casosTreinamento.add(new CasoCliente(transacao));
                        }
                        break;
                    }
                    case TESTE: {
                        if (transacao.isFlagTeste()){
                            casosTreinamento.add(new CasoCliente(transacao));
                        }
                        break;
                    }
                }
                
            }
        }
        return casosTreinamento;
    }
    
    

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos Persistência">
    /**
     * Método para criar o objeto no local de persistência
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean create() {
        return Db4oDAOFactory.getAnalisePerfilClienteDAO().createAnalisePerfilCliente(this);
    }

    /**
     * Método para atualizar o objeto no local de persistência
     * @return
     */
    public boolean update() {
        return Db4oDAOFactory.getAnalisePerfilClienteDAO().updateAnalisePerfilCliente(this);
    }

    /**
     * Método para excluir o objeto no local de persistência
     * @return
     */
    public boolean delete() {
        return Db4oDAOFactory.getAnalisePerfilClienteDAO().deleteAnalisePerfilCliente(this);
    }

    /**
     * Método para localizar um objeto AnalisePerfilCliente no local de persitência
     * @param id
     * @return AnalisePerfilCliente
     */
    public static AnalisePerfilCliente find(int id) {
        return Db4oDAOFactory.getAnalisePerfilClienteDAO().findAnalisePerfilCliente(id);
    }

    /**
     * Método para obter uma lista de AnalisePerfilCliente do local de persistência
     * @return
     */
    public static List<AnalisePerfilCliente> findAll() {
        return Db4oDAOFactory.getAnalisePerfilClienteDAO().listAnalisePerfilCliente();
    }

    /**
     * Método para obter a quantidade de AnalisePerfilCliente do local de persistência
     * @return
     */
    public static int count() {
        return Db4oDAOFactory.getAnalisePerfilClienteDAO().getAnalisePerfilClienteCount();
    }    // </editor-fold>
    
    
 
    
    


}
