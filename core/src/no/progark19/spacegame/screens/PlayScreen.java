package no.progark19.spacegame.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import no.progark19.spacegame.SpaceGame;
import no.progark19.spacegame.managers.AudioManager;
import no.progark19.spacegame.managers.EntityManager;

public class PlayScreen implements Screen {

    private final SpaceGame game;
    private Stage stage;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private AudioManager audioManager;
    private EntityManager entityManager;
    private PooledEngine engine;
    public Sound theme;

    public PlayScreen(SpaceGame game){
        this.game = game;
        this.stage = new Stage(new FitViewport(SpaceGame.WIDTH, SpaceGame.HEIGHT, game.camera));
        this.shapeRenderer = new ShapeRenderer();
        engine = new PooledEngine();
        //entityManager = new EntityManager(engine, game.batch);
    }

    @Override
    public void show() {
        System.out.println("PLAY SCREEN");

    }

    @Override
    public void render(float delta) {
        game.batch.begin();
        game.batch.draw(new Texture("img/space_sample.png"), 0, 0);
        //entityManager.update();
        game.batch.end();


    }

    @Override
    public void resize(int width, int height) {

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