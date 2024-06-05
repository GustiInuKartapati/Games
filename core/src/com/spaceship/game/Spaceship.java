package com.spaceship.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.Game;

import java.util.ArrayList;
import java.util.List;

public class Spaceship extends Game {
    private SpriteBatch batch;
    private ship ship;
    private List<meteor> meteors;
    private float screenWidth;
    private float screenHeight;
	private Stage stage;
	private Skin skin;
	private boolean isGamePaused;
	private float gameTime = 0;
	private Label timerLabel;
	private Texture backgroundTexture;
	private Music backgroundMusic;
	private Music gameoverSound;
	// private MainMenu mainMenuScreen;

    @Override
    public void create() {
		// mainMenuScreen = new MainMenu(this);
        // setScreen(mainMenuScreen);

		backgroundTexture = new Texture("background.jpg");
        batch = new SpriteBatch();
        ship = new ship(new Texture("ship.png"));
        meteors = new ArrayList<>();
        createMeteors();

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

		ship.setPosition(screenWidth / 2 - ship.getWidth() / 2, screenHeight / 2 - ship.getHeight() / 2);

		stage = new Stage();
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		Gdx.input.setInputProcessor(stage);

		timerLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.WHITE)); // Initialize the label
		timerLabel.setPosition(screenWidth / 2 - timerLabel.getWidth() / 2, (screenHeight - timerLabel.getHeight())-10); // Position the label
        stage.addActor(timerLabel); // Add the label to the stage

		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background_music.mp3"));
        backgroundMusic.setLooping(true); // Set to loop the music
        backgroundMusic.play(); // Play the music

		gameoverSound = Gdx.audio.newMusic(Gdx.files.internal("gameover_sound.wav"));

		isGamePaused = false;
    }

	private void createMeteors() {
		for (int i = 0; i < 10; i++) {
            meteor m = new meteor(new Texture("meteor.png"));
            m.init(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            meteors.add(m);
        }
	}

    @Override
	public void render() {
		if (!isGamePaused) {
			
			float deltaTime = Gdx.graphics.getDeltaTime();
			gameTime += deltaTime;

			timerLabel.setText("Time: " + gameTime); // Update the label text

			if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
				ship.velocity.y = ship.getSpeed();
			} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
				ship.velocity.y = -ship.getSpeed();
			} else {
				ship.velocity.y = 0;
			}


			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				ship.velocity.x = -ship.getSpeed();
			} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				ship.velocity.x = ship.getSpeed();
			} else {
				ship.velocity.x = 0;
			}

			ship.update(deltaTime, screenWidth, screenHeight);

			for (meteor m : meteors) {
				m.setSpeed(100 + gameTime * 10);
				m.update(deltaTime, screenWidth, screenHeight);
			}

			boolean collisionDetected = false;
			for (meteor m : meteors) {
				if (ship.getBounds().overlaps(m.getBounds())) {
					collisionDetected = true;
					System.out.println("Collision detected!");
					System.out.println("Ship bounds: " + ship.getBounds());
					System.out.println("Meteor bounds: " + m.getBounds());
					break;
				}
			}

			// if (!collisionDetected) {
			// 	System.out.println("No collision.");
			// }

			if (collisionDetected) {
				isGamePaused = true;


				backgroundMusic.stop();
				gameoverSound.play();

				final float finalGameTime = gameTime;

				Label.LabelStyle labelStyle = new Label.LabelStyle();
				labelStyle.font = new BitmapFont();
				labelStyle.font.getData().setScale(2); // Set the scale of the font

				TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
				textButtonStyle.font = new BitmapFont();
				textButtonStyle.font.getData().setScale(2); // Set the scale of the font

				Dialog restartDialog = new Dialog("Game Over", skin) {
					@Override
					protected void result(Object object) {
						if (object.equals(true)) {
							// Reset the ship's state
							ship.setPosition(0, 0);
							ship.velocity.set(0, 0);
							ship.setRotation(0);
					
							// Recreate the meteors
							meteors.clear();
							createMeteors();

							gameTime = 0;

							System.out.println("Game restarted!");
							ship.setPosition(screenWidth / 2 - ship.getWidth() / 2, screenHeight / 2 - ship.getHeight() / 2);
							isGamePaused = false;
							backgroundMusic.play();
						} else if (object.equals(false)) {
							Gdx.app.exit();
						}
					}
				};
				restartDialog.text(new Label("You survived for " + finalGameTime + " seconds.", labelStyle));
				restartDialog.getContentTable().row();
				restartDialog.text(new Label("Do you want to restart the game?", labelStyle));
				restartDialog.button(new TextButton("Yes", textButtonStyle), true);
				restartDialog.button(new TextButton("No", textButtonStyle), false);
				restartDialog.show(stage);


			}
			// Debug output for ship and meteor positions and sizes
			// System.out.println("Ship position: (" + ship.getX() + ", " + ship.getY() + ")");
			// System.out.println("Ship bounds: " + ship.getBounds());
			// for (meteor m : meteors) {
			// 	System.out.println("Meteor position: (" + m.getX() + ", " + m.getY() + ")");
			// 	System.out.println("Meteor bounds: " + m.getBounds());
			// }

			ship.updateRotation(); // Move this line to the end

			ScreenUtils.clear(1, 0, 0, 1);
			batch.begin();
			batch.draw(backgroundTexture, 0, 0, screenWidth, screenHeight);
			batch.draw(ship.getTexture(), ship.getX(), ship.getY(), ship.getWidth() / 2, ship.getHeight() / 2, ship.getWidth(), ship.getHeight(), 1, 1, ship.getRotation(), 0, 0, ship.getTexture().getWidth(), ship.getTexture().getHeight(), false, false);
			for (meteor m : meteors) {
				batch.draw(
					m.getTexture(), 
					m.getX(), m.getY(), 
					m.getWidth() / 2, m.getHeight() / 2, 
					m.getWidth(), m.getHeight(), 
					1, 1, 
					m.getRotation(), 
					0, 0, 
					m.getTexture().getWidth(), m.getTexture().getHeight(), 
					false, false
				);
			}
			batch.end();
		}

		stage.act();
		stage.draw();
	}
	
    @Override
    public void dispose() {
        batch.dispose();
		backgroundTexture.dispose();
		
		if (backgroundMusic != null) {
			backgroundMusic.dispose();
		}

		gameoverSound.dispose();

		// stage.dispose();
		// skin.dispose();
    }
}
