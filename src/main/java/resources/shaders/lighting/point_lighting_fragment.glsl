// Reference: http://learnopengl.com/#!Lighting/Basic-Lighting

#version 330 core

struct Light{
    vec3 position;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;

    float constant;
    float linear;
    float quadratic;
};

struct Material{
    sampler2D diffuse;
    sampler2D specular;
    float shininess;
};

in vec3 FragPosition;
in vec2 TextureCoords;
in vec3 Normal;

out vec4 fragColor;

uniform vec3 viewPosition;
uniform vec4 objectColor;
uniform Light light;
uniform Material material;

void main() {

    // Ambient
    vec3 ambient = light.ambient * vec3(texture(material.diffuse, TextureCoords));

    //Diffuse
    vec3 norm = normalize(Normal);
    vec3 lightDir = normalize(light.position - FragPosition);
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = light.diffuse * diff * vec3(texture(material.diffuse, TextureCoords));

    // Specular
    vec3 viewDir = normalize(viewPosition - FragPosition);
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    vec3 specular = light.specular * spec * vec3(texture(material.specular, TextureCoords));

    //Point
    float distance = length(light.position - FragPosition);
    float attenuation = 1.0f / (light.constant + light.linear * distance + light.quadratic * (distance * distance));

    ambient *= attenuation;
    diffuse *= attenuation;
    specular *= attenuation;

    //Result
    fragColor = vec4(ambient + diffuse + specular, 1.0f);
}