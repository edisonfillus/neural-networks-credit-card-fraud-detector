package org.ai.agentes;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.ai.agentes.listeners.TreinamentoRedeNeuralListener;
import org.ai.bo.analise.Caso;
import org.ai.bo.neural.rede.Camada;
import org.ai.bo.neural.rede.Neuronio;
import org.ai.bo.neural.rede.RedeNeural;
import org.ai.bo.neural.rede.Sinapse;
import org.ai.bo.neural.rede.StatusRedeNeural;
import org.ai.bo.neural.treino.TipoCriterioParada;
import org.ai.bo.neural.treino.TipoTaxaAprendizagem;
import org.apache.log4j.Logger;

/**
 * Classe Agente Treinador de Rede Neural
 * @author Edison
 */
public class TreinadorRedeNeural implements Runnable {

    private static int QUANTIDADE_EPOCAS_NOTIFICACAO_PROGRESSO = 1;

    // <editor-fold defaultstate="collapsed" desc="Atributos">
    private RedeNeural rede;
    private Logger logger = Logger.getLogger("treinadorLogger"); //Log do TreinadorRedeNeural
    private boolean stopTreinamento;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Propriedades">
    public boolean isStopTreinamento() {
        return stopTreinamento;
    }

    public void setStopTreinamento(boolean stopTreinamento) {
        this.stopTreinamento = stopTreinamento;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Contrutores">
    /**
     * Construtor da classe Agente TreinadorRedeNeural
     * @param rede
     */
    public TreinadorRedeNeural(RedeNeural rede) {
        this.rede = rede;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Eventos">
    private List<TreinamentoRedeNeuralListener> listenersTreinamento = new ArrayList<TreinamentoRedeNeuralListener>();

    /**
     * Adiciona um Listener para receber eventos do treinamento
     * @param listener
     */
    public void addTreinamentoRedeNeuralListener(TreinamentoRedeNeuralListener listener) {
        listenersTreinamento.add(listener);
    }

    /**
     * Remove um Listener para deixar de receber eventos do treinamento
     * @param listener
     */
    public void removeTreinamentoRedeNeuralListener(TreinamentoRedeNeuralListener listener) {
        listenersTreinamento.remove(listener);
    }

    /**
     * Notifica os Listeners com o progresso do treinamento
     * @param index
     */
    private void notifyProgressoTreinamentoAtualizado() {
        for (TreinamentoRedeNeuralListener listener : listenersTreinamento) {
            listener.progressoAtualizado();
        }
    }

    /**
     * Notifica os Listeners que o treinamento foi inicializado
     * @param index
     */
    private void notifyTreinamentoInicializado() {
        for (TreinamentoRedeNeuralListener listener : listenersTreinamento) {
            listener.treinamentoIniciado();
        }
    }

    /**
     * Notifica os Listeners que o treinamento foi finalizado
     * @param index
     */
    private void notifyTreinamentoFinalizado() {
        for (TreinamentoRedeNeuralListener listener : listenersTreinamento) {
            listener.treinamentoFinalizado();
        }
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Métodos">
    /**
     * Método para efetuar o treino da rede
     */
    private void ExecutaTreino() {

        rede.setStatus(StatusRedeNeural.TREINANDO);

        //Inicia o Timer
        final Calendar timer = Calendar.getInstance();

        logger.info("Treinamento da rede " + rede.getNome() + " iniciado.");

        do {
            rede.TesteEpoca(); //Executa uma sessão com os casos de teste
            calcularIndiceAcertosTestes();
            if (rede.getDadosTreinamento().getTipoTaxaAprendizagem() == TipoTaxaAprendizagem.GERENCIADA) {
                gerenciarTaxaAprendizagem(); //Se for gerenciada, então passa para o gerenciador
            } else {
                rede.TreinoEpoca(); //Se não gerenciada, faz um treino normal
            }
            calcularIndiceAcertosTreinamento();
            if (rede.getDadosTreinamento().getEpocas() % QUANTIDADE_EPOCAS_NOTIFICACAO_PROGRESSO == 0) {
                rede.getDadosTreinamento().setTempoDecorrido((Calendar.getInstance().getTimeInMillis() - timer.getTimeInMillis()));
                if (stopTreinamento) {
                    break;
                } else {

                    notifyProgressoTreinamentoAtualizado();
                }
            }


        } while (!atingiuCriterioParada());

        //Troca o status
        rede.setStatus(StatusRedeNeural.TREINADA);
        rede.getDadosTreinamento().setDhUltimoTreinamento(Calendar.getInstance());

        //Registra o tempo decorrido
        rede.getDadosTreinamento().setTempoDecorrido((Calendar.getInstance().getTimeInMillis() - timer.getTimeInMillis()));

        logger.info("Treinamento da rede " + rede.getNome() + " finalizado em " + rede.getDadosTreinamento().getTempoDecorrido() + "ms.");

    }

    /**
     * Método para calcular o indice de acertos nos testes
     */
    private void calcularIndiceAcertosTestes() {
        int qtdAcertos = 0;
        for (Caso casoTeste : rede.getDadosTreinamento().getListCasosTeste()) {
            if (casoTeste.getSaidasEsperadas().get(0) == 0.0F) {
                if (casoTeste.getSaidasRede().get(0) < 0.5F) {
                    qtdAcertos++;
                }
            } else if (casoTeste.getSaidasEsperadas().get(0) == 1.0F) {
                if (casoTeste.getSaidasRede().get(0) > 0.5F) {
                    qtdAcertos++;
                }
            }
        }
        rede.getDadosTreinamento().setIndiceAcertosTestes((double)qtdAcertos/(double)rede.getDadosTreinamento().getListCasosTeste().size());

    }

    /**
     * Método para calcular o indice de acertos nos testes
     */
    private void calcularIndiceAcertosTreinamento() {
        int qtdAcertos = 0;
        for (Caso casoTreinamento : rede.getDadosTreinamento().getListCasosTreinamento()) {
            if (casoTreinamento.getSaidasEsperadas().get(0) == 0.0F) {
                if (casoTreinamento.getSaidasRede().get(0) < 0.5F) {
                    qtdAcertos++;
                }
            } else if (casoTreinamento.getSaidasEsperadas().get(0) == 1.0F) {
                if (casoTreinamento.getSaidasRede().get(0) > 0.5F) {
                    qtdAcertos++;
                }
            }
        }
        rede.getDadosTreinamento().setIndiceAcertosTreinamento((double)qtdAcertos/(double)rede.getDadosTreinamento().getListCasosTreinamento().size());

    }



    private boolean atingiuCriterioParada() {
        switch (rede.getDadosTreinamento().getTipoCriterioParada()) {
            case ERRO_ACEITAVEL:
                return rede.getDadosTreinamento().getErroQuadraticoMedio() <= rede.getDadosTreinamento().getErroAceitavel();
            case NUMERO_EPOCAS:
                return rede.getDadosTreinamento().getEpocas() >= rede.getDadosTreinamento().getMaxEpocas();
            default:
                return true;
        }
    }

    private void gerenciarTaxaAprendizagem() {
        //Captura a taxa e erro atuais.
        double taxaAprendizagemAtual = rede.getDadosTreinamento().getTaxaAprendizagemAtual();
        double erroQuadraticoAtual = rede.getDadosTreinamento().getErroQuadraticoMedio();

        //TODO: ##### Verificar, pois isto desregula a variação ####
        if (taxaAprendizagemAtual > rede.getDadosTreinamento().getTaxaAprendizadoMaxima() / 2) {
            taxaAprendizagemAtual = rede.getDadosTreinamento().getTaxaAprendizadoMaxima() / 2;
        }


        RedeNeural rede_dobro = new RedeNeural();
        rede.transfereConhecimento(rede_dobro);
        RedeNeural rede_meio = new RedeNeural();
        rede.transfereConhecimento(rede_meio);

        //Faz um teste com a taxa de aprendizado atual
        rede.TreinoEpoca();
        rede.TreinoEpoca();
        double erroQuadraticoComTaxaAtual = rede.getDadosTreinamento().getErroQuadraticoMedio();


        //Faz um teste com a metade da taxa de aprendizado
        rede_meio.getDadosTreinamento().setTaxaAprendizado(taxaAprendizagemAtual / 2);
        rede_meio.TreinoEpoca();
        rede_meio.TreinoEpoca();
        double erroQuadraticoComMetadeTaxa = rede_meio.getDadosTreinamento().getErroQuadraticoMedio();


        //Faz um teste com o dobro da taxa de aprendizado
        rede_dobro.getDadosTreinamento().setTaxaAprendizado(taxaAprendizagemAtual * 2);
        rede_dobro.TreinoEpoca();
        rede_dobro.TreinoEpoca();
        double erroQuadraticoComDobroTaxa = rede_dobro.getDadosTreinamento().getErroQuadraticoMedio();

        //Se com o dobro obtiver um aprendizado melhor. usa o dobro
        if (erroQuadraticoComDobroTaxa < erroQuadraticoComTaxaAtual && erroQuadraticoComDobroTaxa < erroQuadraticoComMetadeTaxa) {
            //rede.getDadosTreinamento().setTaxaAprendizado(taxaAprendizagemAtual * 2);
            rede_dobro.transfereConhecimento(rede);

        } else //Se com o dobro obtiver um aprendizado melhor. usa o dobro
        if (erroQuadraticoComMetadeTaxa < erroQuadraticoComTaxaAtual && erroQuadraticoComMetadeTaxa < erroQuadraticoComDobroTaxa) {
            //rede.getDadosTreinamento().setTaxaAprendizado(taxaAprendizagemAtual / 2);
            rede_meio.transfereConhecimento(rede);
        } else {
        }
        if (erroQuadraticoComTaxaAtual > erroQuadraticoAtual) {
            rede_meio.transfereConhecimento(rede);
        }
        DecimalFormat formatErro = new DecimalFormat("0.000000000000");
        if (formatErro.format(erroQuadraticoComTaxaAtual).equals(formatErro.format(erroQuadraticoAtual))) {
            rede.getDadosTreinamento().setReinicio(rede.getDadosTreinamento().getReinicio() + 1);
            Camada camada = rede.getCamadas().get(2);
            for (Neuronio neuronio : camada.getNeuronios()) {
                neuronio.getBias().setPeso(Math.random());
                for (Sinapse sinapse : neuronio.getSinapses()) {
                    sinapse.setPeso(Math.random());
                }
            }

        }

    }

    /**
     * Método para iniciar o TreinadorRedeNeural
     */
    @Override
    public void run() {
        notifyTreinamentoInicializado();
        ExecutaTreino();
        notifyTreinamentoFinalizado();
    }

    // </editor-fold>
}
