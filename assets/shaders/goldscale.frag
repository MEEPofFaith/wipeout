#define HIGHP

#define FADESTART 0.2
#define FADEEND 0.5

uniform sampler2D u_texture;

varying vec2 v_texCoords;

void main() {
    vec2 t = v_texCoords.xy;
    vec4 color = texture2D(u_texture, t);
    vec3 greyScale = vec3(0.3, 0.3, 0.3);
    vec4 gray = vec4(vec3(dot(color.rgb, greyScale)), color.a);

    if(t.y < FADESTART){
        gl_FragColor = gray;
    }else{
        vec4 yellow = vec4(gray.rgb * vec3(1.0, 0.7, 0.3), gray.a);

        if(t.y > FADEEND){
            gl_FragColor = yellow;
        }else{
            gl_FragColor = gray + (yellow - gray) * (t.y - FADESTART) / (FADEEND - FADESTART);
        }
    }
}
