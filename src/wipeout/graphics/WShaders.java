package wipeout.graphics;

import arc.graphics.gl.*;

import static arc.Core.*;
import static mindustry.Vars.*;

public class WShaders{
    public static GrayscaleShader grayscale;
    public static GoldscaleShader golfScale;

    public static void init(){
        grayscale = new GrayscaleShader();
        golfScale = new GoldscaleShader();
    }

    public static class GrayscaleShader extends WLoadShader{
        public GrayscaleShader(){
            super("grayscale");
        }
    }

    public static class GoldscaleShader extends WLoadShader{
        public GoldscaleShader(){
            super("yellowscale");
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
