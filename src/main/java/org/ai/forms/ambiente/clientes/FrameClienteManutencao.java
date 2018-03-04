/*
 * FrameClienteManutencao.java
 *
 * Created on Sep 29, 2008, 8:16:12 PM
 */
package org.ai.forms.ambiente.clientes;

import java.util.List;
import javax.swing.DefaultComboBoxModel;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.table.DefaultTableModel;

import org.ai.agentes.GeradorTransacoes;
import org.ai.bo.ambiente.Cartao;
import org.ai.bo.ambiente.Cidade;
import org.ai.bo.ambiente.Cliente;
import org.ai.bo.ambiente.Estado;
import org.ai.bo.ambiente.RamoAtividade;
import org.ai.bo.analise.GastoDia;
import org.ai.bo.analise.StatusAnalise;
import org.ai.bo.neural.rede.StatusRedeNeural;
import org.ai.forms.FormMenuPrincipal;
import org.ai.forms.FrameGerador;
import org.ai.forms.ambiente.cartoes.FrameCartaoCadastro;
import org.ai.forms.ambiente.cartoes.FrameCartaoManutencao;
import org.ai.forms.redesneurais.FrameRedeNeuralManutencao;
import org.ai.forms.utils.ColumnResizer;

/**
 *
 * @author Edison
 */
