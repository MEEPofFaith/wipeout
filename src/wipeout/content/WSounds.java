package wipeout.content;

import arc.audio.*;
import mindustry.*;

public class WSounds{
    public static Sound noise = new Sound();

    public static void load(){
        noise = Vars.tree.loadSound("noise");
    }
}
