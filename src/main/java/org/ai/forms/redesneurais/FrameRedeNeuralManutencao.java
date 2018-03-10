/*
 * FrameTreinamentoRedeNeural.java
 *
 * Created on October 8, 2008, 5:57 PM
 */
package org.ai.forms.redesneurais;

import java.awt.Toolkit;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;

import org.ai.agentes.TreinadorRedeNeural;
import org.ai.agentes.listeners.TreinamentoRedeNeuralListener;
import org.ai.bo.analise.Caso;
import org.ai.bo.neural.rede.RedeNeural;
import org.ai.bo.neural.rede.StatusRedeNeural;
import org.ai.bo.neural.treino.EstatisticaTreinamento;
import org.ai.bo.neural.treino.TaxaAprendizadoAdaptativa;
import org.ai.bo.neural.treino.TipoCriterioParada;
import org.ai.bo.neural.treino.TipoFuncaoAtivacao;
import org.ai.bo.neural.treino.TipoTaxaAprendizagem;
import org.ai.bo.neural.treino.TipoTreino;
import org.ai.forms.FormMenuPrincipal;
import org.ai.forms.redesneurais.grafico.DadosGraficoTreinamento;
import org.ai.forms.redesneurais.grafico.FrameRedeNeuralGraficoAprendizagem;
import org.ai.forms.redesneurais.rn3d.FrameRedeNeuralArquitetura3D;
import org.ai.forms.utils.ColumnResizer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author  Edison
 */
