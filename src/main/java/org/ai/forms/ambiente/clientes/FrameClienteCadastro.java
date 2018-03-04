/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FrameClienteCadastra.java
 *
 * Created on Sep 29, 2008, 6:34:59 PM
 */
package org.ai.forms.ambiente.clientes;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.ai.bo.ambiente.Cartao;
import org.ai.bo.ambiente.Cidade;
import org.ai.bo.ambiente.Cliente;
import org.ai.bo.ambiente.Estado;
import org.ai.forms.FormMenuPrincipal;
import org.ai.forms.utils.ColumnResizer;

/**
 *
 * @author Edison
 */
public class FrameClienteCadastro extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    private List<Estado> listEstados;
    private List<Cidade> listCidades;
    private Estado estado;
    private Cidade cidade;
    private DefaultComboBoxModel modelEstados;
    private DefaultComboBoxModel modelCidades;


    public FrameClienteCadastro() {
        initComponents();
        updateComboEstados();
        clearComboCidades();

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

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popUpMenu = new javax.swing.JPopupMenu();
        btnFechar = new javax.swing.JButton();
        btnCadastrar = new javax.swing.JButton();
        txtRenda = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtIdade = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cmbCidade = new javax.swing.JComboBox();
        cmbEstado = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtNomeCliente = new javax.swing.JTextField();

        setTitle("Cadastro de Cliente");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Bank User.png"))); // NOI18N

        btnFechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Symbol Delete 2.png"))); // NOI18N
        btnFechar.setText("Fechar");
        btnFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFecharActionPerformed(evt);
            }
        });

        btnCadastrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Bank User Add.png"))); // NOI18N
        btnCadastrar.setText("Cadastrar");
        btnCadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadastrarActionPerformed(evt);
            }
        });

        jLabel3.setText("Renda");

        jLabel2.setText("Idade");

        jLabel5.setText("Cidade");

        cmbCidade.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbCidade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCidadeActionPerformed(evt);
            }
        });

        cmbEstado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbEstadoActionPerformed(evt);
            }
        });

        jLabel4.setText("Estado");

        jLabel1.setText("Nome");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel1))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel5)
                        .addComponent(jLabel2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(txtIdade, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtRenda, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(cmbCidade, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmbEstado, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtNomeCliente, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnCadastrar)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnFechar))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNomeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cmbCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIdade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(txtRenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFechar)
                    .addComponent(btnCadastrar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharActionPerformed
        dispose();
    }//GEN-LAST:event_btnFecharActionPerformed

    private void btnCadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastrarActionPerformed
        if (estado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um Estado");
        } else if (cidade == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma Cidade");
        } else {
            int resposta = JOptionPane.showConfirmDialog(this, "Confirma cadastro do cliente " + txtNomeCliente.getText() + "?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (resposta == JOptionPane.YES_OPTION) {
                Cliente cliente = new Cliente();
                cliente.setNome(txtNomeCliente.getText());
                cliente.setIdade(Integer.parseInt(txtIdade.getText()));
                try {
                    cliente.setRenda(new DecimalFormat("#,###,###.00").parse(txtRenda.getText()).doubleValue());
                } catch (ParseException ex) {
                    Logger.getLogger(FrameClienteCadastro.class.getName()).log(Level.SEVERE, null, ex);
                }
                cliente.setCidade(cidade);
                boolean created = cliente.create();
                if (created) {
                    JOptionPane.showMessageDialog(this, "Cliente " + cliente.getNome() + " cadastrado com sucesso");
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao Cadastrar o Cliente " + cliente.getNome());
                }
                dispose();
            }
        }
    }//GEN-LAST:event_btnCadastrarActionPerformed

private void cmbCidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCidadeActionPerformed
    if (cmbCidade.getSelectedIndex() != 0) {
        cidade = listCidades.get(cmbCidade.getSelectedIndex() - 1);
    } else {
        cidade = null;
    }
}//GEN-LAST:event_cmbCidadeActionPerformed

private void cmbEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbEstadoActionPerformed
    if (cmbEstado.getSelectedIndex() != 0) {
        estado = listEstados.get(cmbEstado.getSelectedIndex() - 1);
        updateComboCidades();
    } else {
        estado = null;
        clearComboCidades();
    }
}//GEN-LAST:event_cmbEstadoActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadastrar;
    private javax.swing.JButton btnFechar;
    private javax.swing.JComboBox cmbCidade;
    private javax.swing.JComboBox cmbEstado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPopupMenu popUpMenu;
    private javax.swing.JTextField txtIdade;
    private javax.swing.JTextField txtNomeCliente;
    private javax.swing.JTextField txtRenda;
    // End of variables declaration//GEN-END:variables
}
