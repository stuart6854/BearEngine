#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texCoords;
layout(location = 2) in vec3 normal;

out vec3 FragPosition;
out vec2 TextureCoords;
out vec3 Normal;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
    FragPosition = vec3(model * vec4(position, 1.0f));
    TextureCoords = texCoords;
    Normal = mat3(transpose(inverse(model))) * normal;

    mat4 mvp = projection * view * model;
    gl_Position = mvp * vec4(position, 1.0f);
}