public class FrameRedeNeuralManutencao extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    RedeNeural rede;
    private DefaultTableModel modelTaxaAdaptativa;
    private DefaultTableModel modelEntradasRede;
    private DefaultTableModel modelSaidasRede;
    private DefaultTableModel modelArquitetura;
    private DefaultComboBoxModel modelFuncoesAtivacao;
    private List<TipoFuncaoAtivacao> listFuncoesAtivacao;
    DecimalFormat formatter = new DecimalFormat("##0.0000000000");
    DadosGraficoTreinamento dadosGraficoTreinamento;
    FormMenuPrincipal formPrincipal;
    boolean stopTreinamento = false;
    List<Integer> arquitetura;
    List<TaxaAprendizadoAdaptativa> faixasTaxaAdaptativa;

    /** Creates new form FrameTreinamentoRedeNeural
     * @param formPrincipal
     * @param rede 
     */
    public FrameRedeNeuralManutencao(FormMenuPrincipal formPrincipal, RedeNeural rede) {
        initComponents();
        this.formPrincipal = formPrincipal;
        this.rede = rede;
        btnPararTreinamento.setVisible(false);
        lblNome.setText(rede.getNome());

        updateComboTiposTreinamento();
        
        //Inicializa o combo com os tipos de funções de ativação
        updateComboFuncoesAtivacao();
        for (int i = 0; i < listFuncoesAtivacao.size(); i++) {
            if (listFuncoesAtivacao.get(i).getId() == rede.getDadosTreinamento().getTipoFuncaoAtivacao().getId()) {
                cmbFuncaoAtivacao.setSelectedIndex(i);
            }
        }
        //Inicializa as tabelas com os casos de treinamento e teste
        updateTableCasosTreinamento();
        updateTableCasosTeste();

        if (rede.getStatus() == StatusRedeNeural.INICIADA) { //Se não tiver arquitetura, define a padrão
            arquitetura = new ArrayList<Integer>();
            arquitetura.add(rede.getDadosTreinamento().getListCasosTreinamento().get(0).getEntradas().size());
            arquitetura.add(rede.getDadosTreinamento().getListCasosTreinamento().get(0).getEntradas().size() * 3);
            arquitetura.add(rede.getDadosTreinamento().getListCasosTreinamento().get(0).getSaidasEsperadas().size());
            chkBias.setSelected(true);
            btnTreinar.setEnabled(false);
            btnExibir3D.setEnabled(false);
        } else { //Se tiver, seta os spinners
            arquitetura = rede.getArquitetura();
            chkBias.setSelected(rede.isComBias());
        }
        updateTableArquitetura();

        if (rede.getStatus() == StatusRedeNeural.TREINADA) {
            txtTaxaAprendizadoAtual.setText(formatter.format(rede.getDadosTreinamento().getTaxaAprendizado()));
            txtErroMedio.setText(formatter.format(rede.getDadosTreinamento().getErroQuadraticoMedio()));
            txtEpocas.setText(Integer.toString(rede.getDadosTreinamento().getEpocas()));
            txtTempoDecorrido.setText(Long.toString(rede.getDadosTreinamento().getTempoDecorrido()));
            txtIndiceAcertosTreinamento.setText(Integer.toString((int)(rede.getDadosTreinamento().getIndiceAcertosTreinamento() *100)));
            txtErroMedioTeste.setText(formatter.format(rede.getDadosTreinamento().getErroQuadraticoMedioTestes()));
            txtIndiceAcertosTeste.setText(Integer.toString((int)(rede.getDadosTreinamento().getIndiceAcertosTestes() *100)));
            sldTaxaAprendizagem.setValue((int) (rede.getDadosTreinamento().getTaxaAprendizado() * 100));
            prgTreinamento.setValue(100);
            updateTableEntradasRede();
            updateTableSaidasRede();
        }
        txtStatusTreinamento.setText(rede.getStatus().getNome());

        //Cria os gráficos
        dadosGraficoTreinamento = new DadosGraficoTreinamento(rede.getDadosTreinamento().getEstatisticasTreinamento());

        if(rede.getDadosTreinamento().getReinicio() > 0){
            lblReinicio.setVisible(true);
            lblReinicio.setText(rede.getDadosTreinamento().getReinicio() + " reinicios");
        } else {
            lblReinicio.setVisible(false);
        }

        updateCriterioParada();
        updateTaxaAprendizagem();

    }

    private void updateTaxaAprendizagem() {
        switch (rede.getDadosTreinamento().getTipoTaxaAprendizagem()) {
            case FIXA: {
                btnTaxaAdaptativa.setSelected(false);
                if (rede.getDadosTreinamento().getTaxaAprendizado() > 0.0F) {
                    txtTaxaAprendizagem.setText(formatter.format(rede.getDadosTreinamento().getTaxaAprendizado()));
                } else {
                    txtTaxaAprendizagem.setText("0,01");
                }
                break;
            }
            case GERENCIADA: {
                btnTaxaGerenciada.setSelected(true);
                if (rede.getDadosTreinamento().getTaxaAprendizado() > 0.0F) {
                    txtTaxaAprendizadoInicial.setText(formatter.format(rede.getDadosTreinamento().getTaxaAprendizado()));
                    txtTaxaAprendizagemMaxima.setText(formatter.format(rede.getDadosTreinamento().getTaxaAprendizadoMaxima()));
                } else {
                    txtTaxaAprendizadoInicial.setText("0,01");
                    txtTaxaAprendizagemMaxima.setText("1,00");
                }
                break;
            }
            case ADAPTATIVA: {
                if (rede.getDadosTreinamento().getTaxaAprendizagemAdaptativa() == null){
                    faixasTaxaAdaptativa = new ArrayList<TaxaAprendizadoAdaptativa>();
                    faixasTaxaAdaptativa.add(new TaxaAprendizadoAdaptativa(0.00D, 0.01D));
                } else {
                    faixasTaxaAdaptativa = rede.getDadosTreinamento().getTaxaAprendizagemAdaptativa();
                }
                btnTaxaAdaptativa.setSelected(true);
                updateTableTaxaAprendizagemAdaptativa();
            }
        }
    }

    private void updateCriterioParada() {
        switch (rede.getDadosTreinamento().getTipoCriterioParada()) {
            case ERRO_ACEITAVEL: {
                btnCriterioErroAceitavel.setSelected(true);
                if (rede.getDadosTreinamento().getErroAceitavel() > 0.0F) {
                    txtErroAceitavel.setText(formatter.format(rede.getDadosTreinamento().getErroAceitavel()));
                } else {
                    txtErroAceitavel.setText("0,005");
                }
                break;
            }
            case NUMERO_EPOCAS: {
                btnCriterioQtdEpocas.setSelected(true);
                if (rede.getDadosTreinamento().getMaxEpocas() > 0) {
                    txtQtdEpocasParada.setText(Integer.toString(rede.getDadosTreinamento().getMaxEpocas()));
                } else {
                    txtQtdEpocasParada.setText("1000");
                }
                break;
            }
        }
    }

    private void updateTableTaxaAprendizagemAdaptativa() {
        String[] collumnNames = new String[]{"Erro", "Taxa"};
        String[][] data = new String[rede.getDadosTreinamento().getTaxaAprendizagemAdaptativa().size()][];
        int i = 0;
        for (TaxaAprendizadoAdaptativa taxa : rede.getDadosTreinamento().getTaxaAprendizagemAdaptativa()) {
            data[i] = new String[]{formatter.format(taxa.getErroQuadraticoMedio()), formatter.format(taxa.getTaxaAprendizado())};
            i++;
        }
        modelTaxaAdaptativa = new DefaultTableModel(data, collumnNames);
        tableTaxaAprendizagemAdaptativa.setModel(modelTaxaAdaptativa);
        tableTaxaAprendizagemAdaptativa.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableTaxaAprendizagemAdaptativa.setRowSelectionAllowed(true);
        tableTaxaAprendizagemAdaptativa.setColumnSelectionAllowed(false);
        ColumnResizer.adjustColumnPreferredWidths(tableTaxaAprendizagemAdaptativa);
        tableTaxaAprendizagemAdaptativa.revalidate();
    }

    private void updateTableEntradasRede() {
        String[] collumnNames = new String[]{"Entrada", "Valor"};
        String[][] data = new String[rede.getDadosTreinamento().getLabels().size()][];
        for (int i = 0; i < rede.getDadosTreinamento().getLabels().size(); i++) {
            data[i] = new String[]{
                        rede.getDadosTreinamento().getLabels().get(i),
                        formatter.format(rede.getCamadas().get(0).getNeuronios().get(i).getSaida())
                    };
        }

        modelEntradasRede = new DefaultTableModel(data, collumnNames) {

            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 1) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        tableEntradasRede.setModel(modelEntradasRede);
        tableEntradasRede.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableEntradasRede.setRowSelectionAllowed(true);
        tableEntradasRede.setColumnSelectionAllowed(false);
        ColumnResizer.adjustColumnPreferredWidths(tableCasosTreinamento);
        tableEntradasRede.revalidate();
    }

    private void updateTableSaidasRede() {
        String[] collumnNames = new String[]{"Saida", "Valor"};
        String[][] data = new String[rede.getSaidas().size()][];
        for (int i = 0; i < rede.getSaidas().size(); i++) {
            data[i] = new String[]{
                        "Saída " + (i + 1),
                        formatter.format(rede.getSaidas().get(i))
                    };
        }

        modelSaidasRede = new DefaultTableModel(data, collumnNames) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableSaidasRede.setModel(modelSaidasRede);
        tableSaidasRede.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableSaidasRede.setRowSelectionAllowed(true);
        tableSaidasRede.setColumnSelectionAllowed(false);
        ColumnResizer.adjustColumnPreferredWidths(tableSaidasRede);
        tableSaidasRede.revalidate();
    }

    private void updateComboFuncoesAtivacao() {
        listFuncoesAtivacao = TipoFuncaoAtivacao.findAll();
        String[] data = new String[listFuncoesAtivacao.size()];
        for (int i = 0; i < listFuncoesAtivacao.size(); i++) {
            data[i] = listFuncoesAtivacao.get(i).getNome();
        }
        modelFuncoesAtivacao = new DefaultComboBoxModel(data);
        cmbFuncaoAtivacao.setModel(modelFuncoesAtivacao);
        cmbFuncaoAtivacao.revalidate();
    }

    private void updateComboTiposTreinamento() {
        String[] data = new String[]{TipoTreino.BackPropagation.name()};
        cmbTipoTreinamento.setModel(new DefaultComboBoxModel(data));
        cmbTipoTreinamento.revalidate();
    }

    private void updateTableCasosTreinamento() {
        DecimalFormat formatDouble = new DecimalFormat("0.00");
        //Recupera o primeiro caso para vericar a quantidade de entradas e saidas
        Caso caso1 = rede.getDadosTreinamento().getListCasosTreinamento().get(0);
        String[] collumnNames = new String[caso1.getEntradas().size() + caso1.getSaidasEsperadas().size()];
        for (int i = 0; i < caso1.getEntradas().size(); i++) {
            collumnNames[i] = rede.getDadosTreinamento().getLabels().get(i);
        }
        for (int i = 0; i < caso1.getSaidasEsperadas().size(); i++) {
            collumnNames[i + caso1.getEntradas().size()] = "Saida" + (i + 1);
        }
        String[][] data = new String[rede.getDadosTreinamento().getListCasosTreinamento().size()][];
        for (int i = 0; i < rede.getDadosTreinamento().getListCasosTreinamento().size(); i++) {
            String[] row = new String[collumnNames.length];
            for (int j = 0; j < caso1.getEntradas().size(); j++) {
                row[j] = formatDouble.format(rede.getDadosTreinamento().getListCasosTreinamento().get(i).getEntradas().get(j));
            }
            for (int j = 0; j < caso1.getSaidasEsperadas().size(); j++) {
                row[j + caso1.getEntradas().size()] = formatDouble.format(rede.getDadosTreinamento().getListCasosTreinamento().get(i).getSaidasEsperadas().get(j));
            }
            data[i] = row;
        }
        DefaultTableModel tableModel = new DefaultTableModel(data, collumnNames) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableCasosTreinamento.setModel(tableModel);
        tableCasosTreinamento.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableCasosTreinamento.setRowSelectionAllowed(true);
        tableCasosTreinamento.setColumnSelectionAllowed(false);
        ColumnResizer.adjustColumnPreferredWidths(tableCasosTreinamento);
        tableCasosTreinamento.revalidate();
        lblTotalCasosTreinamento.setText(Integer.toString(rede.getDadosTreinamento().getListCasosTreinamento().size()));
    }

    private void updateTableCasosTeste() {
        DecimalFormat formatDouble = new DecimalFormat("0.00");
        //Recupera o primeiro caso para vericar a quantidade de entradas e saidas
        //Utilizando o caso treinamento pois pode não haver casos de teste
        Caso caso1 = rede.getDadosTreinamento().getListCasosTreinamento().get(0);
        String[] collumnNames = new String[caso1.getEntradas().size() + caso1.getSaidasEsperadas().size()];
        for (int i = 0; i < caso1.getEntradas().size(); i++) {
            collumnNames[i] = rede.getDadosTreinamento().getLabels().get(i);
        }
        for (int i = 0; i < caso1.getSaidasEsperadas().size(); i++) {
            collumnNames[i + caso1.getEntradas().size()] = "Saida" + (i + 1);
        }
        String[][] data = new String[rede.getDadosTreinamento().getListCasosTeste().size()][];
        for (int i = 0; i < rede.getDadosTreinamento().getListCasosTeste().size(); i++) {
            String[] row = new String[collumnNames.length];
            for (int j = 0; j < caso1.getEntradas().size(); j++) {
                row[j] = formatDouble.format(rede.getDadosTreinamento().getListCasosTeste().get(i).getEntradas().get(j));
            }
            for (int j = 0; j < caso1.getSaidasEsperadas().size(); j++) {
                row[j + caso1.getEntradas().size()] = formatDouble.format(rede.getDadosTreinamento().getListCasosTeste().get(i).getSaidasEsperadas().get(j));
            }
            data[i] = row;
        }
        DefaultTableModel tableModel = new DefaultTableModel(data, collumnNames) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableCasosTeste.setModel(tableModel);
        tableCasosTeste.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableCasosTeste.setRowSelectionAllowed(true);
        tableCasosTeste.setColumnSelectionAllowed(false);
        ColumnResizer.adjustColumnPreferredWidths(tableCasosTeste);
        tableCasosTeste.revalidate();
        lblTotalCasosTeste.setText(Integer.toString(rede.getDadosTreinamento().getListCasosTeste().size()));
    }

    private void updateTableArquitetura() {

        String[] collumnNames = new String[]{"Camada", "Qtd"};
        String[][] data = new String[arquitetura.size()][];
        int i = 0;
        data[i] = new String[]{"Entrada", arquitetura.get(i).toString()};
        for (i = 1; i < arquitetura.size() - 1; i++) {
            data[i] = new String[]{
                        "Oculta " + i,
                        arquitetura.get(i).toString()
                    };
        }

        data[i] = new String[]{"Saída", arquitetura.get(i).toString()};

        modelArquitetura = new DefaultTableModel(data, collumnNames) {

            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 1 && row != 0 && row != arquitetura.size() - 1) {
                    return true; //Se for a quantidade de neuronios da camada intermediaria
                } else {
                    return false;
                }
            }
        };

        tableArquitetura.setModel(modelArquitetura);
        tableArquitetura.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableArquitetura.setRowSelectionAllowed(true);
        tableArquitetura.setColumnSelectionAllowed(false);
        ColumnResizer.adjustColumnPreferredWidths(tableArquitetura);
        tableArquitetura.revalidate();
        if (arquitetura.size() == 3) {
            btnExcluirCamada.setEnabled(false);
        }

    }

    private void registraEstatisticaTreinamento() {
        EstatisticaTreinamento estatistica = new EstatisticaTreinamento();
        estatistica.setEpoca(rede.getDadosTreinamento().getEpocas());
        estatistica.setErroQuadraticoMedio(rede.getDadosTreinamento().getErroQuadraticoMedio());
        estatistica.setTaxaAprendizagem(rede.getDadosTreinamento().getTaxaAprendizagemAtual());
        rede.getDadosTreinamento().getEstatisticasTreinamento().add(estatistica);
    }

    private void updateProgressoTreinamento() {
        txtEpocas.setText(Integer.toString(rede.getDadosTreinamento().getEpocas()));
        txtErroMedio.setText(formatter.format(rede.getDadosTreinamento().getErroQuadraticoMedio()));
        txtTaxaAprendizadoAtual.setText(formatter.format(rede.getDadosTreinamento().getTaxaAprendizagemAtual()));
        sldTaxaAprendizagem.setValue((int) (rede.getDadosTreinamento().getTaxaAprendizagemAtual() * 100));
        txtIndiceAcertosTreinamento.setText(Integer.toString((int)(rede.getDadosTreinamento().getIndiceAcertosTreinamento() *100)));
        txtErroMedioTeste.setText(formatter.format(rede.getDadosTreinamento().getErroQuadraticoMedioTestes()));
        txtIndiceAcertosTeste.setText(Integer.toString((int)(rede.getDadosTreinamento().getIndiceAcertosTestes() *100)));
        //int progresso = (int) (((1/rede.getDadosTreinamento().getErroQuadraticoMedio()) / (1 / rede.getDadosTreinamento().getErroAceitavel())) * 100);
        int progresso = (int) ((rede.getDadosTreinamento().getErroAceitavel() / rede.getDadosTreinamento().getErroQuadraticoMedio()) * 100);
        if (progresso > 0) {
            //Captura a ultima estatistica
            registraEstatisticaTreinamento();
            dadosGraficoTreinamento.addValue(rede.getDadosTreinamento().getEstatisticasTreinamento().get(rede.getDadosTreinamento().getEstatisticasTreinamento().size() - 1));
            dadosGraficoTreinamento.updateChart();
            if (prgTreinamento.isIndeterminate()) {
                prgTreinamento.setIndeterminate(false);
                prgTreinamento.setString(null);
            }
            prgTreinamento.setValue(progresso);
        }
        if(rede.getDadosTreinamento().getReinicio() > 0){
            lblReinicio.setVisible(true);
            lblReinicio.setText(rede.getDadosTreinamento().getReinicio() + " reinicios");
        } else {
            lblReinicio.setVisible(false);
        }

        txtTempoDecorrido.setText(String.valueOf(rede.getDadosTreinamento().getTempoDecorrido()));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grpBtnTaxaAprendizado = new javax.swing.ButtonGroup();
        grpBtnCriterioParada = new javax.swing.ButtonGroup();
        tabPanel = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cmbFuncaoAtivacao = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        cmbTipoTreinamento = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        chkBias = new javax.swing.JCheckBox();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableArquitetura = new javax.swing.JTable();
        btnInserirCamada = new javax.swing.JButton();
        btnExcluirCamada = new javax.swing.JButton();
        btnTaxaFixa = new javax.swing.JRadioButton();
        btnTaxaAdaptativa = new javax.swing.JRadioButton();
        jLabel7 = new javax.swing.JLabel();
        txtTaxaAprendizagemMaxima = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtTaxaAprendizagem = new javax.swing.JTextField();
        btnTaxaGerenciada = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        txtTaxaAprendizadoInicial = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableTaxaAprendizagemAdaptativa = new javax.swing.JTable();
        btnRemoverTaxaAdaptativa = new javax.swing.JButton();
        btnAdicionarTaxaAdaptativa = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableCasosTreinamento = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        lblTotalCasosTreinamento = new javax.swing.JLabel();
        lblNome = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tableCasosTeste = new javax.swing.JTable();
        jLabel16 = new javax.swing.JLabel();
        lblTotalCasosTeste = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtTempoDecorrido = new javax.swing.JTextField();
        txtEpocas = new javax.swing.JTextField();
        txtTaxaAprendizadoAtual = new javax.swing.JTextField();
        txtErroMedio = new javax.swing.JTextField();
        prgTreinamento = new javax.swing.JProgressBar();
        jLabel13 = new javax.swing.JLabel();
        sldTaxaAprendizagem = new javax.swing.JSlider();
        jLabel14 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtIndiceAcertosTreinamento = new javax.swing.JTextField();
        lblReinicio = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        txtErroAceitavel = new javax.swing.JTextField();
        btnCriterioErroAceitavel = new javax.swing.JRadioButton();
        btnCriterioQtdEpocas = new javax.swing.JRadioButton();
        txtQtdEpocasParada = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        txtErroMedioTeste = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtIndiceAcertosTeste = new javax.swing.JTextField();
        btnPararTreinamento = new javax.swing.JButton();
        panelAnalise = new javax.swing.JPanel();
        panelEntradasRede = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableEntradasRede = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableSaidasRede = new javax.swing.JTable();
        btnAnalisar = new javax.swing.JButton();
        txtFechar = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        txtStatusTreinamento = new javax.swing.JTextField();
        btnCriarArquitetura = new javax.swing.JButton();
        btnExibirGrafico = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnExibir3D = new javax.swing.JButton();
        btnTreinar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Manutenção de Rede Neural");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Node.png"))); // NOI18N

        jLabel3.setText("Função Ativação:");

        cmbFuncaoAtivacao.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbFuncaoAtivacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbFuncaoAtivacaoActionPerformed(evt);
            }
        });

        jLabel4.setText("Tipo Treinamento:");

        cmbTipoTreinamento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Arquitetura"));

        chkBias.setText("Utilizar Bias");

        tableArquitetura.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(tableArquitetura);

        btnInserirCamada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Symbol Add.png"))); // NOI18N
        btnInserirCamada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInserirCamadaActionPerformed(evt);
            }
        });

        btnExcluirCamada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Symbol Restricted.png"))); // NOI18N
        btnExcluirCamada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirCamadaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnExcluirCamada, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnInserirCamada, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(chkBias))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(chkBias)
                .addGap(8, 8, 8)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnInserirCamada)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExcluirCamada)
                        .addContainerGap(98, Short.MAX_VALUE))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)))
        );

        grpBtnTaxaAprendizado.add(btnTaxaFixa);
        btnTaxaFixa.setSelected(true);
        btnTaxaFixa.setText("Taxa Aprendizagem Fixa");
        btnTaxaFixa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaxaFixaActionPerformed(evt);
            }
        });

        grpBtnTaxaAprendizado.add(btnTaxaAdaptativa);
        btnTaxaAdaptativa.setText("Taxa Aprendizagem Adaptativa");
        btnTaxaAdaptativa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaxaAdaptativaActionPerformed(evt);
            }
        });
        btnTaxaAdaptativa.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                btnTaxaAdaptativaPropertyChange(evt);
            }
        });

        jLabel7.setText("Taxa Maxima:");

        jLabel2.setText("Taxa Fixa:");

        txtTaxaAprendizagem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTaxaAprendizagemActionPerformed(evt);
            }
        });

        grpBtnTaxaAprendizado.add(btnTaxaGerenciada);
        btnTaxaGerenciada.setText("Taxa Aprendizagem Gerenciada");
        btnTaxaGerenciada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaxaGerenciadaActionPerformed(evt);
            }
        });

        jLabel6.setText("Taxa Inicial:");

        tableTaxaAprendizagemAdaptativa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Taxa", "Erro"
            }
        ));
        jScrollPane1.setViewportView(tableTaxaAprendizagemAdaptativa);

        btnRemoverTaxaAdaptativa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Symbol Restricted.png"))); // NOI18N
        btnRemoverTaxaAdaptativa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoverTaxaAdaptativaActionPerformed(evt);
            }
        });

        btnAdicionarTaxaAdaptativa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Symbol Add.png"))); // NOI18N
        btnAdicionarTaxaAdaptativa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarTaxaAdaptativaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbFuncaoAtivacao, 0, 154, Short.MAX_VALUE)
                            .addComponent(cmbTipoTreinamento, 0, 154, Short.MAX_VALUE)))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnTaxaAdaptativa, 0, 0, Short.MAX_VALUE)
                    .addComponent(btnTaxaFixa, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                    .addComponent(btnTaxaGerenciada, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTaxaAprendizagemMaxima)
                            .addComponent(txtTaxaAprendizadoInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTaxaAprendizagem, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnRemoverTaxaAdaptativa, 0, 0, Short.MAX_VALUE)
                            .addComponent(btnAdicionarTaxaAdaptativa, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnTaxaFixa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtTaxaAprendizagem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTaxaGerenciada)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtTaxaAprendizadoInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtTaxaAprendizagemMaxima, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTaxaAdaptativa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnAdicionarTaxaAdaptativa)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnRemoverTaxaAdaptativa)
                                .addGap(51, 51, 51))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(cmbFuncaoAtivacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(cmbTipoTreinamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(29, 29, 29))
        );

        tabPanel.addTab("Arquitetura", jPanel1);

        tableCasosTreinamento.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tableCasosTreinamento);

        jLabel8.setText("Total de Casos Treinamento:");

        lblTotalCasosTreinamento.setText("total");

        lblNome.setText("nome");

        tableCasosTeste.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane6.setViewportView(tableCasosTeste);

        jLabel16.setText("Total de Casos Teste:");

        lblTotalCasosTeste.setText("total");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblNome)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 264, Short.MAX_VALUE)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotalCasosTreinamento))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotalCasosTeste)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalCasosTreinamento)
                    .addComponent(jLabel8)
                    .addComponent(lblNome, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalCasosTeste)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                .addGap(11, 11, 11))
        );

        tabPanel.addTab("Casos", jPanel2);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Progresso"));

        jLabel9.setText("Erro Médio:");

        jLabel10.setText("Épocas:");

        jLabel11.setText("Taxa de Aprendizado:");

        jLabel12.setText("Tempo Decorrido:");

        txtTempoDecorrido.setEditable(false);

        txtEpocas.setEditable(false);

        txtTaxaAprendizadoAtual.setEditable(false);

        txtErroMedio.setEditable(false);

        prgTreinamento.setStringPainted(true);

        jLabel13.setText("0");

        sldTaxaAprendizagem.setMajorTickSpacing(50);
        sldTaxaAprendizagem.setMinorTickSpacing(25);
        sldTaxaAprendizagem.setPaintTicks(true);
        sldTaxaAprendizagem.setValue(0);
        sldTaxaAprendizagem.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                sldTaxaAprendizagemPropertyChange(evt);
            }
        });

        jLabel14.setText("1");

        jLabel5.setText("% Acertos:");

        txtIndiceAcertosTreinamento.setEditable(false);

        lblReinicio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/alert.png"))); // NOI18N
        lblReinicio.setText("0 reinicios");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel9)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtIndiceAcertosTreinamento)
                    .addComponent(txtTempoDecorrido)
                    .addComponent(txtTaxaAprendizadoAtual)
                    .addComponent(txtEpocas)
                    .addComponent(txtErroMedio, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(prgTreinamento, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblReinicio)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(sldTaxaAprendizagem, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel14))))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txtErroMedio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(txtEpocas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(txtTaxaAprendizadoAtual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(txtTempoDecorrido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(prgTreinamento, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(sldTaxaAprendizagem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtIndiceAcertosTreinamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblReinicio)))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Critério de Parada"));

        txtErroAceitavel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtErroAceitavelActionPerformed(evt);
            }
        });

        grpBtnCriterioParada.add(btnCriterioErroAceitavel);
        btnCriterioErroAceitavel.setSelected(true);
        btnCriterioErroAceitavel.setText("Erro Aceitável");
        btnCriterioErroAceitavel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCriterioErroAceitavelActionPerformed(evt);
            }
        });

        grpBtnCriterioParada.add(btnCriterioQtdEpocas);
        btnCriterioQtdEpocas.setText("Qtd Épocas");
        btnCriterioQtdEpocas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCriterioQtdEpocasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCriterioQtdEpocas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCriterioErroAceitavel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtErroAceitavel, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(txtQtdEpocasParada, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCriterioErroAceitavel)
                    .addComponent(txtErroAceitavel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCriterioQtdEpocas)
                    .addComponent(txtQtdEpocasParada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Testes"));

        txtErroMedioTeste.setEditable(false);

        jLabel17.setText("Erro Médio:");

        jLabel1.setText("% Acertos:");

        txtIndiceAcertosTeste.setEditable(false);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtIndiceAcertosTeste, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                    .addComponent(txtErroMedioTeste, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txtErroMedioTeste, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtIndiceAcertosTeste, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        btnPararTreinamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Node Error.png"))); // NOI18N
        btnPararTreinamento.setText("Parar Treinamento");
        btnPararTreinamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPararTreinamentoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnPararTreinamento, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, 0, 79, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPararTreinamento)
                .addGap(35, 35, 35))
        );

        tabPanel.addTab("Treinamento", jPanel6);

        panelEntradasRede.setBorder(javax.swing.BorderFactory.createTitledBorder("Entradas"));

        tableEntradasRede.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Entrada", "Valor"
            }
        ));
        jScrollPane3.setViewportView(tableEntradasRede);

        javax.swing.GroupLayout panelEntradasRedeLayout = new javax.swing.GroupLayout(panelEntradasRede);
        panelEntradasRede.setLayout(panelEntradasRedeLayout);
        panelEntradasRedeLayout.setHorizontalGroup(
            panelEntradasRedeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
        );
        panelEntradasRedeLayout.setVerticalGroup(
            panelEntradasRedeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Saídas"));

        tableSaidasRede.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Saída", "Valor"
            }
        ));
        jScrollPane4.setViewportView(tableSaidasRede);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
        );

        btnAnalisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Node Warning.png"))); // NOI18N
        btnAnalisar.setText("Analisar");
        btnAnalisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnalisarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAnaliseLayout = new javax.swing.GroupLayout(panelAnalise);
        panelAnalise.setLayout(panelAnaliseLayout);
        panelAnaliseLayout.setHorizontalGroup(
            panelAnaliseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAnaliseLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelEntradasRede, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAnaliseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAnalisar))
                .addContainerGap())
        );
        panelAnaliseLayout.setVerticalGroup(
            panelAnaliseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAnaliseLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAnaliseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAnaliseLayout.createSequentialGroup()
                        .addComponent(panelEntradasRede, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(panelAnaliseLayout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAnalisar)
                        .addGap(21, 21, 21))))
        );

        tabPanel.addTab("Análise", panelAnalise);

        txtFechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Symbol Delete 2.png"))); // NOI18N
        txtFechar.setText("Fechar");
        txtFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFecharActionPerformed(evt);
            }
        });

        btnSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Node Check.png"))); // NOI18N
        btnSalvar.setText("Salvar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        jLabel15.setText("Status");

        txtStatusTreinamento.setEditable(false);

        btnCriarArquitetura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Node Add.png"))); // NOI18N
        btnCriarArquitetura.setText("Criar Arquitetura");
        btnCriarArquitetura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCriarArquiteturaActionPerformed(evt);
            }
        });

        btnExibirGrafico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Chart Line.png"))); // NOI18N
        btnExibirGrafico.setText("Gráfico");
        btnExibirGrafico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExibirGraficoActionPerformed(evt);
            }
        });

        btnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Node Remove.png"))); // NOI18N
        btnExcluir.setText("Excluir");
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        btnExibir3D.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/3d_modeling_16.png"))); // NOI18N
        btnExibir3D.setText("3D");
        btnExibir3D.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExibir3DActionPerformed(evt);
            }
        });

        btnTreinar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Node Configuration.png"))); // NOI18N
        btnTreinar.setText("Treinar");
        btnTreinar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTreinarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tabPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnCriarArquitetura)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTreinar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExibir3D)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExibirGrafico)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFechar))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtStatusTreinamento, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExcluir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSalvar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSalvar)
                    .addComponent(btnExcluir)
                    .addComponent(jLabel15)
                    .addComponent(txtStatusTreinamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnExibirGrafico)
                    .addComponent(txtFechar)
                    .addComponent(btnExibir3D)
                    .addComponent(btnTreinar)
                    .addComponent(btnCriarArquitetura))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void txtFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFecharActionPerformed
    dispose();
}//GEN-LAST:event_txtFecharActionPerformed

