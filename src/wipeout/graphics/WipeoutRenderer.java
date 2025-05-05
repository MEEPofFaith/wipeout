package wipeout.graphics;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.*;
import mindustry.audio.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import wipeout.content.*;

import static arc.Core.*;

public class WipeoutRenderer{
    private final FrameBuffer buffer1;
    private final FrameBuffer buffer2;
    private final SoundLoop staticLoop;

    private float animTimer = -1f;
    private boolean play = false;
    private boolean loss = false;
    private boolean noisePlayed = true;
    private boolean pain = false;

    public WipeoutRenderer(){
        buffer1 = new FrameBuffer();
        buffer2 = new FrameBuffer();
        staticLoop = new SoundLoop(WSounds.noise, 1f);

        //Stuff that needs to be run
        Events.run(Trigger.update, this::update);
        Events.run(Trigger.draw, this::draw);

        Events.on(WorldLoadEvent.class, e -> reset());
        Events.on(SectorCaptureEvent.class, e -> { //Win
            WSounds.noise.play();
            Sounds.corexplode.play();
            play = true;
            animTimer = 5 * 60;
            noisePlayed = false;
            WShaders.contrast.seed = Time.time;
        });
        Events.on(GameOverEvent.class, e -> { //Loss. No, not that kind.
            if(Vars.player.team() == e.winner) return;

            pain = Mathf.randomBoolean(0.01f); //1% chance

            if(pain) WSounds.pain.play();
            Sounds.largeCannon.play();
            play = true;
            animTimer = 2 * 60;
            loss = true;
            WShaders.contrast.seed = Time.time;
        });
    }

    private void reset(){
        play = false;
        animTimer = -1f;
        loss = false;
        staticLoop.stop();
        pain = false;
    }

    private void update(){
        if(Vars.state.isPaused() || !play) return;

        animTimer -= Time.delta;

        if(!loss){
            if(!noisePlayed && animTimer < 0.3 * 60){
                WSounds.noise.play();
                noisePlayed = true;
            }
        }else{
            if(Time.time - WShaders.contrast.seed > (1f - glitchFin()) * 20f) WShaders.contrast.seed = Time.time;
            Vec2 camPos = camera.position;
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
        if(animTimer > 4.7 * 60 || animTimer < 0.3 * 60){
            Draw.draw(WLayer.goldBegin, () -> {
                buffer1.resize(graphics.getWidth(), graphics.getHeight());
                buffer1.begin(Color.clear);
            });

            Draw.draw(WLayer.grayEnd, () -> {
                buffer1.end();
                WShaders.contrast.intensity = Interp.pow2In.apply(animTimer > 4.7f * 60f
                    ? Mathf.curve(animTimer, 4.7f * 60f, 5f * 60f)
                    : Mathf.curve(animTimer, 0f, 0.3f * 60f));
                buffer1.blit(WShaders.contrast);
            });
            return;
        }

        Draw.draw(WLayer.goldBegin, () -> {
            buffer2.resize(graphics.getWidth(), graphics.getHeight());
            buffer2.begin(Color.clear);
        });

        Draw.draw(WLayer.grayBegin - 0.1f, () -> {
            buffer2.end();
            buffer2.blit(WShaders.goldScale);
        });

        Draw.draw(WLayer.grayBegin, () -> {
            buffer2.resize(graphics.getWidth(), graphics.getHeight());
            buffer2.begin(Color.clear);
        });

        Draw.draw(WLayer.grayEnd, () -> {
            buffer2.end();
            buffer2.blit(WShaders.grayscale);
        });

        Draw.draw(WLayer.grayEnd + 0.1f, () -> {
            buffer2.resize(graphics.getWidth(), graphics.getHeight());
            buffer2.begin(Color.clear);
        });

        Draw.draw(WLayer.goldEnd, () -> {
            buffer2.end();
            buffer2.blit(WShaders.goldScale);
        });
    }

    private void drawLoss(){
        Draw.draw(WLayer.goldBegin, () -> {
            buffer1.resize(graphics.getWidth(), graphics.getHeight());
            buffer1.begin(Color.clear);
        });

        Draw.draw(WLayer.grayEnd, () -> {
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
}
