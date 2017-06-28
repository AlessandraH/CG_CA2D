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

    @Override
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
        Float[][] centroCirculo = {
                {figuraTransformada.coordenadas[0][0]},
                {figuraTransformada.coordenadas[1][0]},
                {figuraTransformada.coordenadas[2][0]}
        };
        centroCirculo = multiplicacaoMatrizes(matrizRotacao, centroCirculo);
        centroCirculo = multiplicacaoMatrizes(matrizTranslacao, centroCirculo);
        figuraTransformada.coordenadas = copiaMatrizes(centroCirculo, figuraTransformada.coordenadas, 2, 1);
        return figuraTransformada;
    }

    @Override
    public Figura transladar(Figura figuraTransformada, float dx, float dy) {
        figuraTransformada.coordenadas[0][0] = dx;
        figuraTransformada.coordenadas[1][0] = dy;
        return figuraTransformada;
    }

    @Override
    public Figura mudarEscala(Figura figuraTransformada, float sx, float sy, float x, float y) {
        figuraTransformada.coordenadas[0][1] = figuraTransformada.coordenadas[0][1] * sx;
        return figuraTransformada;
    }
}
