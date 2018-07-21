package me.battleblast.screens;

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


public class CreditsScreen implements Screen {
    private final BattleBlast game;
    private Stage stage;
    private Skin skin;

    public CreditsScreen(final BattleBlast game) {
        this.game = game;
        stage = new Stage();
        skin = game.assets.get("ui/star-soldier/skin/star-soldier-ui.json");

        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.row();
        addCreditsText(table);

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

    private void addCreditsText(Table table) {
        Window window = new Window("Special thanks to:", skin);
        table.add(window);
        String credits = "Game art:\n"
                + "* Kenney (kenney.nl)\n"
                + "\nGame music:\n"
                + "* 'Spring' (opengameart.org/users/spring)\n"
                + "\nGUI theme:\n"
                + "* Raymond 'Raeleus' Buckley - (ray3k.wordpress.com)\n"
                + "\nProgramming:\n"
                + "* 'aeter' - (github.com/aeter)\n";
        Label creditsLabel  = new Label(credits, skin);
        creditsLabel.setWrap(true);
        creditsLabel.setWidth(500f);
        window.add(creditsLabel).width(500f);
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

