package com.alessandrah.game;

/**
 * Created by Alessandra on 23/06/2017.
 */
public class Linha extends Figura {

    public Linha(int n) {
        super(n);
    }

    @Override
    public Float[][] preencheCoordenadas(Float[][] coordenadas, float x, float y) {
        for(int clique=0; clique<2; clique ++) {
            coordenadas[0][clique] = x;
            coordenadas[1][clique] = y;
        }
        return coordenadas;
    }
}
