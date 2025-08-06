#define HIGHP

uniform sampler2D u_texture;
uniform float u_wipe;

varying vec2 v_texCoords;

bool inBounds(vec2 v){
    return v.x > 0.0 && v.y > 0.0 && v.x < 1.0 && v.y < 1.0;
}

void main() {
    vec2 t = v_texCoords.xy;
    vec4 color = texture2D(u_texture, t);
    vec3 greyScale = vec3(0.4, 0.4, 0.4);
    gl_FragColor = vec4(vec3(dot(color.rgb, greyScale)), color.a);
}
