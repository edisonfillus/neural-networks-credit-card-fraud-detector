package org.ai.forms.redesneurais.rn3d;

import java.util.List;
import javax.vecmath.Vector3d;

/**
 * Topologie pro vrstevnate neuronove site.
 *
 * V jednom rozmneru ubihaji vrstvy,
 * ve druhem, kolmem, jsou neurony kazde vrstvy rozmisteny.
 * Spoje jsou jen mezi neurony sousednich vrstev stylem kazdy s kazdym.
 */

public class LayeredNetTopology implements Topology {
    private List<Integer> arquitetura;
    private double xDist = 4.0;
    private double yDist = 1.0;

    /**
     * Vyvori danou topologii.
     *
     * @param base pole udavajici pocty neuronu v jednotlivych vrstvach.     
     */    
    public LayeredNetTopology(List<Integer> arquitetura) {
        this.arquitetura = arquitetura;
    }

    /**
     * Vyvori danou topologii.
     *
     * @param base pole udavajici pocty neuronu v jednotlivych vrstvach.     
     * @param xDist vzdalenost mezi vsrtvami.
     * @param yDist vzdalenost mezi neurony ve vrstve.
     */
    LayeredNetTopology(List<Integer> arquitetura, double xDist, double yDist) {
        this(arquitetura);
        this.xDist = xDist;
        this.yDist = yDist;
    }

    public Vector3d[] getInitialPositions() {
        int count = 0;
        for (int i=0; i<arquitetura.size(); i++)
            count += arquitetura.get(i);

        Vector3d[] positions = new Vector3d[count];

        int idx = 0;
        double curX = -xDist*(arquitetura.size()-1)/2.0;
        double curY;
        for (int i=0; i<arquitetura.size(); i++) {
            curY = -yDist*(arquitetura.get(i)-1)/2.0;
            for(int j=0; j<arquitetura.get(i); j++) {
                positions[idx++] = new Vector3d(curX,curY,0.0);
                curY += yDist;
            }

            curX += xDist;
        }

        return positions;
    }

    public Ligacao[] getEdges() {
        int count = 0;
        for (int i = 0; i < arquitetura.size() - 1; i++)
            count += arquitetura.get(i) * arquitetura.get(i + 1);

        Ligacao[] edges = new Ligacao[count];

        int idx = 0;
        int off = 0;
        int curSize;
        for (int i = 0; i < arquitetura.size() - 1; i++) {
            curSize = arquitetura.get(i);
            for (int j = 0; j < arquitetura.get(i); j++)
                for (int k = 0; k < arquitetura.get(i + 1); k++)
                    edges[idx++] = new Ligacao(off+j,off+curSize+k);
            off += curSize;
        }

        return edges;
    }

    /**
     * Z dvojrozmernych indexu (vrstva, poradi ve vrstve) pocita index do pole predavaneho v getInitialPosition.     
     */    
    public int getIndex(int layer, int pos) {
        int sum = 0;
        for(int i=0; (i<arquitetura.size()) && (i<layer); i++)
            sum += arquitetura.get(i);
        return sum + pos;
    }

    /**
     * Z jednorozmerneho indexu 
     * (vraceneho napr. v {@link NeuronSelectListener#neuronSelected(int)})
     * extrahuje vrstvu.
     */     
    public int getLayer(int idx)
    {
        int sum = arquitetura.get(0);
        int i = 0;
        while(sum<=idx)
        { 
            ++i;
            sum+=arquitetura.get(i);
        }
        return i;
    }
       
    /**
     * Z jednorozmerneho indexu 
     * (vraceneho napr. v {@link NeuronSelectListener#neuronSelected(int)})
     * extrahuje pozici ve vrstve.
     *
     * @param idx index, ktery se ma prevest
     * @param layer jiz spocitane cislo vrstvy, ve ktere se neuron nachazi (pouzij {@link #getLayer(int)} )
     */     
    public int getPosition(int idx, int layer)
    {
         int sum = 0;
         for(int i = 0; i<layer; ++i)
             sum +=arquitetura.get(i);
         int pos = idx-sum;
         return pos;              
    }          
}