private void btnExibirGraficoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExibirGraficoActionPerformed
    FrameRedeNeuralGraficoAprendizagem frame = new FrameRedeNeuralGraficoAprendizagem(dadosGraficoTreinamento);
    formPrincipal.getDesktopPane().add(frame);
    frame.setSize(this.getWidth(), this.getHeight());
    frame.setLocation((int) this.getBounds().getX() + this.getWidth(), (int) this.getBounds().getY());
    frame.setVisible(true);
}//GEN-LAST:event_btnExibirGraficoActionPerformed

private void btnTreinarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTreinarActionPerformed
    btnTreinar.setEnabled(false);
    btnCriarArquitetura.setEnabled(false);
    btnPararTreinamento.setVisible(true);
    if (btnCriterioErroAceitavel.isSelected()) {
        try {
            rede.getDadosTreinamento().setTipoCriterioParada(TipoCriterioParada.ERRO_ACEITAVEL);
            rede.getDadosTreinamento().setErroAceitavel(formatter.parse(txtErroAceitavel.getText()).doubleValue());
        } catch (ParseException ex) {
        	LogManager.getLogger(FrameRedeNeuralManutencao.class.getName()).log(Level.ERROR,  ex);
        }
    } else {
        rede.getDadosTreinamento().setTipoCriterioParada(TipoCriterioParada.NUMERO_EPOCAS);
        rede.getDadosTreinamento().setMaxEpocas(Integer.parseInt(txtQtdEpocasParada.getText()));
    }
    if (btnTaxaAdaptativa.isSelected()) {
        rede.getDadosTreinamento().setTipoTaxaAprendizagem(TipoTaxaAprendizagem.ADAPTATIVA);
        rede.getDadosTreinamento().getTaxaAprendizagemAdaptativa().clear();
        for (int i = 0; i < modelTaxaAdaptativa.getRowCount(); i++) {
            try {
                TaxaAprendizadoAdaptativa taxa = new TaxaAprendizadoAdaptativa();
                taxa.setErroQuadraticoMedio(formatter.parse(modelTaxaAdaptativa.getValueAt(i, 0).toString()).doubleValue());
                taxa.setTaxaAprendizado(formatter.parse(modelTaxaAdaptativa.getValueAt(i, 1).toString()).doubleValue());
                rede.getDadosTreinamento().getTaxaAprendizagemAdaptativa().add(taxa);
            } catch (ParseException ex) {
                LogManager.getLogger(FrameRedeNeuralManutencao.class.getName()).log(Level.ERROR, ex);
            }
        }
    } else if (btnTaxaGerenciada.isSelected()) {
        rede.getDadosTreinamento().setTipoTaxaAprendizagem(TipoTaxaAprendizagem.GERENCIADA);
        try {
            rede.getDadosTreinamento().setTaxaAprendizado(formatter.parse(txtTaxaAprendizadoInicial.getText()).doubleValue());
            rede.getDadosTreinamento().setTaxaAprendizadoMaxima(formatter.parse(txtTaxaAprendizagemMaxima.getText()).doubleValue());
        } catch (ParseException ex) {
        	LogManager.getLogger(FrameRedeNeuralManutencao.class.getName()).log(Level.ERROR, ex);
        }
    } else {
        rede.getDadosTreinamento().setTipoTaxaAprendizagem(TipoTaxaAprendizagem.FIXA);
        try {
            rede.getDadosTreinamento().setTaxaAprendizado(formatter.parse(txtTaxaAprendizagem.getText()).doubleValue());
        } catch (ParseException ex) {
            LogManager.getLogger(FrameRedeNeuralManutencao.class.getName()).log(Level.ERROR,  ex);
        }
    }



    prgTreinamento.setIndeterminate(true);

    final TreinadorRedeNeural treinador = new TreinadorRedeNeural(rede);
    treinador.addTreinamentoRedeNeuralListener(new TreinamentoRedeNeuralListener() {

        public void progressoAtualizado() {

            updateProgressoTreinamento();

            if (stopTreinamento) {
                treinador.setStopTreinamento(true);
            }
        }

        public void treinamentoIniciado() {
            txtStatusTreinamento.setText(rede.getStatus().getNome());
            btnTreinar.setEnabled(false);
            prgTreinamento.setString("Treinando...");
            prgTreinamento.setValue(0);
        }

        public void treinamentoFinalizado() {
            //Implementado com invokeLater para liberar a thread treinamento
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {

                    btnPararTreinamento.setVisible(false);
                    stopTreinamento = false;
                    txtStatusTreinamento.setText(rede.getStatus().getNome());
                    btnTreinar.setEnabled(true);
                    btnCriarArquitetura.setEnabled(true);
                    updateTableEntradasRede();
                    updateTableSaidasRede();
                    Toolkit.getDefaultToolkit().beep();//Emite um beep
                    JOptionPane.showMessageDialog(null, "Treinamento Finalizado " +
                            "\n Erro Médio: \n        " + formatter.format(rede.getDadosTreinamento().getErroQuadraticoMedio()) +
                            "\n Tempo Decorrido: \n\t        " + rede.getDadosTreinamento().getTempoDecorrido() + "ms" +
                            "\n Numero de Épocas: \n\t        " + rede.getDadosTreinamento().getEpocas());
                    updateProgressoTreinamento();
                }
            });



        }
    });
    Thread threadTreinamento = new Thread(treinador);
    threadTreinamento.setPriority(Thread.MIN_PRIORITY);
    threadTreinamento.setName("Treinador");
    threadTreinamento.start();

}//GEN-LAST:event_btnTreinarActionPerformed

