package wipeout.graphics;

import arc.*;
import arc.graphics.*;
import arc.graphics.Texture.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import arc.util.pooling.*;
import mindustry.*;
import mindustry.audio.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import wipeout.content.*;

import static arc.Core.*;

public class WipeoutRenderer{
    public static final float textBorder = 32f;
    private final FrameBuffer buffer1;
    private final FrameBuffer buffer2;
    private final SoundLoop staticLoop;

    private float animTimer = -1f;
    private boolean play = false;
    private boolean loss = false;
    private boolean pain = false;
    private String displayText = "CAPTURED";

    public WipeoutRenderer(){
        buffer1 = new FrameBuffer();
        buffer2 = new FrameBuffer();
        staticLoop = new SoundLoop(WSounds.noise, 1f);

        //Stuff that needs to be run
        Events.run(Trigger.update, this::update);
        Events.run(Trigger.draw, this::draw);

        Events.on(WorldLoadEvent.class, e -> reset());
        Events.on(SectorCaptureEvent.class, e -> {
            displayText = Vars.state.rules.attackMode ? "CAPTURED" : "DEFENDED";
            winStart();
        });
        Events.on(GameOverEvent.class, e -> {
            if(Vars.player.team() == e.winner){
                displayText = "WIPEOUT";
                winStart();
            }else{
                lossStart();
            }
        });
    }

    public void winStart(){
        Sounds.corexplode.play();
        play = true;
        animTimer = 5 * 60;
        loss = false;
        WShaders.contrast.seed = Time.time;
    }

    public void lossStart(){
        pain = Mathf.randomBoolean(0.05f); // 5% chance

        if(pain) WSounds.pain.play();
        Sounds.largeCannon.play();
        play = true;
        animTimer = 2 * 60;
        loss = true;
        WShaders.contrast.seed = Time.time;
    }

    public void reset(){
        play = false;
        animTimer = -1f;
        loss = false;
        staticLoop.stop();
        pain = false;
    }

    private void update(){
        if(Vars.state.isPaused() || !play){
            staticLoop.stop();
            return;
        }

        animTimer -= Time.delta;

        Vec2 camPos = camera.position;
        if(!loss){
            staticLoop.update(camPos.x, camPos.y, winStatic() && animTimer > 0, 1f);
        }else{
            if(Time.time - WShaders.contrast.seed > (1f - glitchFin()) * 20f) WShaders.contrast.seed = Time.time;
            staticLoop.update(camPos.x, camPos.y, !pain && animTimer > 0, glitchFin() * 2f);
        }
    }

    private void draw(){
        if(loss){
            drawLoss();
        }else if(animTimer > 0){
            drawWin();
        }
    }

    private void drawWin(){
        Draw.draw(Layer.darkness + 0.1f, () -> {
            buffer2.resize(graphics.getWidth(), graphics.getHeight());
            buffer2.begin(Color.clear);

            displayText();

            buffer2.end();

            TextureFilter filter = buffer2.getTexture().getMinFilter();
            buffer2.getTexture().setFilter(TextureFilter.nearest);
            buffer2.blit(WShaders.passThrough);
            buffer2.getTexture().setFilter(filter);
        });

        if(winStatic()){
            Draw.draw(WLayer.goldBegin, () -> {
                buffer1.resize(graphics.getWidth(), graphics.getHeight());
                buffer1.begin(Color.clear);
            });

            Draw.draw(WLayer.goldEnd, () -> {
                buffer1.end();
                WShaders.contrast.sections = Mathf.ceil(graphics.getHeight() / 25f);
                WShaders.contrast.intensity = Interp.pow2In.apply(animTimer > 4.7f * 60f
                    ? Mathf.curve(animTimer, 4.7f * 60f, 5f * 60f)
                    : Mathf.curve(animTimer, 0f, 0.3f * 60f)) / 2f;
                buffer1.blit(WShaders.contrast);
            });
            return;
        }

        Draw.draw(WLayer.goldBegin, () -> {
            buffer1.resize(graphics.getWidth(), graphics.getHeight());
            buffer1.begin(Color.clear);
        });

        for(int i = 0; i < WLayer.grayLayers.length; i += 2){
            Draw.draw(WLayer.grayLayers[i], () -> {
                buffer1.end();
                buffer1.blit(WShaders.goldScale);

                buffer1.begin(Color.clear);
            });

            Draw.draw(WLayer.grayLayers[i + 1], () -> {
                Draw.reset();

                buffer1.end();
                buffer1.blit(WShaders.grayscale);

                buffer1.begin(Color.clear);
            });
        }

        Draw.draw(WLayer.goldEnd, () -> {
            buffer1.end();
            buffer1.blit(WShaders.goldScale);
        });
    }

    private void drawLoss(){
        Draw.draw(WLayer.goldBegin, () -> {
            buffer1.resize(graphics.getWidth(), graphics.getHeight());
            buffer1.begin(Color.clear);
        });

        Draw.draw(WLayer.goldEnd, () -> {
            buffer1.end();

            buffer2.resize(graphics.getWidth(), graphics.getHeight());
            buffer2.begin();

            WShaders.contrast.sections = Mathf.ceil(graphics.getHeight() / 100f * (1f + glitchFin() * 4f));
            WShaders.contrast.intensity = glitchFin() * 20f;
            buffer1.blit(WShaders.contrast);

            buffer2.end();

            WShaders.shutdown.wipe = (animTimer < 0 ? Mathf.clamp(-animTimer / 30f) : 0f);
            buffer2.blit(WShaders.shutdown);
        });
    }

    private float glitchFin(){
        return Interp.pow3In.apply(1f - (animTimer / 120f));
    }

    private boolean winStatic(){
        return animTimer > 4.7 * 60 || animTimer < 0.3 * 60;
    }

    private void displayText(){
        Font font = Fonts.tech;
        GlyphLayout layout = Pools.obtain(GlyphLayout.class, GlyphLayout::new);
        boolean ints = font.usesIntegerPositions();
        font.setUseIntegerPositions(false);

        font.getData().setScale(1f);
        layout.setText(font, displayText);
        float sWidth = graphics.getWidth() - 2 * textBorder,
            sHeight = graphics.getHeight() - 2 * textBorder;
        float targetRatio = sHeight / sWidth;
        float sourceRatio = layout.height / layout.width;
        float scale = targetRatio > sourceRatio ? sWidth / layout.width : sHeight / layout.height;
        font.getData().setScale(scale / Vars.renderer.getDisplayScale());
        layout.setText(font, displayText);

        font.setColor(Pal.accent);
        font.draw(displayText, camera.position.x, camera.position.y, Align.center);

        font.setUseIntegerPositions(ints);
        font.setColor(Color.white);
        font.getData().setScale(1f);
        Draw.reset();
        Pools.free(layout);
    }
}
