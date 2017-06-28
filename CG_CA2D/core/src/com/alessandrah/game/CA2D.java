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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;

public class CA2D extends ApplicationAdapter implements ApplicationListener {
    SpriteBatch batch;
    OrthographicCamera camera;
    Stage stage;
    ArrayList<Figura> objetos = new ArrayList<Figura>();
    ShapeRenderer renderizador;
    int clique = 0, indice = -1;
    boolean desenhando = false, deletando = false,
            terminal = false, transladando = false,
            rotacionando = false, mudandoEscala = false,
            pegandoCoordenadas = false;
    String text = "", terminalmsg = "";
    BitmapFont fonte;

    float angulo, s;

    Figura figura, figuraTransformar;

    @Override
    public void create() {
        renderizador = new ShapeRenderer();
        fonte = new BitmapFont();
        fonte.setColor(1f, 1f, 1f, 1f);

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
                } else if (rotacionando && !pegandoCoordenadas) {
                    if (figuraTransformar.desenho == 1) {
                        figuraTransformar = new Circulo(1).transformarEmCoordenadasHomogeneas(figuraTransformar);
                        figuraTransformar = new Circulo(1).rotacionar(figuraTransformar,angulo,x,y);
                    } else {
                        figuraTransformar = figuraTransformar.transformarEmCoordenadasHomogeneas(figuraTransformar);
                        figuraTransformar = figuraTransformar.rotacionar(figuraTransformar, angulo, x, y);
                    }
                    objetos.add(figuraTransformar);
                    inicializaVariaveis();
                } else if (mudandoEscala && !pegandoCoordenadas) {
                    if (figuraTransformar.desenho == 1) {
                        figuraTransformar = new Circulo(1).transformarEmCoordenadasHomogeneas(figuraTransformar);
                        figuraTransformar = new Circulo(1).mudarEscala(figuraTransformar,s,s,x,y);
                    } else {
                        figuraTransformar = figuraTransformar.transformarEmCoordenadasHomogeneas(figuraTransformar);
                        figuraTransformar = figuraTransformar.mudarEscala(figuraTransformar, s, s, x, y);
                    }
                    objetos.add(figuraTransformar);
                    inicializaVariaveis();
                } else if (transladando) {
                    if (figuraTransformar.desenho == 1) {
                        figuraTransformar = new Circulo(1).transformarEmCoordenadasHomogeneas(figuraTransformar);
                        figuraTransformar = new Circulo(1).transladar(figuraTransformar,x,y);
                    } else {
                        figuraTransformar = figuraTransformar.transformarEmCoordenadasHomogeneas(figuraTransformar);
                        figuraTransformar = figuraTransformar.transladar(figuraTransformar, x - figuraTransformar.coordenadas[0][0], y - figuraTransformar.coordenadas[1][0]);
                    }
                    objetos.add(figuraTransformar);
                    inicializaVariaveis();
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
            fonte.draw(batch, terminalmsg + text, 0, 700);

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
        pegandoCoordenadas = false;
        text = "";
        terminalmsg = "";
        indice = -1;
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
            if (deletando) {
                indice = Integer.parseInt(text.trim()) - 1;
                objetos.remove(indice);
                inicializaVariaveis();
            } else if (pegandoCoordenadas) {
                if (rotacionando) {
                    angulo = Float.parseFloat(text.trim());
                    terminal = false;
                    pegandoCoordenadas = false;
                } else if (mudandoEscala) {
                    s = Float.parseFloat(text.trim());
                    terminal = false;
                    pegandoCoordenadas = false;
                }
            } else if (rotacionando || transladando || mudandoEscala) {
                indice = Integer.parseInt(text.trim()) - 1;
                figuraTransformar = new Figura();
                figuraTransformar = objetos.get(indice);
                pegandoCoordenadas = true;
                text = "";
                if (rotacionando) {
                    pegandoCoordenadas = true;
                    terminalmsg = "Digite o angulo de rotação: ";
                    fonte.draw(batch, terminalmsg, 0, 700);
                } else if (mudandoEscala) {
                    pegandoCoordenadas = true;
                    terminalmsg = "Digite o valor de escala: ";
                    fonte.draw(batch, terminalmsg, 0, 700);
                }
            }
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
                    exibeTerminal();
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
                exibeTerminal();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
                System.out.println("Você escolheu rotacionar");
                rotacionando = true;
                exibeTerminal();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
                System.out.println("Você escolheu transladar");
                transladando = true;
                exibeTerminal();
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            System.out.println("Você pressionou Esc");
            inicializaVariaveis();
        }
    }

    public void exibeTerminal() {
        terminalmsg = exibeListaObjetos();
        fonte.draw(batch, terminalmsg, 0, 700);
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
