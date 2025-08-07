package wipeout.graphics;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.util.*;
import arc.util.pooling.*;
import mindustry.*;
import mindustry.graphics.*;
import mindustry.ui.*;

import static arc.Core.*;

public class WDrawf{
    public static final float textBorder = 32f;

    public static void displayText(CharSequence text){
        Font font = Fonts.tech;
        GlyphLayout layout = Pools.obtain(GlyphLayout.class, GlyphLayout::new);
        boolean ints = font.usesIntegerPositions();
        font.setUseIntegerPositions(false);

        font.getData().setScale(1f);
        layout.setText(font, text);
        float sWidth = graphics.getWidth() - 2 * textBorder,
            sHeight = graphics.getHeight() - 2 * textBorder;
        float targetRatio = sHeight / sWidth;
        float sourceRatio = layout.height / layout.width;
        float scale = targetRatio > sourceRatio ? sWidth / layout.width : sHeight / layout.height;
        font.getData().setScale(scale / Vars.renderer.getDisplayScale());
        layout.setText(font, text);

        font.setColor(Pal.accent);
        font.draw(text, camera.position.x, camera.position.y, Align.center);

        font.setUseIntegerPositions(ints);
        font.setColor(Color.white);
        font.getData().setScale(1f);
        Draw.reset();
        Pools.free(layout);
    }
}
