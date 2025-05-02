package wipeout.graphics;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.util.*;
import mindustry.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;

import static arc.Core.*;

public class WipeoutRenderer{
    private final FrameBuffer globalBuffer;
    private final FrameBuffer grayBuffer;
    private final FrameBuffer goldBuffer;

    private float winTimer = -1f;
    private boolean loss = false;

    public WipeoutRenderer(){
        globalBuffer = new FrameBuffer();
        grayBuffer = new FrameBuffer();
        goldBuffer = new FrameBuffer();

        //Stuff that needs to be run
        Events.run(Trigger.update, this::update);
        Events.run(Trigger.draw, this::draw);

        Events.on(WorldLoadEvent.class, e -> { //Reset on world load
            winTimer = -1f;
            loss = false;
        });
        Events.on(SectorCaptureEvent.class, e -> { //Win
            Log.info("Capture");
            Sounds.corexplode.play();
            winTimer = 5 * 60;
        });
        Events.on(GameOverEvent.class, e -> { //Loss. No, not that kind.
            if(Vars.player.team() == e.winner) return;

            Log.info("Game Over");
            Sounds.largeCannon.play();
            winTimer = -1f;
            loss = true;
        });
    }

    private void update(){
        if(Vars.state.isPaused()) return;

        winTimer -= Time.delta;
    }

    private void draw(){
        if(loss){
            drawLoss();
        }else if(winTimer > 0){
            drawWin();
        }
    }

    private void drawWin(){
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
            grayBuffer.blit(WShaders.grayscale);
        });
    }

    private void drawLoss(){
        Draw.draw(WLayer.goldBegin, () -> {
            globalBuffer.resize(graphics.getWidth(), graphics.getHeight());
            globalBuffer.begin(Color.clear);
        });

        Draw.draw(WLayer.grayEnd, () -> {
            globalBuffer.end();
            globalBuffer.blit(WShaders.grayscale);
        });
    }
}
