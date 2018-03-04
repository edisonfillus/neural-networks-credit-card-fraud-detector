package org.ai.forms.redesneurais.rn3d.primitivas;

import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.ai.forms.redesneurais.rn3d.*;

/**
 * Classe que estende Shape3D para criar as sinapses
 * @author Edison
 */
public class ShapeSinapse extends Shape3D {

    public ShapeSinapse(Vector3d[] initialPositions, Ligacao[] edges) {
        if (edges.length > 0) { 
            setGeometry(createGeometry(initialPositions, edges));
            setCapability(ENABLE_PICK_REPORTING);
            setCapability(ALLOW_PICKABLE_READ);
            this.setPickable(true);
            
        }
    }

    public void setPosition(int index, Vector3d newPos) {
        if (vahyLines != null) {
            Point3d thePoint = new Point3d(newPos);
            for (int i = 0, j = 0; i < ligacoes.length; i++) {
                if (ligacoes[i].i == index) {
                    vahyLines.setCoordinate(j, thePoint);
                }
                j++;
                if (ligacoes[i].j == index) {
                    vahyLines.setCoordinate(j, thePoint);
                }
                j++;
            }
        }
    }

    public void setColor(int index, Color3f col) {
        vahyLines.setColor(2 * index, col);
        vahyLines.setColor(2 * index + 1, col);
    }
    private LineArray vahyLines;
    private Ligacao[] ligacoes;

    private Geometry createGeometry(Vector3d[] initialPositions, Ligacao[] ligacoes) {

        this.ligacoes = ligacoes; //Para ser utilizado mais tarde

        vahyLines = new LineArray(2 * ligacoes.length, GeometryArray.COORDINATES | GeometryArray.COLOR_3);
        vahyLines.setCapability(LineArray.ALLOW_COORDINATE_WRITE);
        vahyLines.setCapability(LineArray.ALLOW_COLOR_WRITE);


        for (int i = 0, j = 0; i < ligacoes.length; i++) {
            vahyLines.setCoordinate(j++, new Point3d(initialPositions[ligacoes[i].i]));
            vahyLines.setCoordinate(j++, new Point3d(initialPositions[ligacoes[i].j]));
        }

        //Define a cor inicial da sinapse
        // Color3f colorWhite = new Color3f(1.0f, 1.0f, 1.0f);
        Color3f colorBlack = new Color3f(0.0f, 0.0f, 0.0f);

        for (int i = 0; i < 2 * ligacoes.length; i++) {
            vahyLines.setColor(i, colorBlack);
        }
        return vahyLines;
    }
}
