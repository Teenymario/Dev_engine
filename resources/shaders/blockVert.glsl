#version 460 core

layout(location = 0) in vec3 pos;

out vec2 passTextureCoord;
out float visibility;

uniform mat4 view;
uniform mat4 project;
uniform int[6] faces;

layout(binding = 0, std430) buffer TextureCoords {
    vec4 texCoords[];
    /*
    xy - Top left
    zy - Top right
    xw - Bottom left
    zw - Bottom right
    */
};

const float density = 0.07;
const float gradient = 1.5;

void main() {
    vec4 posRelativeToCam = view * vec4(pos, 1.0);
    gl_Position = project * posRelativeToCam;

    //It looks terrible I know
    int faceIndex = int(floor(float(gl_VertexID) / 6));
    int corner = gl_VertexID % 6;

    // Select the correct texture coordinate based on the vertex index
    if (corner == 0) {
        passTextureCoord = texCoords[faces[faceIndex]].xy;
    } else if (corner == 1) {
        passTextureCoord = texCoords[faces[faceIndex]].zw;
    } else if (corner == 2) {
        passTextureCoord = texCoords[faces[faceIndex]].xw;
    } else if (corner == 3) {
        passTextureCoord = texCoords[faces[faceIndex]].xy;
    } else if (corner == 4) {
        passTextureCoord = texCoords[faces[faceIndex]].zy;
    } else {
        passTextureCoord = texCoords[faces[faceIndex]].zw;
    }

    float distance = length(posRelativeToCam.xyz);
    visibility = exp(-pow((distance * density), gradient));
}