/*
 * FrameEstabelecimentoCadastro.java
 *
 * Created on October 1, 2008, 8:03 PM
 */
package org.ai.forms.ambiente.estabelecimentos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.table.DefaultTableModel;

import org.ai.bo.ambiente.Cidade;
import org.ai.bo.ambiente.Estabelecimento;
import org.ai.bo.ambiente.Estado;
import org.ai.bo.ambiente.RamoAtividade;
import org.ai.bo.ambiente.TerminalPOS;
import org.ai.forms.FormMenuPrincipal;
import org.ai.forms.ambiente.terminais.FrameTerminalCadastro;
import org.ai.forms.ambiente.terminais.FrameTerminalManutencao;
import org.ai.forms.utils.ColumnResizer;

/**
 *
 * @author  Edison
 */
public class FrameEstabelecimentoManutencao extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    private List<Estado> listEstados;
    private List<Cidade> listCidades;
    private List<RamoAtividade> listRamos;
    private List<TerminalPOS> listTerminais;
    private Estado estado;
    private Cidade cidade;
    private RamoAtividade ramo;
    private Estabelecimento estabelecimento;
    private DefaultComboBoxModel modelEstados;
    private DefaultComboBoxModel modelCidades;
    private DefaultComboBoxModel modelRamos;
    private DefaultTableModel modelTerminais;
    FormMenuPrincipal formPrincipal;

    public FrameEstabelecimentoManutencao(FormMenuPrincipal formPrincipal, int idEstabelecimento) {
        initComponents();
        this.formPrincipal = formPrincipal;
        this.estabelecimento = Estabelecimento.find(idEstabelecimento);
        txtNomeEstabelecimento.setText(estabelecimento.getNome());
        txtDataAfiliacao.setText(new SimpleDateFormat("dd/MM/yyyy").format(estabelecimento.getDataAfiliacao().getTime()));
        updateComboEstados();
        estado = estabelecimento.getCidade().getEstado();
        for (int i = 0; i < listEstados.size(); i++) {
            if (listEstados.get(i).getId() == estado.getId()) {
                cmbEstado.setSelectedIndex(i + 1);
            }
        }
        updateComboCidades();
        cidade = estabelecimento.getCidade();
        for (int i = 0; i < listCidades.size(); i++) {
            if (listCidades.get(i).getId() == cidade.getId()) {
                cmbCidade.setSelectedIndex(i + 1);
            }
        }
        updateComboRamos();
        ramo = estabelecimento.getRamoAtividade();
        for (int i = 0; i < listRamos.size(); i++) {
            if (listRamos.get(i).getId() == ramo.getId()) {
                cmbRamo.setSelectedIndex(i + 1);
            }
        }
        updateTableTerminaisEstabelecimento();
    }
    
    public void updateTableTerminaisEstabelecimento() {
        updateListTerminaisEstabelecimento();
        tableTerminais.setModel(modelTerminais);
        tableTerminais.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableTerminais.setRowSelectionAllowed(true);
        tableTerminais.setColumnSelectionAllowed(false);
        ColumnResizer.adjustColumnPreferredWidths(tableTerminais);
        tableTerminais.revalidate();
        lblTotalTerminais.setText(Integer.toString(listTerminais.size()));

    }

    private void updateListTerminaisEstabelecimento() {
        listTerminais = estabelecimento.getTerminaisPOS();
        String[] collumnNames = new String[]{"Numero", "Rede", "Transações"};
        String[][] data = new String[listTerminais.size()][];
        for (int i = 0; i < listTerminais.size(); i++) {
            data[i] = new String[]{
                        Integer.toString(listTerminais.get(i).getId()),
                        listTerminais.get(i).getRede().getNome(),
                        Integer.toString(listTerminais.get(i).getTransacoes().size())
                    };
        }
        modelTerminais = new DefaultTableModel(data, collumnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

    }
    
    

    private void updateListEstados() {
        listEstados = Estado.findAll();
        String[] data = new String[listEstados.size() + 1];
        data[0] = "Selecione um Estado...";
        for (int i = 0; i < listEstados.size(); i++) {
            data[i + 1] = listEstados.get(i).getNome();
        }
        modelEstados = new DefaultComboBoxModel(data);
    }

    private void updateComboEstados() {
        updateListEstados();
        cmbEstado.setModel(modelEstados);
        cmbEstado.revalidate();
        estado = null;
    }

    private void clearComboCidades() {
        listCidades = null;
        String[] data = new String[]{"Selecione um Estado..."};
        modelCidades = new DefaultComboBoxModel(data);
        cmbCidade.setModel(modelCidades);
        cmbCidade.revalidate();
        cidade = null;
    }

    private void updateComboCidades() {
        listCidades = null;
        listCidades = estado.getCidades();
        String[] data = new String[listCidades.size() + 1];
        data[0] = "Selecione uma Cidade...";
        for (int i = 0; i < listCidades.size(); i++) {
            data[i + 1] = listCidades.get(i).getNome();
        }
        modelCidades = new DefaultComboBoxModel(data);
        cmbCidade.setModel(modelCidades);
        cmbCidade.revalidate();
        cidade = null;
    }

    private void updateListRamos() {
        listRamos = RamoAtividade.findAll();
        String[] data = new String[listRamos.size() + 1];
        data[0] = "Selecione um ramo de atividade...";
        for (int i = 0; i < listRamos.size(); i++) {
            data[i + 1] = listRamos.get(i).getNome();
        }
        modelRamos = new DefaultComboBoxModel(data);
    }

    private void updateComboRamos() {
        updateListRamos();
        cmbRamo.setModel(modelRamos);
        cmbRamo.revalidate();
        ramo = null;
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtDataAfiliacao = new javax.swing.JTextField();
        cmbEstado = new javax.swing.JComboBox();
        txtNomeEstabelecimento = new javax.swing.JTextField();
        cmbCidade = new javax.swing.JComboBox();
        cmbRamo = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableTerminais = new javax.swing.JTable();
        btnInserirTerminal = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        lblTotalTerminais = new javax.swing.JLabel();
        btnSalvar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Manutenção de Estabelecimento");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/industry_16.png"))); // NOI18N

        btnFechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Symbol Delete 2.png"))); // NOI18N
        btnFechar.setText("Fechar");
        btnFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFecharActionPerformed(evt);
            }
        });

        jLabel2.setText("Afiliação");

        jLabel1.setText("Nome");

        jLabel4.setText("Estado");

        jLabel5.setText("Cidade");

        jLabel3.setText("Ramo");

        cmbEstado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbEstadoActionPerformed(evt);
            }
        });

        cmbCidade.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbCidade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCidadeActionPerformed(evt);
            }
        });

        cmbRamo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbRamo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbRamoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtDataAfiliacao, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNomeEstabelecimento, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                    .addComponent(cmbEstado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbCidade, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbRamo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNomeEstabelecimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtDataAfiliacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cmbCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cmbRamo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Estabelecimento", jPanel1);

        tableTerminais.setModel(new javax.swing.table.DefaultTableModel(
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
        tableTerminais.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableTerminaisMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableTerminais);

        btnInserirTerminal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Card Swiper Add.png"))); // NOI18N
        btnInserirTerminal.setText("Inserir");
        btnInserirTerminal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInserirTerminalActionPerformed(evt);
            }
        });

        jLabel6.setText("Total de Terminais:");

        lblTotalTerminais.setText("total");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnInserirTerminal)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotalTerminais)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lblTotalTerminais)
                    .addComponent(btnInserirTerminal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("POS", jPanel2);

        btnSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/industry_save_16.png"))); // NOI18N
        btnSalvar.setText("Salvar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        btnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/industry_remove_16.png"))); // NOI18N
        btnExcluir.setText("Excluir");
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnExcluir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSalvar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFechar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFechar)
                    .addComponent(btnSalvar)
                    .addComponent(btnExcluir))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void cmbEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbEstadoActionPerformed
    if (cmbEstado.getSelectedIndex() != 0) {
        estado = listEstados.get(cmbEstado.getSelectedIndex() - 1);
        updateComboCidades();
    } else {
        estado = null;
        clearComboCidades();
    }
}//GEN-LAST:event_cmbEstadoActionPerformed

