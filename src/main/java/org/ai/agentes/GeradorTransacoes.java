/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ai.agentes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.ai.bo.ambiente.Banco;
import org.ai.bo.ambiente.Bandeira;
import org.ai.bo.ambiente.Cartao;
import org.ai.bo.ambiente.Cliente;
import org.ai.bo.ambiente.Estabelecimento;
import org.ai.bo.ambiente.RamoAtividade;
import org.ai.bo.ambiente.TerminalPOS;
import org.ai.bo.ambiente.Transacao;

/**
 *
 * @author Edison
 */
public class GeradorTransacoes {

    // <editor-fold defaultstate="collapsed" desc="Parâmetros">
    //LIMITE CARTOES EM RELACAO A RENDA
    public static float LIMITE_MIN = 0.5F; //50%
    public static float LIMITE_MAX = 2.0F; //200%
    //NUMERO CARTOES
    public static int CARTAO_MIN = 1;
    public static int CARTAO_MAX = 2;
    //QUANTIDADE DE COMPRAS MENSAIS
    public static int COMPRAS_MES_MIN = 5;
    public static int COMPRAS_MES_MAX = 25;
    //MEDIA UTILIZACAO LIMITE
    public static float UTILIZACAO_LIMITE_MIN = 0.0F; //  0%
    public static float UTILIZACAO_LIMITE_MAX = 1.0F; //100%
    //DESVIO VALOR COMPRA EM RELACAO MEDIA VALOR
    public static float DESVIO_MEDIA_COMPRA_MIN = 0.25F; // 25%
    public static float DESVIO_MEDIA_COMPRA_MAX = 3.00F; //300%
    //DESVIO UTILIZACAO LIMITE MES
    public static float DESVIO_UTILIZACAO_LIMITE_MIN = 0.75F;// 75%
    public static float DESVIO_UTILIZACO_LIMITE_MAX = 1.25F; //125%

    //LOCALIDADE
    public static float COMPRAS_NA_CIDADE = 0.70F;
    public static float COMPRAS_NA_REGIAO = 0.15F;
    public static float COMPRAS_NO_ESTADO = 0.10F;
    public static float COMPRAS_FORA_ESTADO = 0.05F;

    //PERIODO
    public static int PERIODO_INICIAL_MES = 1;
    public static int PERIODO_INICIAL_ANO = 2008;
    public static int PERIODO_FINAL_MES = 9;
    public static int PERIODO_FINAL_ANO = 2008;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Atributos">
    private Cliente cliente;
    private List<Banco> bancos;
    private List<TerminalPOS> terminais;
    private List<TerminalPOS> terminais2; //Para manter uma cópia
    private int limiteGerado;
    private double limiteMin = LIMITE_MIN;
    private double limiteMax = LIMITE_MAX;
    private double mediaUtilizacaoLimiteGerado;
    private double mediaUtilizacaoLimiteMin = UTILIZACAO_LIMITE_MIN;
    private double mediaUtilizacaoLimiteMax = UTILIZACAO_LIMITE_MAX;
    private double desvioUtilizacaoLimiteGerado;
    private double desvioUtilizacaoLimiteMin = DESVIO_UTILIZACAO_LIMITE_MIN;
    private double desvioUtilizacaoLimiteMax = DESVIO_UTILIZACO_LIMITE_MAX;
    private int numCartoesGerado;
    private int numCartoesMin = CARTAO_MIN;
    private int numCartoesMax = CARTAO_MAX;
    private int numComprasGerado;
    private int numComprasMin = COMPRAS_MES_MIN;
    private int numComprasMax = COMPRAS_MES_MAX;
    private double desvioMediaCompraGerado;
    private double desvioMediaCompraMin = DESVIO_MEDIA_COMPRA_MIN;
    private double desvioMediaCompraMax = DESVIO_MEDIA_COMPRA_MAX;
    private int periodoInicialMes = PERIODO_INICIAL_MES;
    private int periodoInicialAno = PERIODO_INICIAL_ANO;
    private int periodoFinalMes = PERIODO_FINAL_MES;
    private int periodoFinalAno = PERIODO_FINAL_ANO;
    private int mediaValorCompras;
    private int transacoesGeradas;

    private int[][] distribuicao;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Propriedades">
    public double getDesvioUtilizacaoLimiteGerado() {
        return desvioUtilizacaoLimiteGerado;
    }

    public void setDesvioUtilizacaoLimiteGerado(double desvioUtilizacaoLimiteGerado) {
        this.desvioUtilizacaoLimiteGerado = desvioUtilizacaoLimiteGerado;
    }

    public double getDesvioUtilizacaoLimiteMax() {
        return desvioUtilizacaoLimiteMax;
    }

    public void setDesvioUtilizacaoLimiteMax(double desvioUtilizacaoLimiteMax) {
        this.desvioUtilizacaoLimiteMax = desvioUtilizacaoLimiteMax;
    }

