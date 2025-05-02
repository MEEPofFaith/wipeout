#define HIGHP

uniform sampler2D u_texture;

varying vec2 v_texCoords;

void main() {
    gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);
}