private void cmbCidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCidadeActionPerformed
    if (cmbCidade.getSelectedIndex() != 0) {
        cidade = listCidades.get(cmbCidade.getSelectedIndex() - 1);
    } else {
        cidade = null;
    }
}//GEN-LAST:event_cmbCidadeActionPerformed

private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharActionPerformed
    dispose();
}//GEN-LAST:event_btnFecharActionPerformed

private void cmbRamoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbRamoActionPerformed
    if (cmbRamo.getSelectedIndex() != 0) {
        ramo = listRamos.get(cmbRamo.getSelectedIndex() - 1);
    } else {
        ramo = null;
    }
}//GEN-LAST:event_cmbRamoActionPerformed

private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
    if (cidade == null) {
        JOptionPane.showMessageDialog(this, "Selecione uma Cidade");
    } else if (ramo == null) {
        JOptionPane.showMessageDialog(this, "Selecione um Ramo de Atividade");
    } else {
        int resposta = JOptionPane.showConfirmDialog(this, "Salvar alterações do Estabelecimento " + estabelecimento.getNome() + "?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (resposta == JOptionPane.YES_OPTION) {
            estabelecimento.setNome(txtNomeEstabelecimento.getText());
            estabelecimento.setCidade(cidade);
            estabelecimento.setRamoAtividade(ramo);
            Calendar afiliacao = Calendar.getInstance();
            try {
                afiliacao.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(txtDataAfiliacao.getText()));
            } catch (ParseException ex) {
                Logger.getLogger(FrameEstabelecimentoManutencao.class.getName()).log(Level.SEVERE, null, ex);
            }
            estabelecimento.setDataAfiliacao(afiliacao);
            boolean updated = estabelecimento.update();
            if (updated) {
                JOptionPane.showMessageDialog(this, "Estabelecimento " + estabelecimento.getNome() + " salvo com sucesso");
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao salvar estabelecimento " + estabelecimento.getNome());
            }
            dispose();
        }
    }
}//GEN-LAST:event_btnSalvarActionPerformed

