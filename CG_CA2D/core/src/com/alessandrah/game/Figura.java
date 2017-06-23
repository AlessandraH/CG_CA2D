package com.alessandrah.game;

/**
 * Created by Alessandra on 23/06/2017.
 */
public abstract class Figura {

    private Float[][] coordenadas;
    private int desenho;

    public Figura(int n) {
        if(n > 2) {
            this.coordenadas = new Float[2][n];
        }
        else {
            this.coordenadas = new Float[2][2];
        }
        this.desenho = n;
    }

    public Float[][] transformarEmCoordenadasHomogeneas (int n, Float[][] coordenadas) {
        Float[][] coordenadasHomogeneas = new Float[3][coordenadas[0].length];
        System.arraycopy(coordenadas, 0, coordenadasHomogeneas, 0, coordenadas.length);
        for(int i=0; i<coordenadasHomogeneas[0].length; i++) {
            coordenadasHomogeneas[3][i] = 1f;
        }
        return coordenadasHomogeneas;
    }
    public void rotacionar(float angulo) {
        double anguloRadiano = (Math.PI/180) * angulo;
        Float[][] matrizRotacao = {
                {(float)Math.cos(anguloRadiano), (float)-Math.sin(anguloRadiano), 0f},
                {(float)Math.sin(anguloRadiano), (float)Math.cos(anguloRadiano), 0f},
                {0f, 0f, 1f}
        };
    }
    public void transladar(float dx, float dy) {
        Float[][] matrizTranslacao = {
            {1f, 0f, dx},
            {0f, 1f, dy},
            {0f, 0f, 1f}
        };
    }
    public void mudarEscala(float sx, float sy) {
        Float[][] matrizMudancaEscala = {
                {sx, 0f, 0f},
                {0f, sy, 0f},
                {0f, 0f, 1f}
        };
    }
    public void excluirFigura() {

    }
}
