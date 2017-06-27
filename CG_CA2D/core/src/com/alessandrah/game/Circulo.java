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
        double anguloRadiano = (Math.PI/180) * angulo;
        Float[][] matrizRotacao = {
                {(float)Math.cos(anguloRadiano), (float)-Math.sin(anguloRadiano), (float)(y*Math.sin(anguloRadiano)-x*Math.cos(anguloRadiano))},
                {(float)Math.sin(anguloRadiano), (float)Math.cos(anguloRadiano), (float)(-x*Math.sin(anguloRadiano)-y*Math.cos(anguloRadiano))},
                {0f, 0f, 1f}
        };
        figuraTransformada.coordenadas = multiplicacaoMatrizes(matrizRotacao,figuraTransformada.coordenadas);
        return figuraTransformada;
    }

    @Override
    public Figura transladar(Figura figuraTransformada, float dx, float dy) {
        Float[][] matrizTranslacao = {
                {1f, 0f, dx},
                {0f, 1f, dy},
                {0f, 0f, 1f}
        };
        figuraTransformada.coordenadas = multiplicacaoMatrizes(matrizTranslacao, figuraTransformada.coordenadas);
        return figuraTransformada;
    }

    @Override
    public Figura mudarEscala(Figura figuraTransformada, float sx, float sy, float x, float y) {
        Float[][] matrizMudancaEscala = {
                {sx, 0f, x-x*sx},
                {0f, sy, y-y*sy},
                {0f, 0f, 1f}
        };
        figuraTransformada.coordenadas = multiplicacaoMatrizes(matrizMudancaEscala, figuraTransformada.coordenadas);
        return figuraTransformada;
    }
}
