package main.java.org.bearengine.objects;

import main.java.org.bearengine.graphics.shaders.Shader;
import main.java.org.bearengine.graphics.shaders.ShaderProgram;
import main.java.org.bearengine.graphics.types.Cubemap;
import main.java.org.bearengine.graphics.types.Image;
import main.java.org.bearengine.graphics.types.Mesh;
import main.java.org.bearengine.utils.ResourceLoader;

/**
 * Created by Stuart on 25/09/2016.
 */
public class Skybox {
    
    private Cubemap cubemap;
    
    private Mesh mesh;
    
    private ShaderProgram shaderProgram;
    
    ///
    ///@params skyboxTextures - In order of Right,  Left,  Up,  Down,  Back,  Front
    ///
    public Skybox(String[] skyboxTexturePaths){
        this.cubemap = new Cubemap();
        this.cubemap.UploadCubemap(LoadSkyboxTextures(skyboxTexturePaths));
        SetupShaders();
        SetupMesh();
    }
    
    private Image[] LoadSkyboxTextures(String[] texturePaths){
        Image[] images = new Image[6];
        
        for(int i = 0; i < 6; i++){
            images[i] = ResourceLoader.Load(texturePaths[i],  Image.class, false);
        }
        
        return images;
    }
    
    private void SetupShaders(){
        Shader vert = new Shader("/main/java/resources/shaders/skybox/skybox_vertex.glsl",  Shader.VERTEX_SHADER);
        vert.LoadSourceCode();
        vert.CompileShader();
    
        Shader frag = new Shader("/main/java/resources/shaders/skybox/skybox_fragment.glsl",  Shader.FRAGMENT_SHADER);
        frag.LoadSourceCode();
        frag.CompileShader();
        
        shaderProgram = new ShaderProgram();
        shaderProgram.Initialise();
        shaderProgram.AttachShader(vert);
        shaderProgram.AttachShader(frag);
        shaderProgram.Link();
    }
    
    private void SetupMesh(){
        mesh = new Mesh();
        mesh.material.shaderProgram = shaderProgram;
    
        final float SIZE = 1f;
    
        final float[] vertices = {
                -SIZE,  SIZE, -SIZE,//North
                -SIZE, -SIZE, -SIZE,
                SIZE, -SIZE, -SIZE,
                SIZE, -SIZE, -SIZE,
                SIZE,  SIZE, -SIZE,
                -SIZE,  SIZE, -SIZE,
                //
                -SIZE, -SIZE,  SIZE,//west
                -SIZE, -SIZE, -SIZE,
                -SIZE,  SIZE, -SIZE,
                -SIZE,  SIZE, -SIZE,
                -SIZE,  SIZE,  SIZE,
                -SIZE, -SIZE,  SIZE,
                //
                SIZE, -SIZE, -SIZE,//east
                SIZE, -SIZE,  SIZE,
                SIZE,  SIZE,  SIZE,
                SIZE,  SIZE,  SIZE,
                SIZE,  SIZE, -SIZE,
                SIZE, -SIZE, -SIZE,
                //
                -SIZE, -SIZE,  SIZE,//south
                -SIZE,  SIZE,  SIZE,
                SIZE,  SIZE,  SIZE,
                SIZE,  SIZE,  SIZE,
                SIZE, -SIZE,  SIZE,
                -SIZE, -SIZE,  SIZE,
                //
                -SIZE,  SIZE, -SIZE,//Up
                SIZE,  SIZE, -SIZE,
                SIZE,  SIZE,  SIZE,
                SIZE,  SIZE,  SIZE,
                -SIZE,  SIZE,  SIZE,
                -SIZE,  SIZE, -SIZE,
                //
                -SIZE, -SIZE, -SIZE,//Down
                -SIZE, -SIZE,  SIZE,
                SIZE, -SIZE, -SIZE,
                SIZE, -SIZE, -SIZE,
                -SIZE, -SIZE,  SIZE,
                SIZE, -SIZE,  SIZE
        };
    
        final int[] indices = {
                0, 1, 2,
                3, 4, 5,
                6, 7, 8,
                9, 10, 11,
                12, 13, 14,
                15, 16, 17,
                18, 19, 20,
                21, 22, 23,
                24, 25, 26,
                27, 28, 29,
                30, 31, 32,
                33, 34, 35
        };
        
        mesh.SetIndices(indices);
        mesh.SetVertices(vertices);
        mesh.CreateRenderModel();
    }
    
    public Cubemap GetCubemap() {
        return cubemap;
    }
    
    public Mesh GetMesh() {
        return mesh;
    }
    
}
