package com.alessandrah.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
    Texture img;
    Sprite sprite;
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

        img = new Texture(Gdx.files.internal("ca2d.png"));
        sprite = new Sprite(img);

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
                //System.out.println("Você clicou em [" + x + "][" + y + "]");

                if (desenhando) {
                    figura.setCoordenadas(clique, x, y);
                    clique = incrementaClique(figura, objetos, clique);
                } else if (rotacionando && !pegandoCoordenadas) {
                    if (figuraTransformar.desenho == 1) {
                        figuraTransformar = new Circulo(1).transformarEmCoordenadasHomogeneas(figuraTransformar);
                        figuraTransformar = new Circulo(1).rotacionar(figuraTransformar, angulo, x, y);
                    } else {
                        figuraTransformar = figuraTransformar.transformarEmCoordenadasHomogeneas(figuraTransformar);
                        figuraTransformar = figuraTransformar.rotacionar(figuraTransformar, angulo, x, y);
                    }
                    objetos.add(figuraTransformar);
                    inicializaVariaveis();
                } else if (mudandoEscala && !pegandoCoordenadas) {
                    if (figuraTransformar.desenho == 1) {
                        figuraTransformar = new Circulo(1).transformarEmCoordenadasHomogeneas(figuraTransformar);
                        figuraTransformar = new Circulo(1).mudarEscala(figuraTransformar, s, s, x, y);
                    } else {
                        figuraTransformar = figuraTransformar.transformarEmCoordenadasHomogeneas(figuraTransformar);
                        figuraTransformar = figuraTransformar.mudarEscala(figuraTransformar, s, s, x, y);
                    }
                    objetos.add(figuraTransformar);
                    inicializaVariaveis();
                } else if (transladando) {
                    if (figuraTransformar.desenho == 1) {
                        figuraTransformar = new Circulo(1).transladar(figuraTransformar, x, y);
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
        sprite.draw(batch, 0.3f);
        verificaTeclaPressionada();
        if (terminal)
            fonte.draw(batch, terminalmsg + text, 5, 750);

        batch.end();
    }

    @Override
    public void dispose() {
        img.dispose();
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
            terminalmsg = exibeInstrucoes();
            fonte.draw(batch, terminalmsg, 5, 750);
        } else if (terminal && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (deletando) {
                if (text.length()==1) {
                    indice = 0;
                } else {
                    indice = Integer.parseInt(text.trim()) - 1;
                }
                objetos.remove(indice);
                inicializaVariaveis();
            } else if (pegandoCoordenadas) {
                if (rotacionando) {
                    if (text.length()==1) {
                        angulo = 45;
                    } else {
                        angulo = Float.parseFloat(text.trim());
                    }
                    terminal = false;
                    pegandoCoordenadas = false;
                } else if (mudandoEscala) {
                    if (text.length()==1) {
                        s = 2;
                    } else {
                        s = Float.parseFloat(text.trim());
                    }
                    terminal = false;
                    pegandoCoordenadas = false;
                }
            } else if (rotacionando || transladando || mudandoEscala) {
                if (text.length()==1) {
                    indice = 0;
                } else {
                    indice = Integer.parseInt(text.trim()) - 1;
                }
                figuraTransformar = new Figura();
                figuraTransformar = objetos.get(indice);
                pegandoCoordenadas = true;
                text = "";
                if (rotacionando) {
                    terminalmsg = "Digite o angulo de rotação: ";
                    fonte.draw(batch, terminalmsg, 5, 750);
                } else if (mudandoEscala) {
                    terminalmsg = "Digite o valor de escala: ";
                    fonte.draw(batch, terminalmsg, 5, 750);
                }
            } else {
                terminal = false;
            }
        }

        if (!(desenhando || deletando || rotacionando || mudandoEscala || transladando)) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                terminal = true;
                //System.out.println("Você pressionou 1");
                //terminalmsg = "Clique em 2 pontos para desenhar o círculo";
                //fonte.draw(batch, terminalmsg, 5, 750);
                desenhando = true;
                figura = new Circulo(1);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                //System.out.println("Você pressionou 2");
                //terminalmsg = "Clique em 2 pontos para desenhar a reta";
                //fonte.draw(batch, terminalmsg, 5, 750);
                desenhando = true;
                figura = new Figura(2);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
                //System.out.println("Você pressionou 3");
                //terminalmsg = "Clique em 3 pontos para desenhar o triângulo";
                //fonte.draw(batch, terminalmsg, 5, 750);
                desenhando = true;
                figura = new Figura(3);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
                //System.out.println("Você pressionou 4");
                //terminalmsg = "Clique em 4 pontos para desenhar o quadrilátero";
                //fonte.draw(batch, terminalmsg, 5, 750);
                desenhando = true;
                figura = new Figura(4);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
                if (objetos.size() > 0) {
                    //System.out.println("Você pressionou backspace");
                    deletando = true;
                    terminalmsg = "Apagar figura\n";
                    exibeTerminal();
                    fonte.draw(batch, terminalmsg, 5, 750);
                } else {
                    //System.out.println("Não há objetos");
                }
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
                //System.out.println("Você pressionou delete");
                deletando = true;
                objetos.clear();
                inicializaVariaveis();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
                //System.out.println("Você escolheu mudar de escala");
                mudandoEscala = true;
                terminalmsg = "Mudar de escala\n";
                exibeTerminal();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
                //System.out.println("Você escolheu rotacionar");
                rotacionando = true;
                terminalmsg = "Rotacionar\n";
                exibeTerminal();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
                //System.out.println("Você escolheu transladar");
                transladando = true;
                terminalmsg = "Transladar\n";
                exibeTerminal();
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            //System.out.println("Você pressionou Esc");
            inicializaVariaveis();
        }
    }

    public void exibeTerminal() {
        terminalmsg += exibeListaObjetos();
        fonte.draw(batch, terminalmsg, 5, 750);
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

    public String exibeInstrucoes() {
        String instrucoes = "";
        instrucoes += "CA2D - Instruções de uso\n\n";
        instrucoes += "Desenho:\n";
        instrucoes += "- Para desenhar um CÍRCULO, pressione <1> e, em seguida, clique em dois pontos (o primeiro clique determinará o centro do círculo, e o segundo servirá para o cálculo do raio);\n";
        instrucoes += "- Para desenhar uma RETA, pressione <2> e, em seguida, clique em dois pontos, que determinarão o início e o fim da reta;\n";
        instrucoes += "- Para desenhar um TRIÂNGULO, pressione <3> e, em seguida, clique em três pontos, que determinarão os vértices do triângulo;\n";
        instrucoes += "- Para desenhar um QUADRILÁTERO, pressione <4> e, em seguida, clique em quatro pontos, que determinarão os vértices do quadrilátero.\n\n";
        instrucoes += "Transformações 2D:\n";
        instrucoes += "- Para realizar uma MUDANÇA DE ESCALA, pressione <F1>. " +
                "Em seguida, digite o número correspondente, da lista, da figura que deseja realizar a transformação. " +
                "\nDepois, insira o valor de escala. Por fim, clique em um ponto que servirá de referência para a transformação;\n";
        instrucoes += "- Para realizar uma ROTAÇÃO, pressione <F2>. " +
                "Em seguida, digite o número correspondente, da lista, da figura que deseja realizar a transformação." +
                "\nDepois, digite o ângulo, em graus. Por fim, clique em um ponto que servirá de referência para a transformação;\n";
        instrucoes += "- Para realizar uma TRANSLAÇÃO, pressione <F3>. " +
                "Em seguida, digite o número correspondente, da lista, da figura que deseja realizar a transformação. " +
                "\nPor fim, clique em um ponto que servirá de referência para a transformação.\n\n";
        instrucoes += "Outras operações:\n";
        instrucoes += "- Para APAGAR APENAS UMA FIGURA, pressione <backspace>. " +
                "Em seguida, digite o número correspondente, da lista, da figura que deseja realizar apagar;\n";
        instrucoes += "- Para APAGAR TODAS AS FIGURAS (CLEAR), pressione <0>;\n";
        instrucoes += "- Para SAIR, pressione <ALT+F4>.\n\n";
        instrucoes += "Observações:\n";
        instrucoes += "- A translação é feita em relação ao primeiro vértice (primeiro ponto clicado) no momento da criação da figura;\n";
        instrucoes += "- Se a figura a ser selecionada não for especificada, a primeira figura será selecionada;\n";
        instrucoes += "- Se o ângulo da rotação não for especificado, o ângulo será 45 graus;\n";
        instrucoes += "- Se a escala da mudança de escala não for especificada, a escala será 2.";
        return instrucoes;
    }
}
