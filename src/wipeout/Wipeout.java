package wipeout;

import arc.*;
import arc.util.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import mindustry.ui.dialogs.*;
import wipeout.graphics.*;

public class Wipeout extends Mod{
    private WipeoutRenderer renderer;

    public Wipeout(){
    }

    @Override
    public void init(){
        WShaders.init();
        renderer = new WipeoutRenderer();
    }
}
