#define HIGHP

uniform sampler2D u_texture;
uniform float u_wipe;

varying vec2 v_texCoords;

bool inBounds(vec2 v){
    return v.x > 0.0 && v.y > 0.0 && v.x < 1.0 && v.y < 1.0;
}

//Interp.expOut
float e(float a, float v, float p){
    float min = pow(v, -p);
    return (1.0 - (pow(v, -p * a) - min) * (1.0 / (1.0 - min)));
}

void main() {
    vec2 t = v_texCoords.xy;
    vec4 color = vec4(0.0, 0.0, 0.0, 1.0);

    if(u_wipe <= 0.001){
        color = texture2D(u_texture, t);
    }else{
        if(u_wipe > 0.999){ //Don't divide by 0
            gl_FragColor = color;
            return;
        }
        t -= vec2(0.5);

        t.x *= e(1.0 - u_wipe, 0.5, 6.0);
        t.y /= 1.0 - e(u_wipe, 15.0, 3.0);

        t += vec2(0.5);

        if(inBounds(t)){
            color = texture2D(u_texture, t);
        }
    }
    gl_FragColor = color;
}
