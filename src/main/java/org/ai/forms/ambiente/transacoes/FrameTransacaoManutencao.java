/*
 * FrameTransacaoCadastro.java
 *
 * Created on October 1, 2008, 8:02 PM
 */
package org.ai.forms.ambiente.transacoes;

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
import javax.swing.table.DefaultTableModel;

import org.ai.bo.ambiente.Analista;
import org.ai.bo.ambiente.Cartao;
import org.ai.bo.ambiente.Cliente;
import org.ai.bo.ambiente.Estabelecimento;
import org.ai.bo.ambiente.Item;
import org.ai.bo.ambiente.StatusAlerta;
import org.ai.bo.ambiente.StatusTransacao;
import org.ai.bo.ambiente.TerminalPOS;
import org.ai.bo.ambiente.Transacao;
import org.ai.bo.analise.Caso;
import org.ai.bo.analise.CasoCliente;
import org.ai.bo.analise.StatusAnalise;
import org.ai.bo.neural.rede.RedeNeural;
import org.ai.forms.utils.ColumnResizer;

/**
 *
 * @author  Edison
 */
public class FrameTransacaoManutencao extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Atributos">
    private List<Cartao> listCartoes;
    private List<Cliente> listClientes;
    private List<Estabelecimento> listEstabelecimentos;
    private List<TerminalPOS> listTerminais;
    private Transacao transacao;
    private Analista analista;
    private Cliente cliente;
    private Estabelecimento estabelecimento;
    private List<Analista> listAnalistas;
    private DefaultComboBoxModel modelAnalistas;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construtores">
    public FrameTransacaoManutencao() {
        initComponents();
        transacao = new Transacao();
        txtStatus.setText(StatusTransacao.CADASTRANDO.getNome());
        updateComboClientes();
        updateComboEstabelecimentos();
        loadDadosAnalise();
        loadDadosAlerta();
        chkAlterarData.setEnabled(true);
    }

    public FrameTransacaoManutencao(Transacao transacao) {
        initComponents();
        this.transacao = transacao;

        loadDadosCartao();
        loadDadosTerminal();
        loadDadosCompra();
        loadDadosAnalise();
        loadDadosAlerta();

        txtStatus.setText(transacao.getStatus().getNome());
        chkUtilizarTreinamento.setSelected(transacao.isFlagTreinamento());
        chkUtilizarTeste.setSelected(transacao.isFlagTeste());

        if (transacao.getStatus().getId() >= 2) {
            txtResposta.setText(transacao.getResposta().getMensagem());
            txtTempoResposta.setText(Long.toString(transacao.getTempoResposta()));
            txtDhTransacao.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss SSS").format(transacao.getDhTransacao().getTime()));
            txtCodAutorizacao.setText(transacao.getCodAutorizacao());
            setComboClientes();
            setComboCartoes();
            setComboEstabelecimentos();
            setComboTerminais();
        }
        if (transacao.getStatus() == StatusTransacao.PROCESSADA) {
            btnProcessar.setVisible(false);
            btnAnalisar.setVisible(true);
            btnAnalisar.setEnabled(true);
        }
        if (transacao.getStatus().getId() >= 4) {
            btnProcessar.setVisible(false);
        }



    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Métodos">
    public void loadDadosCartao() {
        txtEstado.setText(transacao.getCartao().getCliente().getCidade().getEstado().getNome());
        txtCidade.setText(transacao.getCartao().getCliente().getCidade().getNome());
        txtRegiao.setText(transacao.getCartao().getCliente().getCidade().getRegiao().getNome());
        txtBanco.setText(transacao.getCartao().getBanco().getNome());
        txtBandeira.setText(transacao.getCartao().getBandeira().getNome());
        txtAtivacao.setText(new SimpleDateFormat("dd/MM/yyyy").format(transacao.getCartao().getDataAtivacao().getTime()));
        txtExpiracao.setText(new SimpleDateFormat("dd/MM/yyyy").format(transacao.getCartao().getDataExpiracao().getTime()));
        txtLimite.setText(new DecimalFormat("#,###,###.00").format(transacao.getCartao().getLimite()));
        txtSaldo.setText(new DecimalFormat("#,###,###.00").format(transacao.getCartao().getSaldo()));
        chkBloqueado.setSelected(transacao.getCartao().isBloqueado());
    }

    public void clearDadosCartao() {
        txtEstado.setText("");
        txtCidade.setText("");
        txtRegiao.setText("");
        txtBanco.setText("");
        txtBandeira.setText("");
        txtAtivacao.setText("");
        txtExpiracao.setText("");
        txtLimite.setText("");
        txtSaldo.setText("");
        chkBloqueado.setSelected(false);
    }

    public void loadDadosTerminal() {

        txtEstadoTerminal.setText(transacao.getTerminalPOS().getEstabelecimento().getCidade().getEstado().getNome());
        txtCidadeTerminal.setText(transacao.getTerminalPOS().getEstabelecimento().getCidade().getNome());
        txtRegiaoTerminal.setText(transacao.getTerminalPOS().getEstabelecimento().getCidade().getRegiao().getNome());
        txtRedeTerminal.setText(transacao.getTerminalPOS().getRede().getNome());
        txtRamoTerminal.setText(transacao.getTerminalPOS().getEstabelecimento().getRamoAtividade().getNome());

    }

    public void clearDadosTerminal() {

        txtEstadoTerminal.setText("");
        txtCidadeTerminal.setText("");
        txtRegiaoTerminal.setText("");
        txtRedeTerminal.setText("");
        txtRamoTerminal.setText("");

    }

    public void loadDadosCompra() {
        txtIdOrdem.setText(Integer.toString(transacao.getIdOrdem()));
        txtIdOrdem.setEnabled(false);
        txtValorCompra.setText(new DecimalFormat("#,###,##0.00").format(transacao.getValorTotal()));
        txtNomeItem.setEnabled(false);
        txtQuantidadeItem.setEnabled(false);
        txtValorUnitario.setEnabled(false);
        btnInserirItem.setEnabled(false);
        updateTableItensCompra();
        txtValorCompra.setEnabled(false);
    }

    public void loadDadosAnalise() {

        if (transacao.getAnaliseTransacao().getStatus() == StatusAnalise.ANALISADA) {
            lblPontuacaoGeralClientes.setText(Integer.toString(transacao.getAnaliseTransacao().getPontuacao()));
            lblDhUltimaAnalise.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss SSS").format(transacao.getAnaliseTransacao().getDhUltimaAnalise().getTime()));
            lblStatusAnalise.setText(transacao.getAnaliseTransacao().getStatus().getNome());
            updateTableEntradasRede();
        }else {
            if(transacao.getStatus().getId() >= StatusTransacao.PROCESSADA.getId()){
                updateTableEntradasRede();
            }
            lblPontuacaoGeralClientes.setText("0");
            lblStatusAnalise.setText(transacao.getAnaliseTransacao().getStatus().getNome());
            lblDhUltimaAnalise.setText("Não analisada");

        }


    }

    public void loadDadosAlerta() {
        if (transacao.getAlerta() == null) {
            jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfTab("Alerta"), false);

        } else {
            jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfTab("Alerta"), true);
            updateComboAnalistas();
            analista = transacao.getAlerta().getAnalista();
            if (analista != null) {
                for (int i = 0; i < listAnalistas.size(); i++) {
                    if (listAnalistas.get(i).getId() == analista.getId()) {
                        cmbAnalista.setSelectedIndex(i + 1);
                    }
                }
            }

            txtDhGeracao.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss SSS").format(transacao.getAlerta().getDhGeracao().getTime()));
            if (transacao.getAlerta().getStatus() != StatusAlerta.ATIVO) {
                txtDhConfirmacao.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss SSS").format(transacao.getAlerta().getDhConfirmacao().getTime()));
                txtDescricaoAlerta.setText(transacao.getAlerta().getDescricaoMotivo());
                txtDescricaoAlerta.setEnabled(false);
                btnCancelarAlerta.setVisible(false);
                btnConfirmarFraude.setVisible(false);
                btnFalsoPositivo.setVisible(false);
                cmbAnalista.setEnabled(false);
            } else {
                txtDhConfirmacao.setText("");
                txtDescricaoAlerta.setText("");
                txtDescricaoAlerta.setEnabled(true);
                btnCancelarAlerta.setVisible(true);
                btnConfirmarFraude.setVisible(true);
                btnFalsoPositivo.setVisible(true);
                cmbAnalista.setEnabled(true);
            }
            txtStatusAlerta.setText(transacao.getAlerta().getStatus().getNome());

        }

    }

    private void updateComboCartoes() {
        listCartoes = cliente.getCartoes();
        String[] data = new String[listCartoes.size() + 1];
        data[0] = "Selecione um Cartão...";
        for (int i = 0; i < listCartoes.size(); i++) {
            data[i + 1] = listCartoes.get(i).getNumCartao();
        }
        DefaultComboBoxModel comboModel = new DefaultComboBoxModel(data);
        cmbCartao.setModel(comboModel);
        cmbCartao.revalidate();
        clearDadosCartao();
    }

    private void setComboCartoes() {
        String[] data = new String[]{transacao.getCartao().getNumCartao()};
        DefaultComboBoxModel comboModel = new DefaultComboBoxModel(data);
        cmbCartao.setModel(comboModel);
        cmbCartao.revalidate();
        loadDadosCartao();
    }

    private void updateComboTerminais() {
        listTerminais = estabelecimento.getTerminaisPOS();
        String[] data = new String[listTerminais.size() + 1];
        data[0] = "Selecione um Terminal...";
        for (int i = 0; i < listTerminais.size(); i++) {
            data[i + 1] = listTerminais.get(i).getRede().getNome() + "(" + listTerminais.get(i).getId() + ")";
        }
        DefaultComboBoxModel comboModel = new DefaultComboBoxModel(data);
        cmbTerminal.setModel(comboModel);
        cmbTerminal.revalidate();
        clearDadosTerminal();
    }

    private void setComboTerminais() {
        String[] data = new String[]{transacao.getTerminalPOS().getRede().getNome() + "(" + transacao.getTerminalPOS().getId() + ")"};
        DefaultComboBoxModel comboModel = new DefaultComboBoxModel(data);
        cmbTerminal.setModel(comboModel);
        cmbTerminal.revalidate();
        loadDadosTerminal();
    }

    private void clearComboCartoes() {
        String[] data = new String[]{"Selecione um Cliente..."};
        DefaultComboBoxModel comboModel = new DefaultComboBoxModel(data);
        cmbCartao.setModel(comboModel);
        cmbCartao.revalidate();
    }

    private void clearComboTerminais() {
        String[] data = new String[]{"Selecione um Estabelecimento..."};
        DefaultComboBoxModel comboModel = new DefaultComboBoxModel(data);
        cmbTerminal.setModel(comboModel);
        cmbTerminal.revalidate();
    }

    private void updateComboClientes() {
        listClientes = Cliente.findAll();
        String[] data = new String[listClientes.size() + 1];
        data[0] = "Selecione um Cartão...";
        for (int i = 0; i < listClientes.size(); i++) {
            data[i + 1] = listClientes.get(i).getNome();
        }
        DefaultComboBoxModel comboModel = new DefaultComboBoxModel(data);
        cmbCliente.setModel(comboModel);
        cmbCliente.revalidate();
        cliente = null;
        clearComboCartoes();
    }

    private void setComboClientes() {
        String[] data = new String[]{transacao.getCartao().getCliente().getNome()};
        DefaultComboBoxModel comboModel = new DefaultComboBoxModel(data);
        cmbCliente.setModel(comboModel);
        cmbCliente.revalidate();
    }

    private void updateComboEstabelecimentos() {
        listEstabelecimentos = Estabelecimento.findAll();
        String[] data = new String[listEstabelecimentos.size() + 1];
        data[0] = "Selecione um Estabelecimento...";
        for (int i = 0; i < listEstabelecimentos.size(); i++) {
            data[i + 1] = listEstabelecimentos.get(i).getNome();
        }
        DefaultComboBoxModel comboModel = new DefaultComboBoxModel(data);
        cmbEstabelecimento.setModel(comboModel);
        cmbEstabelecimento.revalidate();
        estabelecimento = null;
        clearComboTerminais();
    }

    private void setComboEstabelecimentos() {
        String[] data = new String[]{transacao.getTerminalPOS().getEstabelecimento().getNome()};
        DefaultComboBoxModel comboModel = new DefaultComboBoxModel(data);
        cmbEstabelecimento.setModel(comboModel);
        cmbEstabelecimento.revalidate();
    }

    public void updateTableItensCompra() {
        String[] collumnNames = new String[]{"Item", "Qtd", "Valor Unit", "Total"};
        String[][] data = new String[transacao.getItens().size()][];
        for (int i = 0; i < transacao.getItens().size(); i++) {
            data[i] = new String[]{
                        transacao.getItens().get(i).getNome(),
                        Integer.toString(transacao.getItens().get(i).getQuantidade()),
                        new DecimalFormat("#,###,##0.00").format(transacao.getItens().get(i).getValor()),
                        new DecimalFormat("#,###,##0.00").format(transacao.getItens().get(i).getValor() * transacao.getItens().get(i).getQuantidade()),};
        }
        DefaultTableModel tableModel = new DefaultTableModel(data, collumnNames) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableItensCompra.setModel(tableModel);
        tableItensCompra.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableItensCompra.setRowSelectionAllowed(true);
        tableItensCompra.setColumnSelectionAllowed(false);
        ColumnResizer.adjustColumnPreferredWidths(tableItensCompra);
        tableItensCompra.revalidate();
        lblTotalItens.setText(Integer.toString(transacao.getItens().size()));
        if (transacao.getItens().size() > 0) {
            txtValorCompra.setEnabled(false);
            double total = 0F;
            for (Item item : transacao.getItens()) {
                total += item.getQuantidade() * item.getValor();
            }
            txtValorCompra.setText(new DecimalFormat("#,###,##0.00").format(total));
        } else {
            txtValorCompra.setEnabled(true);
        }

    }

    private void updateComboAnalistas() {
        listAnalistas = Analista.findAll();
        String[] data = new String[listAnalistas.size() + 1];
        data[0] = "Selecione um Analista...";
        for (int i = 0; i < listAnalistas.size(); i++) {
            data[i + 1] = listAnalistas.get(i).getNome();
        }
        modelAnalistas = new DefaultComboBoxModel(data);
        cmbAnalista.setModel(modelAnalistas);
        cmbAnalista.revalidate();
        analista = null;
    }

    private void updateTableEntradasRede() {
        Caso caso = new CasoCliente(transacao);
        String[] collumnNames = new String[]{"Entrada", "Valor"};
        String[][] data = new String[caso.getEntradas().size()][];
        for (int i = 0; i < caso.getEntradas().size(); i++) {
            data[i] = new String[]{
                        CasoCliente.getLabels().get(i),
                        Double.toString(caso.getEntradas().get(i))
                    };
        }
        DefaultTableModel tableModel = new DefaultTableModel(data, collumnNames) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableEntradasRede.setModel(tableModel);
        tableEntradasRede.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableEntradasRede.setRowSelectionAllowed(true);
        tableEntradasRede.setColumnSelectionAllowed(false);
        ColumnResizer.adjustColumnPreferredWidths(tableEntradasRede);
        tableEntradasRede.revalidate();
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        txtBanco = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txtBandeira = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtAtivacao = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtExpiracao = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        txtLimite = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        txtSaldo = new javax.swing.JTextField();
        chkBloqueado = new javax.swing.JCheckBox();
        jLabel33 = new javax.swing.JLabel();
        txtCidade = new javax.swing.JTextField();
        txtRegiao = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        txtEstado = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        cmbCartao = new javax.swing.JComboBox();
        jLabel20 = new javax.swing.JLabel();
        cmbCliente = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        txtRedeTerminal = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        txtCidadeTerminal = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        txtRegiaoTerminal = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        txtEstadoTerminal = new javax.swing.JTextField();
        txtRamoTerminal = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        cmbEstabelecimento = new javax.swing.JComboBox();
        cmbTerminal = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtIdOrdem = new javax.swing.JTextField();
        txtValorCompra = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        lblTotalItens = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableItensCompra = new javax.swing.JTable();
        btnInserirItem = new javax.swing.JButton();
        jLabel42 = new javax.swing.JLabel();
        txtNomeItem = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        txtQuantidadeItem = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        txtValorUnitario = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtResposta = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtTempoResposta = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtCodAutorizacao = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtDhTransacao = new javax.swing.JTextField();
        chkAlterarData = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        btnFraude = new javax.swing.JButton();
        chkUtilizarTreinamento = new javax.swing.JCheckBox();
        jLabel39 = new javax.swing.JLabel();
        lblDhUltimaAnalise = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        lblStatusAnalise = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        lblPontuacaoGeralClientes = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableEntradasRede = new javax.swing.JTable();
        chkUtilizarTeste = new javax.swing.JCheckBox();
        jPanel7 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cmbAnalista = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        txtDhGeracao = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        txtDhConfirmacao = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        txtStatusAlerta = new javax.swing.JTextField();
        btnConfirmarFraude = new javax.swing.JButton();
        btnFalsoPositivo = new javax.swing.JButton();
        btnCancelarAlerta = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDescricaoAlerta = new javax.swing.JTextArea();
        btnFechar = new javax.swing.JButton();
        btnProcessar = new javax.swing.JButton();
        btnAnalisar = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        txtStatus = new javax.swing.JTextField();
        btnExcluir = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Cadastro de Transação");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/transaction_bank_16.png"))); // NOI18N

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Credit Card_1.png"))); // NOI18N

        jLabel1.setText("Cartão");

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados"));

        jLabel21.setText("Banco");
        jLabel21.setEnabled(false);

        txtBanco.setEnabled(false);

        jLabel22.setText("Bandeira");
        jLabel22.setEnabled(false);

        txtBandeira.setEnabled(false);

        jLabel23.setText("Ativação");
        jLabel23.setEnabled(false);

        txtAtivacao.setEnabled(false);

        jLabel24.setText("Expiração");
        jLabel24.setEnabled(false);

        txtExpiracao.setEnabled(false);

        jLabel25.setText("Limite");
        jLabel25.setEnabled(false);

        txtLimite.setEnabled(false);

        jLabel26.setText("Saldo");
        jLabel26.setEnabled(false);

        txtSaldo.setEnabled(false);

        chkBloqueado.setText("Bloqueado?");
        chkBloqueado.setEnabled(false);

        jLabel33.setText("Cidade");
        jLabel33.setEnabled(false);

        txtCidade.setEnabled(false);

        txtRegiao.setEnabled(false);

        jLabel34.setText("Região");
        jLabel34.setEnabled(false);

        txtEstado.setEnabled(false);

        jLabel35.setText("Estado");
        jLabel35.setEnabled(false);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel34)
                            .addComponent(jLabel35)
                            .addComponent(jLabel33)))
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel25)
                        .addComponent(jLabel21)
                        .addComponent(jLabel22)
                        .addComponent(jLabel23)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(chkBloqueado))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(txtLimite, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                                .addComponent(jLabel26))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(txtAtivacao, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                                .addComponent(jLabel24)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtExpiracao, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(txtBanco, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                    .addComponent(txtCidade, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                    .addComponent(txtEstado, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                    .addComponent(txtRegiao, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                    .addComponent(txtBandeira, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(txtEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(txtRegiao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(txtCidade))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addGap(28, 28, 28))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(txtBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(txtBandeira, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtExpiracao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24)
                            .addComponent(jLabel23)
                            .addComponent(txtAtivacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(txtSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtLimite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkBloqueado))))
        );

        jLabel27.setText("Cod Seg");

        cmbCartao.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbCartao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCartaoActionPerformed(evt);
            }
        });

        jLabel20.setText("Cliente");

        cmbCliente.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbClienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel20))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cmbCartao, 0, 195, Short.MAX_VALUE)
                            .addComponent(cmbCliente, 0, 195, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel27)
                            .addComponent(cmbCartao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Cartão", jPanel1);

        jLabel2.setText("Terminal:");

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Card Swiper_1.png"))); // NOI18N

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder("Dados")));

        jLabel28.setText("Rede");
        jLabel28.setEnabled(false);

        txtRedeTerminal.setEnabled(false);

        jLabel30.setText("Cidade");
        jLabel30.setEnabled(false);

        txtCidadeTerminal.setEnabled(false);

        jLabel31.setText("Região");
        jLabel31.setEnabled(false);

        txtRegiaoTerminal.setEnabled(false);

        jLabel32.setText("Estado");
        jLabel32.setEnabled(false);

        txtEstadoTerminal.setEnabled(false);

        txtRamoTerminal.setEnabled(false);

        jLabel29.setText("Ramo");
        jLabel29.setEnabled(false);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel28)
                    .addComponent(jLabel31)
                    .addComponent(jLabel30)
                    .addComponent(jLabel32)
                    .addComponent(jLabel29))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtRegiaoTerminal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                    .addComponent(txtEstadoTerminal, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                    .addComponent(txtCidadeTerminal, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                    .addComponent(txtRedeTerminal, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                    .addComponent(txtRamoTerminal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(txtEstadoTerminal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(txtRegiaoTerminal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCidadeTerminal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRedeTerminal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(txtRamoTerminal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jLabel40.setText("Estabelecimento:");

        cmbEstabelecimento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbEstabelecimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbEstabelecimentoActionPerformed(evt);
            }
        });

        cmbTerminal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbTerminal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTerminalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel40)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cmbEstabelecimento, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbTerminal, 0, 240, Short.MAX_VALUE))
                        .addContainerGap(61, Short.MAX_VALUE))
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(49, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(cmbEstabelecimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbTerminal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(101, 101, 101))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel11)
                .addContainerGap(277, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Terminal", jPanel2);

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/basket_48.png"))); // NOI18N

        jLabel16.setText("Num Ordem");

        jLabel6.setText("Valor Total");

        jLabel17.setText("Total Itens:");

        lblTotalItens.setText("total");

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Itens"));

        tableItensCompra.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Item", "Quantidade", "Valor Unitário", "Total"
            }
        ));
        jScrollPane1.setViewportView(tableItensCompra);

        btnInserirItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/basket_add_16.png"))); // NOI18N
        btnInserirItem.setText("Inserir Item");
        btnInserirItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInserirItemActionPerformed(evt);
            }
        });

        jLabel42.setText("Nome");

        jLabel43.setText("Qtd");

        jLabel44.setText("Valor Unit");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel43)
                            .addComponent(jLabel42))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(txtQuantidadeItem, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel44)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtValorUnitario, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
                            .addComponent(txtNomeItem, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnInserirItem)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(txtNomeItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(txtQuantidadeItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44)
                    .addComponent(txtValorUnitario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInserirItem))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtValorCompra)
                            .addComponent(txtIdOrdem, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblTotalItens)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                        .addComponent(jLabel12))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(txtIdOrdem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtValorCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17)
                            .addComponent(lblTotalItens)))
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Compra", jPanel3);

        jLabel3.setText("Resposta");

        txtResposta.setEnabled(false);

        jLabel4.setText("Tempo");

        txtTempoResposta.setEnabled(false);

        jLabel5.setText("Cod Auth");

        txtCodAutorizacao.setEnabled(false);

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/transaction_bank_config_48.png"))); // NOI18N

        jLabel19.setText("Data Hora");

        txtDhTransacao.setEnabled(false);

        chkAlterarData.setText("Alterar Data");
        chkAlterarData.setEnabled(false);
        chkAlterarData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkAlterarDataActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txtDhTransacao, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(chkAlterarData))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addComponent(txtTempoResposta, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCodAutorizacao))
                            .addComponent(txtResposta, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 101, Short.MAX_VALUE)
                        .addComponent(jLabel13))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtResposta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtTempoResposta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(txtCodAutorizacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDhTransacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19)
                            .addComponent(chkAlterarData)))
                    .addComponent(jLabel13))
                .addContainerGap(235, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Processamento", jPanel4);

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/transaction_bank_zoom_48.png"))); // NOI18N

        btnFraude.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/alert_error_16.png"))); // NOI18N
        btnFraude.setText("Falso Negativo");
        btnFraude.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFraudeActionPerformed(evt);
            }
        });

        chkUtilizarTreinamento.setText("Treinamento");
        chkUtilizarTreinamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkUtilizarTreinamentoActionPerformed(evt);
            }
        });

        jLabel39.setText("Data Hora Ultima Análise:");

        lblDhUltimaAnalise.setText("datahora");

        jLabel41.setText("Status Análise:");

        lblStatusAnalise.setText("status");

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Rede Neural"));

        jLabel9.setText("Pontuação:");

        lblPontuacaoGeralClientes.setText("total");

        tableEntradasRede.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(tableEntradasRede);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPontuacaoGeralClientes)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPontuacaoGeralClientes)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        chkUtilizarTeste.setText("Teste");
        chkUtilizarTeste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkUtilizarTesteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel39)
                                        .addComponent(jLabel41))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblStatusAnalise)
                                        .addComponent(lblDhUltimaAnalise)))
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addComponent(chkUtilizarTreinamento)
                                    .addGap(18, 18, 18)
                                    .addComponent(chkUtilizarTeste)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
                                    .addComponent(btnFraude)))
                            .addContainerGap())
                        .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel39)
                            .addComponent(lblDhUltimaAnalise))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel41)
                            .addComponent(lblStatusAnalise))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(chkUtilizarTreinamento)
                        .addComponent(chkUtilizarTeste))
                    .addComponent(btnFraude))
                .addGap(25, 25, 25))
        );

        jTabbedPane1.addTab("Análise", jPanel5);

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/alert_48.png"))); // NOI18N

        jLabel7.setText("Analista");

        cmbAnalista.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbAnalista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbAnalistaActionPerformed(evt);
            }
        });

        jLabel8.setText("Geração");

        txtDhGeracao.setEnabled(false);

        jLabel36.setText("Confirmação");

        txtDhConfirmacao.setEnabled(false);

        jLabel37.setText("Descrição");

        jLabel38.setText("Status do Alerta");

        txtStatusAlerta.setEditable(false);
        txtStatusAlerta.setText("NÃO POSSUI ALERTA");

        btnConfirmarFraude.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/alert_error_16.png"))); // NOI18N
        btnConfirmarFraude.setText("Confirmar Fraude");
        btnConfirmarFraude.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmarFraudeActionPerformed(evt);
            }
        });

        btnFalsoPositivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/alert_ok_16.png"))); // NOI18N
        btnFalsoPositivo.setText("Falso Positivo");
        btnFalsoPositivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFalsoPositivoActionPerformed(evt);
            }
        });

        btnCancelarAlerta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/alert_delete_16.png"))); // NOI18N
        btnCancelarAlerta.setText("Cancelar");
        btnCancelarAlerta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarAlertaActionPerformed(evt);
            }
        });

        txtDescricaoAlerta.setColumns(20);
        txtDescricaoAlerta.setRows(5);
        jScrollPane2.setViewportView(txtDescricaoAlerta);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel37)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addGap(20, 20, 20)
                                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel8)
                                        .addComponent(jLabel7)))
                                .addComponent(jLabel36)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                            .addComponent(txtDhConfirmacao, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                            .addComponent(txtDhGeracao, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                            .addComponent(cmbAnalista, 0, 271, Short.MAX_VALUE))
                        .addGap(4, 4, 4)
                        .addComponent(jLabel15))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel38)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtStatusAlerta, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(btnConfirmarFraude)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFalsoPositivo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelarAlerta)
                        .addContainerGap())))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(cmbAnalista, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtDhGeracao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(txtDhConfirmacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel37)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelarAlerta)
                    .addComponent(btnFalsoPositivo)
                    .addComponent(btnConfirmarFraude))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(txtStatusAlerta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Alerta", jPanel7);

        btnFechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Symbol Delete 2.png"))); // NOI18N
        btnFechar.setText("Fechar");
        btnFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFecharActionPerformed(evt);
            }
        });

        btnProcessar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/transaction_bank_config_16.png"))); // NOI18N
        btnProcessar.setText("Processar");
        btnProcessar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessarActionPerformed(evt);
            }
        });

        btnAnalisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/transaction_bank_zoom_16.png"))); // NOI18N
        btnAnalisar.setText("Analisar");
        btnAnalisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnalisarActionPerformed(evt);
            }
        });

        jLabel18.setText("Status");
        jLabel18.setEnabled(false);

        txtStatus.setEnabled(false);

        btnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/transaction_bank_remove_16.png"))); // NOI18N
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
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnProcessar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAnalisar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExcluir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFechar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txtStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFechar)
                    .addComponent(btnExcluir)
                    .addComponent(btnAnalisar)
                    .addComponent(btnProcessar))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharActionPerformed
    dispose();
}//GEN-LAST:event_btnFecharActionPerformed

