/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FrameRamoAtividadeConsulta.java
 *
 * Created on Sep 30, 2008, 10:36:32 AM
 */

package org.ai.forms.ambiente.ramos;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.table.DefaultTableModel;

import org.ai.bo.ambiente.Cidade;
import org.ai.bo.ambiente.RamoAtividade;
import org.ai.forms.FormMenuPrincipal;
import org.ai.forms.utils.ColumnResizer;

/**
 *
 * @author Edison
 */
public class FrameRamoAtividadeConsulta extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    
    private List<RamoAtividade> listRamosAtividade;
    private DefaultTableModel modelRamosAtividade;
    Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();
    FormMenuPrincipal formPrincipal;

    
    
    /** Creates new form FrameRamoAtividadeConsulta
     * @param formPrincipal 
     */
    public FrameRamoAtividadeConsulta(FormMenuPrincipal formPrincipal) {
        initComponents();
        this.formPrincipal = formPrincipal;
        updateTableRamosAtividade();
    }
    
    public void updateTableRamosAtividade() {
        updateListRamosAtividade();
        tableRamosAtividade.setModel(modelRamosAtividade);
        tableRamosAtividade.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableRamosAtividade.setRowSelectionAllowed(true);
        tableRamosAtividade.setColumnSelectionAllowed(false);
        ColumnResizer.adjustColumnPreferredWidths(tableRamosAtividade);
        tableRamosAtividade.revalidate();
        lblTotalRamosAtividade.setText(Integer.toString(listRamosAtividade.size()));
    }

    private void updateListRamosAtividade() {
        listRamosAtividade = RamoAtividade.findAll();
        String[] collumnNames = new String[]{"Nome","Estabelecimentos"};
        String[][] data = new String[listRamosAtividade.size()][];
        for (int i = 0; i < listRamosAtividade.size(); i++) {
            data[i] = new String[]{
                        listRamosAtividade.get(i).getNome(),
                        Integer.toString(listRamosAtividade.get(i).getEstabelecimentos().size())    
                    };
        }
        modelRamosAtividade = new DefaultTableModel(data, collumnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tableRamosAtividade = new javax.swing.JTable();
        btnFechar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lblTotalRamosAtividade = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Consulta de Ramo Atividade");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/activity_16.png"))); // NOI18N

        tableRamosAtividade.setModel(new javax.swing.table.DefaultTableModel(
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
        tableRamosAtividade.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableRamosAtividadeMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableRamosAtividade);

        btnFechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Symbol Delete 2.png"))); // NOI18N
        btnFechar.setText("Fechar");
        btnFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFecharActionPerformed(evt);
            }
        });

        jLabel1.setText("Total de Ramos de Atividade:");

        lblTotalRamosAtividade.setText("total");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotalRamosAtividade)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnFechar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFechar)
                    .addComponent(jLabel1)
                    .addComponent(lblTotalRamosAtividade))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharActionPerformed
    dispose();
}//GEN-LAST:event_btnFecharActionPerformed

private void tableRamosAtividadeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableRamosAtividadeMouseClicked
    RamoAtividade ramo = listRamosAtividade.get(tableRamosAtividade.getSelectedRow());
    FrameRamoAtividadeManutencao frame = new FrameRamoAtividadeManutencao(formPrincipal,ramo.getId());
    formPrincipal.getDesktopPane().add(frame);
    frame.setLocation((int) this.getBounds().getX() + this.getWidth(), (int) this.getBounds().getY());
    frame.setVisible(true);
    frame.addInternalFrameListener(new InternalFrameListener() {
        public void internalFrameOpened(InternalFrameEvent e) {}
        public void internalFrameClosing(InternalFrameEvent e) {}
        public void internalFrameClosed(InternalFrameEvent e) {
            updateTableRamosAtividade();
        }
        public void internalFrameIconified(InternalFrameEvent e) {}
        public void internalFrameDeiconified(InternalFrameEvent e) {}
        public void internalFrameActivated(InternalFrameEvent e) {}
        public void internalFrameDeactivated(InternalFrameEvent e) {}
    });
}//GEN-LAST:event_tableRamosAtividadeMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFechar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTotalRamosAtividade;
    private javax.swing.JTable tableRamosAtividade;
    // End of variables declaration//GEN-END:variables

}
