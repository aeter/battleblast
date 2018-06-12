package me.battleblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.EventListener;


public class MainMenuScreen implements Screen {
    private final BattleBlast game;
    private Stage stage;
    private Skin skin;
    private Slider volumeSlider;

    public MainMenuScreen(final BattleBlast game) {
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        // TODO - use AssetManager for the skin
        skin = new Skin(Gdx.files.internal("ui/star-soldier/skin/star-soldier-ui.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);


        table.row();

        final TextButton startButton = new TextButton("New Game", skin, "default");
        table.add(startButton).colspan(2);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });

        table.row();

        final TextButton quitButton = new TextButton("Quit", skin, "default");
        table.add(quitButton).colspan(2);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        table.row();

        table.add(new Label( "Volume", skin)).space(10);
        volumeSlider = new Slider(0, 100, 1, false, skin);
        table.add(volumeSlider).space(10);
        volumeSlider.setValue(50);
        volumeSlider.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                game.music.setVolume(volumeSlider.getValue() / 100f);
                return false;
            }
        });

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void show() {}

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}
}
