#define HIGHP

uniform sampler2D u_texture;

varying vec2 v_texCoords;

vec3 contrast(vec3 col){
    col = col * col * (3 - 2 * col);
    return col * col * (3 - 2 * col);
}

void main() {
    vec2 t = v_texCoords.xy;
    vec4 color = texture2D(u_texture, t);
    vec3 greyScale = vec3(0.4, 0.4, 0.4);

    gl_FragColor = vec4(contrast(vec3(dot(color.rgb, greyScale))), color.a);
}