private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
    int resposta = JOptionPane.showConfirmDialog(this, "Confirma exclusão da Transação?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    if (resposta == JOptionPane.YES_OPTION) {
        boolean deleted = transacao.delete();
        if (deleted) {
            JOptionPane.showMessageDialog(this, "Transacao excluida com sucesso");
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao excluir transacao");
        }
        dispose();
    }
}//GEN-LAST:event_btnExcluirActionPerformed

private void btnAnalisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnalisarActionPerformed
    transacao.analisar();
    transacao.update();
    loadDadosAnalise();
    loadDadosAlerta();
    txtStatus.setText(transacao.getStatus().getNome());

}//GEN-LAST:event_btnAnalisarActionPerformed

private void btnFraudeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFraudeActionPerformed
    int resposta = JOptionPane.showConfirmDialog(this, "Fraude na transação foi confirmada?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    if (resposta == JOptionPane.YES_OPTION) {
        transacao.setStatus(StatusTransacao.FALSO_POSITIVO);
        boolean updated = transacao.update();
        txtStatus.setText(transacao.getStatus().getNome());
        if (updated) {
            JOptionPane.showMessageDialog(this, "Fraude confirmada com sucesso.");
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao confirmar fraude");
        }
    }
}//GEN-LAST:event_btnFraudeActionPerformed

