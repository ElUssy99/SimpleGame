package com.spritesheet.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class SpritesheetGame implements ApplicationListener {

	private static final int FRAME_COLS = 6, FRAME_ROWS = 5;

	Animation<TextureRegion> walkAnimation;
	Texture walkSheet;
	SpriteBatch spriteBatch;
	Sprite sprite;

	private long last;
	float stateTime;
	private Array<Rectangle> posiciones;

	@Override
	public void create() {

		walkSheet = new Texture(Gdx.files.internal("sprite-animation1.png"));

		TextureRegion[][] tmp = TextureRegion.split(walkSheet,walkSheet.getWidth() / FRAME_COLS,walkSheet.getHeight() / FRAME_ROWS);

		TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
			for (int j = 0; j < FRAME_COLS; j++) {
				walkFrames[index++] = tmp[i][j];
			}
		}

		walkAnimation = new Animation<TextureRegion>(0.020f, walkFrames);

		spriteBatch = new SpriteBatch();
		stateTime = 0f;

		sprite = new Sprite(walkSheet);
		sprite.setPosition(0,10);

		posiciones = new Array<>();
		spawnRaindrop();
	}

	private void spawnRaindrop() {
		Rectangle rec = new Rectangle();
		rec.y = 100;
		rec.x = 100;
		rec.width = 100;
		rec.height = 100;
		posiciones.add(rec);
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stateTime += Gdx.graphics.getDeltaTime();

		TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true);
		spriteBatch.begin();

		for ( Rectangle rec :  posiciones) {
			spriteBatch.draw(currentFrame, rec.x, rec.y);
		}

		for (Iterator<Rectangle> iter = posiciones.iterator(); iter.hasNext(); ) {
			Rectangle raindrop = iter.next();
			raindrop.x += 150 * Gdx.graphics.getDeltaTime();
			if(raindrop.x - 64 < 0) iter.remove();
		}

		spriteBatch.end();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		walkSheet.dispose();
	}
}