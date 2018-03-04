/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FrameTerminalManutencao.java
 *
 * Created on Oct 2, 2008, 11:17:28 PM
 */
package org.ai.forms.ambiente.terminais;

import java.text.DecimalFormat;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.table.DefaultTableModel;

import org.ai.bo.ambiente.Cidade;
import org.ai.bo.ambiente.Estabelecimento;
import org.ai.bo.ambiente.Estado;
import org.ai.bo.ambiente.Rede;
import org.ai.bo.ambiente.TerminalPOS;
import org.ai.bo.ambiente.Transacao;
import org.ai.forms.FormMenuPrincipal;
import org.ai.forms.ambiente.transacoes.FrameTransacaoManutencao;
import org.ai.forms.utils.ColumnResizer;

/**
 *
 * @author Edison
 */
public class FrameTerminalManutencao extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    
    // <editor-fold defaultstate="collapsed" desc="Atributos">
    
    private List<Estado> listEstados;
    private List<Cidade> listCidades;
    private List<Estabelecimento> listEstabelecimento;
    private List<Rede> listRedes;
    private List<Transacao> listTransacoesTerminal;
    private Estado estado;
    private Cidade cidade;
    private Estabelecimento estabelecimento;
    private Rede rede;
    private TerminalPOS terminal;
    private DefaultComboBoxModel modelEstados;
    private DefaultComboBoxModel modelCidades;
    private DefaultComboBoxModel modelEstabelecimentos;
    private DefaultComboBoxModel modelRedes;
    private DefaultTableModel modelTransacoesTerminal;
    private FormMenuPrincipal formPrincipal;

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Construtores">
    
    public FrameTerminalManutencao(FormMenuPrincipal formPrincipal,int idTerminal) {
        initComponents();
        this.formPrincipal = formPrincipal;
        this.terminal = TerminalPOS.find(idTerminal);
        this.estabelecimento = terminal.getEstabelecimento();
        this.estado = terminal.getEstabelecimento().getCidade().getEstado();
        this.cidade = terminal.getEstabelecimento().getCidade();

        txtIdTerminal.setText(Integer.toString(terminal.getId()));

        updateComboEstados();
        estado = terminal.getEstabelecimento().getCidade().getEstado();
        for (int i = 0; i < listEstados.size(); i++) {
            if (listEstados.get(i).getId() == estado.getId()) {
                cmbEstado.setSelectedIndex(i + 1);
            }
        }
        
        updateComboCidades();
        cidade = terminal.getEstabelecimento().getCidade();
        for (int i = 0; i < listCidades.size(); i++) {
            if (listCidades.get(i).getId() == cidade.getId()) {
                cmbCidade.setSelectedIndex(i + 1);
            }
        }
        
        updateComboEstabelecimentos();
        estabelecimento = terminal.getEstabelecimento();
        for (int i = 0; i < listEstabelecimento.size(); i++) {
            if (listEstabelecimento.get(i).getId() == estabelecimento.getId()) {
                cmbEstabelecimento.setSelectedIndex(i + 1);
            }
        }

        updateComboRedes();
        rede = terminal.getRede();
        for (int i = 0; i < listRedes.size(); i++) {
            if (listRedes.get(i).getId() == rede.getId()) {
                cmbRede.setSelectedIndex(i + 1);
            }
        }
        updateTableTransacoesTerminal();
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos">
    
    public void updateTableTransacoesTerminal() {
        updateListTransacoesEstabelecimento();
        tableTransacoesTerminal.setModel(modelTransacoesTerminal);
        tableTransacoesTerminal.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableTransacoesTerminal.setRowSelectionAllowed(true);
        tableTransacoesTerminal.setColumnSelectionAllowed(false);
        ColumnResizer.adjustColumnPreferredWidths(tableTransacoesTerminal);
        tableTransacoesTerminal.revalidate();
        lblTotalTransacoesTerminal.setText(Integer.toString(listTransacoesTerminal.size()));
    }

    private void updateListTransacoesEstabelecimento() {
        listTransacoesTerminal = terminal.getTransacoes();
        String[] collumnNames = new String[]{"Id", "Cartão", "Cliente", "Valor"};
        String[][] data = new String[listTransacoesTerminal.size()][];
        for (int i = 0; i < listTransacoesTerminal.size(); i++) {
            data[i] = new String[]{
                        Integer.toString(listTransacoesTerminal.get(i).getId()),
                        listTransacoesTerminal.get(i).getCartao().getNumCartao(),
                        listTransacoesTerminal.get(i).getCartao().getCliente().getNome(),
                        new DecimalFormat("#,###,###.00").format(listTransacoesTerminal.get(i).getValorTotal())
                    };
        }
        modelTransacoesTerminal = new DefaultTableModel(data, collumnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

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

    // </editor-fold>
    
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
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cmbRede = new javax.swing.JComboBox();
        cmbCidade = new javax.swing.JComboBox();
        cmbEstado = new javax.swing.JComboBox();
        cmbEstabelecimento = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        txtIdTerminal = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableTransacoesTerminal = new javax.swing.JTable();
        btnInserirTransacao = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        lblTotalTransacoesTerminal = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnExcluir = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Manutenção de Terminal");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Card Swiper.png"))); // NOI18N

        btnFechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Symbol Delete 2.png"))); // NOI18N
        btnFechar.setText("Fechar");
        btnFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFecharActionPerformed(evt);
            }
        });

        jLabel1.setText("Estabelecimento");

        jLabel4.setText("Estado");

        jLabel2.setText("Rede");

        jLabel5.setText("Cidade");

        cmbRede.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbRede.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbRedeActionPerformed(evt);
            }
        });

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

        cmbEstabelecimento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbEstabelecimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbEstabelecimentoActionPerformed(evt);
            }
        });

        jLabel3.setText("Numero");

        txtIdTerminal.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIdTerminal, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbEstado, 0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbCidade, 0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbEstabelecimento, 0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbRede, 0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIdTerminal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbEstabelecimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbRede, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(56, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Terminal", jPanel1);

        tableTransacoesTerminal.setModel(new javax.swing.table.DefaultTableModel(
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
        tableTransacoesTerminal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableTransacoesTerminalMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableTransacoesTerminal);

        btnInserirTransacao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/transaction_bank_add_16.png"))); // NOI18N
        btnInserirTransacao.setText("Inserir");
        btnInserirTransacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInserirTransacaoActionPerformed(evt);
            }
        });

        jLabel6.setText("Total Transaçoes:");

        lblTotalTransacoesTerminal.setText("total");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnInserirTransacao)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotalTransacoesTerminal)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInserirTransacao)
                    .addComponent(jLabel6)
                    .addComponent(lblTotalTransacoesTerminal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Transações", jPanel2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 311, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 218, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Estatísticas", jPanel3);

        btnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Card Swiper Delete .png"))); // NOI18N
        btnExcluir.setText("Excluir");
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        btnSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Card Swiper Edit 2.png"))); // NOI18N
        btnSalvar.setText("Salvar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnExcluir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSalvar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFechar))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSalvar)
                    .addComponent(btnExcluir)
                    .addComponent(btnFechar))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbRedeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbRedeActionPerformed
        if (cmbRede.getSelectedIndex() != 0) {
            rede = listRedes.get(cmbRede.getSelectedIndex() - 1);
        } else {
            rede = null;
        }
}//GEN-LAST:event_cmbRedeActionPerformed

    private void cmbCidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCidadeActionPerformed
        if (cmbCidade.getSelectedIndex() != 0) {
            cidade = listCidades.get(cmbCidade.getSelectedIndex() - 1);
            updateComboEstabelecimentos();
        } else {
            cidade = null;
            clearComboEstabelecimentos();
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

    private void cmbEstabelecimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbEstabelecimentoActionPerformed
        if (cmbEstabelecimento.getSelectedIndex() != 0) {
            estabelecimento = listEstabelecimento.get(cmbEstabelecimento.getSelectedIndex() - 1);
        } else {
            estabelecimento = null;
        }
}//GEN-LAST:event_cmbEstabelecimentoActionPerformed

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharActionPerformed
        dispose();
}//GEN-LAST:event_btnFecharActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        int resposta = JOptionPane.showConfirmDialog(this, "Confirma exclusão do terminal?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (resposta == JOptionPane.YES_OPTION) {
            boolean deleted = terminal.delete();
            if (deleted) {
                JOptionPane.showMessageDialog(this, "Terminal excluido com sucesso");
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao excluir terminal");
            }
            dispose();
        }
    }//GEN-LAST:event_btnExcluirActionPerformed

