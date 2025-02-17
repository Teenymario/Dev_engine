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

uniform mat4 model;
uniform mat4 view;
uniform mat4 project;
uniform vec3 lightPos;

const float density = 0.07;
const float gradient = 1.5;

void main() {
    vec4 worldPos = model * vec4(pos, 1.0);
    vec4 posRelativeToCam = view * worldPos;
    gl_Position = project * posRelativeToCam;
    passTextureCoord = textureCoord;
}