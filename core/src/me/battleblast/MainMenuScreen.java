package me.battleblast;

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


public class MainMenuScreen implements Screen {
    private final BattleBlast game;
    private Stage stage;
    private Skin skin;

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
    public void show() {}

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    private void addPreferencesSection(Table table) {
        Window window = new Window("Preferences", skin);
        table.add(window);
        window.add(new Label( "Volume", skin)).space(10);
        final Slider volumeSlider = new Slider(0, 100, 1, false, skin);
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
