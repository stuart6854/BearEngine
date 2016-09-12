#version 330 core

in vec3 FragPosition;
in vec3 Normal;

out vec4 fragColor;

uniform vec4 objectColor;
uniform vec4 lightColor;
uniform vec3 lightPosition;

void main() {

    // Ambient
    float ambientStrength = 0.1f;
    vec3 ambient = ambientStrength * lightColor.rgb;

    //Diffuse
    vec3 norm = normalize(Normal);
    vec3 lightDir = normalize(lightPosition - FragPosition);
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = diff * lightColor.rgb;

    //Result
    vec3 result = (ambient + diffuse) * objectColor.rgb;
    fragColor = vec4(result, 1.0f);
//    fragColor = vec4(FragPosition, 1.0f);
}