private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
    int resposta = JOptionPane.showConfirmDialog(this, "Confirma exclusão do estabelecimento " + estabelecimento.getNome() + "?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    if (resposta == JOptionPane.YES_OPTION) {
        boolean deleted = estabelecimento.delete();
        if (deleted) {
            JOptionPane.showMessageDialog(this, "Estabelecimento " + estabelecimento.getNome() + " excluido com sucesso");
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao excluir estabelecimento " + estabelecimento.getNome());
        }
        dispose();
    }
}//GEN-LAST:event_btnExcluirActionPerformed

private void btnInserirTerminalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInserirTerminalActionPerformed
    FrameTerminalCadastro frame = new FrameTerminalCadastro(estabelecimento.getId());
    formPrincipal.getDesktopPane().add(frame);
    frame.setLocation((int) this.getBounds().getX(), (int) this.getBounds().getY() + this.getHeight());
    frame.setVisible(true);
    frame.addInternalFrameListener(new InternalFrameListener() {
            public void internalFrameOpened(InternalFrameEvent e) {}
            public void internalFrameClosing(InternalFrameEvent e) {}
            public void internalFrameClosed(InternalFrameEvent e) {
                estabelecimento.setTerminaisPOS(Estabelecimento.find(estabelecimento.getId()).getTerminaisPOS());
                updateTableTerminaisEstabelecimento();
            }
            public void internalFrameIconified(InternalFrameEvent e) {}
            public void internalFrameDeiconified(InternalFrameEvent e) {}
            public void internalFrameActivated(InternalFrameEvent e) {}
            public void internalFrameDeactivated(InternalFrameEvent e) {}
        });
}//GEN-LAST:event_btnInserirTerminalActionPerformed

private void tableTerminaisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableTerminaisMouseClicked
    TerminalPOS terminal = listTerminais.get(tableTerminais.getSelectedRow());
    FrameTerminalManutencao frame = new FrameTerminalManutencao(formPrincipal, terminal.getId());
    formPrincipal.getDesktopPane().add(frame);
    frame.setLocation((int) this.getBounds().getX(), (int) this.getBounds().getY() + this.getHeight());
    frame.setVisible(true);
    frame.addInternalFrameListener(new InternalFrameListener() {
            public void internalFrameOpened(InternalFrameEvent e) {}
            public void internalFrameClosing(InternalFrameEvent e) {}
            public void internalFrameClosed(InternalFrameEvent e) {
                estabelecimento.setTerminaisPOS(Estabelecimento.find(estabelecimento.getId()).getTerminaisPOS());
                updateTableTerminaisEstabelecimento();
            }
            public void internalFrameIconified(InternalFrameEvent e) {}
            public void internalFrameDeiconified(InternalFrameEvent e) {}
            public void internalFrameActivated(InternalFrameEvent e) {}
            public void internalFrameDeactivated(InternalFrameEvent e) {}
        });
}//GEN-LAST:event_tableTerminaisMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnInserirTerminal;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JComboBox cmbCidade;
    private javax.swing.JComboBox cmbEstado;
    private javax.swing.JComboBox cmbRamo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblTotalTerminais;
    private javax.swing.JTable tableTerminais;
    private javax.swing.JTextField txtDataAfiliacao;
    private javax.swing.JTextField txtNomeEstabelecimento;
    // End of variables declaration//GEN-END:variables
}