private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
    int resposta = JOptionPane.showConfirmDialog(this, "Salvar alterações da Rede Neural?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    if (resposta == JOptionPane.YES_OPTION) {
        boolean updated = rede.update();
        if (updated) {
            JOptionPane.showMessageDialog(this, "Rede Neural salva com sucesso");
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao Salvar a Rede Neural");
        }
    }
}//GEN-LAST:event_btnSalvarActionPerformed

private void btnCriarArquiteturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCriarArquiteturaActionPerformed
    arquitetura.clear();
    for (int i = 0; i < modelArquitetura.getRowCount(); i++) {
        arquitetura.add(Integer.parseInt(modelArquitetura.getValueAt(i, 1).toString()));
    }

    rede.CriarArquitetura(arquitetura, chkBias.isSelected());
    rede.setStatus(StatusRedeNeural.ARQUITETURA_CRIADA);
    txtStatusTreinamento.setText(rede.getStatus().getNome());
    btnTreinar.setEnabled(true);
    btnExibir3D.setEnabled(true);
    dadosGraficoTreinamento.clear(); //Limpa o gráfico
    rede.getDadosTreinamento().getEstatisticasTreinamento().clear();
    rede.getDadosTreinamento().setEpocas(0);
    rede.getDadosTreinamento().setTempoDecorrido(0);
    rede.getDadosTreinamento().setReinicio(0);
    
}//GEN-LAST:event_btnCriarArquiteturaActionPerformed

