#version 460 core

layout(location = 0) in vec3 pos;
layout(location = 1) in vec2 tex;

out vec2 passTextureCoord;
out float visibility;
out float distanceToCam;

uniform ivec3 worldPos;
uniform mat4 view;
uniform mat4 project;

const float density = 0.0035;
const float gradient = 1.5;

void main() {
    vec4 posRelativeToCam = view * vec4(pos + worldPos, 1.0);
    gl_Position = project * posRelativeToCam;

    passTextureCoord = tex;

    float distance = length(posRelativeToCam.xyz);
    distanceToCam = pow(distance * 0.3, 0.25);
    visibility = exp(-pow((distance * density), gradient));
}