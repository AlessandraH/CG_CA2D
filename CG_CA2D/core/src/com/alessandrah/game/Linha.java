package com.alessandrah.game;

/**
 * Created by Alessandra on 23/06/2017.
 */
public class Linha extends Figura {

    public Linha(int n) {
        super(n);
    }

    @Override
    public Float[][] preencheCoordenadas(float x1, float y1, float x2, float y2) {
        Float[][] coordenadas = new Float[2][2];
        coordenadas[0][0] = x1;
        coordenadas[1][0] = y1;
        coordenadas[0][1] = x2;
        coordenadas[1][1] = y2;
        return coordenadas;
    }
}