private void chkUtilizarTreinamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkUtilizarTreinamentoActionPerformed
    transacao.setFlagTreinamento(chkUtilizarTreinamento.isSelected());
    transacao.update();
}//GEN-LAST:event_chkUtilizarTreinamentoActionPerformed

private void cmbAnalistaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbAnalistaActionPerformed
    if (cmbAnalista.getSelectedIndex() != 0) {
        analista = listAnalistas.get(cmbAnalista.getSelectedIndex() - 1);
    } else {
        analista = null;
    }
}//GEN-LAST:event_cmbAnalistaActionPerformed

private void btnConfirmarFraudeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmarFraudeActionPerformed
    if (analista == null) {
        JOptionPane.showMessageDialog(this, "Selecione um Analista");
    } else {
        int resposta = JOptionPane.showConfirmDialog(this, "Fraude na transação foi confirmada?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (resposta == JOptionPane.YES_OPTION) {
            transacao.getAlerta().setAnalista(analista);
            transacao.getAlerta().setDescricaoMotivo(txtDescricaoAlerta.getText());
            transacao.getAlerta().confirmarFraude();
            transacao.setStatus(StatusTransacao.FRAUDE);
            boolean updated = transacao.update();
            loadDadosAlerta();
            txtStatus.setText(transacao.getStatus().getNome());
            if (updated) {
                JOptionPane.showMessageDialog(this, "Fraude confirmada com sucesso.");
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao confirmar fraude");
            }

        }
    }
}//GEN-LAST:event_btnConfirmarFraudeActionPerformed

