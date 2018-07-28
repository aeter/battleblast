package me.battleblast.screens;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import me.battleblast.BattleBlast;


public class GameOverVictoryScreen implements Screen {
    private final BattleBlast game;
    private Stage stage;
    private Skin skin;

    public Music victoryMusic;

    public GameOverVictoryScreen(final BattleBlast game) {
        this.game = game;
        stage = new Stage();
        skin = game.assets.get("ui/star-soldier/skin/star-soldier-ui.json");

        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.row();
        Label gameOverLabel = new Label("YOU WIN!", skin);
        gameOverLabel.setFontScale(3f);
        table.add(gameOverLabel);

        table.row();
        table.row();
        Label anyKeyLabel = new Label("press any key to continue...", skin);
        table.add(anyKeyLabel);
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
        game.music.stop();
        playVictoryMusic();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        victoryMusic.stop();
        game.music.play();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Keys.ANY_KEY)) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    private void playVictoryMusic() {
        victoryMusic = game.assets.get("snd_music2.ogg", Music.class);
        victoryMusic.setLooping(true);
        victoryMusic.setVolume(game.music.getVolume());
        victoryMusic.play();
    }
}

