// Reference: http://learnopengl.com/#!Lighting/Basic-Lighting

#version 330 core

struct Light{
    vec3 position;
    vec3 direction;
    float cutOff; // or Light Distance
    float outerCutOff;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
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

    float theta = dot(lightDir, normalize(-light.direction));
    float epsilon = light.cutOff - light.outerCutOff;
    float intensity = clamp((theta - light.outerCutOff) / epsilon, 0.0, 1.0);

    //Leave ambient alone so their is a little light.
    diffuse *= intensity;
    specular *= intensity;

    //Result
    fragColor = vec4(ambient + diffuse + specular, 1.0f);
}