public class FrameClienteManutencao extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    
    private List<Estado> listEstados;
    private List<Cidade> listCidades;
    private List<Cartao> listCartoesCliente;
    private Estado estado;
    private Cidade cidade;
    private Cliente cliente;
    private DefaultComboBoxModel modelEstados;
    private DefaultComboBoxModel modelCidades;
    private DefaultTableModel modelCartoesCliente;

    FormMenuPrincipal formPrincipal;

    /** Creates new form FrameClienteManutencao
     * @param formPrincipal
     * @param idCliente 
     */
    public FrameClienteManutencao(FormMenuPrincipal formPrincipal, int idCliente) {
        initComponents();
        this.formPrincipal = formPrincipal;
        this.cliente = Cliente.find(idCliente);
        txtNomeCliente.setText(cliente.getNome());
        txtIdade.setText(Integer.toString(cliente.getIdade()));
        txtRenda.setText(new DecimalFormat("#,###,###.00").format(cliente.getRenda()));
        updateComboEstados();
        estado = cliente.getCidade().getEstado();
        for (int i = 0; i < listEstados.size(); i++) {
            if (listEstados.get(i).getId() == estado.getId()) {
                cmbEstado.setSelectedIndex(i + 1);
            }
        }
        updateComboCidades();
        cidade = cliente.getCidade();
        for (int i = 0; i < listCidades.size(); i++) {
            if (listCidades.get(i).getId() == cidade.getId()) {
                cmbCidade.setSelectedIndex(i + 1);
            }
        }
        listCartoesCliente = cliente.getCartoes();
        updateTableCartoesCliente();
        updateLabelsRedeNeural();
        updateLabelsAnalisePerfil();

    }

    public void addCartaoCliente(Cartao cartao) {
        listCartoesCliente.add(cartao);
        updateTableCartoesCliente();
    }

    public void deleteCartaoCliente(Cartao cartao) {
        listCartoesCliente.remove(cartao);
        updateTableCartoesCliente();
    }

    private void updateTableCartoesCliente() {
        listCartoesCliente = cliente.getCartoes();
        String[] collumnNames = new String[]{"Bandeira", "Numero", "Limite"};
        String[][] data = new String[listCartoesCliente.size()][];
        for (int i = 0; i < listCartoesCliente.size(); i++) {
            data[i] = new String[]{
                        listCartoesCliente.get(i).getBandeira().getNome(),
                        listCartoesCliente.get(i).getNumCartao(),
                        new DecimalFormat("#,###,###.00").format(listCartoesCliente.get(i).getLimite())
                    };
        }
        modelCartoesCliente = new DefaultTableModel(data, collumnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableCartoesCliente.setModel(modelCartoesCliente);
        tableCartoesCliente.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableCartoesCliente.setRowSelectionAllowed(true);
        tableCartoesCliente.setColumnSelectionAllowed(false);
        ColumnResizer.adjustColumnPreferredWidths(tableCartoesCliente);
        tableCartoesCliente.revalidate();
        lblTotalCartoesCliente.setText(Integer.toString(listCartoesCliente.size()));

    }

    private void updateTableGastosCliente() {
        List<GastoDia> gastos = cliente.getAnalisePerfil().getGastosDiarios();
        String[] collumnNames = new String[]{"Data", "Qtd", "Valor"};
        String[][] data = new String[gastos.size()][];
        for (int i = 0; i < gastos.size(); i++) {
            Calendar dia = Calendar.getInstance();
            dia.set(Calendar.DAY_OF_MONTH, gastos.get(i).dia);
            dia.set(Calendar.MONTH, gastos.get(i).mes);
            dia.set(Calendar.YEAR, gastos.get(i).ano);
            data[i] = new String[]{
                        new SimpleDateFormat("dd/MM/yyyy").format(dia.getTime()),
                        Integer.toString(gastos.get(i).qtd),
                        new DecimalFormat("#,###,###.00").format(gastos.get(i).valor)
                    };
        }
        DefaultTableModel tableModel = new DefaultTableModel(data, collumnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableGastos.setModel(tableModel);
        tableGastos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableGastos.setRowSelectionAllowed(true);
        tableGastos.setColumnSelectionAllowed(false);
        ColumnResizer.adjustColumnPreferredWidths(tableGastos);
        tableGastos.revalidate();
    }
    
    private void updateTableComprasRamo() {
        
        String[] collumnNames = new String[]{"Ramo de atividade", "Qtd"};
        String[][] data = new String[cliente.getAnalisePerfil().getCountComprasEfetuadasRamo().size()][];
        int i = 0;
        for(Entry<Integer,Integer> ramo : cliente.getAnalisePerfil().getCountComprasEfetuadasRamo().entrySet()){
            data[i] = new String[]{
                RamoAtividade.find(ramo.getKey()).getNome(),
                Integer.toString(ramo.getValue())
            };
            i++;
        }
        DefaultTableModel tableModel = new DefaultTableModel(data, collumnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableRamo.setModel(tableModel);
        tableRamo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableRamo.setRowSelectionAllowed(true);
        tableRamo.setColumnSelectionAllowed(false);
        ColumnResizer.adjustColumnPreferredWidths(tableRamo);
        tableRamo.revalidate();
    }

    private void updateTableComprasLocalidade() {
        String[] collumnNames = new String[]{"Local", "Qtd"};
        String[][] data = new String[4][];
        data[0] = new String[]{"Na cidade",Integer.toString(cliente.getAnalisePerfil().getCountComprasEfetuadasCidade())};
        data[1] = new String[]{"Na região",Integer.toString(cliente.getAnalisePerfil().getCountComprasEfetuadasRegiao())};
        data[2] = new String[]{"No estado",Integer.toString(cliente.getAnalisePerfil().getCountComprasEfetuadasEstado())};
        data[3] = new String[]{"Fora estado",Integer.toString(cliente.getAnalisePerfil().getCountComprasEfetuadasForaEstado())};

        DefaultTableModel tableModel = new DefaultTableModel(data, collumnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableLocalidade.setModel(tableModel);
        tableLocalidade.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableLocalidade.setRowSelectionAllowed(true);
        tableLocalidade.setColumnSelectionAllowed(false);
        ColumnResizer.adjustColumnPreferredWidths(tableLocalidade);
        tableLocalidade.revalidate();
    }

    private void updateComboEstados() {
        listEstados = Estado.findAll();
        String[] data = new String[listEstados.size() + 1];
        data[0] = "Selecione um Estado...";
        for (int i = 0; i < listEstados.size(); i++) {
            data[i + 1] = listEstados.get(i).getNome();
        }
        modelEstados = new DefaultComboBoxModel(data);
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

    private void updateLabelsRedeNeural(){
        lblNumCasos.setText(Integer.toString(cliente.getRedeNeural().getDadosTreinamento().getListCasosTreinamento().size()));
        lblStatusRedeNeural.setText(cliente.getRedeNeural().getStatus().getNome());
        if (cliente.getRedeNeural().getStatus() == StatusRedeNeural.TREINADA){
            lblDhUltimoTreinamentoRedeNeural.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss SSS").format(cliente.getRedeNeural().getDadosTreinamento().getDhUltimoTreinamento().getTime()));   
        } else {
            lblDhUltimoTreinamentoRedeNeural.setText("Não possui treinamento");
        }
        if (cliente.getRedeNeural().getDadosTreinamento().getListCasosTreinamento().isEmpty()){
            btnExibirRedeNeural.setEnabled(false);
        } else {
            btnExibirRedeNeural.setEnabled(true);
        }
        
    }
    
    private void updateLabelsAnalisePerfil(){
        lblValorMedioCompras.setText(new DecimalFormat("#,###,##0.00").format(cliente.getAnalisePerfil().getMediaValorCompras()));
        lblQuantidadeCompras.setText(Integer.toString(cliente.getAnalisePerfil().getCountComprasEfetuadasTotal()));
        lblMediaComprasDiarias.setText(new DecimalFormat("#,###,##0.00").format(cliente.getAnalisePerfil().getMediaComprasEfetuadasDiarias()));
        lblMediaValorComprasDiarias.setText(new DecimalFormat("#,###,##0.00").format(cliente.getAnalisePerfil().getMediaValorComprasDiaria()));
        lblQuantidadeDiasCompras.setText(Integer.toString(cliente.getAnalisePerfil().getCountDiasOcorreuCompras()));
        lblStatusAnalise.setText(cliente.getAnalisePerfil().getStatus().getNome().toUpperCase());
        if (cliente.getAnalisePerfil().getStatus() == StatusAnalise.ANALISADA){
            lblDhUltimaAnalise.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss SSS").format(cliente.getAnalisePerfil().getDhUltimaAnalise().getTime()));   
        } else {
            lblDhUltimaAnalise.setText("Não possui análise");
        }

        
        updateTableGastosCliente();
        updateTableComprasLocalidade();
        updateTableComprasRamo();

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
        btnFechar1 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtNomeCliente = new javax.swing.JTextField();
        cmbEstado = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        txtIdade = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtRenda = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cmbCidade = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableCartoesCliente = new javax.swing.JTable();
        btnInserirCartao = new javax.swing.JButton();
        btnGerarTransacoes = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btnAnalisar = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        lblValorMedioCompras = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        lblQuantidadeCompras = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblStatusAnalise = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lblDhUltimaAnalise = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lblMediaComprasDiarias = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lblMediaValorComprasDiarias = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lblQuantidadeDiasCompras = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableRamo = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableLocalidade = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableGastos = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        btnExibirRedeNeural = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lblNumCasos = new javax.swing.JLabel();
        lblStatusRedeNeural = new javax.swing.JLabel();
        lblDhUltimoTreinamentoRedeNeural = new javax.swing.JLabel();
        btnExcluir = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        lblTotalCartoesCliente = new javax.swing.JLabel();

        btnFechar.setText("Fechar");
        btnFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFecharActionPerformed(evt);
            }
        });

        setClosable(true);
        setIconifiable(true);
        setTitle("Manutenção de Cliente");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Bank User.png"))); // NOI18N

        btnFechar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Symbol Delete 2.png"))); // NOI18N
        btnFechar1.setText("Fechar");
        btnFechar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFechar1ActionPerformed(evt);
            }
        });

        jLabel4.setText("Estado");

        jLabel1.setText("Nome");

        cmbEstado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbEstadoActionPerformed(evt);
            }
        });

        jLabel2.setText("Idade");

        jLabel3.setText("Renda");

        jLabel5.setText("Cidade");

        cmbCidade.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbCidade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCidadeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel5)
                        .addComponent(jLabel2))
                    .addComponent(jLabel4)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(txtIdade, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtRenda, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cmbEstado, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtNomeCliente, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                    .addComponent(cmbCidade, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNomeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cmbCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIdade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(txtRenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(93, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Cliente", jPanel2);

        tableCartoesCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Bandeira", "Numero", "Limite"
            }
        ));
        tableCartoesCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableCartoesClienteMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableCartoesCliente);

        btnInserirCartao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Credit Card Add.png"))); // NOI18N
        btnInserirCartao.setText("Inserir Cartão");
        btnInserirCartao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInserirCartaoActionPerformed(evt);
            }
        });

        btnGerarTransacoes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/transaction_bank_level_16.png"))); // NOI18N
        btnGerarTransacoes.setText("Gerar Cartão e Transações");
        btnGerarTransacoes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGerarTransacoesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnInserirCartao)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnGerarTransacoes)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInserirCartao)
                    .addComponent(btnGerarTransacoes))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Cartões", jPanel3);

        btnAnalisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Bank User Rating.png"))); // NOI18N
        btnAnalisar.setText("Analisar Perfil");
        btnAnalisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnalisarActionPerformed(evt);
            }
        });

        jLabel13.setText("Média Valor de Compras:");

        lblValorMedioCompras.setText("valorMedioCompras");

        jLabel18.setText("Quantidade Compras:");

        lblQuantidadeCompras.setText("quantidadeCompras");

        jLabel10.setText("Status da Análise:");

        lblStatusAnalise.setText("statusAnalise");

        jLabel11.setText("Ultima Análise:");

        lblDhUltimaAnalise.setText("ultimaAnalise");

        jLabel12.setText("Media Qtd Compras Diárias:");

        lblMediaComprasDiarias.setText("mediaComprasDiarias");

        jLabel14.setText("Media Valor Compras Diárias:");

        lblMediaValorComprasDiarias.setText("mediaValorComprasDiarias");

        jLabel15.setText("Dias Ocorreram Compras:");

        lblQuantidadeDiasCompras.setText("quantidadeDiasCompras");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel15)
                    .addComponent(jLabel14)
                    .addComponent(jLabel12)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel18)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblStatusAnalise)
                    .addComponent(lblMediaComprasDiarias)
                    .addComponent(lblQuantidadeDiasCompras)
                    .addComponent(lblDhUltimaAnalise)
                    .addComponent(lblMediaValorComprasDiarias)
                    .addComponent(lblQuantidadeCompras)
                    .addComponent(lblValorMedioCompras))
                .addContainerGap(62, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(216, Short.MAX_VALUE)
                .addComponent(btnAnalisar)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(lblValorMedioCompras))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(lblQuantidadeCompras))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(lblQuantidadeDiasCompras))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(lblMediaComprasDiarias))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(lblMediaValorComprasDiarias))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(lblStatusAnalise))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(lblDhUltimaAnalise))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addComponent(btnAnalisar)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Perfil", jPanel1);

        tableRamo.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(tableRamo);

        tableLocalidade.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane4.setViewportView(tableLocalidade);

        tableGastos.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tableGastos);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane4, 0, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Distribuicao", jPanel6);

        btnExibirRedeNeural.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Node.png"))); // NOI18N
        btnExibirRedeNeural.setText("Exibir Rede Neural");
        btnExibirRedeNeural.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExibirRedeNeuralActionPerformed(evt);
            }
        });

        jLabel7.setText("Numero de Casos:");

        jLabel8.setText("Estado da Rede:");

        jLabel9.setText("Ultimo Treinamento:");

        lblNumCasos.setText("numCasos");

        lblStatusRedeNeural.setText("statusRede");

        lblDhUltimoTreinamentoRedeNeural.setText("ultimoTreinamento");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(192, Short.MAX_VALUE)
                .addComponent(btnExibirRedeNeural)
                .addContainerGap())
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblStatusRedeNeural)
                    .addComponent(lblDhUltimoTreinamentoRedeNeural)
                    .addComponent(lblNumCasos))
                .addContainerGap(143, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(lblNumCasos))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStatusRedeNeural)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDhUltimoTreinamentoRedeNeural)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 112, Short.MAX_VALUE)
                .addComponent(btnExibirRedeNeural))
        );

        jTabbedPane1.addTab("Rede Neural", jPanel4);

        btnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Bank User Delete.png"))); // NOI18N
        btnExcluir.setText("Excluir");
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        btnSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/facear/tcc/forms/resources/Bank User Edit 2.png"))); // NOI18N
        btnSalvar.setText("Salvar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        jLabel6.setText("Cartões: ");

        lblTotalCartoesCliente.setText("total");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotalCartoesCliente)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnExcluir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSalvar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFechar1))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalCartoesCliente)
                    .addComponent(jLabel6)
                    .addComponent(btnFechar1)
                    .addComponent(btnSalvar)
                    .addComponent(btnExcluir))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharActionPerformed
        dispose();
}//GEN-LAST:event_btnFecharActionPerformed