    public double getDesvioUtilizacaoLimiteMin() {
        return desvioUtilizacaoLimiteMin;
    }

    public void setDesvioUtilizacaoLimiteMin(double desvioUtilizacaoLimiteMin) {
        this.desvioUtilizacaoLimiteMin = desvioUtilizacaoLimiteMin;
    }

    public int getLimiteGerado() {
        return limiteGerado;
    }

    public void setLimiteGerado(int limiteGerado) {
        this.limiteGerado = limiteGerado;
    }

    public double getLimiteMax() {
        return limiteMax;
    }

    public void setLimiteMax(double limiteMax) {
        this.limiteMax = limiteMax;
    }

    public double getLimiteMin() {
        return limiteMin;
    }

    public void setLimiteMin(double limiteMin) {
        this.limiteMin = limiteMin;
    }

    public int getMediaValorCompras() {
        return mediaValorCompras;
    }

    public void setMediaValorCompras(int mediaValorCompras) {
        this.mediaValorCompras = mediaValorCompras;
    }

    public int getNumCartoesGerado() {
        return numCartoesGerado;
    }

    public void setNumCartoesGerado(int numCartoesGerado) {
        this.numCartoesGerado = numCartoesGerado;
    }

    public int getNumCartoesMax() {
        return numCartoesMax;
    }

    public void setNumCartoesMax(int numCartoesMax) {
        this.numCartoesMax = numCartoesMax;
    }

    public int getNumCartoesMin() {
        return numCartoesMin;
    }

    public void setNumCartoesMin(int numCartoesMin) {
        this.numCartoesMin = numCartoesMin;
    }

    public int getNumComprasGerado() {
        return numComprasGerado;
    }

    public void setNumComprasGerado(int numComprasGerado) {
        this.numComprasGerado = numComprasGerado;
    }

    public int getNumComprasMax() {
        return numComprasMax;
    }

    public void setNumComprasMax(int numComprasMax) {
        this.numComprasMax = numComprasMax;
    }

    public int getNumComprasMin() {
        return numComprasMin;
    }

    public void setNumComprasMin(int numComprasMin) {
        this.numComprasMin = numComprasMin;
    }

    public int getTransacoesGeradas() {
        return transacoesGeradas;
    }

    public void setTransacoesGeradas(int transacoesGeradas) {
        this.transacoesGeradas = transacoesGeradas;
    }

    public double getDesvioMediaCompraGerado() {
        return desvioMediaCompraGerado;
    }

    public void setDesvioMediaCompraGerado(double desvioMediaCompraGerado) {
        this.desvioMediaCompraGerado = desvioMediaCompraGerado;
    }

    public double getDesvioMediaCompraMax() {
        return desvioMediaCompraMax;
    }

    public void setDesvioMediaCompraMax(double desvioMediaCompraMax) {
        this.desvioMediaCompraMax = desvioMediaCompraMax;
    }

    public double getDesvioMediaCompraMin() {
        return desvioMediaCompraMin;
    }

    public void setDesvioMediaCompraMin(double desvioMediaCompraMin) {
        this.desvioMediaCompraMin = desvioMediaCompraMin;
    }

    public void setMediaUtilizacaoLimiteGerado(double mediaUtilizacaoLimiteGerado) {
        this.mediaUtilizacaoLimiteGerado = mediaUtilizacaoLimiteGerado;
    }

    public double getMediaUtilizacaoLimiteMax() {
        return mediaUtilizacaoLimiteMax;
    }

    public void setMediaUtilizacaoLimiteMax(double mediaUtilizacaoLimiteMax) {
        this.mediaUtilizacaoLimiteMax = mediaUtilizacaoLimiteMax;
    }

    public double getMediaUtilizacaoLimiteMin() {
        return mediaUtilizacaoLimiteMin;
    }

    public void setMediaUtilizacaoLimiteMin(double mediaUtilizacaoLimiteMin) {
        this.mediaUtilizacaoLimiteMin = mediaUtilizacaoLimiteMin;
    }

    public int getPeriodoFinalAno() {
        return periodoFinalAno;
    }

    public void setPeriodoFinalAno(int periodoFinalAno) {
        this.periodoFinalAno = periodoFinalAno;
    }

    public int getPeriodoFinalMes() {
        return periodoFinalMes;
    }

    public void setPeriodoFinalMes(int periodoFinalMes) {
        this.periodoFinalMes = periodoFinalMes;
    }

    public int getPeriodoInicialAno() {
        return periodoInicialAno;
    }

    public void setPeriodoInicialAno(int periodoInicialAno) {
        this.periodoInicialAno = periodoInicialAno;
    }

    public int getPeriodoInicialMes() {
        return periodoInicialMes;
    }

