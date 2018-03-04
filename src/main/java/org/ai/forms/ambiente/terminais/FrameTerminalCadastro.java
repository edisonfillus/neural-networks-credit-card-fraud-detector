/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FrameTerminalCadastro.java
 *
 * Created on Oct 2, 2008, 6:24:50 PM
 */
package org.ai.forms.ambiente.terminais;

import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

import org.ai.bo.ambiente.Cidade;
import org.ai.bo.ambiente.Estabelecimento;
import org.ai.bo.ambiente.Estado;
import org.ai.bo.ambiente.Rede;
import org.ai.bo.ambiente.TerminalPOS;

/**
 *
 * @author Edison
 */
public class FrameTerminalCadastro extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    private List<Estado> listEstados;
    private List<Cidade> listCidades;
    private List<Estabelecimento> listEstabelecimento;
    private List<Rede> listRedes;
    private Estado estado;
    private Cidade cidade;
    private Estabelecimento estabelecimento;
    private Rede rede;
    private DefaultComboBoxModel modelEstados;
    private DefaultComboBoxModel modelCidades;
    private DefaultComboBoxModel modelEstabelecimentos;
    private DefaultComboBoxModel modelRedes;

    public FrameTerminalCadastro() {
        initComponents();
        updateComboEstados();
        clearComboCidades();
        clearComboEstabelecimentos();
        updateComboRedes();
    }

    public FrameTerminalCadastro(int idEstabelecimento) {
        initComponents();
        this.estabelecimento = Estabelecimento.find(idEstabelecimento);
        
        this.estado = estabelecimento.getCidade().getEstado();
        this.cidade = estabelecimento.getCidade();


        //Define o estado fixo no combo
        modelEstados = new DefaultComboBoxModel(new String[]{estabelecimento.getCidade().getEstado().getNome()});
        cmbEstado.setModel(modelEstados);
        cmbEstado.revalidate();
        cmbEstado.setEnabled(false);

        //Define a cidade fixa no combo
        modelCidades = new DefaultComboBoxModel(new String[]{estabelecimento.getCidade().getNome()});
        cmbCidade.setModel(modelCidades);
        cmbCidade.revalidate();
        cmbCidade.setEnabled(false);

        //Define o estabelecimento fixo no combo
        modelEstabelecimentos = new DefaultComboBoxModel(new String[]{estabelecimento.getNome()});
        cmbEstabelecimento.setModel(modelEstabelecimentos);
        cmbEstabelecimento.revalidate();
        cmbEstabelecimento.setEnabled(false);

        updateComboRedes();

    }

    private void updateListRedes() {
        listRedes = Rede.findAll();
        String[] data = new String[listRedes.size() + 1];
        data[0] = "Selecione uma Rede...";
        for (int i = 0; i < listRedes.size(); i++) {
            data[i + 1] = listRedes.get(i).getNome();
        }
        modelRedes = new DefaultComboBoxModel(data);
    }

    private void updateComboRedes() {
        updateListRedes();
        cmbRede.setModel(modelRedes);
        cmbRede.revalidate();
        rede = null;
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
    
    private void clearComboEstabelecimentos() {
        listEstabelecimento = null;
        String[] data = new String[]{"Selecione uma Cidade..."};
        modelEstabelecimentos = new DefaultComboBoxModel(data);
        cmbEstabelecimento.setModel(modelEstabelecimentos);
        cmbEstabelecimento.revalidate();
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
    
    private void updateComboEstabelecimentos() {
        listEstabelecimento = null;
        listEstabelecimento = cidade.getEstabelecimentos();
        String[] data = new String[listEstabelecimento.size() + 1];
        data[0] = "Selecione um Estabelecimento...";
        for (int i = 0; i < listEstabelecimento.size(); i++) {
            data[i + 1] = listEstabelecimento.get(i).getNome();
        }
        modelEstabelecimentos = new DefaultComboBoxModel(data);
        cmbEstabelecimento.setModel(modelEstabelecimentos);
        cmbEstabelecimento.revalidate();
        estabelecimento = null;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        jLabel4 = new javax.swing.JLabel();
        cmbEstado = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        cmbCidade = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        cmbEstabelecimento = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        cmbRede = new javax.swing.JComboBox();
        btnCadastrar = new javax.swing.JButton();
        btnFechar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Cadastro de Terminal");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Card Swiper.png"))); // NOI18N

        jLabel4.setText("Estado");

        cmbEstado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbEstadoActionPerformed(evt);
            }
        });

        jLabel5.setText("Cidade");

        cmbCidade.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbCidade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCidadeActionPerformed(evt);
            }
        });

        jLabel1.setText("Estabelecimento");

        cmbEstabelecimento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbEstabelecimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbEstabelecimentoActionPerformed(evt);
            }
        });

        jLabel2.setText("Rede");

        cmbRede.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbRede.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbRedeActionPerformed(evt);
            }
        });

        btnCadastrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Card Swiper Add.png"))); // NOI18N
        btnCadastrar.setText("Cadastrar");
        btnCadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadastrarActionPerformed(evt);
            }
        });

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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbRede, 0, 217, Short.MAX_VALUE)
                            .addComponent(cmbCidade, 0, 217, Short.MAX_VALUE)
                            .addComponent(cmbEstado, 0, 217, Short.MAX_VALUE)
                            .addComponent(cmbEstabelecimento, 0, 217, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnCadastrar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnFechar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbEstabelecimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbRede, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFechar)
                    .addComponent(btnCadastrar))
                .addContainerGap())
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
        updateComboEstabelecimentos();
    } else {
        cidade = null;
        clearComboEstabelecimentos();
    }
}//GEN-LAST:event_cmbCidadeActionPerformed

