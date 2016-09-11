#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texcoord;
layout(location = 2) in vec3 normal;
layout(location = 3) in vec4 color;

out vec2 textureCoord;
out vec3 vertexNormal;
out vec4 vertexColor;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

void main() {
    textureCoord = texcoord;
    vertexNormal = normal;
    vertexColor = color;

    mat4 mvp = projection * view * model;

    gl_Position = mvp * vec4(position, 1.0);
}