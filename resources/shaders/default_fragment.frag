#version 330 core

in vec4 vertexColor;
in vec2 textureCoord;
in vec3 vertexNormal;

out vec4 fragColor;

uniform sampler2D texture;

void main() {
    vec4 textureColor = texture2D(texture, textureCoord);

    fragColor = vertexColor * textureColor;
}