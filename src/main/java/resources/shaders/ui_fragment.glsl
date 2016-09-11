#version 330 core

in vec2 textureCoord;

out vec4 fragColor;

uniform sampler2D texture;

void main() {
    vec4 textureColor = texture2D(texture, textureCoord);

//    fragColor = vec4(textureColor.xyz, 0.25);
    fragColor = textureColor;
}