private void btnExibir3DActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExibir3DActionPerformed

    FrameRedeNeuralArquitetura3D frame = new FrameRedeNeuralArquitetura3D(rede);
    formPrincipal.getDesktopPane().add(frame);
    frame.setLocation((int) this.getBounds().getX() + this.getWidth(), (int) this.getBounds().getY());
    frame.setVisible(true);

}//GEN-LAST:event_btnExibir3DActionPerformed

private void cmbFuncaoAtivacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbFuncaoAtivacaoActionPerformed
    rede.getDadosTreinamento().setTipoFuncaoAtivacao(listFuncoesAtivacao.get(cmbFuncaoAtivacao.getSelectedIndex()));
}//GEN-LAST:event_cmbFuncaoAtivacaoActionPerformed

private void btnAnalisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnalisarActionPerformed

    List<Double> entradas = new ArrayList<Double>();
    for (int i = 0; i < modelEntradasRede.getRowCount(); i++) {
        try {
            entradas.add(formatter.parse(modelEntradasRede.getValueAt(i, 1).toString()).doubleValue());
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(FrameRedeNeuralManutencao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
    Caso caso = new Caso() {
    };
    caso.setEntradas(entradas);
    rede.PreparaCamadaEntradaParaPulsar(caso);
    rede.Pulsar();
    updateTableSaidasRede();
}//GEN-LAST:event_btnAnalisarActionPerformed

private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
    int resposta = JOptionPane.showConfirmDialog(this, "Confirma exclusão da Rede Neural: " + rede.getNome() + "?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    if (resposta == JOptionPane.YES_OPTION) {
        boolean deleted = rede.delete();
        if (deleted) {
            JOptionPane.showMessageDialog(null, "Rede Neural " + rede.getNome() + " excluida com sucesso");
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao excluir Rede Neural " + rede.getNome());
        }
        dispose();
    }
}//GEN-LAST:event_btnExcluirActionPerformed

private void btnPararTreinamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPararTreinamentoActionPerformed
    stopTreinamento = true;
}//GEN-LAST:event_btnPararTreinamentoActionPerformed

