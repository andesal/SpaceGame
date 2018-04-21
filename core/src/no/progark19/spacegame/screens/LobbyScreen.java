package no.progark19.spacegame.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Random;

import no.progark19.spacegame.GameSettings;
import no.progark19.spacegame.SpaceGame;
import no.progark19.spacegame.interfaces.ReceivedDataListener;
import no.progark19.spacegame.utils.Paths;
import no.progark19.spacegame.utils.SpaceNameGenerator;
import no.progark19.spacegame.utils.json.JsonPayload;
import no.progark19.spacegame.utils.json.JsonPayloadTags;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class LobbyScreen implements Screen, ReceivedDataListener {

    private final SpaceGame game;
    private BitmapFont font;
    private GlyphLayout layout;
    private boolean onSupportedDevice;

    private static final int LOGO_WIDTH = 260;
    private static final int LOGO_HEIGHT = 70;
    private static final int LOGO_Y = 570;

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
        //FIXME REMOVE
        onSupportedDevice = true;

    }

    @Override
    public void show() {
        if (onSupportedDevice){
            game.p2pConnector.setThisDeviceName(thisName);

            game.p2pConnector.addReceivedDataListener(this);
            game.p2pConnector.discoverPeers();
        }

        System.out.println("LOBBY SCREEN");
        Gdx.input.setInputProcessor(stage);
        stage.clear();
        initButtons();
    }

    @Override
    public void render(float delta) {
        //TODO clean up method
        if(seedDecided){
            game.setScreen(new PlayScreen(game));
        }
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(game.camera.combined);



        stage.getBatch().begin();
        //stage.getBatch().draw(background, 0, 0, SpaceGame.WIDTH, SpaceGame.HEIGHT);
        stage.getBatch().draw(game.assetManager.get(Paths.LOBBY_TEXT_TEXTURE_PATH, Texture.class), SpaceGame.WIDTH / 2 - LOGO_WIDTH / 2, LOGO_Y, LOGO_WIDTH, LOGO_HEIGHT);
        stage.getBatch().end();
        game.batch.begin();
        //font.setColor(Color.BLACK);
        //font.getData().setScale(4);
        //layout.setText(font, "LOBBY");

        //font.draw(game.batch, layout,
        //        SpaceGame.WIDTH/2 - layout.width/2, SpaceGame.HEIGHT - layout.height*0.5f
        //);

        font.getData().setScale(2);
        font.setColor(Color.FIREBRICK);
        layout.setText(font, "Your name is: \n" + thisName);
        font.draw(game.batch, layout,
                SpaceGame.WIDTH/2 - layout.width/2, 500
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
                    //stage.removeListener(readListener);

                    if(!seedMessageSent){
                        thisPlayerSeed = random.nextLong();

                        JsonPayload jpl = new JsonPayload();
                        jpl.setTAG(JsonPayloadTags.GAME_SEED);
                        jpl.setValue(thisPlayerSeed);
                        game.p2pConnector.sendData(jpl);

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
    public void onReceive(JsonPayload data) {
        int TAG = data.getTAG();

        switch (TAG){
            case JsonPayloadTags.READY:
                otherPlayerReady = (Boolean) data.getValue();
                break;
            case JsonPayloadTags.GAME_SEED:
                game.p2pConnector.removeReceivedDataListener(this);
                otherPlayerSeed = (Long) data.getValue();

                GameSettings.isLeftPlayer = otherPlayerSeed > thisPlayerSeed;

                GameSettings.setRandomSeed(otherPlayerSeed + thisPlayerSeed);

                seedDecided = true;
                break;
            default:
                System.out.println("NOT LEGAL JSON TAG");
        }
    }

    @Override
    public void onReceive(String data) {
        latestData = data;
    }


    private void initButtons() {

        TextButton buttonExit, buttonInitiate;
        buttonInitiate = new TextButton("Initiate Game", game.skin2, "default");
        buttonInitiate.setPosition(110, 190);
        buttonInitiate.setSize(280, 60);
        buttonInitiate.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        buttonInitiate.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                JsonPayload jpl = new JsonPayload();
                jpl.setTAG(JsonPayloadTags.READY);
                jpl.setValue(true);
                game.p2pConnector.sendData(jpl);
                LobbyScreen.this.setPlayerReady(true);
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                JsonPayload jpl = new JsonPayload();
                jpl.setTAG(JsonPayloadTags.READY);
                jpl.setValue(false);
                game.p2pConnector.sendData(jpl);
                LobbyScreen.this.setPlayerReady(false);
            }
        });

        buttonExit = new TextButton("Main Menu", game.skin2, "default");
        buttonExit.setPosition(110, 100);
        buttonExit.setSize(280, 60);
        buttonExit.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });


        stage.addActor(buttonInitiate);
        stage.addActor(buttonExit);
    }
}
