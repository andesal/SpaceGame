package no.progark19.spacegame.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by anderssalvesen on 21.04.2018.
 */

public class MyProgressBar extends ProgressBar {

    public MyProgressBar(int width, int height, Color color) {
        super(0f, 1f, 0.01f, false, new ProgressBarStyle());
        getStyle().background = getColoredDrawable(width, height, Color.GRAY);
        getStyle().knob = getColoredDrawable(0, height, color);
        getStyle().knobBefore = getColoredDrawable(width, height,color);
        setWidth(width);
        setHeight(height);
        setAnimateDuration(0.0f);
        setAnimateDuration(0.1f);
    }

    public static Drawable getColoredDrawable(int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();

        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));

        pixmap.dispose();

        return drawable;
    }

}
