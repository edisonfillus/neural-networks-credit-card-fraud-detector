package org.ai.forms.redesneurais.rn3d;

import javax.vecmath.Vector3d;

/**
 * Topologie definuji rozmisteni "kulicek" a "carecek" pro neurovis.
 */

public interface Topology {
    /**
     * Vyrobi pole 3dVektoru urcijici pozice "kulicek" v prostoru.
     */ 
    public Vector3d[] getInitialPositions();
    
    /**
     * Vyrobi pole dvojic urcujici,
     * ktere "kulicky" se kterymi chceme spojit "careckami"
     */
    public Ligacao[] getEdges();

}
