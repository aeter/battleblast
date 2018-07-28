package me.battleblast.screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.EventListener;

import me.battleblast.BattleBlast;


public class MainMenuScreen implements Screen {
    private final BattleBlast game;
    private Stage stage;
    private Skin skin;
    private Slider volumeSlider;

    public MainMenuScreen(final BattleBlast game) {
        this.game = game;
        stage = new Stage();
        skin = game.assets.get("ui/star-soldier/skin/star-soldier-ui.json");

        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.row();
        addMusicControls(table);

        table.row();
        addNewGameButton(table);

        table.row();
        addControlsButton(table);

        table.row();
        addCreditsButton(table);

        table.row();
        addQuitButton(table);
    }

    @Override
    public void render(float delta) {
        handleInput(); 

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            float value = volumeSlider.getValue() - 2 < 0 ? 0f : volumeSlider.getValue() - 2;
            volumeSlider.setValue(value);
            game.music.setVolume(volumeSlider.getValue() / 100f);
        } 
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            float value = volumeSlider.getValue() + 2 > 100 ? 100f : volumeSlider.getValue() + 2;
            volumeSlider.setValue(value);
            game.music.setVolume(volumeSlider.getValue() / 100f);
        } 
        // we use isKeyJustPressed - in case the user has come from another screen
        // by pressing 'ESC' there, we want to give them a choice (i.e. to stay
        // in the main menu and here if they click 'ESC' again, to exit the app).
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        // we use isKeyJustPressed - in case we're coming from a
        // GameOverLostScreen  we want to give the player a choice (i.e. to
        // stay in the main menu, and not start a game immediately because
        // 'ENTER' is still held).
        if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    private void addMusicControls(Table table) {
        table.add(new Label( "Volume", skin)).space(10);
        volumeSlider = new Slider(0, 100, 1, false, skin);
        table.add(volumeSlider).space(10).width(100);
        game.music.setVolume(BattleBlast.INITIAL_MUSIC_VOLUME);
        volumeSlider.setValue(BattleBlast.INITIAL_MUSIC_VOLUME * 100f);
        volumeSlider.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                game.music.setVolume(volumeSlider.getValue() / 100f);
                return false;
            }
        });
    }

    private void addNewGameButton(Table table) {
        final TextButton startButton = new TextButton("New Game", skin, "default");
        table.add(startButton).colspan(2);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });
    }

    private void addControlsButton(Table table) {
        final TextButton controlsButton = new TextButton("Controls", skin, "default");
        table.add(controlsButton).colspan(2);
        controlsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ControlsScreen(game));
                dispose();
            }
        });
    }


    private void addCreditsButton(Table table) {
        final TextButton creditsButton = new TextButton("Credits", skin, "default");
        table.add(creditsButton).colspan(2);
        creditsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new CreditsScreen(game));
                dispose();
            }
        });
    }

    private void addQuitButton(Table table) {
        final TextButton quitButton = new TextButton("Quit", skin, "default");
        table.add(quitButton).colspan(2);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }
}
