package org.ai.forms.redesneurais.rn3d;

import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.behaviors.mouse.*;
import com.sun.j3d.utils.picking.behaviors.*;
import com.sun.j3d.utils.picking.*;
import com.sun.j3d.utils.geometry.*;
import java.util.ArrayList;
import java.util.List;
import javax.media.j3d.*;
import javax.swing.JOptionPane;
import javax.vecmath.*;

import org.ai.forms.redesneurais.rn3d.listeners.NeuronioSelecionadoListener;
import org.ai.forms.redesneurais.rn3d.primitivas.ShapeSinapse;
import org.ai.forms.redesneurais.rn3d.primitivas.SphereNeuronio;
import org.ai.forms.utils.Utils3D;

/**
 * Classe de geração da arquitetura da Rede Neural em 3D
 * @author Edison
 */
public final class RedeNeural3D extends Canvas3D {

    private static final long serialVersionUID = 1L;    
    
    // <editor-fold defaultstate="collapsed" desc="Parâmetros">
    private final static float coneRadius = 0.1f;
    private final static float coneHeight = 0.2f;    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Atributos Ambiente 3D">
    
    private SimpleUniverse simpleU;
    private TransformGroup mainTransform;
    private BranchGroup netTopology;
    private BranchGroup neuronGroup;
    private Switch selectorSwt;
    private MouseTranslate selectorDrag;
    private Alpha selectorRotationAlpha;
    private Vector3d[] neuronPositions;
    private ShapeSinapse edgesMesh;
    private int lastSelected = -1;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Propriedades">
    
    public int getLastSelected() {
        return lastSelected;
    }

    public void setLastSelected(int sel) {
        lastSelected = sel;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Construtores">
    /**
     * Contrutor da Rede Neural 3D
     */
    public RedeNeural3D() {
        super(SimpleUniverse.getPreferredConfiguration());
        simpleU = new SimpleUniverse(this);

        View view = simpleU.getViewer().getView();
        view.setBackClipDistance(3 * view.getBackClipDistance());

        simpleU.addBranchGraph(createSceneGraph());
    }    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Listener Seleçao Neuronio">
    private List<NeuronioSelecionadoListener> listenersNeuronioSelecionado = new ArrayList<NeuronioSelecionadoListener>();

    /**
     * Adiciona um Listener para neurônio selecionado
     * @param listener
     */
    public void addNeuronioSelecionadoListener(NeuronioSelecionadoListener listener) {
        listenersNeuronioSelecionado.add(listener);
    }

    /**
     * Remove um Listener para neurônio selecionado
     * @param listener
     */
    public void removeNeuronioSelecionadoListener(NeuronioSelecionadoListener listener) {
        listenersNeuronioSelecionado.remove(listener);
    }

    /**
     * Notifica os Listeners com o index do neurônio selecionado
     * @param index
     */
    private void notifyNeuronioSelecionadoListeners(int index) {
        for (NeuronioSelecionadoListener listener : listenersNeuronioSelecionado) {
            listener.neuronioSelecionado(index);
        }
    }    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Classe SelectNeuronBehavior">
    
    /**
     * Classe que estende PickMouseBehavior para simular o comportamento de seleção do neurônio
     * @author Edison
     */
    private class SelectNeuronBehavior extends PickMouseBehavior {

        SelectNeuronBehavior(BranchGroup root, Canvas3D canvas, Bounds bounds) {
            super(canvas, root, bounds);
            this.setSchedulingBounds(bounds);
        }

        public void updateScene(int xpos, int ypos) {
            SphereNeuronio neuron = null;

            if (!mevent.isMetaDown() && !mevent.isAltDown()) {

                //obchazi jeden bug v J3D - !!! - je podstatne pro spravne rozpoznani nejblizsiho
                pickCanvas.setTolerance(0.0f);
                pickCanvas.setMode(PickCanvas.GEOMETRY);

                pickCanvas.setShapeLocation(xpos, ypos);
                PickResult pr = pickCanvas.pickClosest();
                if ((pr != null) && ((neuron = (SphereNeuronio) pr.getNode(PickResult.PRIMITIVE)) != null)) {
                    selectNeuron(neuron.index);
                //UtilFreelistManager.pickResultFreelist.add(pr);
                } else {
                    //nez odoznacit, radsi nebudem delat nic
                    //selectNeuron( -1);
                }
            }
        }
    }
     
    // </editor-fold>

    /**
     * Método para criar um neurônio
     * @param index
     * @return
     */
    private Node createNeuron(int index) {
        Transform3D transform = new Transform3D();
        transform.setTranslation(neuronPositions[index]);
        TransformGroup neuronPosun = new TransformGroup(transform);
        neuronPosun.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
        neuronPosun.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        //Criação da esfera
        SphereNeuronio sph = new SphereNeuronio(index, 0.3f);

        //Define a aparência da esfera
        Appearance app = new Appearance();
        app.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_READ);
        ColoringAttributes ca = new ColoringAttributes();
        ca.setCapability(ColoringAttributes.ALLOW_COLOR_WRITE);
        //Seta uma cor aleatória
        //ca.setColor((float) Math.random(), (float) Math.random(),(float) Math.random());
        ca.setColor(0F, 0F, 1F);
        app.setColoringAttributes(ca);
        sph.setAppearance(app);
        neuronPosun.addChild(sph);
        return neuronPosun;
    }

