#version 460 core

in vec2 passTextureCoord;
in float visibility;
in float distanceToCam;

out vec4 outColor;

uniform vec3 skyColor;

uniform sampler2D textureAtlas;

/*
Definitions of material in float[] form
0 = texture ID
1 = transparency
2 = specExp
3 = opticalDensity
4, 5, 6 = ambient
7, 8, 9 = diffuse
10, 11, 12 = specular
13, 14, 15 = emmisive
*/

void main() {
    outColor = mix(vec4(skyColor, 1.0), texture(textureAtlas, passTextureCoord), visibility);

    if(distanceToCam < 10.0) {
        outColor /= distanceToCam;
    } else {
        outColor = vec4(0.0, 0.0, 0.0, 1.0);
    }

    if(outColor.a < 0.1) {
        discard;
    }
}