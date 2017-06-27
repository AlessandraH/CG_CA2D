package com.alessandrah.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
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
    boolean desenhando = false, deletando = false, terminal = false;
    boolean transladando = false, rotacionando = false, mudandoEscala = false;
    String text = "", terminalmsg = "", batata = "";
    BitmapFont fonte;

    Figura figura;

    @Override
    public void create() {
        renderizador = new ShapeRenderer();
        fonte = new BitmapFont();
        fonte.setColor(1f, 1f, 1f, 1f);

        coordenadas = new Float[2][2]; //tô criando aqui só pra tu ter um exemplo de pegar clique
        //quando for fazer de verdade, vai precisar de alguma forma de saber qual figura tu quer criar
        //ai da um new Float do tamanho que vai precisar

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false);
        camera.update();

        batch = new SpriteBatch();

        stage = new Stage(new ScreenViewport(camera));
        stage.clear();

        InputListener bash = new InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                if (terminal)
                    switch (character) {
                        case '\b':
                            if ((text.length() - 1) >= 0)
                                text = text.substring(0, text.length() - 1);
                            break;
                        default:
                            text += character;
                            break;
                    }

                return true;
            }
        };

        stage.addListener(bash);

        Gdx.input.setInputProcessor(stage);
        stage.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Você clicou em [" + x + "][" + y + "]");

                if (desenhando) {
                    figura.setCoordenadas(clique, x, y);
                    clique = incrementaClique(figura, objetos, clique);
                }

                return true;
            }
        });
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
        verificaTeclaPressionada();
        if (terminal)
            fonte.draw(batch, terminalmsg + text, 0, 250);

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

    public void inicializaVariaveis() {
        desenhando = false;
        deletando = false;
        rotacionando = false;
        transladando = false;
        mudandoEscala = false;
        terminal = false;
        text = "";
        terminalmsg = "";
    }


    public int incrementaClique(Figura figura, ArrayList<Figura> objetos, int clique) {
        clique++;
        if (clique == figura.coordenadas[0].length) {
            objetos.add(figura);
            clique = 0;
            inicializaVariaveis();
        }
        return clique;
    }

    public void verificaTeclaPressionada() {

        if (!terminal && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            terminal = true;
        } else if (terminal && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if(deletando){
                objetos.remove(Integer.parseInt(text.trim())-1);
            }
            inicializaVariaveis();
        }

        if (!(desenhando || deletando || rotacionando || mudandoEscala || transladando)) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                terminal = true;
                System.out.println("Você pressionou 1");
                desenhando = true;
                figura = new Circulo(1);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                System.out.println("Você pressionou 2");
                desenhando = true;
                figura = new Figura(2);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
                System.out.println("Você pressionou 3");
                desenhando = true;
                figura = new Figura(3);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
                System.out.println("Você pressionou 4");
                desenhando = true;
                figura = new Figura(4);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
                if (objetos.size() > 0) {
                    System.out.println("Você pressionou backspace");
                    deletando = true;
                    int indice;
                    terminalmsg = exibeListaObjetos();
                    fonte.draw(batch, terminalmsg, 0, 250);
                } else {
                    System.out.println("Não há objetos");
                }
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
                System.out.println("Você pressionou delete");
                deletando = true;
                objetos.clear();
                inicializaVariaveis();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
                System.out.println("Você escolheu mudar de escala");
                mudandoEscala = true;
                //mudança de escala
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
                System.out.println("Você escolheu rotacionar");
                rotacionando = true;
                //rotação
                Figura novo = new Figura();
                novo = novo.transformarEmCoordenadasHomogeneas(objetos.get(objetos.size()-1));
                novo = objetos.get(objetos.size()-1).rotacionar(novo, 1, 0,0);
                objetos.add(novo);
                inicializaVariaveis();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
                System.out.println("Você escolheu transladar");
                transladando = true;
                //translada
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            System.out.println("Você pressionou Esc");
            inicializaVariaveis();
        }
    }

    public String exibeListaObjetos() {
        int i = 0;
        terminal = true;
        String opcao;
        opcao = "";
        for (Figura obj : objetos) {
            i++;
            opcao += i + " - ";
            switch (obj.desenho) {
                case 1:
                    opcao += "Círculo de centro ["
                            + obj.coordenadas[0][0] + "]["
                            + obj.coordenadas[1][0] + "] e raio ["
                            + obj.coordenadas[0][1] + "];";
                    break;
                case 2:
                    opcao += "Linha de coordenadas ["
                            + obj.coordenadas[0][0] + "]["
                            + obj.coordenadas[1][0] + "] e ["
                            + obj.coordenadas[0][1] + "]["
                            + obj.coordenadas[1][1] + "];";
                    break;
                case 3:
                    opcao += "Triângulo de coordenadas ["
                            + obj.coordenadas[0][0] + "]["
                            + obj.coordenadas[1][0] + "], ["
                            + obj.coordenadas[0][1] + "]["
                            + obj.coordenadas[1][1] + "] e ["
                            + obj.coordenadas[0][2] + "]["
                            + obj.coordenadas[1][2] + "];";
                    break;
                case 4:
                    opcao += "Quadrilátero de coordenadas ["
                            + obj.coordenadas[0][0] + "]["
                            + obj.coordenadas[1][0] + "], ["
                            + obj.coordenadas[0][1] + "]["
                            + obj.coordenadas[1][1] + "], ["
                            + obj.coordenadas[0][2] + "]["
                            + obj.coordenadas[1][2] + "] e ["
                            + obj.coordenadas[0][3] + "]["
                            + obj.coordenadas[1][3] + "];";
                    break;
            }
            opcao += "\n";
        }
        opcao += "\nDigite o número que corresponde ao objeto a ser selecionado: ";
        return opcao;
    }

    public void exibeObjetos() {
        if (objetos.size() > 0) {
            for (Figura obj : objetos) {
                switch (obj.desenho) {
                    case 1: //circulo de cores
                        renderizador.circle(obj.coordenadas[0][0],
                                obj.coordenadas[1][0],
                                obj.coordenadas[0][1],
                                256);
                        break;
                    case 2: //linha do equador
                        renderizador.line(obj.coordenadas[0][0],
                                obj.coordenadas[1][0],
                                obj.coordenadas[0][1],
                                obj.coordenadas[1][1]);
                        break;
                    case 3: //triangulo das bermudas
                        renderizador.triangle(obj.coordenadas[0][0],
                                obj.coordenadas[1][0],
                                obj.coordenadas[0][1],
                                obj.coordenadas[1][1],
                                obj.coordenadas[0][2],
                                obj.coordenadas[1][2]);
                        break;
                    case 4: //quadrilatero ferrífero
                        float[] auxiliar = {obj.coordenadas[0][0],
                                obj.coordenadas[1][0],
                                obj.coordenadas[0][1],
                                obj.coordenadas[1][1],
                                obj.coordenadas[0][2],
                                obj.coordenadas[1][2],
                                obj.coordenadas[0][3],
                                obj.coordenadas[1][3]};
                        renderizador.polygon(auxiliar);
                        break;
                }
            }
        }
    }
}
