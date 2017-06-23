package com.alessandrah.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;

public class CA2D extends ApplicationAdapter implements ApplicationListener {
    SpriteBatch batch;
    //Texture img;
    OrthographicCamera camera;
    Stage stage;
    TextButton botao;
    Float[][] coordenadas;
    ArrayList<Float[][]> objetos = new ArrayList<Float[][]>();
    ShapeRenderer renderizador;
    int contaCliques = 0;

    @Override
    public void create() {
        renderizador = new ShapeRenderer();

        coordenadas = new Float[2][2]; //tô criando aqui só pra tu ter um exemplo de pegar clique
        //quando for fazer de verdade, vai precisar de alguma forma de saber qual figura tu quer criar
        //ai da um new Float do tamanho que vai precisar

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false);
        camera.update();

        batch = new SpriteBatch();

        stage = new Stage(new ScreenViewport(camera));
        stage.clear();
        Gdx.input.setInputProcessor(stage);
        stage.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Você clicou em ["+x+"]["+y+"]");

                coordenadas[0][contaCliques] = x;
                coordenadas[1][contaCliques] = y;
                contaCliques++;
                if(contaCliques == 2 ){
                    objetos.add(coordenadas);
                    contaCliques = 0;
                    coordenadas = new Float[2][2];
                }

                return true;
            }
        });

        criaObjetosTeste();

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();


        renderizador.setProjectionMatrix(camera.combined);
        renderizador.begin(ShapeRenderer.ShapeType.Line);
        renderizador.setColor(1f, 1f, 1f, 1f);
        exibeObjetos();
        renderizador.end();

        batch.begin();
        //batch.draw(img, 0, 0);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        //img.dispose();
    }


    void criaObjetosTeste() {
        Float[][] line = new Float[2][2];
        line[0][0] = 0f;
        line[0][1] = 200f;
        line[1][0] = 0f;
        line[1][1] = 110f;
        objetos.add(line);

        Float[][] triangle = new Float[2][3];
        triangle[0][0] = 200f;
        triangle[1][0] = 100f;
        triangle[0][1] = 400f;
        triangle[1][1] = 250f;
        triangle[0][2] = 550f;
        triangle[1][2] = 500f;
        objetos.add(triangle);

    }

    void exibeObjetos() {
        if (objetos.size() > 0)
            for (Float[][] obj : objetos) {
                switch (obj[0].length) {
                    case 2: //linha ou circulo
                        renderizador.line(obj[0][0], obj[1][0], obj[0][1], obj[1][1]);
                        break;
                    case 3: //triangulo
                        renderizador.triangle(obj[0][0], obj[1][0], obj[0][1], obj[1][1], obj[0][2], obj[1][2]);
                        break;
                    case 4: //quadrilatero
                        float[] auxiliar = {obj[0][0], obj[1][0], obj[0][1], obj[1][1], obj[0][2], obj[1][2], obj[0][3], obj[1][3]};
                        renderizador.polygon(auxiliar);
                        break;
                }
            }
    }

}
