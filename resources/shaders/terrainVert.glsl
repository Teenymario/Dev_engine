#version 460 core

layout(location = 0) in vec3 pos;
layout(location = 1) in vec2 textureCoord;
layout(location = 2) in vec3 normal;
layout(location = 3) in float material;

out vec2 passTextureCoord;
out vec3 surfaceNorm;
out vec3 toLighVec;
out vec3 toCamVec;
out float visibility;
out float fmaterialID;

uniform vec3 blockPos;
uniform vec3 cameraPos;
uniform mat4 view;
uniform mat4 project;
uniform vec3 lightPos;

const float density = 0.07;
const float gradient = 1.5;

void main() {
    vec3 worldPos = blockPos + pos;
    vec4 posRelativeToCam = view * vec4(worldPos, 1.0);
    gl_Position = project * posRelativeToCam;
    passTextureCoord = textureCoord;

    surfaceNorm = normal;
    toLighVec = lightPos - worldPos;
    toCamVec = cameraPos - worldPos;

    float distance = length(posRelativeToCam.xyz);
    visibility = exp(-pow((distance * density), gradient));
    fmaterialID = material;
}