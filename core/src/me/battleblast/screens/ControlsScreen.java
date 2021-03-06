package me.battleblast.screens;

import java.lang.StringBuilder;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import me.battleblast.BattleBlast;


public class ControlsScreen implements Screen {
    private final BattleBlast game;
    private Stage stage;
    private Skin skin;

    public ControlsScreen(final BattleBlast game) {
        this.game = game;
        stage = new Stage();
        skin = game.assets.get("ui/star-soldier/skin/star-soldier-ui.json");

        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.row();
        addControlsText(table);

        table.row();
        addBackButton(table);
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
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    private void addControlsText(Table table) {
        Window window = new Window("", skin);
        table.add(window);
        String controls = new StringBuilder()
            .append("SPACE - shoot\n\n")
            .append("ARROW LEFT - move left\n\n")
            .append("ARROW RIGHT - move right\n\n")
            .append("ARROW UP - move up\n\n")
            .append("ARROW DOWN - move down\n\n")
            .append("ESC - exit\n\n")
            .toString();
        Label controlsLabel  = new Label(controls, skin);
        controlsLabel.setWrap(true);
        controlsLabel.setWidth(500f);
        window.add(controlsLabel).width(500f);
    }

    private void addBackButton(Table table) {
        final TextButton backButton = new TextButton("Back", skin, "default");
        table.add(backButton).colspan(2);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        });
    }
}

