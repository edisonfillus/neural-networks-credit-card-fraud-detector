/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FrameTransacaoSelecaoTreinamento.java
 *
 * Created on 01/11/2008, 19:56:55
 */
package org.ai.forms.ambiente.transacoes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.ai.bo.ambiente.Transacao;
import org.ai.bo.analise.Caso;
import org.ai.bo.analise.CasoCliente;
import org.ai.forms.FormMenuPrincipal;
import org.ai.forms.utils.ColumnResizer;
import org.jvnet.substance.api.renderers.SubstanceDefaultTableCellRenderer.BooleanRenderer;

/**
 *
 * @author Edison
 */
public class FrameTransacaoSelecaoTreinamento extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;

    private List<Transacao> transacoes;
    private FormMenuPrincipal formPrincipal;

    
    public FrameTransacaoSelecaoTreinamento(final FormMenuPrincipal formPrincipal) {
        initComponents();
        this.formPrincipal = formPrincipal;
        transacoes = Transacao.findAll();
        updateTableCasosTreinamento();
        updateLabels();
        final FrameTransacaoSelecaoTreinamento thisFrame = this;
       
        JMenuItem menuExibir = new JMenuItem("Exibir");
        menuExibir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = tableCasos.getSelectedRow();
                FrameTransacaoManutencao frame = new FrameTransacaoManutencao(transacoes.get(index));
                formPrincipal.getDesktopPane().add(frame);
                frame.setLocation((int) thisFrame.getBounds().getX() + thisFrame.getWidth(), (int) thisFrame.getBounds().getY());
                frame.setVisible(true);
            }
        });
        popup.add(menuExibir);
    }

    private void updateLabels(){
        lblTotalCasos.setText(Integer.toString(transacoes.size()));
        int totalTreinamento = 0;
        int totalTeste = 0;
        for (Transacao transacao : transacoes) {
            if(transacao.isFlagTeste()){
                totalTeste++;
            }
            if(transacao.isFlagTreinamento()){
                totalTreinamento++;
            }
        }
        lblTotalTeste.setText(Integer.toString(totalTeste));
        lblTotalTreinamento.setText(Integer.toString(totalTreinamento));

    }

    private void updateTableCasosTreinamento() {
        DecimalFormat formatDouble = new DecimalFormat("0.00");
        //Recupera o primeiro caso para verificar a quantidade de entradas e saida
        Caso caso1 = new CasoCliente(transacoes.get(0));
        String[] collumnNames = new String[caso1.getEntradas().size() + caso1.getSaidasEsperadas().size() + 2];
        for (int i = 0; i < caso1.getEntradas().size(); i++) {
            collumnNames[i] = CasoCliente.getLabels().get(i);
        }
        for (int i = 0; i < caso1.getSaidasEsperadas().size(); i++) {
            collumnNames[i + caso1.getEntradas().size()] = "Saida" + (i + 1);
        }
        collumnNames[collumnNames.length-2] = "TRN";
        collumnNames[collumnNames.length-1] = "TST";
        Object[][] data = new Object[transacoes.size()][];
        for (int i = 0; i < transacoes.size(); i++) {
            Object[] row = new Object[collumnNames.length];
            Caso caso = new CasoCliente(transacoes.get(i));
            for (int j = 0; j < caso1.getEntradas().size(); j++) {
                row[j] = formatDouble.format(caso.getEntradas().get(j));
            }
            for (int j = 0; j < caso1.getSaidasEsperadas().size(); j++) {
                row[j + caso1.getEntradas().size()] = formatDouble.format(caso.getSaidasEsperadas().get(j));
            }
            row[row.length-2] = transacoes.get(i).isFlagTreinamento();
            row[row.length-1] = transacoes.get(i).isFlagTeste();
            data[i] = row;
        }
        DefaultTableModel tableModel = new DefaultTableModel(data, collumnNames) {

            @Override
            public boolean isCellEditable(int row, int column) {
                if (column > this.getColumnCount() - 3){
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void setValueAt(Object aValue, int row, int column) {
                super.setValueAt(aValue, row, column);
                if(column == getColumnCount() - 2){ //Treinamento
                    transacoes.get(row).setFlagTreinamento((Boolean)aValue);
                    transacoes.get(row).update();
                }
                if(column == getColumnCount() - 1){ //Treinamento
                    transacoes.get(row).setFlagTeste((Boolean)aValue);
                    transacoes.get(row).update();
                }
                updateLabels();
            }


        };

        tableCasos.setModel(tableModel);
        //Define as 2 ultimas como checkbox

        tableCasos.getColumnModel().getColumn(collumnNames.length - 2).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        tableCasos.getColumnModel().getColumn(collumnNames.length - 2).setCellRenderer(new BooleanRenderer());
        tableCasos.getColumnModel().getColumn(collumnNames.length - 1).setCellEditor( new DefaultCellEditor(new JCheckBox()));
        tableCasos.getColumnModel().getColumn(collumnNames.length - 1).setCellRenderer(new BooleanRenderer());
        tableCasos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableCasos.setRowSelectionAllowed(true);
        tableCasos.setColumnSelectionAllowed(false);
        ColumnResizer.adjustColumnPreferredWidths(tableCasos);
        tableCasos.revalidate();
        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popup = new javax.swing.JPopupMenu();
        jLabel1 = new javax.swing.JLabel();
        lblTotalCasos = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblTotalTreinamento = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblTotalTeste = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableCasos = new javax.swing.JTable();
        btnFechar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Seleção de Transações");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/transaction_bank_ok_16.png"))); // NOI18N

        jLabel1.setText("Total de Casos:");

        lblTotalCasos.setText("total");

        jLabel3.setText("Treinamento:");

        lblTotalTreinamento.setText("total");

        jLabel5.setText("Teste:");

        lblTotalTeste.setText("total");

        tableCasos.setModel(new javax.swing.table.DefaultTableModel(
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
        tableCasos.setComponentPopupMenu(popup);
        jScrollPane1.setViewportView(tableCasos);

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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotalCasos)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotalTreinamento)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotalTeste))
                    .addComponent(btnFechar, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblTotalCasos)
                    .addComponent(jLabel3)
                    .addComponent(lblTotalTreinamento)
                    .addComponent(jLabel5)
                    .addComponent(lblTotalTeste))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnFechar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharActionPerformed
        dispose();
    }//GEN-LAST:event_btnFecharActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFechar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTotalCasos;
    private javax.swing.JLabel lblTotalTeste;
    private javax.swing.JLabel lblTotalTreinamento;
    private javax.swing.JPopupMenu popup;
    private javax.swing.JTable tableCasos;
    // End of variables declaration//GEN-END:variables
}
