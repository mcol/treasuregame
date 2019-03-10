#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

uniform vec2 u_resolution;
uniform vec4 u_ambient;
uniform sampler2D u_texture;
uniform sampler2D u_mask;

void main() {
    vec4 color = texture2D(u_texture, v_texCoords);
    vec2 lightCoord = gl_FragCoord.xy / u_resolution;
    vec4 light = texture2D(u_mask, lightCoord);
    vec3 ambient = u_ambient.rgb * u_ambient.a;
    vec3 intensity = ambient + light.rgb;
    gl_FragColor = v_color * vec4(color.rgb * intensity, color.a);
}
