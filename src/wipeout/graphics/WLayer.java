package wipeout.graphics;

import mindustry.graphics.*;

public class WLayer{
    public static final float

        goldBegin = Layer.background - 0.1f,

        goldEnd = Layer.endPixeled - 0.1f;

    public static final float[] grayLayers = {
        Layer.turret - 0.5f, Layer.power - 1.1f, //Turrets and ground units
        Layer.legUnit - 2f, Layer.darkness - 1.1f, //Leg units
        Layer.flyingUnitLow - 0.1f, Layer.flyingUnit + 0.2f //Bullets and flying units
    };
}
