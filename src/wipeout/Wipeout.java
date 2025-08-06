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
            Table test = new Table(t -> {
                t.button("Win", () -> renderer.winStart());
                t.row();
                t.button("Loss", () -> renderer.lossStart());
            });
            test.left();
            test.name = "WipeoutTest";
            test.setOrigin(Align.left);
            Vars.ui.hudGroup.addChild(test);
        }
    }
}