private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharActionPerformed
    dispose();
}//GEN-LAST:event_btnFecharActionPerformed

private void btnCadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastrarActionPerformed
    if (estado == null) {
        JOptionPane.showMessageDialog(this, "Selecione um Estado");
    } else if (cidade == null) {
        JOptionPane.showMessageDialog(this, "Selecione uma Cidade");
    } else if (estabelecimento == null) {
        JOptionPane.showMessageDialog(this, "Selecione um Estabelecimento");
    } else if (rede == null) {
        JOptionPane.showMessageDialog(this, "Selecione uma Rede");
    } else {
        int resposta = JOptionPane.showConfirmDialog(this, "Confirma cadastro de um terminal " + rede.getNome() + " no estabelecimento " + estabelecimento.getNome() + "?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (resposta == JOptionPane.YES_OPTION) {
            TerminalPOS terminal = new TerminalPOS();
            terminal.setEstabelecimento(estabelecimento);
            terminal.setRede(rede);
            boolean created = terminal.create();
            if (created) {
                JOptionPane.showMessageDialog(this, "Terminal cadastrado com sucesso");
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao Cadastrar o Terminal ");
            }
            dispose();
        }
    }
}//GEN-LAST:event_btnCadastrarActionPerformed

private void cmbEstabelecimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbEstabelecimentoActionPerformed
    if (cmbEstabelecimento.getSelectedIndex() != 0) {
        estabelecimento = listEstabelecimento.get(cmbEstabelecimento.getSelectedIndex() - 1);
    } else {
        estabelecimento = null;
    }
}//GEN-LAST:event_cmbEstabelecimentoActionPerformed

private void cmbRedeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbRedeActionPerformed
    if (cmbRede.getSelectedIndex() != 0) {
        rede = listRedes.get(cmbRede.getSelectedIndex() - 1);
    } else {
        rede = null;
    }
}//GEN-LAST:event_cmbRedeActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadastrar;
    private javax.swing.JButton btnFechar;
    private javax.swing.JComboBox cmbCidade;
    private javax.swing.JComboBox cmbEstabelecimento;
    private javax.swing.JComboBox cmbEstado;
    private javax.swing.JComboBox cmbRede;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLayeredPane jLayeredPane1;
    // End of variables declaration//GEN-END:variables
}
