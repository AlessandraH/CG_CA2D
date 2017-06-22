package com.alessandrah.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import javafx.stage.Stage;

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

	@Override
	public void create () {
		renderizador = new ShapeRenderer();

		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setToOrtho(false);
		camera.update();

		batch = new SpriteBatch();

		stage = new Stage(new ScreenViewport(camera));
		stage.clear();

		Gdx.input.setInputProcessor(stage);
		stage.addListener(new ClickListener(Input.Buttons.LEFT)){

		};
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		//batch.draw(img, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		//img.dispose();
	}
}
