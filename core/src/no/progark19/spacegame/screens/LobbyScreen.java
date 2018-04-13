package no.progark19.spacegame.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;

import no.progark19.spacegame.SpaceGame;

public class LobbyScreen implements Screen {

    private final SpaceGame game;
    private BitmapFont font;
    private GlyphLayout layout;
    private boolean onSupportedDevice;

    private Stage stage;

    private ShapeRenderer shapeRenderer;

    public LobbyScreen(final SpaceGame game){
        this.game = game;
        this.stage = new Stage(new FitViewport(SpaceGame.WIDTH, SpaceGame.HEIGHT, game.camera));
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
        this.layout = new GlyphLayout();
        onSupportedDevice = Gdx.app.getType() != Application.ApplicationType.Android;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();

        font.setColor(Color.BLACK);
        font.getData().setScale(4);
        layout.setText(font, "LOBBY");

        font.draw(game.batch, layout,
                SpaceGame.WIDTH/2 - layout.width/2, SpaceGame.HEIGHT - layout.height*0.5f
        );


        font.getData().setScale(1);
        if (!onSupportedDevice){
            font.setColor(Color.RED);
            layout.setText(font, "this device is currently not supported");

        } else {
            font.setColor(Color.GREEN);
            layout.setText(font, "Looking for crew");
        }
        font.draw(game.batch, layout,
                SpaceGame.WIDTH/2 - layout.width/2, SpaceGame.HEIGHT/2 - layout.height/2
        );
        game.batch.end();
        //game.setScreen(new PlayScreen_DEMO(game));
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        shapeRenderer.dispose();
    }
}
