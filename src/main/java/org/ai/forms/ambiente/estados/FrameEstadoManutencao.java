/*
 * FormEstadoManutencao.java
 *
 * Created on October 5, 2008, 4:57 PM
 */
package org.ai.forms.ambiente.estados;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.table.DefaultTableModel;

import org.ai.bo.ambiente.Cidade;
import org.ai.bo.ambiente.Estado;
import org.ai.forms.FormMenuPrincipal;
import org.ai.forms.ambiente.cidades.FrameCidadeCadastro;
import org.ai.forms.ambiente.cidades.FrameCidadeManutencao;
import org.ai.forms.utils.ColumnResizer;

/**
 *
 * @author  Edison
 */
public class FrameEstadoManutencao extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    
    private Estado estado;

    private List<Cidade> listCidadesEstado;
    private DefaultTableModel modelCidadesEstado;
    private FormMenuPrincipal formPrincipal;
    
    public FrameEstadoManutencao(FormMenuPrincipal formPrincipal,int idEstado) {
        initComponents();
        this.formPrincipal = formPrincipal;
        estado = Estado.find(idEstado);
        updateTableCidadesEstado();
        txtNome.setText(estado.getNome());
        txtSigla.setText(estado.getSigla());
    }

    public void updateTableCidadesEstado() {
        updateListCidadesEstado();
        tableCidadesEstado.setModel(modelCidadesEstado);
        tableCidadesEstado.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableCidadesEstado.setRowSelectionAllowed(true);
        tableCidadesEstado.setColumnSelectionAllowed(false);
        ColumnResizer.adjustColumnPreferredWidths(tableCidadesEstado);
        tableCidadesEstado.revalidate();
        lblTotalCidades.setText(Integer.toString(listCidadesEstado.size()));
    }

    private void updateListCidadesEstado() {
        listCidadesEstado = estado.getCidades();
        String[] collumnNames = new String[]{"Nome", "Regiao"};
        String[][] data = new String[listCidadesEstado.size()][];
        for (int i = 0; i < listCidadesEstado.size(); i++) {
            data[i] = new String[]{
                        listCidadesEstado.get(i).getNome(),
                        listCidadesEstado.get(i).getRegiao().getNome()
                    };
        }
        modelCidadesEstado = new DefaultTableModel(data, collumnNames) {
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
        btnSalvar = new javax.swing.JToggleButton();
        btnExcluir = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        txtSigla = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btnInserirCidade = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        lblTotalCidades = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableCidadesEstado = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setTitle("Manutenção de Estado");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/location_16.png"))); // NOI18N

        btnFechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Symbol Delete 2.png"))); // NOI18N
        btnFechar.setText("Fechar");
        btnFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFecharActionPerformed(evt);
            }
        });

        btnSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/location_write_16.png"))); // NOI18N
        btnSalvar.setText("Salvar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        btnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/location_remove_16.png"))); // NOI18N
        btnExcluir.setText("Excluir");
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        jLabel2.setText("Sigla");

        jLabel1.setText("Nome");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSigla, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtSigla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(116, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Estado", jPanel1);

        btnInserirCidade.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/location_add_16.png"))); // NOI18N
        btnInserirCidade.setText("Inserir");
        btnInserirCidade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInserirCidadeActionPerformed(evt);
            }
        });

        jLabel3.setText("Total Cidades:");

        lblTotalCidades.setText("total");

        tableCidadesEstado.setModel(new javax.swing.table.DefaultTableModel(
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
        tableCidadesEstado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableCidadesEstadoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableCidadesEstado);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnInserirCidade)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotalCidades)))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInserirCidade)
                    .addComponent(jLabel3)
                    .addComponent(lblTotalCidades))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Cidades", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnExcluir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSalvar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFechar))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnFechar)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnSalvar)
                        .addComponent(btnExcluir)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharActionPerformed
    dispose();
}//GEN-LAST:event_btnFecharActionPerformed

private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
    int resposta = JOptionPane.showConfirmDialog(this, "Salvar alterações do estado " + estado.getNome() + "?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    if (resposta == JOptionPane.YES_OPTION) {
        estado.setNome(txtNome.getText());
        estado.setSigla(txtSigla.getText());
        boolean updated = estado.update();
        if (updated) {
            JOptionPane.showMessageDialog(this, "Estado " + estado.getNome() + " salvo com sucesso");
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao salvar Estado " + estado.getNome());
        }
        dispose();
    }
}//GEN-LAST:event_btnSalvarActionPerformed

private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
    int resposta = JOptionPane.showConfirmDialog(this, "Confirma exclusão da estado " + estado.getNome() + "?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    if (resposta == JOptionPane.YES_OPTION) {
        boolean deleted = estado.delete();
        if (deleted) {
            JOptionPane.showMessageDialog(this, "Estado " + estado.getNome() + " excluido com sucesso");
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao excluir estado " + estado.getNome());
        }
        dispose();
    }
}//GEN-LAST:event_btnExcluirActionPerformed

private void btnInserirCidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInserirCidadeActionPerformed
    FrameCidadeCadastro frame = new FrameCidadeCadastro(estado);
    formPrincipal.getDesktopPane().add(frame);
    frame.setLocation((int) this.getBounds().getX(), (int) this.getBounds().getY() + this.getHeight());
    frame.setVisible(true);
    frame.addInternalFrameListener(new InternalFrameListener() {
        public void internalFrameOpened(InternalFrameEvent e) {}
        public void internalFrameClosing(InternalFrameEvent e) {}
        public void internalFrameClosed(InternalFrameEvent e) {
            estado.setCidades(Estado.find(estado.getId()).getCidades());
            updateTableCidadesEstado();
        }
        public void internalFrameIconified(InternalFrameEvent e) {}
        public void internalFrameDeiconified(InternalFrameEvent e) {}
        public void internalFrameActivated(InternalFrameEvent e) {}
        public void internalFrameDeactivated(InternalFrameEvent e) {}
    });
}//GEN-LAST:event_btnInserirCidadeActionPerformed

private void tableCidadesEstadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableCidadesEstadoMouseClicked
     if (tableCidadesEstado.getSelectedRow() >= 0) {
        Cidade cidade = listCidadesEstado.get(tableCidadesEstado.getSelectedRow());
        FrameCidadeManutencao frame = new FrameCidadeManutencao(cidade.getId());
        formPrincipal.getDesktopPane().add(frame);
        frame.setLocation((int) this.getBounds().getX(), (int) this.getBounds().getY() + this.getHeight());
        frame.setVisible(true);
        frame.addInternalFrameListener(new InternalFrameListener() {
                public void internalFrameOpened(InternalFrameEvent e) {}
                public void internalFrameClosing(InternalFrameEvent e) {}
                public void internalFrameClosed(InternalFrameEvent e) {
                    estado.setCidades(Estado.find(estado.getId()).getCidades());
                    updateTableCidadesEstado();
                }
                public void internalFrameIconified(InternalFrameEvent e) {}
                public void internalFrameDeiconified(InternalFrameEvent e) {}
                public void internalFrameActivated(InternalFrameEvent e) {}
                public void internalFrameDeactivated(InternalFrameEvent e) {}
            });
    }
}//GEN-LAST:event_tableCidadesEstadoMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnInserirCidade;
    private javax.swing.JToggleButton btnSalvar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblTotalCidades;
    private javax.swing.JTable tableCidadesEstado;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtSigla;
    // End of variables declaration//GEN-END:variables
}
