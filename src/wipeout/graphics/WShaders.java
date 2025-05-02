package wipeout.graphics;

import arc.graphics.gl.*;

import static arc.Core.*;
import static mindustry.Vars.*;

public class WShaders{
    public static GrayscaleShader grayscale;
    public static GoldscaleShader goldScale;

    public static void init(){
        grayscale = new GrayscaleShader();
        goldScale = new GoldscaleShader();
    }

    public static class GrayscaleShader extends WLoadShader{
        public GrayscaleShader(){
            super("screenspace", "grayscale");
        }
    }

    public static class GoldscaleShader extends WLoadShader{
        public GoldscaleShader(){
            super("screenspace", "goldscale");
        }
    }

    public static class WLoadShader extends Shader{
        public WLoadShader(String vert, String frag){
            super(
                files.internal("shaders/" + vert + ".vert"),
                tree.get("shaders/" + frag + ".frag")
            );
        }

        public WLoadShader(String frag){
            this("defualt", frag);
        }
    }
}