    public void setPeriodoInicialMes(int periodoInicialMes) {
        this.periodoInicialMes = periodoInicialMes;
    }

    public int[][] getDistribuicao() {
        return distribuicao;
    }

    public void setDistribuicao(int[][] distribuicao) {
        this.distribuicao = distribuicao;
    }
    

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Contrutores">
    public GeradorTransacoes(Cliente cliente) {
        this.cliente = cliente;
    }

    public static void main(String[] args) {
        org.apache.log4j.PropertyConfigurator.configure(GeradorTransacoes.class.getClassLoader().getResource("br/edu/facear/tcc/log/log4j.properties"));
        GeradorTransacoes ger = new GeradorTransacoes(Cliente.findAll().get(0));
        ger.carregaListaProbabilisticaTerminais();

    }



    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Métodos">
    public void iniciarGeracao() {
        bancos = Banco.findAll();
        terminais = new ArrayList<TerminalPOS>();
        gerarLimite();
        gerarCartoes();
        gerarTransacoes();
    }

    public void gerarLimite() {
        int min = (int) (cliente.getRenda() * limiteMin);
        int max = (int) (cliente.getRenda() * limiteMax);
        limiteGerado = randomInterval(min, max);
    }

    /**
     * Método para gerar cartões
     * OBS: Esta gerando somente um cartao por cliente
     */
    public void gerarCartoes() {
        Cartao cartao = new Cartao();
        cartao.setCliente(cliente);
        cartao.setBloqueado(false);
        cartao.setCodSeguranca(123);
        String numCartao = 4 + Long.toString(UUID.randomUUID().getLeastSignificantBits()).substring(1, 15);
        cartao.setNumCartao(numCartao);
        Banco banco = bancos.get(randomInterval(0, bancos.size() - 1)); //Banco randomico
        cartao.setBanco(banco);
        Bandeira bandeira = Bandeira.findAll().get(randomInterval(0, Bandeira.count())); //Bandeira randomica
        cartao.setBandeira(bandeira);
        Calendar dataAtivacao = Calendar.getInstance();
        dataAtivacao.set(Calendar.YEAR, 2007);
        cartao.setDataAtivacao(dataAtivacao);
        Calendar dataExpiracao = Calendar.getInstance();
        dataExpiracao.set(Calendar.YEAR, 2009);
        cartao.setDataExpiracao(dataExpiracao);
        cartao.setLimite((double) limiteGerado);
        cliente.getCartoes().add(cartao);
    }

    /**
     * Método para gerar transacoes
     */
    public void gerarTransacoes() {
        for (Cartao cartao : cliente.getCartoes()) {
            //Gera a media de compras mensais
            numComprasGerado = randomInterval(numComprasMin, numComprasMax);
            //Gera a media de utilizacao do limite
            mediaUtilizacaoLimiteGerado = randomInterval((int) (cartao.getLimite() * mediaUtilizacaoLimiteMin), (int) (cartao.getLimite() * mediaUtilizacaoLimiteMax));
            //Gera a média do valor de compras
            mediaValorCompras = (int) (mediaUtilizacaoLimiteGerado / numComprasGerado);

            //Do ano inicial ao final
            for (int ano = periodoInicialAno; ano < periodoFinalAno + 1; ano++) {
                //Do mes inicial ao final no ano final
                int mesInicial = 1;
                int mesFinal = 12;
                if (ano == periodoInicialAno) {
                    mesInicial = periodoInicialMes;
                }
                if (ano == periodoFinalAno) {
                    mesFinal = periodoFinalMes;
                }

                for (int mes = mesInicial - 1; mes < mesFinal; mes++) {
                    int limiteUtilizado = 0;
                    //Gera o limite que será utilizado no mes corrente, levando em conta o média de limite utilizado e o desvio
                    int limiteMaximoUtilizadoMesAtual = randomInterval((int) (mediaUtilizacaoLimiteGerado * desvioUtilizacaoLimiteMin), (int) (mediaUtilizacaoLimiteGerado * desvioUtilizacaoLimiteMax));
                    while (limiteUtilizado < limiteMaximoUtilizadoMesAtual) { //Enquanto não alcançar o limite gerado para o mes
                        //Gera o valor da compra atual, levando em conta a média de valor de compras e o desvio do valor
                        int valorcompra = randomInterval((int) (mediaValorCompras * desvioMediaCompraMin), (int) (mediaValorCompras * desvioMediaCompraMax));
                        if ((limiteUtilizado + valorcompra) > limiteMaximoUtilizadoMesAtual) {
                            break; //Se vai ultrapassar o limite, para
                        }
                        limiteUtilizado += valorcompra;
                        Transacao transacao = new Transacao();
                        transacao.setCartao(cartao);
                        transacao.setTerminalPOS(proximoTerminal());
                        transacao.setIdOrdem(randomInterval(1, 10000));
                        transacao.setValorTotal(valorcompra);

                        //Seta a Data e Hora da transacao
                        Calendar dhTransacao = Calendar.getInstance();
                        dhTransacao.set(Calendar.DAY_OF_MONTH, randomInterval(1, dhTransacao.getActualMaximum(Calendar.DAY_OF_MONTH)));
                        dhTransacao.set(Calendar.MONTH, mes); //Mês
                        dhTransacao.set(Calendar.YEAR, ano); //Ano
                        dhTransacao.set(Calendar.HOUR_OF_DAY, randomInterval(8, 20)); //Das 8H -  20H
                        dhTransacao.set(Calendar.MINUTE, randomInterval(0, 59));
                        transacao.setDhTransacao(dhTransacao);

                        transacao.processar();
                        cartao.getTransacoes().add(transacao);
                        transacoesGeradas++;
                    }

                }


            }
        }

    }