private void btnFalsoPositivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFalsoPositivoActionPerformed
    if (analista == null) {
        JOptionPane.showMessageDialog(this, "Selecione um Analista");
    } else {
        int resposta = JOptionPane.showConfirmDialog(this, "Confirma falso positivo?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (resposta == JOptionPane.YES_OPTION) {
            transacao.getAlerta().setAnalista(analista);
            transacao.getAlerta().setDescricaoMotivo(txtDescricaoAlerta.getText());
            transacao.getAlerta().falsoPositivo();
            transacao.setStatus(StatusTransacao.FALSO_POSITIVO);
            boolean updated = transacao.update();
            loadDadosAlerta();
            txtStatus.setText(transacao.getStatus().getNome());
            if (updated) {
                JOptionPane.showMessageDialog(this, "Falso positivo confirmado com sucesso.");
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao confirmar falso positivo");
            }
        }
    }
}//GEN-LAST:event_btnFalsoPositivoActionPerformed

private void btnCancelarAlertaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarAlertaActionPerformed

    int resposta = JOptionPane.showConfirmDialog(this, "Confirma cancelamento do alerta?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    if (resposta == JOptionPane.YES_OPTION) {
        transacao.getAlerta().cancelarAlerta();
        transacao.setStatus(StatusTransacao.ANALISE_OK);
        boolean updated = transacao.update();
        loadDadosAlerta();
        txtStatus.setText(transacao.getStatus().getNome());
        if (updated) {
            JOptionPane.showMessageDialog(this, "Alerta cancelado com sucesso.");
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao cancelar alerta");
        }
    }

}//GEN-LAST:event_btnCancelarAlertaActionPerformed

