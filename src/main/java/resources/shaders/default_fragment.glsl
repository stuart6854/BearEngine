#version 330 core

in vec2 textureCoord;
in vec3 vertexNormal;
in vec4 vertexColor;

out vec4 fragColor;

uniform sampler2D texture;

void main() {
    vec4 textureColor = texture2D(texture, textureCoord);

    fragColor = textureColor * vertexColor;
//    fragColor = vec4(0, 1, 0, 1);
}