    private TerminalPOS proximoTerminal(){
        //Se a lista estiver zerada, carrega a lista
        if(terminais.size() == 0){
            if(terminais2 == null){
                //Se nunca foi criada lista, carrega
                carregaListaProbabilisticaTerminais();
            } else {
                //Senão, reaproveita a lista carregada anteriormente
                for (TerminalPOS terminal : terminais2) {
                    terminais.add(terminal);
                }
            }
        }
        //Retorna o primeiro da fila e remove
        return terminais.remove(0);
    }

    public void carregaListaProbabilisticaTerminais() {
        
        terminais = new ArrayList<TerminalPOS>();

        float fator = 0.5F;//100*0.5 = 50 estabelecimentos

        //Carrega Estabelecimentos na região
        for (int numRamo = 0; numRamo < distribuicao.length; numRamo++) {
            int qtd_terminal_ramo = (int) (distribuicao[numRamo][1] * fator); //Recupera % de compras no ramo
            if (qtd_terminal_ramo > 0) { //Possuirá compras neste ramo?
                RamoAtividade ramo = RamoAtividade.find(distribuicao[numRamo][0]); //Recupera o ramo
                for (int localidade = 2; localidade < distribuicao[0].length; localidade++) {
                    int qtd_terminal_ramo_localidade = (int)((float)qtd_terminal_ramo * ((float)distribuicao[numRamo][localidade]/100)); //Recupera a % de compras na localidade para este ramo
                    if (qtd_terminal_ramo_localidade > 0) { //Possuirá compras na localidade para este ramo?

                        //Seleciona todos os terminais da localidade no ramo de atividade
                        List<TerminalPOS> terminais_ramo_localidade = new ArrayList<TerminalPOS>();
                        switch (localidade) {
                            case 2: { //Cidade
                                terminais_ramo_localidade = TerminalPOS.findByCidadeAndRamoAtividade(cliente.getCidade(),ramo);
                                break;
                            }
                            case 3:{ //Regiao
                                terminais_ramo_localidade = TerminalPOS.findByRegiaoAndRamoAtividadeExcluindoCidade(cliente.getCidade(),ramo);
                                break;
                            }
                            case 4:{ //Estado
                                terminais_ramo_localidade = TerminalPOS.findByEstadoAndRamoAtividadeExcluindoRegiao(cliente.getCidade(),ramo);
                                break;
                            }
                            case 5:{ //Fora
                                terminais_ramo_localidade = TerminalPOS.findByRamoAtividadeExcluindoEstado(cliente.getCidade(),ramo);
                                break;
                            }
                        }
                        if (terminais_ramo_localidade.size() != 0) { //Existe terminal neste ramos de atividade nesta localidade? Senão ignora
                            if (terminais_ramo_localidade.size() >= qtd_terminal_ramo_localidade) {//Tem mais terminais do que o necessário?
                                //Se tiver, randomiza a lista e adiciona até completar o necessário
                                Collections.shuffle(terminais_ramo_localidade); //Randomiza
                                terminais.addAll(terminais_ramo_localidade.subList(0, qtd_terminal_ramo_localidade));
                            } else {
                                //Se não tiver, adiciona randomicamente várias vezes até chegar o necessário
                                int qtdAdicionada = 0;
                                while (qtdAdicionada < qtd_terminal_ramo_localidade) {
                                    terminais.add(terminais_ramo_localidade.get(randomInterval(0, terminais_ramo_localidade.size())));
                                    qtdAdicionada++;
                                }
                            }
                        }
                    }

                }
            }
        }
        Collections.shuffle(terminais); //Randomiza
        //Tira uma cópia de terminal para não necessitar montar novamente
        terminais2 = new ArrayList<TerminalPOS>();
        for (TerminalPOS terminal : terminais) {
            terminais2.add(terminal);
        }

    }

    public int randomInterval(int min, int max) {
        return min + (int) ((max - min) * Math.random());
    }
    // </editor-fold>
}
