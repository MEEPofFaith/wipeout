package wipeout;

import mindustry.mod.*;
import wipeout.content.*;
import wipeout.graphics.*;

public class Wipeout extends Mod{
    private WipeoutRenderer renderer;

    public Wipeout(){
    }

    @Override
    public void init(){
        WSounds.load();
        WShaders.init();
        renderer = new WipeoutRenderer();
    }
}
