#version 460 core

in vec2 passTextureCoord;
in vec3 surfaceNorm;
in vec3 toLighVec;
in vec3 toCamVec;
in float visibility;
in float fmaterialID;

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
    //outColor = texture(textureAtlas, passTextureCoord);
    outColor = mix(vec4(skyColor, 1.0), texture(textureAtlas, passTextureCoord), visibility);
}