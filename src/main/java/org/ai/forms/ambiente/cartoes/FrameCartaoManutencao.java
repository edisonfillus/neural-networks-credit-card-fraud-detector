/*
 * FrameCartaoManutencao.java
 *
 * Created on September 30, 2008, 2:28 PM
 */
package org.ai.forms.ambiente.cartoes;

import java.text.DecimalFormat;
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

import org.ai.bo.ambiente.Banco;
import org.ai.bo.ambiente.Bandeira;
import org.ai.bo.ambiente.Cartao;
import org.ai.bo.ambiente.Cliente;
import org.ai.bo.ambiente.Transacao;
import org.ai.forms.FormMenuPrincipal;
import org.ai.forms.ambiente.transacoes.FrameTransacaoManutencao;
import org.ai.forms.utils.ColumnResizer;

/**
 *
 * @author  Edison
 */
public class FrameCartaoManutencao extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;

    private List<Banco> listBancos;
    private List<Cliente> listClientes;
    private List<Bandeira> listBandeiras;
    private List<Transacao> listTransacoesCartao;
    private Banco banco;
    private Cliente cliente;
    private Bandeira bandeira;
    private Cartao cartao;
    private DefaultComboBoxModel modelBancos;
    private DefaultComboBoxModel modelClientes;
    private DefaultComboBoxModel modelBandeiras;
    private DefaultTableModel modelTransacoesCartao;
    private FormMenuPrincipal formPrincipal;

    public FrameCartaoManutencao(FormMenuPrincipal formPrincipal,Cartao cartao) {
        initComponents();
        this.formPrincipal = formPrincipal;
        this.cartao = cartao;
        txtNumeroCartao.setText(cartao.getNumCartao());
        txtCodSeguranca.setText(Integer.toString(cartao.getCodSeguranca()));
        txtAtivacao.setText(new SimpleDateFormat("dd/MM/yyyy").format(cartao.getDataAtivacao().getTime()));
        txtExpiracao.setText(new SimpleDateFormat("dd/MM/yyyy").format(cartao.getDataExpiracao().getTime()));
        txtLimite.setText(new DecimalFormat("#,###,###.00").format(cartao.getLimite()));
        chkBloqueado.setSelected(cartao.isBloqueado());
        updateComboClientes();
        cliente = cartao.getCliente();
        for (int i = 0; i < listClientes.size(); i++) {
            if (listClientes.get(i).getId() == cliente.getId()) {
                cmbCliente.setSelectedIndex(i + 1);
            }
        }
        updateComboBandeiras();
        bandeira = cartao.getBandeira();
        for (int i = 0; i < listBandeiras.size(); i++) {
            if (listBandeiras.get(i).getId() == bandeira.getId()) {
                cmbBandeira.setSelectedIndex(i + 1);
            }
        }
        updateComboBancos();
        banco = cartao.getBanco();
        for (int i = 0; i < listBancos.size(); i++) {
            if (listBancos.get(i).getCodBanco() == banco.getCodBanco()) {
                cmbBanco.setSelectedIndex(i + 1);
            }
        }
        updateTableTransacoesCartao();
    }


     public void updateTableTransacoesCartao() {
        updateListTransacoesCartao();
        tableTransacoesCartao.setModel(modelTransacoesCartao);
        tableTransacoesCartao.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableTransacoesCartao.setRowSelectionAllowed(true);
        tableTransacoesCartao.setColumnSelectionAllowed(false);
        ColumnResizer.adjustColumnPreferredWidths(tableTransacoesCartao);
        tableTransacoesCartao.revalidate();
        lblTotalTransacoesCartao.setText(Integer.toString(listTransacoesCartao.size()));
    }

    private void updateListTransacoesCartao() {
        listTransacoesCartao = cartao.getTransacoes();
        String[] collumnNames = new String[]{"Estabelecimento", "Data", "Valor"};
        String[][] data = new String[listTransacoesCartao.size()][];
        for (int i = 0; i < listTransacoesCartao.size(); i++) {
            data[i] = new String[]{
                        listTransacoesCartao.get(i).getTerminalPOS().getEstabelecimento().getNome(),
                        new SimpleDateFormat("dd/MM/yyyy").format(listTransacoesCartao.get(i).getDhTransacao().getTime()),
                        new DecimalFormat("#,###,###.00").format(listTransacoesCartao.get(i).getValorTotal())
                    };
        }
        modelTransacoesCartao = new DefaultTableModel(data, collumnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

    }
    
    
    private void updateListClientes() {
        listClientes = Cliente.findAll();
        String[] data = new String[listClientes.size() + 1];
        data[0] = "Selecione um Cliente...";
        for (int i = 0; i < listClientes.size(); i++) {
            data[i + 1] = listClientes.get(i).getNome();
        }
        modelClientes = new DefaultComboBoxModel(data);
    }

    private void updateComboClientes() {
        updateListClientes();
        cmbCliente.setModel(modelClientes);
        cmbCliente.revalidate();
        cliente = null;
    }

    private void updateListBancos() {
        listBancos = Banco.findAll();
        String[] data = new String[listBancos.size() + 1];
        data[0] = "Selecione um Banco...";
        for (int i = 0; i < listBancos.size(); i++) {
            data[i + 1] = listBancos.get(i).getNome();
        }
        modelBancos = new DefaultComboBoxModel(data);
    }

    private void updateComboBancos() {
        updateListBancos();
        cmbBanco.setModel(modelBancos);
        cmbBanco.revalidate();
        banco = null;
    }

    private void updateListBandeiras() {
        listBandeiras = Bandeira.findAll();
        String[] data = new String[listBandeiras.size() + 1];
        data[0] = "Selecione uma Bandeira...";
        for (int i = 0; i < listBandeiras.size(); i++) {
            data[i + 1] = listBandeiras.get(i).getNome();
        }
        modelBandeiras = new DefaultComboBoxModel(data);
    }

    private void updateComboBandeiras() {
        updateListBandeiras();
        cmbBandeira.setModel(modelBandeiras);
        cmbBandeira.revalidate();
        bandeira = null;
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
        btnSalvar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtNumeroCartao = new javax.swing.JTextField();
        txtCodSeguranca = new javax.swing.JTextField();
        cmbBanco = new javax.swing.JComboBox();
        cmbCliente = new javax.swing.JComboBox();
        cmbBandeira = new javax.swing.JComboBox();
        txtLimite = new javax.swing.JTextField();
        txtAtivacao = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        txtExpiracao = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        chkBloqueado = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableTransacoesCartao = new javax.swing.JTable();
        btnInserirTransacao = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        lblTotalTransacoesCartao = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();

        setClosable(true);
        setIconifiable(true);
        setTitle("Manutenção de Cartão");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Credit Card.png"))); // NOI18N

        btnFechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Symbol Delete 2.png"))); // NOI18N
        btnFechar.setText("Fechar");
        btnFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFecharActionPerformed(evt);
            }
        });

        btnSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Credit Card Edit 2.png"))); // NOI18N
        btnSalvar.setText("Salvar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        btnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Credit Card Delete.png"))); // NOI18N
        btnExcluir.setText("Excluir");
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        jLabel5.setText("Limite");

        jLabel6.setText("Ativação");

        jLabel4.setText("Bandeira");

        jLabel3.setText("Banco");

        jLabel2.setText("Cliente");

        jLabel1.setText("Numero");

        txtNumeroCartao.setEditable(false);

        cmbBanco.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbBanco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbBancoActionPerformed(evt);
            }
        });

        cmbCliente.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbClienteActionPerformed(evt);
            }
        });

        cmbBandeira.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbBandeira.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbBandeiraActionPerformed(evt);
            }
        });

        jLabel7.setText("Expiração");

        jLabel8.setText("Bloqueado");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbBanco, 0, 244, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNumeroCartao, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                        .addComponent(txtCodSeguranca, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbCliente, 0, 244, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addComponent(jLabel6)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbBandeira, 0, 244, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addGap(106, 106, 106)
                                        .addComponent(jLabel7))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(txtLimite, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtAtivacao, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                                        .addComponent(jLabel8)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtExpiracao, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(chkBloqueado))))))
                .addContainerGap(15, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNumeroCartao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(txtCodSeguranca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbBandeira, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(13, 13, 13)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtExpiracao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txtAtivacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtLimite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(jLabel8))
                    .addComponent(chkBloqueado))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Cartão", jPanel2);

        tableTransacoesCartao.setModel(new javax.swing.table.DefaultTableModel(
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
        tableTransacoesCartao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableTransacoesCartaoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableTransacoesCartao);

        btnInserirTransacao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/transaction_bank_add_16.png"))); // NOI18N
        btnInserirTransacao.setText("Inserir");
        btnInserirTransacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInserirTransacaoActionPerformed(evt);
            }
        });

        jLabel9.setText("Total Transações: ");

        lblTotalTransacoesCartao.setText("total");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnInserirTransacao)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotalTransacoesCartao))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(11, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInserirTransacao)
                    .addComponent(jLabel9)
                    .addComponent(lblTotalTransacoesCartao))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Transações", jPanel3);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 311, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 202, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Estatísticas", jPanel1);

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
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFechar)
                    .addComponent(btnSalvar)
                    .addComponent(btnExcluir))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharActionPerformed
    dispose();
}//GEN-LAST:event_btnFecharActionPerformed

