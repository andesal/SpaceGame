package no.progark19.spacegame.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Random;

import no.progark19.spacegame.GameSettings;
import no.progark19.spacegame.SpaceGame;
import no.progark19.spacegame.interfaces.ReceivedDataListener;
import no.progark19.spacegame.utils.RenderableWorldState;
import no.progark19.spacegame.utils.SpaceNameGenerator;

public class LobbyScreen implements Screen, ReceivedDataListener {

    private final SpaceGame game;
    private BitmapFont font;
    private GlyphLayout layout;
    private boolean onSupportedDevice;

    private Stage stage;

    private ShapeRenderer shapeRenderer;

    // Dark network magics
    private String latestData = "";
    private String thisName = SpaceNameGenerator.generate();
    private boolean otherPlayerReady = false;
    private boolean thisPlayerReady = false;
    //Seed-stuff
    private long thisPlayerSeed;
    private long otherPlayerSeed;
    private boolean seedMessageSent = false;
    private boolean seedDecided = false;
    private Random random = new Random();

    private ClickListener readListener = new ClickListener() {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            game.p2pConnector.sendData("ready|true");

            LobbyScreen.this.setPlayerReady(true);
            return true;
        }
        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            game.p2pConnector.sendData("ready|false");

            LobbyScreen.this.setPlayerReady(false);
        }
    };

    protected void setPlayerReady(boolean isReady){
        thisPlayerReady = isReady;
    }

    public LobbyScreen(final SpaceGame game){
        this.game = game;
        this.stage = new Stage(new FitViewport(SpaceGame.WIDTH, SpaceGame.HEIGHT, game.camera));
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
        this.layout = new GlyphLayout();

        onSupportedDevice = Gdx.app.getType() == Application.ApplicationType.Android;

        stage.addListener(readListener);
    }

    @Override
    public void show() {
        if (onSupportedDevice){
            game.p2pConnector.setThisDeviceName(thisName);

            game.p2pConnector.addReceivedDataListener(this);
            game.p2pConnector.discoverPeers();
        }

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        //TODO clean up method
        if(seedDecided){
            game.setScreen(new PlayScreen(game));
        }

        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(game.camera.combined);

        game.batch.begin();

        font.setColor(Color.BLACK);
        font.getData().setScale(4);
        layout.setText(font, "LOBBY");

        font.draw(game.batch, layout,
                SpaceGame.WIDTH/2 - layout.width/2, SpaceGame.HEIGHT - layout.height*0.5f
        );


        font.getData().setScale(2);
        font.setColor(Color.FIREBRICK);
        layout.setText(font, "Your name is: \n" + thisName);

        font.draw(game.batch, layout,
                SpaceGame.WIDTH/2 - layout.width/2, SpaceGame.HEIGHT - layout.height*1.5f
        );


        if (onSupportedDevice){
            if (game.p2pConnector.hasConnection()){
                font.setColor(Color.GREEN);
                layout.setText(font, "FOUND A CREW-MEMBER!\n"+ game.p2pConnector.getOtherPeerName());
                font.draw(game.batch, layout,
                        SpaceGame.WIDTH/2 - layout.width/2, SpaceGame.HEIGHT/2 - layout.height/2
                );

                if (thisPlayerReady && otherPlayerReady) {
                    font.setColor(Color.CHARTREUSE);
                    layout.setText(font, "Both players ready!");
                    font.draw(game.batch, layout,
                            SpaceGame.WIDTH/2 - layout.width/2, SpaceGame.HEIGHT/4 - layout.height/2
                    );

                    //Make it so they cant back off now that both are ready "unready
                    stage.removeListener(readListener);

                    if(!seedMessageSent){
                        thisPlayerSeed = random.nextLong();

                        game.p2pConnector.sendData("seed|"+String.valueOf(thisPlayerSeed));

                        seedMessageSent = true;
                    }
                }

            } else {
                font.setColor(Color.GREEN);
                layout.setText(font, "Looking for crew");
                font.draw(game.batch, layout,
                        SpaceGame.WIDTH/2 - layout.width/2, SpaceGame.HEIGHT/2 - layout.height/2
                );
                //game.p2pConnector.printSomething();
            }

        } else {
            font.setColor(Color.RED);
            layout.setText(font, "this device is currently not supported");
            font.draw(game.batch, layout,
                    SpaceGame.WIDTH/2 - layout.width/2, SpaceGame.HEIGHT/2 - layout.height/2
            );
        }
        game.batch.end();

        stage.act();
        stage.draw();
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


    @Override
    public void onReceive(RenderableWorldState data) {
        //
    }

    @Override
    public void onReceive(String data) {
        System.out.println("Received:" + data);
        String[] msg = data.split("\\|");

        if (msg[0].equals("ready")){
            otherPlayerReady = Boolean.valueOf(msg[1]);
        }
        else if (msg[0].equals("seed")){
            game.p2pConnector.removeReceivedDataListener(this);
            otherPlayerSeed = Long.valueOf(msg[1]);

            GameSettings.isPhysicsResponsible = otherPlayerSeed > thisPlayerSeed;

            GameSettings.setRandomSeed(otherPlayerSeed + thisPlayerSeed);

            seedDecided = true;

        }
        else {
            System.out.println("String tag not understood!");

        }


    }
}