private void cmbCartaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCartaoActionPerformed
    if (cmbCartao.getSelectedIndex() != 0) {
        transacao.setCartao(listCartoes.get(cmbCartao.getSelectedIndex() - 1));
        loadDadosCartao();
    } else {
        transacao.setCartao(null);
        clearDadosCartao();
    }
}//GEN-LAST:event_cmbCartaoActionPerformed

private void btnInserirItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInserirItemActionPerformed
    Item item = new Item();
    item.setNome(txtNomeItem.getText());
    item.setQuantidade(Integer.parseInt(txtQuantidadeItem.getText()));
    try {
        item.setValor(new DecimalFormat("#,###,##0.00").parse(txtValorUnitario.getText()).doubleValue());
    } catch (ParseException ex) {
        Logger.getLogger(FrameTransacaoManutencao.class.getName()).log(Level.SEVERE, null, ex);
    }
    transacao.getItens().add(item);
    updateTableItensCompra();
    txtNomeItem.setText("");
    txtQuantidadeItem.setText("");
    txtValorUnitario.setText("");
}//GEN-LAST:event_btnInserirItemActionPerformed

private void cmbClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbClienteActionPerformed
    if (cmbCliente.getSelectedIndex() != 0) {
        cliente = listClientes.get(cmbCliente.getSelectedIndex() - 1);
        updateComboCartoes();
    } else {
        cliente = null;
        clearComboCartoes();
    }
}//GEN-LAST:event_cmbClienteActionPerformed

