package wipeout.graphics;

import arc.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.util.*;
import mindustry.game.*;
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
        Events.on(WinEvent.class, e -> { //Win
            Sounds.corexplode.play();
            winTimer = 5 * 60;
        });
        Events.on(LoseEvent.class, e -> { //Loss. No, not that kind.
            Sounds.largeCannon.play();
            winTimer = -1f;
            loss = true;
        });
    }

    private void update(){
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
        Draw.draw(WLayer.grayBegin, () -> {
            grayBuffer.resize(graphics.getWidth(), graphics.getHeight());
            grayBuffer.begin();
        });

        Draw.draw(WLayer.grayEnd, () -> {
            grayBuffer.end();
            grayBuffer.blit(WShaders.grayscale);
        });

        Draw.draw(WLayer.goldBegin, () -> {
            goldBuffer.resize(graphics.getWidth(), graphics.getHeight());
            goldBuffer.begin();
        });

        Draw.draw(WLayer.goldEnd, () -> {
            goldBuffer.end();
            goldBuffer.blit(WShaders.grayscale);
        });
    }

    private void drawLoss(){
        Draw.draw(WLayer.grayBegin, () -> {
            globalBuffer.resize(graphics.getWidth(), graphics.getHeight());
            globalBuffer.begin();
        });

        Draw.draw(WLayer.goldEnd, () -> {
            globalBuffer.end();
            globalBuffer.blit(WShaders.grayscale);
        });
    }
}