private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
    if (estado == null) {
        JOptionPane.showMessageDialog(this, "Selecione um Estado");
    } else if (cidade == null) {
        JOptionPane.showMessageDialog(this, "Selecione uma Cidade");
    } else if (estabelecimento == null) {
        JOptionPane.showMessageDialog(this, "Selecione um Estabelecimento");
    } else if (rede == null) {
        JOptionPane.showMessageDialog(this, "Selecione uma Rede");
    } else {
        int resposta = JOptionPane.showConfirmDialog(this, "Salvar alterações do Terminal?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (resposta == JOptionPane.YES_OPTION) {
            terminal.setEstabelecimento(estabelecimento);
            terminal.setRede(rede);
            boolean updated = terminal.update();
            if (updated) {
                JOptionPane.showMessageDialog(this, "Terminal salvo com sucesso");
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao Salvar o Terminal");
            }
            dispose();
        }
    }
}//GEN-LAST:event_btnSalvarActionPerformed

private void btnInserirTransacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInserirTransacaoActionPerformed
    FrameTransacaoManutencao frame = new FrameTransacaoManutencao();
    formPrincipal.getDesktopPane().add(frame);
    frame.setLocation((int) this.getBounds().getX() - frame.getWidth(), (int) this.getBounds().getY());
    frame.setVisible(true);
    frame.addInternalFrameListener(new InternalFrameListener() {
        public void internalFrameOpened(InternalFrameEvent e) {}
        public void internalFrameClosing(InternalFrameEvent e) {}
        public void internalFrameClosed(InternalFrameEvent e) {
            terminal.setTransacoes(TerminalPOS.find(terminal.getId()).getTransacoes());
            updateTableTransacoesTerminal();
        }
        public void internalFrameIconified(InternalFrameEvent e) {}
        public void internalFrameDeiconified(InternalFrameEvent e) {}
        public void internalFrameActivated(InternalFrameEvent e) {}
        public void internalFrameDeactivated(InternalFrameEvent e) {}
    });
}//GEN-LAST:event_btnInserirTransacaoActionPerformed

