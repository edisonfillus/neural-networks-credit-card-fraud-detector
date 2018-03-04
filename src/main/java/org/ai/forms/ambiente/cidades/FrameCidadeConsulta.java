/*
 * FrameCidadeConsulta.java
 *
 * Created on Sep 28, 2008, 11:18:45 PM
 */

package org.ai.forms.ambiente.cidades;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.table.DefaultTableModel;

import org.ai.bo.ambiente.Cidade;
import org.ai.forms.FormMenuPrincipal;
import org.ai.forms.utils.ColumnResizer;

/**
 *
 * @author Edison
 */
public class FrameCidadeConsulta extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    
    private List<Cidade> listCidades;
    private DefaultTableModel modelCidades;
    Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();
    FormMenuPrincipal formPrincipal;

    
    /** Creates new form FrameCidadeConsulta
     * @param formPrincipal 
     */
    public FrameCidadeConsulta(FormMenuPrincipal formPrincipal) {
        initComponents();
        this.formPrincipal = formPrincipal;
        updateTableCidades();
    }
    
    public void updateTableCidades() {
        updateListCidades();
        tableCidades.setModel(modelCidades);
        tableCidades.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableCidades.setRowSelectionAllowed(true);
        tableCidades.setColumnSelectionAllowed(false);
        ColumnResizer.adjustColumnPreferredWidths(tableCidades);
        tableCidades.revalidate();
        lblTotalCidades.setText(Integer.toString(listCidades.size()));

    }

    private void updateListCidades() {
        listCidades = Cidade.findAll();
        String[] collumnNames = new String[]{"Nome","Regiao","Estado"};
        String[][] data = new String[listCidades.size()][3];
        for (int i = 0; i < listCidades.size(); i++) {
            data[i] = new String[]{
                        listCidades.get(i).getNome(),
                        listCidades.get(i).getRegiao().getNome(),
                        listCidades.get(i).getEstado().getNome()
                    };
        }
        modelCidades = new DefaultTableModel(data, collumnNames) {
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
        tableCidades = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        lblTotalCidades = new javax.swing.JLabel();
        btnFechar = new javax.swing.JButton();
        btnCadastrar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Consulta de Cidades");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/location_16.png"))); // NOI18N

        tableCidades.setModel(new javax.swing.table.DefaultTableModel(
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
        tableCidades.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableCidadesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableCidades);

        jLabel1.setText("Total de Cidades: ");

        lblTotalCidades.setText("total");

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, 0, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotalCidades)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                        .addComponent(btnCadastrar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFechar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblTotalCidades)
                    .addComponent(btnFechar)
                    .addComponent(btnCadastrar))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharActionPerformed
        dispose();
    }//GEN-LAST:event_btnFecharActionPerformed

    private void btnCadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastrarActionPerformed
        FrameCidadeCadastro frame = new FrameCidadeCadastro();
        formPrincipal.getDesktopPane().add(frame);
        frame.setLocation((int) this.getBounds().getX() - frame.getWidth(), (int) this.getBounds().getY());
        frame.setVisible(true);
        frame.addInternalFrameListener(new InternalFrameListener() {
            public void internalFrameOpened(InternalFrameEvent e) {}
            public void internalFrameClosing(InternalFrameEvent e) {}
            public void internalFrameClosed(InternalFrameEvent e) {
                updateTableCidades();
            }
            public void internalFrameIconified(InternalFrameEvent e) {}
            public void internalFrameDeiconified(InternalFrameEvent e) {}
            public void internalFrameActivated(InternalFrameEvent e) {}
            public void internalFrameDeactivated(InternalFrameEvent e) {}
        });
    }//GEN-LAST:event_btnCadastrarActionPerformed

    private void tableCidadesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableCidadesMouseClicked
        Cidade cidade = listCidades.get(tableCidades.getSelectedRow());
        FrameCidadeManutencao frame = new FrameCidadeManutencao(cidade.getId());
        formPrincipal.getDesktopPane().add(frame);
        frame.setLocation((int) this.getBounds().getX() + this.getWidth(), (int) this.getBounds().getY());
        frame.setVisible(true);
        frame.addInternalFrameListener(new InternalFrameListener() {
            public void internalFrameOpened(InternalFrameEvent e) {}
            public void internalFrameClosing(InternalFrameEvent e) {}
            public void internalFrameClosed(InternalFrameEvent e) {
                updateTableCidades();
            }
            public void internalFrameIconified(InternalFrameEvent e) {}
            public void internalFrameDeiconified(InternalFrameEvent e) {}
            public void internalFrameActivated(InternalFrameEvent e) {}
            public void internalFrameDeactivated(InternalFrameEvent e) {}
        });
    }//GEN-LAST:event_tableCidadesMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadastrar;
    private javax.swing.JButton btnFechar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTotalCidades;
    private javax.swing.JTable tableCidades;
    // End of variables declaration//GEN-END:variables

}
