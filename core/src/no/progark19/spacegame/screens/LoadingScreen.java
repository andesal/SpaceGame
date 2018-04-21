package no.progark19.spacegame.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import no.progark19.spacegame.SpaceGame;
import no.progark19.spacegame.utils.LoadingBarWithBorders;

public class LoadingScreen implements Screen{

    private final SpaceGame game;

    private ShapeRenderer shapeRenderer;

    private float progress;

    public LoadingScreen(final SpaceGame game){
        this.game = game;

        this.shapeRenderer = new ShapeRenderer();
    }

    //TODO move to assetManager/Assetsmanager
    private void queueAssets(){
        game.assetManager.load("ui/uiskin.atlas", TextureAtlas.class);

    }

    @Override
    public void show() {
        System.out.println("GAME: LOADING"); // For debug purposessses
        shapeRenderer.setProjectionMatrix(game.camera.combined);
        this.progress = 0f;
    }

    private void update(float delta) {
        progress = MathUtils.lerp(progress, game.assetManager.getProgress(), .1f);
        if (game.assetManager.update() && progress >= game.assetManager.getProgress() - .001f) {
                //game.setScreen(new MainMenuScreen(game));
            game.setScreen(new PlayScreen(game));
        }
    }



    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1f,1f,1f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(32, game.camera.viewportHeight / 2 - 8, game.camera.viewportWidth - 64, 16);

        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(32, game.camera.viewportHeight / 2 - 8, progress * (game.camera.viewportWidth - 64), 16);
        shapeRenderer.end();

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
        shapeRenderer.dispose();

    }
}
