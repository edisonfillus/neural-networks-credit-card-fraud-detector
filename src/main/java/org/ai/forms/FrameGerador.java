/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FrameGerador.java
 *
 * Created on Oct 21, 2008, 12:50:29 PM
 */

package org.ai.forms;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.ai.agentes.GeradorTransacoes;
import org.ai.bo.ambiente.Cliente;
import org.ai.bo.ambiente.RamoAtividade;
import org.ai.forms.utils.ColumnResizer;

/**
 *
 * @author Edison
 */
public class FrameGerador extends javax.swing.JInternalFrame {

    Cliente cliente;
    GeradorTransacoes gerador;
    private DefaultTableModel modelDistribuicao;
    private List<RamoAtividade> ramos;

    
    public FrameGerador(Cliente cliente) {
        initComponents();
        this.cliente = cliente;
        gerador = new GeradorTransacoes(cliente);
        loadParametros();
        updateTableDistribuicao();
    }

    private void updateTableDistribuicao(){
        ramos = RamoAtividade.findAll();
        String[] collumnNames = new String[]{"Ramo Atividade", "%", "Cidade","Região","Estado","Fora"};
        String[][] data = new String[ramos.size()][];
        for (int i = 0; i < ramos.size(); i++) {
            data[i] = new String[]{
                        ramos.get(i).getNome(),
                        Integer.toString(100 / ramos.size()),
                        "70",
                        "15",
                        "10",
                        "5"
                    };
        }
        modelDistribuicao = new DefaultTableModel(data, collumnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if(column == 0){ //Se for a coluna ramo de atividade
                    return false;
                } else {
                    return true;
                }
            }
        };
        tableDistribuicao.setModel(modelDistribuicao);
        tableDistribuicao.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableDistribuicao.setRowSelectionAllowed(true);
        tableDistribuicao.setColumnSelectionAllowed(false);
        ColumnResizer.adjustColumnPreferredWidths(tableDistribuicao);
        tableDistribuicao.revalidate();
    }


    private void loadParametros(){
        spnLimiteMin.setValue((int)(GeradorTransacoes.LIMITE_MIN * 100));
        spnLimiteMax.setValue((int)(GeradorTransacoes.LIMITE_MAX * 100));

        spnUtilizacaoLimiteMin.setValue((int)(GeradorTransacoes.UTILIZACAO_LIMITE_MIN * 100));
        spnUtilizacaoLimiteMax.setValue((int)(GeradorTransacoes.UTILIZACAO_LIMITE_MAX * 100));

        spnDesvioLimiteMin.setValue((int)(GeradorTransacoes.DESVIO_UTILIZACAO_LIMITE_MIN * 100));
        spnDesvioLimiteMax.setValue((int)(GeradorTransacoes.DESVIO_UTILIZACO_LIMITE_MAX * 100));

        spnComprasMesMin.setValue(GeradorTransacoes.COMPRAS_MES_MIN);
        spnComprasMesMax.setValue(GeradorTransacoes.COMPRAS_MES_MAX);
        
        spnDesvioComprasMin.setValue((int)(GeradorTransacoes.DESVIO_MEDIA_COMPRA_MIN * 100));
        spnDesvioComprasMax.setValue((int)(GeradorTransacoes.DESVIO_MEDIA_COMPRA_MAX * 100));

        spnPeriodoInicialMes.setValue(GeradorTransacoes.PERIODO_INICIAL_MES);
        spnPeriodoInicialAno.setValue(GeradorTransacoes.PERIODO_INICIAL_ANO);
        spnPeriodoFinalMes.setValue(GeradorTransacoes.PERIODO_FINAL_MES);
        spnPeriodoFinalAno.setValue(GeradorTransacoes.PERIODO_FINAL_ANO);


    }

    private void capturarParametros(){
        gerador.setLimiteMin(Double.parseDouble(spnLimiteMin.getValue().toString())/100);
        gerador.setLimiteMax(Double.parseDouble(spnLimiteMax.getValue().toString())/100);
        
        gerador.setMediaUtilizacaoLimiteMin(Double.parseDouble(spnUtilizacaoLimiteMin.getValue().toString())/100);
        gerador.setMediaUtilizacaoLimiteMax(Double.parseDouble(spnUtilizacaoLimiteMax.getValue().toString())/100);
        
        gerador.setDesvioUtilizacaoLimiteMin(Double.parseDouble(spnDesvioLimiteMin.getValue().toString())/100);
        gerador.setDesvioUtilizacaoLimiteMax(Double.parseDouble(spnDesvioLimiteMax.getValue().toString())/100);
        
        gerador.setNumComprasMin(Integer.parseInt(spnComprasMesMin.getValue().toString()));
        gerador.setNumComprasMax(Integer.parseInt(spnComprasMesMax.getValue().toString()));
        
        gerador.setDesvioMediaCompraMin(Double.parseDouble(spnDesvioComprasMin.getValue().toString())/100);
        gerador.setDesvioMediaCompraMax(Double.parseDouble(spnDesvioComprasMax.getValue().toString())/100);

        gerador.setPeriodoInicialMes(Integer.parseInt(spnPeriodoInicialMes.getValue().toString()));
        gerador.setPeriodoInicialAno(Integer.parseInt(spnPeriodoInicialAno.getValue().toString()));
        gerador.setPeriodoFinalMes(Integer.parseInt(spnPeriodoFinalMes.getValue().toString()));
        gerador.setPeriodoFinalAno(Integer.parseInt(spnPeriodoFinalAno.getValue().toString()));



        int[][] distribuicao = new int[ramos.size()][];
        //Captura a distribuição
        for (int i = 0; i < ramos.size(); i++) {
            int idRamoAtividade = ramos.get(i).getId();
            int porcentagemRamo = Integer.parseInt(tableDistribuicao.getValueAt(i, 1).toString());
            int porcentagemCidade = (Integer.parseInt(tableDistribuicao.getValueAt(i, 2).toString()));
            int porcentagemRegiao = (Integer.parseInt(tableDistribuicao.getValueAt(i, 3).toString()));
            int porcentagemEstado = (Integer.parseInt(tableDistribuicao.getValueAt(i, 4).toString()));
            int porcentagemFora = (Integer.parseInt(tableDistribuicao.getValueAt(i, 5).toString()));
            distribuicao[i] = new int[]{idRamoAtividade,porcentagemRamo,porcentagemCidade,porcentagemRegiao,porcentagemEstado,porcentagemFora};
        }
        gerador.setDistribuicao(distribuicao);

    }



    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grpBtnLimite = new javax.swing.ButtonGroup();
        grpBtnUtilizacaoLimite = new javax.swing.ButtonGroup();
        grpBtnDesvioLimite = new javax.swing.ButtonGroup();
        grpBtnComprasMensais = new javax.swing.ButtonGroup();
        grpBtnDesvioCompras = new javax.swing.ButtonGroup();
        btnGerarTransacoes = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jRadioButton9 = new javax.swing.JRadioButton();
        jRadioButton10 = new javax.swing.JRadioButton();
        spnDesvioComprasMin = new javax.swing.JSpinner();
        spnDesvioComprasFixo = new javax.swing.JSpinner();
        jLabel23 = new javax.swing.JLabel();
        spnDesvioComprasMax = new javax.swing.JSpinner();
        jRadioButton7 = new javax.swing.JRadioButton();
        jRadioButton8 = new javax.swing.JRadioButton();
        spnComprasMesMin = new javax.swing.JSpinner();
        spnComprasMesFixo = new javax.swing.JSpinner();
        jLabel22 = new javax.swing.JLabel();
        spnComprasMesMax = new javax.swing.JSpinner();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        spnDesvioLimiteMin = new javax.swing.JSpinner();
        jLabel20 = new javax.swing.JLabel();
        spnDesvioLimiteMax = new javax.swing.JSpinner();
        spnDesvioLimiteFixo = new javax.swing.JSpinner();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        spnUtilizacaoLimiteMin = new javax.swing.JSpinner();
        spnUtilizacaoLimiteFixo = new javax.swing.JSpinner();
        jLabel17 = new javax.swing.JLabel();
        spnUtilizacaoLimiteMax = new javax.swing.JSpinner();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        spnLimiteMin = new javax.swing.JSpinner();
        spnLimiteFixo = new javax.swing.JSpinner();
        jLabel15 = new javax.swing.JLabel();
        spnLimiteMax = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        spnPeriodoInicialMes = new javax.swing.JSpinner();
        jLabel11 = new javax.swing.JLabel();
        spnPeriodoInicialAno = new javax.swing.JSpinner();
        jLabel13 = new javax.swing.JLabel();
        spnPeriodoFinalMes = new javax.swing.JSpinner();
        jLabel18 = new javax.swing.JLabel();
        spnPeriodoFinalAno = new javax.swing.JSpinner();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableDistribuicao = new javax.swing.JTable();
        btnFechar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Gerador de Transações");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/transaction_bank_level_16.png"))); // NOI18N

        btnGerarTransacoes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/transaction_bank_level_16.png"))); // NOI18N
        btnGerarTransacoes.setText("Gerar Transações");
        btnGerarTransacoes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGerarTransacoesActionPerformed(evt);
            }
        });

        jLabel14.setText("(% Renda)");

        jLabel12.setText("Limite Total");

        jLabel16.setText("Utilização Limite");

        jLabel19.setText("Desvio Limite");

        jLabel21.setText("Compras Mês");

        jLabel24.setText("Desvio Compras");

        grpBtnDesvioCompras.add(jRadioButton9);
        jRadioButton9.setText("Fixo");
        jRadioButton9.setEnabled(false);

        grpBtnDesvioCompras.add(jRadioButton10);
        jRadioButton10.setSelected(true);
        jRadioButton10.setText("Entre");

        spnDesvioComprasFixo.setEnabled(false);

        jLabel23.setText("e");

        grpBtnComprasMensais.add(jRadioButton7);
        jRadioButton7.setText("Fixo");
        jRadioButton7.setEnabled(false);

        grpBtnComprasMensais.add(jRadioButton8);
        jRadioButton8.setSelected(true);
        jRadioButton8.setText("Entre");

        spnComprasMesFixo.setEnabled(false);

        jLabel22.setText("e");

        grpBtnDesvioLimite.add(jRadioButton5);
        jRadioButton5.setText("Fixo");
        jRadioButton5.setEnabled(false);

        grpBtnDesvioLimite.add(jRadioButton6);
        jRadioButton6.setSelected(true);
        jRadioButton6.setText("Entre");

        jLabel20.setText("e");

        spnDesvioLimiteFixo.setEnabled(false);

        grpBtnUtilizacaoLimite.add(jRadioButton3);
        jRadioButton3.setText("Fixo");
        jRadioButton3.setEnabled(false);
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });

        grpBtnUtilizacaoLimite.add(jRadioButton4);
        jRadioButton4.setSelected(true);
        jRadioButton4.setText("Entre");

        spnUtilizacaoLimiteFixo.setEnabled(false);

        jLabel17.setText("e");

        grpBtnLimite.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Entre");

        grpBtnLimite.add(jRadioButton2);
        jRadioButton2.setText("Fixo");
        jRadioButton2.setEnabled(false);

        spnLimiteMin.setModel(new javax.swing.SpinnerNumberModel());

        spnLimiteFixo.setEnabled(false);

        jLabel15.setText("e");

        spnLimiteMax.setModel(new javax.swing.SpinnerNumberModel());

        jLabel7.setText("(Quantidade)");

        jLabel8.setText("(% Limite)");

        jLabel9.setText("(% Utilizacao)");

        jLabel10.setText("(% Desvio Valor)");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel14)
                    .addComponent(jLabel12)
                    .addComponent(jLabel19)
                    .addComponent(jLabel21)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(2, 2, 2))
                    .addComponent(jLabel10)
                    .addComponent(jLabel24)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jRadioButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jRadioButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jRadioButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jRadioButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jRadioButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jRadioButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jRadioButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jRadioButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jRadioButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jRadioButton9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(spnLimiteFixo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnUtilizacaoLimiteFixo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnDesvioLimiteFixo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnDesvioComprasFixo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(spnDesvioComprasMin, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spnDesvioComprasMax, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                    .addComponent(spnComprasMesFixo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(spnComprasMesMin, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spnComprasMesMax, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(spnLimiteMin, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spnLimiteMax, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(spnUtilizacaoLimiteMin, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spnUtilizacaoLimiteMax, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(spnDesvioLimiteMin, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spnDesvioLimiteMax, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)))
                .addContainerGap(151, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(jRadioButton1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(jRadioButton2)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(spnLimiteMin, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel15)
                        .addComponent(spnLimiteMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(spnLimiteFixo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel16)
                                .addComponent(jRadioButton4))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(jRadioButton3)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel17)
                                .addComponent(spnUtilizacaoLimiteMin, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(spnUtilizacaoLimiteMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(spnUtilizacaoLimiteFixo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel19)
                                .addComponent(jRadioButton6))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel9)
                                    .addComponent(jRadioButton5))))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jRadioButton7)
                                    .addComponent(jLabel7)))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel21)
                                .addComponent(jRadioButton8)))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel24)
                                .addComponent(jRadioButton10))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel10)
                                    .addComponent(jRadioButton9)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel20)
                                .addComponent(spnDesvioLimiteMin, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(spnDesvioLimiteMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(spnDesvioLimiteFixo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(spnComprasMesMin, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spnComprasMesMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(4, 4, 4)
                        .addComponent(spnComprasMesFixo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel23)
                                .addComponent(spnDesvioComprasMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(spnDesvioComprasMin, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addComponent(spnDesvioComprasFixo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Parâmetros", jPanel1);

        jLabel1.setText("Período:");

        jLabel11.setText("/");

        jLabel13.setText("a");

        jLabel18.setText("/");

        tableDistribuicao.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Ramo Atividade", "%", "Cidade", "Região", "Estado", "Fora"
            }
        ));
        jScrollPane1.setViewportView(tableDistribuicao);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spnPeriodoInicialMes, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spnPeriodoInicialAno, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(spnPeriodoFinalMes, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spnPeriodoFinalAno, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(spnPeriodoInicialMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(spnPeriodoInicialAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(spnPeriodoFinalMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(spnPeriodoFinalAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Distribuição", jPanel3);

        btnFechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Symbol Delete 2.png"))); // NOI18N
        btnFechar.setText("Fechar");
        btnFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFecharActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnGerarTransacoes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFechar))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGerarTransacoes)
                    .addComponent(btnFechar))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGerarTransacoesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGerarTransacoesActionPerformed

        capturarParametros();
        gerador.iniciarGeracao();
        cliente.update();
        JOptionPane.showMessageDialog(this, "Transações Geradas com sucesso");
    
}//GEN-LAST:event_btnGerarTransacoesActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharActionPerformed
        dispose();
    }//GEN-LAST:event_btnFecharActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnGerarTransacoes;
    private javax.swing.ButtonGroup grpBtnComprasMensais;
    private javax.swing.ButtonGroup grpBtnDesvioCompras;
    private javax.swing.ButtonGroup grpBtnDesvioLimite;
    private javax.swing.ButtonGroup grpBtnLimite;
    private javax.swing.ButtonGroup grpBtnUtilizacaoLimite;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton10;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JRadioButton jRadioButton8;
    private javax.swing.JRadioButton jRadioButton9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JSpinner spnComprasMesFixo;
    private javax.swing.JSpinner spnComprasMesMax;
    private javax.swing.JSpinner spnComprasMesMin;
    private javax.swing.JSpinner spnDesvioComprasFixo;
    private javax.swing.JSpinner spnDesvioComprasMax;
    private javax.swing.JSpinner spnDesvioComprasMin;
    private javax.swing.JSpinner spnDesvioLimiteFixo;
    private javax.swing.JSpinner spnDesvioLimiteMax;
    private javax.swing.JSpinner spnDesvioLimiteMin;
    private javax.swing.JSpinner spnLimiteFixo;
    private javax.swing.JSpinner spnLimiteMax;
    private javax.swing.JSpinner spnLimiteMin;
    private javax.swing.JSpinner spnPeriodoFinalAno;
    private javax.swing.JSpinner spnPeriodoFinalMes;
    private javax.swing.JSpinner spnPeriodoInicialAno;
    private javax.swing.JSpinner spnPeriodoInicialMes;
    private javax.swing.JSpinner spnUtilizacaoLimiteFixo;
    private javax.swing.JSpinner spnUtilizacaoLimiteMax;
    private javax.swing.JSpinner spnUtilizacaoLimiteMin;
    private javax.swing.JTable tableDistribuicao;
    // End of variables declaration//GEN-END:variables

}
