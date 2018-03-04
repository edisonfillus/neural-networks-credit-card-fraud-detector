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

import org.ai.bo.ambiente.Cidade;
import org.ai.bo.ambiente.Estabelecimento;
import org.ai.bo.ambiente.Estado;
import org.ai.bo.ambiente.RamoAtividade;
import org.ai.forms.FormMenuPrincipal;

/**
 *
 * @author  Edison
 */
public class FrameEstabelecimentoCadastro extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    private List<Estado> listEstados;
    private List<Cidade> listCidades;
    private List<RamoAtividade> listRamos;
    private Estado estado;
    private Cidade cidade;
    private RamoAtividade ramo;
    private DefaultComboBoxModel modelEstados;
    private DefaultComboBoxModel modelCidades;
    private DefaultComboBoxModel modelRamos;

    public FrameEstabelecimentoCadastro() {
        initComponents();
        updateComboEstados();
        clearComboCidades();
        updateComboRamos();
    }
    
    public FrameEstabelecimentoCadastro(RamoAtividade ramo) {
        initComponents();
        updateComboEstados();
        clearComboCidades();
        this.ramo = ramo;
        String[] data = new String[]{ramo.getNome()};
        modelRamos = new DefaultComboBoxModel(data);
        cmbRamo.setModel(modelRamos);
        cmbRamo.revalidate();
        cmbRamo.setEnabled(false);
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

        jLabel1 = new javax.swing.JLabel();
        txtNomeEstabelecimento = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtDataAfiliacao = new javax.swing.JTextField();
        cmbEstado = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cmbCidade = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        cmbRamo = new javax.swing.JComboBox();
        btnFechar = new javax.swing.JButton();
        btnCadastrar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Cadastro de Estabelecimento");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/industry_16.png"))); // NOI18N

        jLabel1.setText("Nome");

        jLabel2.setText("Afiliação");

        cmbEstado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbEstadoActionPerformed(evt);
            }
        });

        jLabel4.setText("Estado");

        jLabel5.setText("Cidade");

        cmbCidade.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbCidade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCidadeActionPerformed(evt);
            }
        });

        jLabel3.setText("Ramo");

        cmbRamo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbRamo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbRamoActionPerformed(evt);
            }
        });

        btnFechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Symbol Delete 2.png"))); // NOI18N
        btnFechar.setText("Fechar");
        btnFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFecharActionPerformed(evt);
            }
        });

        btnCadastrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/industry_add_16.png"))); // NOI18N
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnCadastrar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFechar))
                    .addComponent(txtDataAfiliacao, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbEstado, 0, 212, Short.MAX_VALUE)
                    .addComponent(txtNomeEstabelecimento, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
                    .addComponent(cmbCidade, 0, 212, Short.MAX_VALUE)
                    .addComponent(cmbRamo, 0, 212, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNomeEstabelecimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtDataAfiliacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cmbCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cmbRamo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFechar)
                    .addComponent(btnCadastrar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

private void btnCadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastrarActionPerformed
    if (estado == null) {
        JOptionPane.showMessageDialog(this, "Selecione um Estado");
    } else if (cidade == null) {
        JOptionPane.showMessageDialog(this, "Selecione uma Cidade");
    } else if (ramo == null) {
        JOptionPane.showMessageDialog(this, "Selecione um Ramo de Atividade");
    } else {
        int resposta = JOptionPane.showConfirmDialog(this, "Confirma cadastro do estabelecimento " + txtNomeEstabelecimento.getText() + "?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (resposta == JOptionPane.YES_OPTION) {
            Estabelecimento estabelecimento = new Estabelecimento();
            estabelecimento.setNome(txtNomeEstabelecimento.getText());
            Calendar afiliacao = Calendar.getInstance();
            try {
                afiliacao.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(txtDataAfiliacao.getText()));
            } catch (ParseException ex) {
                Logger.getLogger(FrameEstabelecimentoCadastro.class.getName()).log(Level.SEVERE, null, ex);
            }
            estabelecimento.setDataAfiliacao(afiliacao);
            estabelecimento.setCidade(cidade);
            estabelecimento.setRamoAtividade(ramo);
            boolean created = estabelecimento.create();
            if (created) {
                JOptionPane.showMessageDialog(this, "Estabelecimento " + estabelecimento.getNome() + " cadastrado com sucesso");
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao Cadastrar o Estabelecimento " + estabelecimento.getNome());
            }
            dispose();
        }
    }
}//GEN-LAST:event_btnCadastrarActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadastrar;
    private javax.swing.JButton btnFechar;
    private javax.swing.JComboBox cmbCidade;
    private javax.swing.JComboBox cmbEstado;
    private javax.swing.JComboBox cmbRamo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField txtDataAfiliacao;
    private javax.swing.JTextField txtNomeEstabelecimento;
    // End of variables declaration//GEN-END:variables
}