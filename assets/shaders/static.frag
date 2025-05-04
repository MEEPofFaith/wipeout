#define HIGHP

#define EXP_VAL 3.0
#define EXP_POW 3.0

uniform sampler2D u_texture;
uniform int u_sections;
uniform float u_seed;
uniform float u_intensity;

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

float rand(float sec){
    return sin(u_seed + sec) * 0.2f;
}

void main() {
    vec2 t = v_texCoords.xy;
    t.x += rand(floor(mod(t.y + u_seed / 251.0, 1.0) * u_sections)) * u_intensity; //random arbitrary value lesgoooooo
    t.x = mod(t.x, 1.0);

    vec4 color = texture2D(u_texture, t);
    vec3 greyScale = vec3(0.35, 0.35, 0.35);

    gl_FragColor = vec4(contrast(vec3(dot(color.rgb, greyScale))), color.a);
}
