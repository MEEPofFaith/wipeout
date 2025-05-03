package wipeout.graphics;

import arc.graphics.gl.*;

import static arc.Core.*;
import static mindustry.Vars.*;

public class WShaders{
    public static ContrastShader contrast;
    public static GrayscaleShader grayscale;
    public static GoldscaleShader goldScale;

    public static void init(){
        contrast = new ContrastShader();
        grayscale = new GrayscaleShader();
        goldScale = new GoldscaleShader();
    }

    public static class ContrastShader extends WLoadShader{
        public ContrastShader(){
            super("screenspace", "contrastscale");
        }
    }

    public static class GrayscaleShader extends WLoadShader{
        public float wipe = 0f;

        public GrayscaleShader(){
            super("screenspace", "grayscale");
        }

        @Override
        public void apply(){
            super.apply();

            setUniformf("u_wipe", wipe);
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
