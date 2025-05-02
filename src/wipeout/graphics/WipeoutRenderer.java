package wipeout.graphics;

import arc.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.util.*;
import mindustry.game.*;
import mindustry.game.EventType.*;

import static arc.Core.*;

public class WipeoutRenderer{
    private final FrameBuffer grayBuffer;
    private final FrameBuffer goldBuffer;
    private float winTimer = -1f;

    public WipeoutRenderer(){
        grayBuffer = new FrameBuffer();
        goldBuffer = new FrameBuffer();

        //Stuff that needs to be run
        Events.run(Trigger.update, this::update);
        Events.run(Trigger.draw, this::draw);

        Events.on(EventType.WinEvent.class, e -> {
            wipeout();
        });
    }

    private void wipeout(){
        winTimer = 5 * 60; //5 seconds for now
    }

    private void update(){
        winTimer -= Time.delta;
    }

    private void draw(){
        if(winTimer < 0) return;

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
}
