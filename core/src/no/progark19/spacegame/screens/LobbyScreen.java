package no.progark19.spacegame.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.FitViewport;

import no.progark19.spacegame.SpaceGame;
import no.progark19.spacegame.interfaces.ReceivedDataListener;

public class LobbyScreen implements Screen, ReceivedDataListener {
    private String recievedData;

    private final SpaceGame game;
    private BitmapFont font;
    private GlyphLayout layout;
    private boolean onSupportedDevice;

    private Stage stage;

    private ShapeRenderer shapeRenderer;

    private Button worthlessButton; //FIXME network deug, remove later

    // Dark network magics
    //private P2pConnector p2pRec;

    public LobbyScreen(final SpaceGame game){
        this.game = game;
        this.stage = new Stage(new FitViewport(SpaceGame.WIDTH, SpaceGame.HEIGHT, game.camera));
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
        this.layout = new GlyphLayout();

        onSupportedDevice = Gdx.app.getType() == Application.ApplicationType.Android;

        /*Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = game.getSkin().getDrawable("up");
        style.down = game.getSkin().getDrawable("down");*/
        worthlessButton = new Button();

        stage.addActor(worthlessButton);


    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        if (onSupportedDevice){
            game.p2pConnector.addReceivedDataListener(this);
            game.p2pConnector.discoverPeers();
        }
    }

    @Override
    public void render(float delta) {
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

        if (onSupportedDevice){
            if (game.p2pConnector.hasConnection()){
                font.setColor(Color.GREEN);
                layout.setText(font, "FOUND A CREW-MEMBER!\n"+ game.p2pConnector.getOtherPeerName());
            } else {
                font.setColor(Color.GREEN);
                layout.setText(font, "Looking for crew");

                //game.p2pConnector.printSomething();

            }
        } else {
            font.setColor(Color.RED);
            layout.setText(font, "this device is currently not supported");
        }
        font.draw(game.batch, layout,
                SpaceGame.WIDTH/2 - layout.width/2, SpaceGame.HEIGHT/2 - layout.height/2
        );
        game.batch.end();

        stage.act();
        stage.draw();
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


    @Override
    public void onReceive(Json data) {
        System.out.println(data);
    }
}
