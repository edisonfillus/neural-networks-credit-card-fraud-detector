package org.ai.bo.ambiente;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

import org.ai.dao.factory.HsqldbDAOFactory;


@Entity
@Table(name="RESPOSTA",schema="public")
/**
 *
 * @author Edison
 */
public class Resposta implements Serializable {

    public static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Parâmetros">

    public static final int APROVADA = 0;
    public static final int REPROVADA = 50;
    public static final int CARTAO_EXPIRADO = 51;
    public static final int CARTAO_BLOQUEADO = 52;
    public static final int CARTAO_PERDIDO_ROUBADO = 57;
    public static final int TRANSACAO_DUPLICADA = 78;
    public static final int NAO_FOI_POSSIVEL_PROCESSAR_TRANSACAO = 100;
    public static final int LIMITE_EXCEDIDO = 110;
    public static final int ESTABELECIMENTO_NAO_CADASTRADO = 150;
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Atributos">


    @Id
    @Basic(optional=false)
    @Column(name="COD_RESPOSTA")
    private int cod;
    
    @Column(name="MENSAGEM")
    @Basic(optional=false)
    private String mensagem;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Propriedades">

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construtores">

    public Resposta() {
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Métodos Persistência">
    
     /**
     * Método para criar o objeto no local de persistência
     * @return <code>true</code> Se executado com sucesso
     */
    public boolean create(){
        return HsqldbDAOFactory.getRespostaDAO().createResposta(this);
    }

    /**
     * Método para atualizar o objeto no local de persistência
     * @return
     */
    public boolean update(){
        return HsqldbDAOFactory.getRespostaDAO().updateResposta(this);
    }

    /**
     * Método para excluir o objeto no local de persistência
     * @return
     */
    public boolean delete(){
        return HsqldbDAOFactory.getRespostaDAO().deleteResposta(this);
    }

    /**
     * Método para localizar um objeto Resposta no local de persitência
     * @param cod
     * @return Resposta
     */
    public static Resposta find(int cod){
        return HsqldbDAOFactory.getRespostaDAO().findResposta(cod);
    }

    /**
     * Método para obter uma lista de Resposta do local de persistência
     * @return
     */
    public static List<Resposta> findAll(){
        return HsqldbDAOFactory.getRespostaDAO().listResposta();
    }

    /**
     * Método para obter a quantidade de Resposta do local de persistência
     * @return
     */
    public static int count(){
        return HsqldbDAOFactory.getRespostaDAO().getRespostaCount();
    }
    
    // </editor-fold>
    
}