private void tableTransacoesTerminalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableTransacoesTerminalMouseClicked
    if (tableTransacoesTerminal.getSelectedRow() >= 0) {
        Transacao transacao = listTransacoesTerminal.get(tableTransacoesTerminal.getSelectedRow());
        FrameTransacaoManutencao frame = new FrameTransacaoManutencao(transacao);
        formPrincipal.getDesktopPane().add(frame);
        frame.setLocation((int) this.getBounds().getX() + this.getWidth(), (int) this.getBounds().getY());
        frame.setVisible(true);
        frame.addInternalFrameListener(new InternalFrameListener() {
                public void internalFrameOpened(InternalFrameEvent e) {}
                public void internalFrameClosing(InternalFrameEvent e) {}
                public void internalFrameClosed(InternalFrameEvent e) {
                     terminal.setTransacoes(TerminalPOS.find(terminal.getId()).getTransacoes());
                     updateTableTransacoesTerminal();
                }
                public void internalFrameIconified(InternalFrameEvent e) {}
                public void internalFrameDeiconified(InternalFrameEvent e) {}
                public void internalFrameActivated(InternalFrameEvent e) {}
                public void internalFrameDeactivated(InternalFrameEvent e) {}
            });
    }
}//GEN-LAST:event_tableTransacoesTerminalMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnInserirTransacao;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JComboBox cmbCidade;
    private javax.swing.JComboBox cmbEstabelecimento;
    private javax.swing.JComboBox cmbEstado;
    private javax.swing.JComboBox cmbRede;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblTotalTransacoesTerminal;
    private javax.swing.JTable tableTransacoesTerminal;
    private javax.swing.JTextField txtIdTerminal;
    // End of variables declaration//GEN-END:variables
}
