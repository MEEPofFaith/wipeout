package wipeout;

import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.*;
import mindustry.mod.*;
import wipeout.content.*;
import wipeout.graphics.*;

public class Wipeout extends Mod{
    private static final boolean debug = true;
    private WipeoutRenderer renderer;

    public Wipeout(){
    }

    @Override
    public void init(){
        WSounds.load();
        WShaders.init();
        renderer = new WipeoutRenderer();

        if(debug){
            Vars.ui.hudGroup.find("overlaymarker");

            Vars.ui.hudGroup.fill(t -> {
                t.name = "WipeoutTest";
                t.center().left().visible(() -> Vars.ui.hudfrag.shown);
                t.defaults().left().fillX();

                t.button("Win", () -> renderer.winStart()).wrapLabel(false);
                t.row();
                t.button("Loss", () -> renderer.lossStart()).wrapLabel(false);
                t.row();
                t.button("Reset", () -> renderer.reset()).wrapLabel(false);
            });
        }
    }
}
