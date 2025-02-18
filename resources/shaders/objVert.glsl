#version 460 core

layout(location = 0) in vec3 pos;
layout(location = 1) in vec2 textureCoord;
layout(location = 2) in vec3 normal;
layout(location = 3) in float material;

out vec2 passTextureCoord;
out float visibility;

uniform mat4 view;
uniform mat4 project;
uniform int blockID;

layout(binding = 0, std430) buffer TextureCoords {
    vec2 texCoords[];
};

const float density = 0.07;
const float gradient = 1.5;

void main() {
    vec4 posRelativeToCam = view * vec4(pos, 1.0);
    gl_Position = project * posRelativeToCam;
    passTextureCoord = texCoords[int(floor(gl_Position.y)) + 4];

    float distance = length(posRelativeToCam.xyz);
    visibility = exp(-pow((distance * density), gradient));
}