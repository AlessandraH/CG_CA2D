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

    public abstract Float[][] preencheCoordenadas(float x1, float y1, float x2, float y2);

    public Float[][] multiplicacaoMatrizes(Float[][] a, Float[][] b) {
        Float[][] matriz = new Float[3][b[0].length];
        int i, j, k;
        float auxiliar;
        for (i=0; i<3; i++) {
            for(j=0; j<b[0].length; j++) {
                auxiliar = 0f;
                for(k=0; k<a[0].length; k++) {
                    auxiliar += a[i][k] * b[k][j];
                }
                matriz[i][j] = auxiliar;
            }
        }
        return matriz;
    }

    public Float[][] transformarEmCoordenadasHomogeneas (Float[][] coordenadas) {
        Float[][] coordenadasHomogeneas = new Float[3][coordenadas[0].length];
        System.arraycopy(coordenadas, 0, coordenadasHomogeneas, 0, coordenadas.length);
        for(int i=0; i<coordenadasHomogeneas[0].length; i++) {
            coordenadasHomogeneas[3][i] = 1f;
        }
        return coordenadasHomogeneas;
    }

    public Float[][] rotacionar(Float[][] matrizObjetoHomogeneo, float angulo) {
        double anguloRadiano = (Math.PI/180) * angulo;
        Float[][] matrizRotacao = {
                {(float)Math.cos(anguloRadiano), (float)-Math.sin(anguloRadiano), 0f},
                {(float)Math.sin(anguloRadiano), (float)Math.cos(anguloRadiano), 0f},
                {0f, 0f, 1f}
        };
        return multiplicacaoMatrizes(matrizRotacao, matrizObjetoHomogeneo);
    }

    public Float[][] transladar(Float[][] matrizObjetoHomogeneo, float dx, float dy) {
        Float[][] matrizTranslacao = {
            {1f, 0f, dx},
            {0f, 1f, dy},
            {0f, 0f, 1f}
        };
        return multiplicacaoMatrizes(matrizTranslacao, matrizObjetoHomogeneo);
    }

    public Float[][] mudarEscala(Float[][] matrizObjetoHomogeneo, float sx, float sy) {
        Float[][] matrizMudancaEscala = {
                {sx, 0f, 0f},
                {0f, sy, 0f},
                {0f, 0f, 1f}
        };
        return multiplicacaoMatrizes(matrizMudancaEscala, matrizObjetoHomogeneo);
    }

    public void excluirFigura() {

    }
}
