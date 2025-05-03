#define HIGHP

#define EXP_VAL 3.0
#define EXP_POW 3.0

uniform sampler2D u_texture;

varying vec2 v_texCoords;

float exp(float a){
    float min = pow(EXP_VAL, -EXP_POW);
    float scale = 1.0 / (1.0 - min);

    if(a <= 0.5f) return (pow(EXP_VAL, EXP_POW * (a * 2.0 - 1.0)) - min) * scale / 2.0;
    return (2 - (pow(EXP_VAL, -EXP_POW * (a * 2.0 - 1.0)) - min) * scale) / 2.0;
}

vec3 contrast(vec3 col){
    col.r = exp(col.r);
    col.g = exp(col.g);
    col.b = exp(col.b);
    return col;
}

void main() {
    vec2 t = v_texCoords.xy;
    vec4 color = texture2D(u_texture, t);
    vec3 greyScale = vec3(0.35, 0.35, 0.35);

    gl_FragColor = vec4(contrast(vec3(dot(color.rgb, greyScale))), color.a);
}