private void txtErroAceitavelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtErroAceitavelActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_txtErroAceitavelActionPerformed

private void txtTaxaAprendizagemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTaxaAprendizagemActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_txtTaxaAprendizagemActionPerformed

private void btnTaxaFixaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaxaFixaActionPerformed
    if (btnTaxaFixa.isSelected()) {
        rede.getDadosTreinamento().setTipoTaxaAprendizagem(TipoTaxaAprendizagem.FIXA);
        if (txtTaxaAprendizagem.getText().equals("")) {
            txtTaxaAprendizagem.setText("0,01");
        }
    }
}//GEN-LAST:event_btnTaxaFixaActionPerformed

private void btnInserirCamadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInserirCamadaActionPerformed
    arquitetura.add(arquitetura.get(arquitetura.size() - 1));
    updateTableArquitetura();
    btnExcluirCamada.setEnabled(true);
}//GEN-LAST:event_btnInserirCamadaActionPerformed

private void btnExcluirCamadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirCamadaActionPerformed
    if (arquitetura.size() > 3) {
        arquitetura.remove(arquitetura.size() - 2);
        updateTableArquitetura();
    }

}//GEN-LAST:event_btnExcluirCamadaActionPerformed

private void btnTaxaGerenciadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaxaGerenciadaActionPerformed
    if (btnTaxaGerenciada.isSelected()) {
        rede.getDadosTreinamento().setTipoTaxaAprendizagem(TipoTaxaAprendizagem.GERENCIADA);
        if (txtTaxaAprendizadoInicial.getText().equals("")) {
            txtTaxaAprendizadoInicial.setText("0,01");
            txtTaxaAprendizagemMaxima.setText("1,00");
        }

    }

}//GEN-LAST:event_btnTaxaGerenciadaActionPerformed

