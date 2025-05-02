#define HIGHP

uniform sampler2D u_texture;

varying vec2 v_texCoords;

void main() {
    vec2 t = v_texCoords.xy;
    vec4 color = texture2D(u_texture, t);
    vec3 greyScale = vec3(0.6, 0.6, 0.6);

    //TODO better shader
    gl_FragColor = vec4(vec3(dot(color.rgb, greyScale)) * vec3(1.0, 0.9, 0.0), color.a);
}
