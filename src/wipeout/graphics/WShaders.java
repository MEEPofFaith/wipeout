package wipeout.graphics;

import arc.*;
import arc.graphics.gl.*;
import arc.math.*;

import static arc.Core.*;
import static mindustry.Vars.*;

public class WShaders{
    public static StaticShader contrast;
    public static ShutdownShader shutdown;
    public static GrayscaleShader grayscale;
    public static GoldscaleShader goldScale;

    public static void init(){
        contrast = new StaticShader();
        shutdown = new ShutdownShader();
        grayscale = new GrayscaleShader();
        goldScale = new GoldscaleShader();
    }

    public static class StaticShader extends WLoadShader{
        public int sections = 1;
        public float seed;
        public float intensity = 1f;

        public StaticShader(){
            super("screenspace", "static");
        }

        @Override
        public void apply(){
            setUniformi("u_sections", sections);
            setUniformf("u_seed", seed);
            setUniformf("u_intensity", intensity);
        }
    }

    public static class ShutdownShader extends WLoadShader{
        public float wipe = 0f;

        public ShutdownShader(){
            super("screenspace", "shutoff");
        }

        @Override
        public void apply(){
            setUniformf("u_wipe", wipe);
        }
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