private void btnTaxaAdaptativaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaxaAdaptativaActionPerformed
    if (btnTaxaAdaptativa.isSelected()) {
        if (faixasTaxaAdaptativa == null) {
            faixasTaxaAdaptativa = new ArrayList<TaxaAprendizadoAdaptativa>();
            faixasTaxaAdaptativa.add(new TaxaAprendizadoAdaptativa(0.00D, 0.01D));
        }
        rede.getDadosTreinamento().setTaxaAprendizagemAdaptativa(faixasTaxaAdaptativa);
        updateTableTaxaAprendizagemAdaptativa();
        btnRemoverTaxaAdaptativa.setEnabled(false);
    }
}//GEN-LAST:event_btnTaxaAdaptativaActionPerformed

private void btnAdicionarTaxaAdaptativaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarTaxaAdaptativaActionPerformed
    faixasTaxaAdaptativa.add(new TaxaAprendizadoAdaptativa(0.00D, 0.01D));
    rede.getDadosTreinamento().setTaxaAprendizagemAdaptativa(faixasTaxaAdaptativa);
    updateTableTaxaAprendizagemAdaptativa();
    btnAdicionarTaxaAdaptativa.setEnabled(true);
}//GEN-LAST:event_btnAdicionarTaxaAdaptativaActionPerformed

private void btnRemoverTaxaAdaptativaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoverTaxaAdaptativaActionPerformed
    faixasTaxaAdaptativa.remove(faixasTaxaAdaptativa.size() - 1);
    rede.getDadosTreinamento().setTaxaAprendizagemAdaptativa(faixasTaxaAdaptativa);
    updateTableTaxaAprendizagemAdaptativa();
    if (faixasTaxaAdaptativa.size() == 1) {
        btnAdicionarTaxaAdaptativa.setEnabled(false);
    }
}//GEN-LAST:event_btnRemoverTaxaAdaptativaActionPerformed

private void btnTaxaAdaptativaPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_btnTaxaAdaptativaPropertyChange
}//GEN-LAST:event_btnTaxaAdaptativaPropertyChange

private void btnCriterioErroAceitavelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCriterioErroAceitavelActionPerformed
    if (btnCriterioErroAceitavel.isSelected()) {
        rede.getDadosTreinamento().setTipoCriterioParada(TipoCriterioParada.ERRO_ACEITAVEL);
        if (txtErroAceitavel.getText().equals("")) {
            txtErroAceitavel.setText("0,0005");
        }
    }
}//GEN-LAST:event_btnCriterioErroAceitavelActionPerformed

private void btnCriterioQtdEpocasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCriterioQtdEpocasActionPerformed
    if (btnCriterioQtdEpocas.isSelected()) {
        rede.getDadosTreinamento().setTipoCriterioParada(TipoCriterioParada.NUMERO_EPOCAS);
        if (txtQtdEpocasParada.getText().equals("")) {
            txtQtdEpocasParada.setText("1000");
        }
    }
}//GEN-LAST:event_btnCriterioQtdEpocasActionPerformed

private void sldTaxaAprendizagemPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_sldTaxaAprendizagemPropertyChange
    rede.getDadosTreinamento().setTaxaAprendizado((double)sldTaxaAprendizagem.getValue() / 100);
}//GEN-LAST:event_sldTaxaAprendizagemPropertyChange

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionarTaxaAdaptativa;
    private javax.swing.JButton btnAnalisar;
    private javax.swing.JButton btnCriarArquitetura;
    private javax.swing.JRadioButton btnCriterioErroAceitavel;
    private javax.swing.JRadioButton btnCriterioQtdEpocas;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnExcluirCamada;
    private javax.swing.JButton btnExibir3D;
    private javax.swing.JButton btnExibirGrafico;
    private javax.swing.JButton btnInserirCamada;
    private javax.swing.JButton btnPararTreinamento;
    private javax.swing.JButton btnRemoverTaxaAdaptativa;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JRadioButton btnTaxaAdaptativa;
    private javax.swing.JRadioButton btnTaxaFixa;
    private javax.swing.JRadioButton btnTaxaGerenciada;
    private javax.swing.JButton btnTreinar;
    private javax.swing.JCheckBox chkBias;
    private javax.swing.JComboBox cmbFuncaoAtivacao;
    private javax.swing.JComboBox cmbTipoTreinamento;
    private javax.swing.ButtonGroup grpBtnCriterioParada;
    private javax.swing.ButtonGroup grpBtnTaxaAprendizado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JLabel lblNome;
    private javax.swing.JLabel lblReinicio;
    private javax.swing.JLabel lblTotalCasosTeste;
    private javax.swing.JLabel lblTotalCasosTreinamento;
    private javax.swing.JPanel panelAnalise;
    private javax.swing.JPanel panelEntradasRede;
    private javax.swing.JProgressBar prgTreinamento;
    private javax.swing.JSlider sldTaxaAprendizagem;
    private javax.swing.JTabbedPane tabPanel;
    private javax.swing.JTable tableArquitetura;
    private javax.swing.JTable tableCasosTeste;
    private javax.swing.JTable tableCasosTreinamento;
    private javax.swing.JTable tableEntradasRede;
    private javax.swing.JTable tableSaidasRede;
    private javax.swing.JTable tableTaxaAprendizagemAdaptativa;
    private javax.swing.JTextField txtEpocas;
    private javax.swing.JTextField txtErroAceitavel;
    private javax.swing.JTextField txtErroMedio;
    private javax.swing.JTextField txtErroMedioTeste;
    private javax.swing.JButton txtFechar;
    private javax.swing.JTextField txtIndiceAcertosTeste;
    private javax.swing.JTextField txtIndiceAcertosTreinamento;
    private javax.swing.JTextField txtQtdEpocasParada;
    private javax.swing.JTextField txtStatusTreinamento;
    private javax.swing.JTextField txtTaxaAprendizadoAtual;
    private javax.swing.JTextField txtTaxaAprendizadoInicial;
    private javax.swing.JTextField txtTaxaAprendizagem;
    private javax.swing.JTextField txtTaxaAprendizagemMaxima;
    private javax.swing.JTextField txtTempoDecorrido;
    // End of variables declaration//GEN-END:variables
}
