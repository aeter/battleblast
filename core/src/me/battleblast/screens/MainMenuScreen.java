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
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.EventListener;

import me.battleblast.BattleBlast;


public class MainMenuScreen implements Screen {
    private final BattleBlast game;
    private Stage stage;
    private Skin skin;
    private Slider volumeSlider;

    // TODO -credits link and screen...
    public MainMenuScreen(final BattleBlast game) {
        this.game = game;
        stage = new Stage();
        skin = game.assets.get("ui/star-soldier/skin/star-soldier-ui.json");

        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.row();
        addPreferencesSection(table);

        table.row();
        addActionsSection(table);
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
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        if (Gdx.input.isKeyPressed(Keys.ENTER)) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    private void addPreferencesSection(Table table) {
        Window window = new Window("Preferences", skin);
        table.add(window);
        window.add(new Label( "Volume", skin)).space(10);
        volumeSlider = new Slider(0, 100, 1, false, skin);
        window.add(volumeSlider).space(10).width(100);
        volumeSlider.setValue(50);
        volumeSlider.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                game.music.setVolume(volumeSlider.getValue() / 100f);
                return false;
            }
        });

    }

    private void addActionsSection(Table table) {
        Window actions = new Window("Actions", skin);
        table.add(actions).space(20);

        final TextButton startButton = new TextButton("New Game", skin, "default");
        actions.add(startButton).colspan(2);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });

        final TextButton quitButton = new TextButton("Quit", skin, "default");
        actions.add(quitButton).colspan(2);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }
}