private void btnFechar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFechar1ActionPerformed
    dispose();
}//GEN-LAST:event_btnFechar1ActionPerformed

private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
    int resposta = JOptionPane.showConfirmDialog(this, "Confirma exclusão do cliente " + cliente.getNome() + "?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    if (resposta == JOptionPane.YES_OPTION) {
        boolean deleted = cliente.delete();
        if (deleted) {
            JOptionPane.showMessageDialog(this, "Cliente " + cliente.getNome() + " excluido com sucesso");
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao excluir cliente " + cliente.getNome());
        }
        dispose();
    }
}//GEN-LAST:event_btnExcluirActionPerformed

private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
    if (estado == null) {
        JOptionPane.showMessageDialog(this, "Selecione um Estado");
    } else if (cidade == null) {
        JOptionPane.showMessageDialog(this, "Selecione uma Cidade");
    } else {
        int resposta = JOptionPane.showConfirmDialog(this, "Salvar alterações do Cliente " + cliente.getNome() + "?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (resposta == JOptionPane.YES_OPTION) {
            cliente.setNome(txtNomeCliente.getText());
            cliente.setIdade(Integer.parseInt(txtIdade.getText()));
            try {
                cliente.setRenda(new DecimalFormat("#,###,###.00").parse(txtRenda.getText()).doubleValue());
            } catch (ParseException ex) {
                Logger.getLogger(FrameClienteCadastro.class.getName()).log(Level.SEVERE, null, ex);
            }
            cliente.setCidade(cidade);
            cliente.setCartoes(listCartoesCliente);
            boolean updated = cliente.update();
            if (updated) {
                JOptionPane.showMessageDialog(this, "Cliente " + cliente.getNome() + " salvo com sucesso");
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao Salvar o Cliente " + cliente.getNome());
            }
        }
    }
}//GEN-LAST:event_btnSalvarActionPerformed

private void btnExibirRedeNeuralActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExibirRedeNeuralActionPerformed
    FrameRedeNeuralManutencao frame = new FrameRedeNeuralManutencao(formPrincipal, cliente.getRedeNeural());
    formPrincipal.getDesktopPane().add(frame);
    frame.setLocation((int) this.getBounds().getX(), (int) this.getBounds().getY() + this.getHeight());
    frame.setVisible(true);
    frame.addInternalFrameListener(new InternalFrameListener() {
        public void internalFrameOpened(InternalFrameEvent e) {
        }
        public void internalFrameClosing(InternalFrameEvent e) {
        }
        public void internalFrameClosed(InternalFrameEvent e) {
            updateLabelsRedeNeural();
        }
        public void internalFrameIconified(InternalFrameEvent e) {
        }
        public void internalFrameDeiconified(InternalFrameEvent e) {
        }
        public void internalFrameActivated(InternalFrameEvent e) {
        }
        public void internalFrameDeactivated(InternalFrameEvent e) {
        }
    });
}//GEN-LAST:event_btnExibirRedeNeuralActionPerformed

private void btnInserirCartaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInserirCartaoActionPerformed
    FrameCartaoCadastro frame = new FrameCartaoCadastro(cliente);
    formPrincipal.getDesktopPane().add(frame);
    frame.setLocation((int) this.getBounds().getX(), (int) this.getBounds().getY() + this.getHeight());
    frame.setVisible(true);
    frame.addInternalFrameListener(new InternalFrameListener() {

        public void internalFrameOpened(InternalFrameEvent e) {
        }

        public void internalFrameClosing(InternalFrameEvent e) {
        }

        public void internalFrameClosed(InternalFrameEvent e) {
            cliente.setCartoes(Cliente.find(cliente.getId()).getCartoes());
            updateTableCartoesCliente();
        }

        public void internalFrameIconified(InternalFrameEvent e) {
        }

        public void internalFrameDeiconified(InternalFrameEvent e) {
        }

        public void internalFrameActivated(InternalFrameEvent e) {
        }

        public void internalFrameDeactivated(InternalFrameEvent e) {
        }
    });
}//GEN-LAST:event_btnInserirCartaoActionPerformed

