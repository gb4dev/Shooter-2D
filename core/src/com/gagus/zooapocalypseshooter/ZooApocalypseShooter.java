package com.gagus.zooapocalypseshooter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gagus.zooapocalypseshooter.Screens.GameScreen;

public class ZooApocalypseShooter extends Game {
	public SpriteBatch batch;
	public OrthographicCamera camera;
	public Viewport viewport;
	public static final int HEIGHT = 720;
	public static final int WIDTH = 1280;
	public static final float PPM = 128;

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		viewport = new FitViewport(WIDTH,HEIGHT,camera);
		viewport.apply();
		camera.position.set(WIDTH/2,HEIGHT/2,0);
		this.setScreen(new GameScreen(this));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		super.dispose();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width,height);
	}
}
