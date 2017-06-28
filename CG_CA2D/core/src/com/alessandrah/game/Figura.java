package com.alessandrah.game;

/**
 * Created by Alessandra on 23/06/2017.
 */
public class Figura {

    protected Float[][] coordenadas;
    protected int desenho;

    public Figura() {
    }

    public Figura(int n) {
        if (n > 2) {
            this.coordenadas = new Float[2][n];
        } else { //circulo ou linha
            this.coordenadas = new Float[2][2];
        }
        this.desenho = n;
    }

    public void setCoordenadas(int clique, float x, float y) {
        this.coordenadas[0][clique] = x;
        this.coordenadas[1][clique] = y;
    }

    public int getDesenho() {
        return this.desenho;
    }

    public Float[][] getCoordenadas() {
        return this.coordenadas;
    }

    public Float[][] multiplicacaoMatrizes(Float[][] a, Float[][] b) {
        Float[][] matriz = new Float[3][b[0].length];
        int i, j, k;
        float auxiliar;
        for (i = 0; i < 3; i++) {
            for (j = 0; j < b[0].length; j++) {
                auxiliar = 0f;
                for (k = 0; k < a[0].length; k++) {
                    auxiliar += a[i][k] * b[k][j];
                }
                matriz[i][j] = auxiliar;
            }
        }
        return matriz;
    }

    public Float[][] copiaMatrizes(Float[][] fonte, Float[][] destino, int linha, int tam) {
        int i, j;
        for (i = 0; i < linha; i++) {
            for (j = 0; j < tam; j++) {
                destino[i][j] = fonte[i][j];
            }
        }
        return destino;
    }

    public Figura transformarEmCoordenadasHomogeneas(Figura figura) {
        Figura figuraTransformada = new Figura();
        figuraTransformada.desenho = figura.desenho;
        figuraTransformada.coordenadas = new Float[3][figura.coordenadas[0].length];
        figuraTransformada.coordenadas = copiaMatrizes(figura.coordenadas, figuraTransformada.coordenadas, 2, figura.coordenadas[0].length);
        for (int i = 0; i < figuraTransformada.coordenadas[0].length; i++) {
            figuraTransformada.coordenadas[2][i] = 1f;
        }
        return figuraTransformada;
    }

    public Figura rotacionar(Figura figuraTransformada, float angulo, float x, float y) {
        Float anguloRadiano = (float) Math.toRadians(angulo);
        Float[][] matrizTranslacao = {
                {1f, 0f, x},
                {0f, 1f, y},
                {0f, 0f, 1f}
        };
        Float[][] matrizRotacao = {
                {(float) Math.cos(anguloRadiano), (float) -Math.sin(anguloRadiano), (float) (y * Math.sin(anguloRadiano) - x * Math.cos(anguloRadiano))},
                {(float) Math.sin(anguloRadiano), (float) Math.cos(anguloRadiano), (float) (-x * Math.sin(anguloRadiano) - y * Math.cos(anguloRadiano))},
                {0f, 0f, 1f}
        };
        Float[][] resultado = multiplicacaoMatrizes(matrizRotacao, figuraTransformada.getCoordenadas());
        resultado = multiplicacaoMatrizes(matrizTranslacao,resultado);
        figuraTransformada.coordenadas = new Float[2][figuraTransformada.getDesenho()];
        figuraTransformada.coordenadas = copiaMatrizes(resultado, figuraTransformada.coordenadas, 2, resultado[0].length);
        return figuraTransformada;
    }

    public Figura transladar(Figura figuraTransformada, float dx, float dy) {
        Float[][] matrizTranslacao = {
                {1f, 0f, dx},
                {0f, 1f, dy},
                {0f, 0f, 1f}
        };
        Float[][] resultado = multiplicacaoMatrizes(matrizTranslacao, figuraTransformada.coordenadas);
        figuraTransformada.coordenadas = new Float[2][figuraTransformada.getDesenho()];
        figuraTransformada.coordenadas = copiaMatrizes(resultado, figuraTransformada.coordenadas, 2, resultado[0].length);
        return figuraTransformada;
    }

    public Figura mudarEscala(Figura figuraTransformada, float sx, float sy, float x, float y) {
        Float[][] matrizMudancaEscala = {
                {sx, 0f, x - x * sx},
                {0f, sy, y - y * sy},
                {0f, 0f, 1f}
        };
        Float[][] resultado = multiplicacaoMatrizes(matrizMudancaEscala, figuraTransformada.coordenadas);
        figuraTransformada.coordenadas = new Float[2][figuraTransformada.getDesenho()];
        figuraTransformada.coordenadas = copiaMatrizes(resultado, figuraTransformada.coordenadas, 2, resultado[0].length);
        return figuraTransformada;
    }

}