private void tableCartoesClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableCartoesClienteMouseClicked
    Cartao cartao = listCartoesCliente.get(tableCartoesCliente.getSelectedRow());
    FrameCartaoManutencao frame = new FrameCartaoManutencao(formPrincipal, cartao);
    formPrincipal.getDesktopPane().add(frame);
    frame.setLocation((int) this.getBounds().getX(), (int) this.getBounds().getY() + this.getHeight());
    frame.setVisible(true);
    frame.addInternalFrameListener(new InternalFrameListener() {

        public void internalFrameOpened(InternalFrameEvent e) {
        }

        public void internalFrameClosing(InternalFrameEvent e) {
        }

        public void internalFrameClosed(InternalFrameEvent e) {
            cliente.setCartoes(Cliente.find(cliente.getId()).getCartoes());
            updateTableCartoesCliente();
        }

        public void internalFrameIconified(InternalFrameEvent e) {
        }

        public void internalFrameDeiconified(InternalFrameEvent e) {
        }

        public void internalFrameActivated(InternalFrameEvent e) {
        }

        public void internalFrameDeactivated(InternalFrameEvent e) {
        }
    });
}//GEN-LAST:event_tableCartoesClienteMouseClicked

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

private void btnAnalisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnalisarActionPerformed
    lblStatusAnalise.setText(StatusAnalise.ANALISANDO.getNome().toUpperCase());
    cliente.analisarPerfil();
    updateLabelsAnalisePerfil();
    updateLabelsRedeNeural();
}//GEN-LAST:event_btnAnalisarActionPerformed

