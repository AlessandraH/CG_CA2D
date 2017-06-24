package com.alessandrah.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;

public class CA2D extends ApplicationAdapter implements ApplicationListener {
    SpriteBatch batch;
    OrthographicCamera camera;
    Stage stage;
    TextButton botao;
    Float[][] coordenadas;
    ArrayList<Figura> objetos = new ArrayList<Figura>();
    ShapeRenderer renderizador;
    int clique = 0;
    boolean desenhando;
    boolean transladando, rotacionando, mudandoEscala;

    Figura figura;

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

                if(desenhando) {
                    figura.setCoordenadas(clique,x,y);
                    clique = incrementaClique(figura,objetos,clique);
                }

                return true;
            }
        });
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        verificaTeclaPressionada();

        stage.act();
        stage.draw();

        renderizador.setProjectionMatrix(camera.combined);
        renderizador.begin(ShapeRenderer.ShapeType.Line);
        renderizador.setColor(1f, 1f, 1f, 1f);
        exibeObjetos();
        renderizador.end();

        batch.begin();
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public void resize(int width, int height) {
        camera.position.set(new Vector3(width / 2, height / 2, 0f));
        stage.getViewport().update(width, height);
    }

    public void inicializaDesenhando() {
        desenhando = false;
    }

    public void inicializaTransformacoes() {
        rotacionando = false;
        transladando = false;
        mudandoEscala = false;
    }

    public int incrementaClique(Figura figura, ArrayList<Figura> objetos, int clique) {
        clique++;
        if(clique==figura.coordenadas[0].length) {
            objetos.add(figura);
            clique = 0;
            inicializaDesenhando();
        }
        return clique;
    }

    public void verificaTeclaPressionada() {
        if(!desenhando) {
            if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                System.out.println("Você pressionou 1");
                desenhando = true;
                figura = new Circulo(1);
            }
            else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                System.out.println("Você pressionou 2");
                desenhando = true;
                figura = new Figura(2);
            }
            else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
                System.out.println("Você pressionou 3");
                desenhando = true;
                figura = new Figura(3);
            }
            else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
                System.out.println("Você pressionou 4");
                desenhando = true;
                figura = new Figura(4);
            }
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            System.out.println("Você pressionou Esc");
            inicializaDesenhando();
        }
    }

    public void exibeObjetos() {
        if (objetos.size() > 0) {
            for (Figura obj : objetos) {
                switch (obj.desenho) {
                    case 1: //circulo
                        renderizador.circle(obj.coordenadas[0][0], obj.coordenadas[1][0], obj.coordenadas[0][1], 256);
                        break;
                    case 2: //linha
                        renderizador.line(obj.coordenadas[0][0], obj.coordenadas[1][0], obj.coordenadas[0][1], obj.coordenadas[1][1]);
                        break;
                    case 3: //triangulo
                        renderizador.triangle(obj.coordenadas[0][0], obj.coordenadas[1][0], obj.coordenadas[0][1], obj.coordenadas[1][1], obj.coordenadas[0][2], obj.coordenadas[1][2]);
                        break;
                    case 4: //quadrilatero
                        float[] auxiliar = {obj.coordenadas[0][0], obj.coordenadas[1][0], obj.coordenadas[0][1], obj.coordenadas[1][1], obj.coordenadas[0][2], obj.coordenadas[1][2], obj.coordenadas[0][3], obj.coordenadas[1][3]};
                        renderizador.polygon(auxiliar);
                        break;
                }
            }
        }
    }
}
