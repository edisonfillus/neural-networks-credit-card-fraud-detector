package org.ai.console;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.ai.bo.ambiente.Estado;
import org.ai.dao.factory.Db4oDAOFactory;
import org.ai.forms.FormMenuPrincipal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jvnet.substance.skin.SubstanceOfficeSilver2007LookAndFeel;


/**
 *  @author Edison
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

    	Logger logger = LogManager.getLogger(Main.class);
    			
        Db4oDAOFactory.setKeepAliveConnection(true);

        Estado.count();
        
        JFrame.setDefaultLookAndFeelDecorated(true);
        try {
            UIManager.setLookAndFeel(new SubstanceOfficeSilver2007LookAndFeel()); //Seta Look and Feel Personalizado
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //Seta o Look and Feel do windows
            } catch (Exception ex) {
                logger.log(org.apache.logging.log4j.Level.WARN, "Não foi possivel Aplicar Skin", ex);
            }
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                FormMenuPrincipal form = new FormMenuPrincipal();
                form.setExtendedState(FormMenuPrincipal.MAXIMIZED_BOTH);
                form.setVisible(true);
            }
        });    
    
    // <editor-fold defaultstate="collapsed" desc="Teste Persistencia Cliente e CartÃµes">
        /*
    Cliente cli = new Cliente();
    cli.setNome("Jefferson");
    cli.setCidade(PostgreSqlDAOFactory.getCidadeDAO().findCidade(1));
    cli.setCpf("30303233032");
    cli.setIdade(23);
    cli.setRenda(1500D);
    
    PostgreSqlDAOFactory.getClienteDAO().createCliente(cli);
    
    
    Cartao card1 = new Cartao();
    
    card1.setBanco(PostgreSqlDAOFactory.getBancoDAO().findBanco(104));
    card1.setBandeira(Bandeira.VISA);
    card1.setBloqueado(false);
    card1.setCodSeguranca(101);
    card1.setDataAtivacao(Calendar.getInstance());
    card1.setDataExpiracao(Calendar.getInstance());
    card1.setLimite(1000.00);
    card1.setNumCartao("540202020233230302");
    card1.setCliente(cli);
    
    PostgreSqlDAOFactory.getCartaoDAO().createCartao(card1);
    
    Cartao card2 = new Cartao();
    
    card2.setBanco(PostgreSqlDAOFactory.getBancoDAO().findBanco(104));
    card2.setBandeira(Bandeira.MASTERCARD);
    card2.setBloqueado(false);
    card2.setCodSeguranca(122);
    card2.setDataAtivacao(Calendar.getInstance());
    card2.setDataExpiracao(Calendar.getInstance());
    card2.setLimite(1000.00);
    card2.setNumCartao("540322020233230302");
    card2.setCliente(cli);
    
    PostgreSqlDAOFactory.getCartaoDAO().createCartao(card2);
    
    PostgreSqlDAOFactory.getCartaoDAO().deleteCartao(card1);
    PostgreSqlDAOFactory.getCartaoDAO().deleteCartao(card2);
    PostgreSqlDAOFactory.getClienteDAO().deleteCliente(cli);
    
     */    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Teste Recuperacao Regiao e Cidades">
    /*
    for (Estado estado : PostgreSqlDAOFactory.getEstadoDAO().listEstado()) {
    for (Cidade cidade : estado.getCidades()) {
    System.out.println(estado.getNome() + " - " + cidade.getNome());
    }
    }
    
    for (Regiao regiao : PostgreSqlDAOFactory.getRegiaoDAO().listRegiao()) {
    for (Cidade cidade : regiao.getCidades()) {
    System.out.println(regiao.getNome() + " - " + cidade.getNome());
    
    }
    }
     */    // </editor-fold>
    }
}
