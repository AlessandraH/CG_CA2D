package com.alessandrah.game;

/**
 * Created by Alessandra on 24/06/2017.
 */
public class Circulo extends Figura {

    public Circulo(int n) {
        super(n);
    }

    @Override
    public void setCoordenadas(int clique, float x, float y) {
        this.coordenadas[0][clique] = x;
        this.coordenadas[1][clique] = y;
        if (clique == 1) {
            double raio = Math.sqrt(Math.pow((this.coordenadas[0][0] - this.coordenadas[0][1]), 2) + Math.pow((this.coordenadas[1][0] - this.coordenadas[1][1]), 2));
            this.coordenadas[0][1] = (float) raio;
            this.coordenadas[1][1] = null;
        }
    }
}
