#define HIGHP

uniform sampler2D u_texture;
uniform float u_wipe;

varying vec2 v_texCoords;

bool inBounds(vec2 v){
    return v.x > 0 && v.y > 0 && v.x < 1 && v.y < 1;
}

void main() {
    vec2 t = v_texCoords.xy;
    vec4 color = vec4(0.0);

    if(u_wipe <= 0.01){
        color = texture2D(u_texture, t);
    }else{
        if(u_wipe > 0.95){
            gl_FragColor = color;
            return;
        }
        t -= vec2(0.5, 0.5);
        t.x /= 1 + u_wipe * 10;
        t.y *= 1 + u_wipe * 200;
        t += vec2(0.5);

        if(inBounds(t)){
            color = texture2D(u_texture, t);
        }
    }
    gl_FragColor = color;
}
