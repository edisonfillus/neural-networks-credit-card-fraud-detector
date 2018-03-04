package org.ai.forms.redesneurais.rn3d.primitivas;

import com.sun.j3d.utils.geometry.Sphere;

/**
 * Classe que herda de Sphere incluindo o atributo index, 
 * o comportamento para ser selecionável e o raio
 */
public class SphereNeuronio extends Sphere {

    public int index;

    /**
     * Construtor de SphereNeuronio
     * @param index indice do neurônio
     * @param radius raio
     */
    public SphereNeuronio(int index, float radius) {
        super(radius, Sphere.ENABLE_APPEARANCE_MODIFY | Sphere.ENABLE_GEOMETRY_PICKING, null);
        this.index = index;
        this.setPickable(true);
    }
}
