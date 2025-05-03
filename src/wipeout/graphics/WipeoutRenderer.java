package wipeout.graphics;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.math.*;
import arc.util.*;
import mindustry.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;

import static arc.Core.*;

public class WipeoutRenderer{
    private final FrameBuffer globalBuffer;
    private final FrameBuffer grayBuffer;
    private final FrameBuffer goldBuffer;

    private float animTimer = -1f;
    private boolean loss = false;

    public WipeoutRenderer(){
        globalBuffer = new FrameBuffer();
        goldBuffer = new FrameBuffer();
        grayBuffer = new FrameBuffer();

        //Stuff that needs to be run
        Events.run(Trigger.update, this::update);
        Events.run(Trigger.draw, this::draw);

        Events.on(WorldLoadEvent.class, e -> { //Reset on world load
            animTimer = -1f;
            loss = false;
        });
        Events.on(SectorCaptureEvent.class, e -> { //Win
            Log.info("Capture");
            Sounds.corexplode.play();
            animTimer = 5 * 60;
        });
        Events.on(GameOverEvent.class, e -> { //Loss. No, not that kind.
            if(Vars.player.team() == e.winner) return;

            Log.info("Game Over");
            Sounds.largeCannon.play();
            animTimer = 2 * 60;
            loss = true;
        });
    }

    private void update(){
        if(Vars.state.isPaused()) return;

        animTimer -= Time.delta;
    }

    private void draw(){
        if(loss){
            drawLoss();
        }else if(animTimer > 0){
            drawWin();
        }
    }

    private void drawWin(){
        if(animTimer > 4.8 * 60 || animTimer < 0.2 * 60){
            Draw.draw(WLayer.goldBegin, () -> {
                globalBuffer.resize(graphics.getWidth(), graphics.getHeight());
                globalBuffer.begin(Color.clear);
            });

            Draw.draw(WLayer.grayEnd, () -> {
                globalBuffer.end();
                globalBuffer.blit(WShaders.contrast);
            });
            return;
        }

        Draw.draw(WLayer.goldBegin, () -> {
            goldBuffer.resize(graphics.getWidth(), graphics.getHeight());
            goldBuffer.begin(Color.clear);
        });

        Draw.draw(WLayer.grayBegin - 0.1f, () -> {
            goldBuffer.end();
            goldBuffer.blit(WShaders.goldScale);
        });

        Draw.draw(WLayer.grayBegin, () -> {
            grayBuffer.resize(graphics.getWidth(), graphics.getHeight());
            grayBuffer.begin(Color.clear);
        });

        Draw.draw(WLayer.grayEnd, () -> {
            grayBuffer.end();
            WShaders.grayscale.wipe = 0f;
            grayBuffer.blit(WShaders.grayscale);
        });

        Draw.draw(WLayer.grayEnd + 0.1f, () -> {
            goldBuffer.resize(graphics.getWidth(), graphics.getHeight());
            goldBuffer.begin(Color.clear);
        });

        Draw.draw(WLayer.goldEnd, () -> {
            goldBuffer.end();
            goldBuffer.blit(WShaders.goldScale);
        });
    }

    private void drawLoss(){
        Draw.draw(WLayer.goldBegin, () -> {
            globalBuffer.resize(graphics.getWidth(), graphics.getHeight());
            globalBuffer.begin(Color.clear);
        });

        Draw.draw(WLayer.grayEnd, () -> {
            globalBuffer.end();
            if(animTimer > 1.9 * 60){
                globalBuffer.blit(WShaders.contrast);
            }else{
                WShaders.grayscale.wipe = (animTimer < 0 ? Mathf.clamp(-animTimer / 30f) : 0f);
                globalBuffer.blit(WShaders.grayscale);
            }
        });
    }
}
