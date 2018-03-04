/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FrameRegiaoConsulta.java
 *
 * Created on Sep 28, 2008, 7:21:29 PM
 */
package org.ai.forms.ambiente.regioes;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.table.DefaultTableModel;

import org.ai.bo.ambiente.Regiao;
import org.ai.forms.FormMenuPrincipal;
import org.ai.forms.utils.ColumnResizer;

/**
 *
 * @author Edison
 */
public class FrameRegiaoConsulta extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;

    private List<Regiao> listRegioes;
    private DefaultTableModel modelRegioes;
    Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();
    FormMenuPrincipal formPrincipal;

    /** Creates new form FrameRegiaoConsulta
     * @param formPrincipal 
     */
    public FrameRegiaoConsulta(FormMenuPrincipal formPrincipal) {
        initComponents();
        this.formPrincipal = formPrincipal;
        updateTableRegioes();
    }

    public void updateTableRegioes() {
        updateListRegioes();
        tableRegioes.setModel(modelRegioes);
        tableRegioes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableRegioes.setRowSelectionAllowed(true);
        tableRegioes.setColumnSelectionAllowed(false);
        ColumnResizer.adjustColumnPreferredWidths(tableRegioes);
        tableRegioes.revalidate();
        lblTotalRegioes.setText(Integer.toString(listRegioes.size()));

    }

    private void updateListRegioes() {
        listRegioes = Regiao.findAll();
        String[] collumnNames = new String[]{"Nome", "Cidades"};
        String[][] data = new String[listRegioes.size()][];
        for (int i = 0; i < listRegioes.size(); i++) {
            data[i] = new String[]{
                        listRegioes.get(i).getNome(),
                        Integer.toString(listRegioes.get(i).getCidades().size())
                    };
        }
        modelRegioes = new DefaultTableModel(data, collumnNames) {

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

        btnFechar = new javax.swing.JButton();
        btnCadastrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableRegioes = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        lblTotalRegioes = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Consulta de Regiões");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/location_16.png"))); // NOI18N

        btnFechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Symbol Delete 2.png"))); // NOI18N
        btnFechar.setText("Fechar");
        btnFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFecharActionPerformed(evt);
            }
        });

        btnCadastrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/location_add_16.png"))); // NOI18N
        btnCadastrar.setText("Cadastrar");
        btnCadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadastrarActionPerformed(evt);
            }
        });

        tableRegioes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Id", "Nome"
            }
        ));
        tableRegioes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableRegioesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableRegioes);

        jLabel1.setText("Total de Regiões: ");

        lblTotalRegioes.setText("total");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotalRegioes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                        .addComponent(btnCadastrar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFechar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFechar)
                    .addComponent(btnCadastrar)
                    .addComponent(jLabel1)
                    .addComponent(lblTotalRegioes))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharActionPerformed
        dispose();
}//GEN-LAST:event_btnFecharActionPerformed

    private void btnCadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastrarActionPerformed
        FrameRegiaoCadastro frame = new FrameRegiaoCadastro();
        formPrincipal.getDesktopPane().add(frame);
        frame.setLocation((int) this.getBounds().getX() - frame.getWidth(), (int) this.getBounds().getY());
        frame.setVisible(true);
        frame.addInternalFrameListener(new InternalFrameListener() {
            public void internalFrameOpened(InternalFrameEvent e) {}
            public void internalFrameClosing(InternalFrameEvent e) {}
            public void internalFrameClosed(InternalFrameEvent e) {
                updateTableRegioes();
            }
            public void internalFrameIconified(InternalFrameEvent e) {}
            public void internalFrameDeiconified(InternalFrameEvent e) {}
            public void internalFrameActivated(InternalFrameEvent e) {}
            public void internalFrameDeactivated(InternalFrameEvent e) {}
        });
}//GEN-LAST:event_btnCadastrarActionPerformed

    private void tableRegioesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableRegioesMouseClicked
        Regiao regiao = listRegioes.get(tableRegioes.getSelectedRow());
        FrameRegiaoManutencao frame = new FrameRegiaoManutencao(formPrincipal, regiao.getId());
        formPrincipal.getDesktopPane().add(frame);
        frame.setLocation((int) this.getBounds().getX() + this.getWidth(), (int) this.getBounds().getY());
        frame.setVisible(true);
        frame.addInternalFrameListener(new InternalFrameListener() {
            public void internalFrameOpened(InternalFrameEvent e) {}
            public void internalFrameClosing(InternalFrameEvent e) {}
            public void internalFrameClosed(InternalFrameEvent e) {
                updateTableRegioes();
            }
            public void internalFrameIconified(InternalFrameEvent e) {}
            public void internalFrameDeiconified(InternalFrameEvent e) {}
            public void internalFrameActivated(InternalFrameEvent e) {}
            public void internalFrameDeactivated(InternalFrameEvent e) {}
        });
    }//GEN-LAST:event_tableRegioesMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadastrar;
    private javax.swing.JButton btnFechar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTotalRegioes;
    private javax.swing.JTable tableRegioes;
    // End of variables declaration//GEN-END:variables
}