private void cmbBancoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbBancoActionPerformed
    if (cmbBanco.getSelectedIndex() != 0) {
        banco = listBancos.get(cmbBanco.getSelectedIndex() - 1);
    } else {
        banco = null;
    }
}//GEN-LAST:event_cmbBancoActionPerformed

private void cmbClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbClienteActionPerformed
    if (cmbCliente.getSelectedIndex() != 0) {
        cliente = listClientes.get(cmbCliente.getSelectedIndex() - 1);
    } else {
        cliente = null;
    }
}//GEN-LAST:event_cmbClienteActionPerformed

private void cmbBandeiraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbBandeiraActionPerformed
    if (cmbBandeira.getSelectedIndex() != 0) {
        bandeira = listBandeiras.get(cmbBandeira.getSelectedIndex() - 1);
    } else {
        bandeira = null;
    }
}//GEN-LAST:event_cmbBandeiraActionPerformed

private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
    int resposta = JOptionPane.showConfirmDialog(this, "Confirma exclusão do cartão " + cartao.getNumCartao() + "?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    if (resposta == JOptionPane.YES_OPTION) {   
        boolean deleted = cartao.delete();
        if (deleted) {
            for(int i = 0 ; i < cartao.getCliente().getCartoes().size() ; i++){
                if (cartao.getCliente().getCartoes().get(i).getNumCartao().equals(cartao.getNumCartao())){
                    cartao.getCliente().getCartoes().remove(i);
                }
            }
            JOptionPane.showMessageDialog(this, "Cartão " + cartao.getNumCartao() + " excluido com sucesso");
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao excluir cartao " + cartao.getNumCartao());
        }
        dispose();
    }
}//GEN-LAST:event_btnExcluirActionPerformed