    /**
     * Seleciona o neurônio especificado.
     *
     * @param index
     */
    public void selectNeuron(int index) {
        if (index >= 0) {
            //transform para fazer o movimento de translação
            Transform3D transform = new Transform3D();
            transform.setTranslation(neuronPositions[index]);

            //Seta o cone
            selectorDrag.getTransformGroup().setTransform(transform);

            //Deixa visivel
            selectorSwt.setWhichChild(Switch.CHILD_ALL);

            //inicia a animação do cone
            selectorRotationAlpha.resume();

        } else {
            //Deixa o cone invisivel
            selectorSwt.setWhichChild(Switch.CHILD_NONE);
            //Pausa a animação
            selectorRotationAlpha.pause();
        }
        notifyNeuronioSelecionadoListeners(index);
        lastSelected = index;
    }

    private Node createCones() {
        //Prepara a aparência
        Appearance app = new Appearance();
        ColoringAttributes ca = new ColoringAttributes();
        ca.setColor(1.0f, 1.0f, 0.0f);
        app.setColoringAttributes(ca);

        BranchGroup selectorGroup = new BranchGroup();

        Cone c;
        TransformGroup tg;
        Transform3D t3D = new Transform3D();
        t3D.setTranslation(new Vector3d(0.0, -0.4, 0.0));

        {
            tg = new TransformGroup(t3D);
            c = new Cone(coneRadius, coneHeight, app);
            tg.addChild(c);

            selectorGroup.addChild(tg);
        }

        return selectorGroup;
    }

    private Node createSelector() {
        //nevim, jestli se jedna o nejjednodussi zpusob,
        //jak objektu docasne zakazat, aby se renderoval, ale co...
        selectorSwt = new Switch(Switch.CHILD_NONE);
        selectorSwt.setCapability(Switch.ALLOW_SWITCH_WRITE);

        //posouvani ukazovatka
        TransformGroup selectorPosun = new TransformGroup();
        selectorPosun.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        selectorPosun.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        //toceni
        TransformGroup selectorToc = new TransformGroup();
        selectorToc.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        selectorToc.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        //samotoceni
        Transform3D zAxis = new Transform3D();
        zAxis.rotX(Math.PI / 2);
        selectorRotationAlpha = new Alpha(-1, 4000);
        RotationInterpolator rotator =
                new RotationInterpolator(selectorRotationAlpha, selectorToc,
                zAxis,
                0.0f, (float) Math.PI * 2.0f);
        rotator.setSchedulingBounds(new BoundingSphere());
        selectorToc.addChild(rotator);

        //pouzivam navic behavior, kvuli update - ale je to nutny?
        selectorDrag = new MouseTranslate(MouseTranslate.MANUAL_WAKEUP);
        selectorDrag.setTransformGroup(selectorPosun);
        selectorPosun.addChild(selectorDrag);

        //pospojovat
        selectorToc.addChild(createCones());
        selectorPosun.addChild(selectorToc);
        selectorSwt.addChild(selectorPosun);

        return selectorSwt;
    }

    private void addNavigable(Group gr) {
        //zatim prazdna skupina pro vlastni neurony a spoje
        netTopology = new BranchGroup();
        netTopology.setCapability(BranchGroup.ALLOW_DETACH);
        gr.addChild(netTopology);
        //bude treba ji umet menit
        gr.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);

