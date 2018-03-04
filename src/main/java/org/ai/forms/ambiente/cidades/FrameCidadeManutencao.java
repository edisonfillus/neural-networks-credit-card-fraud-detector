/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FrameCidadeManutencao.java
 *
 * Created on Sep 29, 2008, 5:37:13 PM
 */
package org.ai.forms.ambiente.cidades;

import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

import org.ai.bo.ambiente.Cidade;
import org.ai.bo.ambiente.Estado;
import org.ai.bo.ambiente.Regiao;

/**
 *
 * @author Edison
 */
public class FrameCidadeManutencao extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    private List<Estado> listEstados;
    private List<Regiao> listRegioes;
    private Estado estado;
    private Regiao regiao;
    private Cidade cidade;
    private DefaultComboBoxModel modelEstados;
    private DefaultComboBoxModel modelRegioes;

    /** Creates new form FrameCidadeManutencao
     * @param idCidade 
     */
    public FrameCidadeManutencao(int idCidade) {
        initComponents();
        this.cidade = Cidade.find(idCidade);
        txtNomeCidade.setText(cidade.getNome());
        updateComboEstados();
        updateComboRegioes();
        for (int i = 0; i < listEstados.size(); i++) {
            if (listEstados.get(i).getId() == cidade.getEstado().getId()) {
                cmbEstado.setSelectedIndex(i + 1);
            }
        }
        for (int i = 0; i < listRegioes.size(); i++) {
            if (listRegioes.get(i).getId() == cidade.getRegiao().getId()) {
                cmbRegiao.setSelectedIndex(i + 1);
            }
        }

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

    private void updateListRegioes() {
        listRegioes = Regiao.findAll();
        String[] data = new String[listRegioes.size() + 1];
        data[0] = "Selecione uma Região...";
        for (int i = 0; i < listRegioes.size(); i++) {
            data[i + 1] = listRegioes.get(i).getNome();
        }
        modelRegioes = new DefaultComboBoxModel(data);
    }

    private void updateComboRegioes() {
        updateListRegioes();
        cmbRegiao.setModel(modelRegioes);
        cmbRegiao.revalidate();
        regiao = null;
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
        jLabel1 = new javax.swing.JLabel();
        txtNomeCidade = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cmbEstado = new javax.swing.JComboBox();
        cmbRegiao = new javax.swing.JComboBox();
        btnSalvar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();

        setTitle("Manutenção de Cidade");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/location_16.png"))); // NOI18N

        btnFechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Symbol Delete 2.png"))); // NOI18N
        btnFechar.setText("Fechar");
        btnFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFecharActionPerformed(evt);
            }
        });

        jLabel1.setText("Nome");

        jLabel3.setText("Região");

        jLabel2.setText("Estado");

        cmbEstado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbEstadoActionPerformed(evt);
            }
        });

        cmbRegiao.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbRegiao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbRegiaoActionPerformed(evt);
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
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(6, 6, 6)
                            .addComponent(jLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtNomeCidade))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel3)
                                .addComponent(jLabel2))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cmbRegiao, 0, 170, Short.MAX_VALUE)))))
                .addContainerGap(11, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNomeCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbRegiao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFechar)
                    .addComponent(btnSalvar)
                    .addComponent(btnExcluir))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharActionPerformed
        dispose();
}//GEN-LAST:event_btnFecharActionPerformed

    private void cmbEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbEstadoActionPerformed
        if (cmbEstado.getSelectedIndex() != 0) {
            estado = listEstados.get(cmbEstado.getSelectedIndex() - 1);
        } else {
            estado = null;
        }
}//GEN-LAST:event_cmbEstadoActionPerformed

    private void cmbRegiaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbRegiaoActionPerformed
        if (cmbRegiao.getSelectedIndex() != 0) {
            regiao = listRegioes.get(cmbRegiao.getSelectedIndex() - 1);
        } else {
            regiao = null;
        }
}//GEN-LAST:event_cmbRegiaoActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        if (estado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um Estado");
        } else if (regiao == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma Região");
        } else {
            int resposta = JOptionPane.showConfirmDialog(this, "Salvar alterações da cidade " + cidade.getNome() + "?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (resposta == JOptionPane.YES_OPTION) {
                cidade.setNome(txtNomeCidade.getText());
                cidade.setEstado(estado);
                cidade.setRegiao(regiao);
                boolean updated = cidade.update();
                if (updated) {
                    JOptionPane.showMessageDialog(this, "Cidade " + cidade.getNome() + " salva com sucesso");
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao salvar cidade " + cidade.getNome());
                }
                dispose();
            }
        }

    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        int resposta = JOptionPane.showConfirmDialog(this, "Confirma exclusão da cidade " + cidade.getNome() + "?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (resposta == JOptionPane.YES_OPTION) {
            boolean deleted = cidade.delete();
            if (deleted) {
                JOptionPane.showMessageDialog(this, "Cidade " + cidade.getNome() + " excluida com sucesso");
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao excluir cidade " + cidade.getNome());
            }
            dispose();
        }
    }//GEN-LAST:event_btnExcluirActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JComboBox cmbEstado;
    private javax.swing.JComboBox cmbRegiao;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField txtNomeCidade;
    // End of variables declaration//GEN-END:variables
}