private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
    if (cliente == null) {
        JOptionPane.showMessageDialog(this, "Selecione um Cliente");
    } else if (banco == null) {
        JOptionPane.showMessageDialog(this, "Selecione um Banco");
    } else if (bandeira == null) {
        JOptionPane.showMessageDialog(this, "Selecione uma Bandeira");
    } else {
        int resposta = JOptionPane.showConfirmDialog(this, "Salvar alterações do Cartão " + cartao.getNumCartao() + "?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (resposta == JOptionPane.YES_OPTION) {
            cartao.setCliente(cliente);
            cartao.setBanco(banco);
            cartao.setBandeira(bandeira);
            cartao.setBloqueado(chkBloqueado.isSelected());
            cartao.setCodSeguranca(Integer.parseInt(txtCodSeguranca.getText()));
                try {
                    cartao.setLimite(new DecimalFormat("#,###,###.00").parse(txtLimite.getText()).doubleValue());
                } catch (ParseException ex) {
                    Logger.getLogger(FrameCartaoManutencao.class.getName()).log(Level.SEVERE, null, ex);
                }
            Calendar ativacao = Calendar.getInstance();
            Calendar expiracao = Calendar.getInstance();
            try {
                ativacao.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(txtAtivacao.getText()));
                expiracao.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(txtExpiracao.getText()));
            } catch (ParseException ex) {
                Logger.getLogger(FrameCartaoCadastro.class.getName()).log(Level.SEVERE, null, ex);
            }
            cartao.setDataAtivacao(ativacao);
            cartao.setDataExpiracao(expiracao);
            boolean updated = cartao.update();
            if (updated) {
                JOptionPane.showMessageDialog(this, "Cartão " + cartao.getNumCartao() + " salvo com sucesso");
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao salvar cartao " + cartao.getNumCartao());
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
            updateTableTransacoesCartao();
        }
        public void internalFrameIconified(InternalFrameEvent e) {}
        public void internalFrameDeiconified(InternalFrameEvent e) {}
        public void internalFrameActivated(InternalFrameEvent e) {}
        public void internalFrameDeactivated(InternalFrameEvent e) {}
    });
}//GEN-LAST:event_btnInserirTransacaoActionPerformed

private void tableTransacoesCartaoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableTransacoesCartaoMouseClicked
        if (tableTransacoesCartao.getSelectedRow() >= 0) {
        Transacao transacao = listTransacoesCartao.get(tableTransacoesCartao.getSelectedRow());
        FrameTransacaoManutencao frame = new FrameTransacaoManutencao(transacao);
        formPrincipal.getDesktopPane().add(frame);
        frame.setLocation((int) this.getBounds().getX() - frame.getWidth(), (int) this.getBounds().getY());
        frame.setVisible(true);
        frame.addInternalFrameListener(new InternalFrameListener() {
                public void internalFrameOpened(InternalFrameEvent e) {}
                public void internalFrameClosing(InternalFrameEvent e) {}
                public void internalFrameClosed(InternalFrameEvent e) {
                    cartao.setTransacoes(Cartao.find(cartao.getNumCartao()).getTransacoes());
                    updateTableTransacoesCartao();
                }
                public void internalFrameIconified(InternalFrameEvent e) {}
                public void internalFrameDeiconified(InternalFrameEvent e) {}
                public void internalFrameActivated(InternalFrameEvent e) {}
                public void internalFrameDeactivated(InternalFrameEvent e) {}
            });
    }
}//GEN-LAST:event_tableTransacoesCartaoMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnInserirTransacao;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JCheckBox chkBloqueado;
    private javax.swing.JComboBox cmbBanco;
    private javax.swing.JComboBox cmbBandeira;
    private javax.swing.JComboBox cmbCliente;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblTotalTransacoesCartao;
    private javax.swing.JTable tableTransacoesCartao;
    private javax.swing.JFormattedTextField txtAtivacao;
    private javax.swing.JTextField txtCodSeguranca;
    private javax.swing.JFormattedTextField txtExpiracao;
    private javax.swing.JTextField txtLimite;
    private javax.swing.JTextField txtNumeroCartao;
    // End of variables declaration//GEN-END:variables
}