        //oznacovatko a s nim spojene objekty
        gr.addChild(createSelector());
    }

    /**
     * Método para criar o cenário do ambiente 3D
     * @return
     */
    private BranchGroup createSceneGraph() {
        //Grupo de objetos root
        BranchGroup objRoot = new BranchGroup();

        //Seta o fundo
        Background backg = new Background(1f, 1f, 1f); //branco
        //Background backg = new Background(194 / 255.0f, 224 / 255.0f, 237 / 255.0f);
        backg.setApplicationBounds(new BoundingSphere());
        objRoot.addChild(backg);

        //Cenário Principal
        mainTransform = new TransformGroup();
        mainTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        mainTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        //Inclusão do cenário principal
        objRoot.addChild(mainTransform);

        //nechame si pod ni pridat vse (,co se bude otacet, posouvat ci menit vzdalenost)
        addNavigable(mainTransform);

        //zajistime ovladani pomoci mysi

        MouseRotate myMouseRotate = new MouseRotate();
        myMouseRotate.setTransformGroup(mainTransform);
        myMouseRotate.setSchedulingBounds(new BoundingSphere());
        objRoot.addChild(myMouseRotate);

        MouseTranslate myMouseTranslate = new MouseTranslate();
        myMouseTranslate.setTransformGroup(mainTransform);
        myMouseTranslate.setSchedulingBounds(new BoundingSphere());
        objRoot.addChild(myMouseTranslate);

        MouseZoom myMouseZoom = new MouseZoom();
        myMouseZoom.setTransformGroup(mainTransform);
        myMouseZoom.setSchedulingBounds(new BoundingSphere());
        objRoot.addChild(myMouseZoom);

        //cela scena se interne kompiluje, kvuli optimalizacim
        objRoot.compile();

        return objRoot;
    }

    /**
     * Limpa o cenário
     */
    public void cleanup() {
        simpleU.cleanup();
    }

    
    /**
     * Reinicia a posição da rede neural, definindo a distância
     * @param dist distância
     */
    public void resetView(float dist) {
        Transform3D myShift = new Transform3D();
        myShift.setTranslation(new Vector3d(0.0, 0.0, dist));
        mainTransform.setTransform(myShift);
    }

    
    public void setPosicao3D(Vector3d posicao) {
        Transform3D novaPosicao = new Transform3D();
        novaPosicao.setTranslation(posicao);
        mainTransform.setTransform(novaPosicao);
    }

    
    public Vector3d getPosicao3D() {
        Transform3D objRoot = new Transform3D();
        mainTransform.getTransform(objRoot);
        Vector3d posicao = new Vector3d();
        objRoot.get(posicao);
        return posicao;
    }

    
    private void resetTopology(Vector3d[] initialPositions, Ligacao[] edges) {
        //musime udelat deep-copy
        neuronPositions = new Vector3d[initialPositions.length];
        for (int i = 0; i < initialPositions.length; i++) {
            neuronPositions[i] = (Vector3d) initialPositions[i].clone();        //skupinka pro samotne neurony (pujdou oznacovat)
        }
        neuronGroup = new BranchGroup();
        neuronGroup.setCapability(BranchGroup.ALLOW_CHILDREN_READ);

        for (int i = 0; i < initialPositions.length; i++) {
            neuronGroup.addChild(createNeuron(i));        //oznacovaci behavior
        }
        BoundingSphere behaveBounds = new BoundingSphere();
        SelectNeuronBehavior selNeur = new SelectNeuronBehavior(neuronGroup, this,behaveBounds);
        neuronGroup.addChild(selNeur);

        //-----------------

        //Linhas
        edgesMesh = new ShapeSinapse(initialPositions, edges);
        

        //-----------------

        netTopology = new BranchGroup(); //uplne fungl nova
        netTopology.addChild(neuronGroup);
        netTopology.addChild(edgesMesh);
        netTopology.setCapability(BranchGroup.ALLOW_DETACH);

        mainTransform.setChild(netTopology, 0);
    }

    /**
     * Nastavi novou topologii pro neurovis.
     */
    public void resetTopology(Topology topology) {
        resetTopology(topology.getInitialPositions(), topology.getEdges());
    }

    /**
     * Nastavi barvu vybraneho neuronu.
     *
     * @param index neuron pro prebarveni
     * @param col nova barva
     */
    public void setColor(int index, Color3f col) {
        TransformGroup neuronSoup = (TransformGroup) neuronGroup.getChild(index);
        SphereNeuronio sph = (SphereNeuronio) neuronSoup.getChild(0);
        sph.getAppearance().getColoringAttributes().setColor(col);
    }

    /**
     * Nastavi barvu vybraneho neuronu. Barva se vypocita pomoci value2color z poslednich tri parametru.
     *
     * @param index neuron pro prebarveni         
     */
    public void setColor(int index, double value, double min, double max) {
        setColor(index, Utils3D.value2color(value, min, max));
    }

    /**
     * Prime nastaveni barvy RGB (0-255).
     */
    public void setColorRGB(int index, double r, double g, double b) {
        setColor(index, new Color3f((float) r / 256, (float) g / 256, (float) b / 256));
    }

    /**
     * Nova poloha neuronu v prostoru.
     *
     * @param index index neuronu, u ktereho chceme zmenit polohu
     * @param newPos nova poloha
     */
    public void setPosition(int index, Vector3d newPos) {
        //ulozit kvuli selectoru
        neuronPositions[index] = (Vector3d) newPos.clone();

        //poloha neuronu (kulicky)
        Transform3D transform = new Transform3D();
        transform.setTranslation(neuronPositions[index]);
        TransformGroup neuronPosun = (TransformGroup) neuronGroup.getChild(index);
        neuronPosun.setTransform(transform);

        //upravit hrany
        edgesMesh.setPosition(index, newPos);
    }

    /**
     * Vrati polohu vybraneho neuronu.
     *
     * @param index index neuronu, u ktereho chceme znat polohu
     */
    public Vector3d getPosition(int index) {
        return (Vector3d) neuronPositions[index].clone();
    }

    /**
     * Nastavi barvu spoje mezi neurony. Barva se vypocita pomoci value2color z poslednich tri parametru.
     */
    public void setEdgeColor(int index, double value, double min, double max) {
        setEdgeColor(index, Utils3D.value2color(value, min, max));
    }

    /**
     * Nastavi barvu spoje mezi neurony.
     *
     * @param index index do pole hran
     * @param col nova barva hrany
     */
    public void setEdgeColor(int index, Color3f col) {
        edgesMesh.setColor(index, col);
    }

    /**
     * <p>Pomocna funkce, ktera pro dva indexy kulicek (v nejake Topology)
     * spocita index hrany (napr pro setEdgeColor).</p>
     *
     * <p>Je to pochopitelne neefektivni. Lepsi by bylo, kdyby si
     * to kazda topologie pocitala sama, neb ta vi, jak tam ty hrany skladala.</p>
     */
    public static int edgeIndex(Ligacao[] edges, int from, int to) {
        for (int i = 0; i < edges.length; i++) {
            if ((edges[i].i == from && edges[i].j == to) ||
                    (edges[i].i == to && edges[i].j == from)) {
                return i;
            }
        }
        return -1;
    }

    // <editor-fold defaultstate="collapsed" desc="Antigo Main - Excluir">
    
    /**
     * Testovani novych featur neurovis.
     * @param topologia
     * @return 
     */
    public void main(List<Integer> topologia) {
        // Applet applet = new Applet();

//        final NeuroVis neuroVis = new NeuroVis();
//        neuroVis.resetTopology(new LayeredNetTopology(topologia));
//
//        neuroVis.resetView(-25.0f);
//        JButton button = new JButton();
//        button.setPreferredSize(new Dimension(800, 30));
//        button.setText("Fechar");
//
//        button.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                frame.dispose();
//            }
//        });
        /*
        JButton button = new JButton();
        
        button.setPreferredSize(new Dimension(300,50));                
        button.setText("Hokus - pokus");
        
        button.addActionListener( new ActionListener() {
        
        public void actionPerformed(ActionEvent e) {
        int idx = (int)(8*Math.random());
        Vector3d pos = neuroVis.getPosition(idx);
        pos.z += 2-Math.random()*4;
        neuroVis.setPosition(idx,pos);
        } 
        
        /* 
        public void actionPerformed(ActionEvent e) {
        int idx = (int)(12*Math.random());
        Color3f col = new Color3f(0.0f,0.0f,0.0f);                
        neuroVis.setEdgeColor(idx,col);
        } 
        
        });
        
         */
    }
    
    // </editor-fold>

    
}