private void cmbEstabelecimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbEstabelecimentoActionPerformed
    if (cmbEstabelecimento.getSelectedIndex() != 0) {
        estabelecimento = listEstabelecimentos.get(cmbEstabelecimento.getSelectedIndex() - 1);
        updateComboTerminais();
    } else {
        estabelecimento = null;
        clearComboTerminais();
    }
}//GEN-LAST:event_cmbEstabelecimentoActionPerformed

private void cmbTerminalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTerminalActionPerformed
    if (cmbTerminal.getSelectedIndex() != 0) {
        transacao.setTerminalPOS(listTerminais.get(cmbTerminal.getSelectedIndex() - 1));
        loadDadosTerminal();
    } else {
        transacao.setTerminalPOS(null);
        clearDadosTerminal();
    }
}//GEN-LAST:event_cmbTerminalActionPerformed

private void chkAlterarDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkAlterarDataActionPerformed
    if (chkAlterarData.isSelected()) {
        txtDhTransacao.setEnabled(true);
        txtDhTransacao.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss SSS").format(Calendar.getInstance().getTime()));
    } else {
        txtDhTransacao.setEnabled(false);
        txtDhTransacao.setText("");
    }
}//GEN-LAST:event_chkAlterarDataActionPerformed

private void chkUtilizarTesteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkUtilizarTesteActionPerformed
    transacao.setFlagTeste(chkUtilizarTeste.isSelected());
    transacao.update();
}//GEN-LAST:event_chkUtilizarTesteActionPerformed

    private void btnProcessarActionPerformed(java.awt.event.ActionEvent evt) {
        if (transacao.getStatus() == StatusTransacao.CADASTRANDO) { //Se cadastro, então valida
            if (transacao.getCartao() == null) {
                JOptionPane.showMessageDialog(this, "Selecione um Cartão");
            } else if (transacao.getTerminalPOS() == null) {
                JOptionPane.showMessageDialog(this, "Selecione um Terminal POS");
            } else if (txtValorCompra.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Defina a compra");
            } else { //Validacao OK
                transacao.setIdOrdem(Integer.parseInt(txtIdOrdem.getText()));
                try {
                    transacao.setValorTotal(new DecimalFormat("#,###,##0.00").parse(txtValorCompra.getText()).doubleValue());
                } catch (ParseException ex) {
                    Logger.getLogger(FrameTransacaoManutencao.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (chkAlterarData.isSelected()) {
                    transacao.setDhTransacao(Calendar.getInstance());
                    try {
                        transacao.getDhTransacao().setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss SSS").parse(txtDhTransacao.getText()));
                    } catch (ParseException ex) {
                        Logger.getLogger(FrameTransacaoManutencao.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                boolean created = transacao.create();
                if (created) {
                    txtStatus.setText(StatusTransacao.PROCESSANDO.getNome());
                    transacao.processar();
                    transacao.update();
                    txtResposta.setText(transacao.getResposta().getMensagem());
                    txtTempoResposta.setText(Long.toString(transacao.getTempoResposta()));
                    txtCodAutorizacao.setText(transacao.getCodAutorizacao());
                    txtStatus.setText(StatusTransacao.PROCESSADA.getNome());
                    txtDhTransacao.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss SSS").format(transacao.getDhTransacao().getTime()));
                    btnProcessar.setVisible(false);
                    btnAnalisar.setVisible(true);
                    btnAnalisar.setEnabled(true);
                    chkAlterarData.setEnabled(false);
                    txtDhTransacao.setEnabled(false);
                    setComboClientes();
                    setComboCartoes();
                    setComboEstabelecimentos();
                    setComboTerminais();
                    loadDadosCompra();
                    loadDadosAnalise();
                    JOptionPane.showMessageDialog(this, "Transação processada com sucesso");
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao processar Transação ");
                }


            }
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnalisar;
    private javax.swing.JButton btnCancelarAlerta;
    private javax.swing.JButton btnConfirmarFraude;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFalsoPositivo;
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnFraude;
    private javax.swing.JButton btnInserirItem;
    private javax.swing.JButton btnProcessar;
    private javax.swing.JCheckBox chkAlterarData;
    private javax.swing.JCheckBox chkBloqueado;
    private javax.swing.JCheckBox chkUtilizarTeste;
    private javax.swing.JCheckBox chkUtilizarTreinamento;
    private javax.swing.JComboBox cmbAnalista;
    private javax.swing.JComboBox cmbCartao;
    private javax.swing.JComboBox cmbCliente;
    private javax.swing.JComboBox cmbEstabelecimento;
    private javax.swing.JComboBox cmbTerminal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JLabel lblDhUltimaAnalise;
    private javax.swing.JLabel lblPontuacaoGeralClientes;
    private javax.swing.JLabel lblStatusAnalise;
    private javax.swing.JLabel lblTotalItens;
    private javax.swing.JTable tableEntradasRede;
    private javax.swing.JTable tableItensCompra;
    private javax.swing.JTextField txtAtivacao;
    private javax.swing.JTextField txtBanco;
    private javax.swing.JTextField txtBandeira;
    private javax.swing.JTextField txtCidade;
    private javax.swing.JTextField txtCidadeTerminal;
    private javax.swing.JTextField txtCodAutorizacao;
    private javax.swing.JTextArea txtDescricaoAlerta;
    private javax.swing.JTextField txtDhConfirmacao;
    private javax.swing.JTextField txtDhGeracao;
    private javax.swing.JTextField txtDhTransacao;
    private javax.swing.JTextField txtEstado;
    private javax.swing.JTextField txtEstadoTerminal;
    private javax.swing.JTextField txtExpiracao;
    private javax.swing.JTextField txtIdOrdem;
    private javax.swing.JTextField txtLimite;
    private javax.swing.JTextField txtNomeItem;
    private javax.swing.JTextField txtQuantidadeItem;
    private javax.swing.JTextField txtRamoTerminal;
    private javax.swing.JTextField txtRedeTerminal;
    private javax.swing.JTextField txtRegiao;
    private javax.swing.JTextField txtRegiaoTerminal;
    private javax.swing.JTextField txtResposta;
    private javax.swing.JTextField txtSaldo;
    private javax.swing.JTextField txtStatus;
    private javax.swing.JTextField txtStatusAlerta;
    private javax.swing.JTextField txtTempoResposta;
    private javax.swing.JTextField txtValorCompra;
    private javax.swing.JTextField txtValorUnitario;
    // End of variables declaration//GEN-END:variables
}
