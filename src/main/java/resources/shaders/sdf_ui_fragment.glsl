#version 330 core

in vec2 textureCoord;

out vec4 fragColor;

uniform sampler2D texture;

const float width = 0.48;
const float edge = 0.15;

void main() {

    float distance = 1.0 - texture2D(texture, textureCoord).a;
    float alpha = 1.0 - smoothstep(width, width + edge, distance);

    fragColor = vec4(0, 0, 0, alpha);
}