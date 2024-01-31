#version 460 core
#define MAX_TEXTURES 16

in vec2 passTextureCoord;
in vec3 surfaceNorm;
in vec3 toLighVec;
in vec3 toCamVec;
in float visibility;
in float fmaterialID;

out vec4 outColor;

uniform sampler2D textureSampler[MAX_TEXTURES];
uniform vec3 lightCol;
uniform vec3 skyColor;
uniform float materials[512];

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

vec4 defineColor(int materialID) {
    vec3 unitNormal = normalize(surfaceNorm);
    vec3 unitLightVec = normalize(toLighVec);

    float nDotL = dot(unitNormal, unitLightVec);
    float brightness = max(nDotL, 0.1);
    vec3 diffuse = vec3(materials[materialID * 16 + 7], materials[materialID * 16 + 8], materials[materialID * 16 + 9]) * lightCol * brightness;

    vec3 unitToCamVec = normalize(toCamVec);
    vec3 reflectLightDir = reflect(-unitLightVec, unitNormal);
    float specularFactor = dot(unitToCamVec, reflectLightDir);
    specularFactor = max(specularFactor, 0.0);
    float dampedFactor = pow(specularFactor, materials[materialID * 16 + 2]);
    vec3 specular = vec3(materials[materialID * 16 + 10], materials[materialID * 16 + 11], materials[materialID * 16 + 12]) * dampedFactor * lightCol;

    return vec4(diffuse, 1.0) + vec4(specular, 1.0) * vec4(materials[materialID * 16 + 4], materials[materialID * 16 + 5], materials[materialID * 16 + 6], materials[materialID * 16 + 1]);
}

void main() {

    int materialID = int(fmaterialID + 0.5);

    if(materials[materialID * 16] != -1) {
        vec4 textureColor = texture(textureSampler[int(materials[materialID * 16] + 0.5)], passTextureCoord);
        if(textureColor.a < 0.0001 || visibility < 0.0001) {
            discard;
        }

        outColor = mix(vec4(skyColor, 1.0), defineColor(materialID) * textureColor, visibility);
    } else {
        outColor = mix(vec4(skyColor, 1.0), defineColor(materialID), visibility);
    }
}