private void btnGerarTransacoesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGerarTransacoesActionPerformed
    FrameGerador frame = new FrameGerador(cliente);
    formPrincipal.getDesktopPane().add(frame);
    frame.setLocation((int) this.getBounds().getX(), (int) this.getBounds().getY() + this.getHeight());
    frame.setVisible(true);
    frame.addInternalFrameListener(new InternalFrameListener() {

        public void internalFrameOpened(InternalFrameEvent e) {
        }

        public void internalFrameClosing(InternalFrameEvent e) {
        }

        public void internalFrameClosed(InternalFrameEvent e) {
            cliente.setCartoes(Cliente.find(cliente.getId()).getCartoes());
            updateTableCartoesCliente();
        }

        public void internalFrameIconified(InternalFrameEvent e) {
        }

        public void internalFrameDeiconified(InternalFrameEvent e) {
        }

        public void internalFrameActivated(InternalFrameEvent e) {
        }

        public void internalFrameDeactivated(InternalFrameEvent e) {
        }
    });
}//GEN-LAST:event_btnGerarTransacoesActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnalisar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnExibirRedeNeural;
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnFechar1;
    private javax.swing.JButton btnGerarTransacoes;
    private javax.swing.JButton btnInserirCartao;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JComboBox cmbCidade;
    private javax.swing.JComboBox cmbEstado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel18;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblDhUltimaAnalise;
    private javax.swing.JLabel lblDhUltimoTreinamentoRedeNeural;
    private javax.swing.JLabel lblMediaComprasDiarias;
    private javax.swing.JLabel lblMediaValorComprasDiarias;
    private javax.swing.JLabel lblNumCasos;
    private javax.swing.JLabel lblQuantidadeCompras;
    private javax.swing.JLabel lblQuantidadeDiasCompras;
    private javax.swing.JLabel lblStatusAnalise;
    private javax.swing.JLabel lblStatusRedeNeural;
    private javax.swing.JLabel lblTotalCartoesCliente;
    private javax.swing.JLabel lblValorMedioCompras;
    private javax.swing.JTable tableCartoesCliente;
    private javax.swing.JTable tableGastos;
    private javax.swing.JTable tableLocalidade;
    private javax.swing.JTable tableRamo;
    private javax.swing.JTextField txtIdade;
    private javax.swing.JTextField txtNomeCliente;
    private javax.swing.JTextField txtRenda;
    // End of variables declaration//GEN-END:variables
}
