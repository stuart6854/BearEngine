package main.java.org.bearengine.graphics.shaders;

import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.graphics.types.Color;
import main.java.org.joml.Matrix4d;
import main.java.org.joml.Matrix4f;
import main.java.org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.*;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {
	
	public static ShaderProgram CURRENT;
	public static ShaderProgram DEFAULT;
	public static ShaderProgram DEFAULT_LIGHTING;
	public static ShaderProgram DEFAULT_LIGHTING_SRC;
	public static ShaderProgram DEFAULT_UI;
	public static ShaderProgram DEFAULT_UI_SDF;
    public static ShaderProgram DEBUG_MESH_SHADER_PROGRAM;

	public int ProgramID;
	
	public Shader VertexShader;
	public Shader FragmentShader;

	private final Map<String, Integer> Uniforms;

    public boolean Initialised = false;
	public boolean Disposed = false;
	
	public ShaderProgram(){
		this.Uniforms = new HashMap<>();
	}
	
	public void Initialise(){
		this.ProgramID = glCreateProgram();
		if(this.ProgramID <= 0){
			Debug.error("ShaderProgram -> Could not create program!");
		}
        Initialised = true;

        Debug.log("ShaderProgram -> Initialised.");
	}
	
	public void AttachShader(Shader shader){
        if(!Initialised) Debug.error("ShaderProgram -> NOT INITIALISED!");

		if(shader.ShaderType == GL_VERTEX_SHADER){
            Debug.log("ShaderProgram -> Attaching Vertex Shader: " + shader.ShaderSourcePath);
			VertexShader = shader;
		}else if(shader.ShaderType == GL_FRAGMENT_SHADER){
            Debug.log("ShaderProgram -> Attaching Fragment Shader: " + shader.ShaderSourcePath);
			FragmentShader = shader;
		}
		
		if(shader.Compiled) {
            glAttachShader(ProgramID, shader.ShaderID);
            Debug.log("ShaderProgram -> Shader Attached.");
        }
	}
	
	public void Link(){
        if(!Initialised) Debug.error("ShaderProgram -> NOT INITIALISED!");

        Debug.log("ShaderProgram -> Linking Program.");

		glLinkProgram(ProgramID);
		if(glGetProgrami(ProgramID, GL_LINK_STATUS) == 0){
			Debug.error("ShaderProgram -> Could not Link Program: \n" + glGetShaderInfoLog(ProgramID));
		}
		
		glValidateProgram(ProgramID);
		if(glGetProgrami(ProgramID, GL_VALIDATE_STATUS) == 0){
			Debug.error("ShaderProgram -> Could not Validate Program: \n" + glGetShaderInfoLog(ProgramID));
		}

        Debug.log("ShaderProgram -> Program Linked.");
	}
	
	public void Bind(){
        if(!Initialised) Debug.error("ShaderProgram -> NOT INITIALISED!");

		if(CURRENT == this)
			return;
		
		if(Disposed)
			Debug.error("ShaderProgram -> Bind() -> Cannot bind Disposed program!");
		
		glUseProgram(ProgramID);
		CURRENT = this;
	}

	public int GetUniform(String name){
        Bind();

        if(Uniforms.containsKey(name))
            return Uniforms.get(name);

        int location = glGetUniformLocation(ProgramID, name);
        if(location == -1)
            Debug.error("ShaderProgram -> GetUniform() -> Could not find Uniform: " + name);

        Uniforms.put(name, location);

        return location;
    }

    public void SetUniform(int location, int[] values){
        if(values.length > 4)
            Debug.exception("Uniform component can not have more than 4 components!");

        Bind();

        switch(values.length){
            case 1:
                glUniform1i(location, values[0]);
                break;
            case 2:
                glUniform2i(location, values[0], values[1]);
                break;
            case 3:
                glUniform3i(location, values[0], values[1], values[2]);
                break;
            case 4:
                glUniform4i(location, values[0], values[1], values[2], values[3]);
                break;
        }
    }

    public void SetUniform(String location, int[] values){
        SetUniform(GetUniform(location), values);
    }

    public void SetUniform(int location, float[] values){
        if(values.length > 4)
            Debug.exception("Uniform component can not have more than 4 components!");

        Bind();

        switch(values.length){
            case 1:
                glUniform1f(location, values[0]);
                break;
            case 2:
                glUniform2f(location, values[0], values[1]);
                break;
            case 3:
                glUniform3f(location, values[0], values[1], values[2]);
                break;
            case 4:
                glUniform4f(location, values[0], values[1], values[2], values[3]);
                break;
        }
    }

    public void SetUniform(String location, float[] values){
        SetUniform(GetUniform(location), values);
    }

    public void SetUniform(String name, Vector3f vector){
		SetUniform(name, new float[]{ vector.x, vector.y, vector.z });
    }

    public void SetUniform(String name, Matrix4d value){
        Bind();

        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        glUniformMatrix4fv(GetUniform(name), false, value.get(buffer));
    }

    public void SetUniform(String name, Matrix4f value){
        Bind();

        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        glUniformMatrix4fv(GetUniform(name), false, value.get(buffer));
    }

    public void SetUniform(String name, Color color){
	    SetUniform(name, new float[]{color.r, color.g, color.b, color.a});
    }

    //TODO: Add Setting Vectors, Matrices, Colours, etc. Uniforms

	public void Cleanup(){
        Debug.log("ShaderProgram -> Cleaning Up.");

		Disposed = true;
		if(ProgramID != 0){
			if(VertexShader.ShaderID != 0)
				glDetachShader(ProgramID, VertexShader.ShaderID);
			
			if(FragmentShader.ShaderID != 0)
				glDetachShader(ProgramID, FragmentShader.ShaderID);
			
			glDeleteProgram(ProgramID);
		}
        Uniforms.clear();
	}

    static{
        Shader vertShader = new Shader("/main/java/resources/shaders/default_vertex.glsl", Shader.VERTEX_SHADER);
        vertShader.LoadSourceCode();
        vertShader.CompileShader();
        Shader fragShader = new Shader("/main/java/resources/shaders/default_fragment.glsl", Shader.FRAGMENT_SHADER);
        fragShader.LoadSourceCode();
        fragShader.CompileShader();

        DEFAULT = new ShaderProgram();
        DEFAULT.Initialise();
        DEFAULT.AttachShader(vertShader);
        DEFAULT.AttachShader(fragShader);
        DEFAULT.Link();

	    Shader lightingVertShader = new Shader("/main/java/resources/shaders/lighting_vertex.glsl", Shader.VERTEX_SHADER);
	    lightingVertShader.LoadSourceCode();
	    lightingVertShader.CompileShader();
	    Shader lightingFragShader = new Shader("/main/java/resources/shaders/lighting_fragment.glsl", Shader.FRAGMENT_SHADER);
	    lightingFragShader.LoadSourceCode();
	    lightingFragShader.CompileShader();

	    DEFAULT_LIGHTING = new ShaderProgram();
	    DEFAULT_LIGHTING.Initialise();
	    DEFAULT_LIGHTING.AttachShader(lightingVertShader);
	    DEFAULT_LIGHTING.AttachShader(lightingFragShader);
	    DEFAULT_LIGHTING.Link();

	    Shader lightingSrcVertShader = new Shader("/main/java/resources/shaders/lighting_lightsrc_vertex.glsl", Shader.VERTEX_SHADER);
	    lightingSrcVertShader.LoadSourceCode();
	    lightingSrcVertShader.CompileShader();
	    Shader lightingSrcFragShader = new Shader("/main/java/resources/shaders/lighting_lightsrc_fragment.glsl", Shader.FRAGMENT_SHADER);
	    lightingSrcFragShader.LoadSourceCode();
	    lightingSrcFragShader.CompileShader();

	    DEFAULT_LIGHTING_SRC = new ShaderProgram();
	    DEFAULT_LIGHTING_SRC.Initialise();
	    DEFAULT_LIGHTING_SRC.AttachShader(lightingSrcVertShader);
	    DEFAULT_LIGHTING_SRC.AttachShader(lightingSrcFragShader);
	    DEFAULT_LIGHTING_SRC.Link();

        Shader uiVertShader = new Shader("/main/java/resources/shaders/ui_vertex.glsl", Shader.VERTEX_SHADER);
        uiVertShader.LoadSourceCode();
        uiVertShader.CompileShader();
        Shader uiFragShader = new Shader("/main/java/resources/shaders/ui_fragment.glsl", Shader.FRAGMENT_SHADER);
        uiFragShader.LoadSourceCode();
        uiFragShader.CompileShader();

        DEFAULT_UI = new ShaderProgram();
        DEFAULT_UI.Initialise();
        DEFAULT_UI.AttachShader(uiVertShader);
        DEFAULT_UI.AttachShader(uiFragShader);
        DEFAULT_UI.Link();

        Shader uiSDFVertShader = new Shader("/main/java/resources/shaders/sdf_ui_vertex.glsl", Shader.VERTEX_SHADER);
        uiSDFVertShader.LoadSourceCode();
        uiSDFVertShader.CompileShader();
        Shader uiSDFFragShader = new Shader("/main/java/resources/shaders/sdf_ui_fragment.glsl", Shader.FRAGMENT_SHADER);
        uiSDFFragShader.LoadSourceCode();
        uiSDFFragShader.CompileShader();

        DEFAULT_UI_SDF = new ShaderProgram();
        DEFAULT_UI_SDF.Initialise();
        DEFAULT_UI_SDF.AttachShader(uiSDFVertShader);
        DEFAULT_UI_SDF.AttachShader(uiSDFFragShader);
        DEFAULT_UI_SDF.Link();

        Shader debugVertShader = new Shader("/main/java/resources/shaders/debugmesh_vertex.glsl", Shader.VERTEX_SHADER);
        debugVertShader.LoadSourceCode();
        debugVertShader.CompileShader();
        Shader debugFragShader = new Shader("/main/java/resources/shaders/debugmesh_fragment.glsl", Shader.FRAGMENT_SHADER);
        debugFragShader.LoadSourceCode();
        debugFragShader.CompileShader();

        DEBUG_MESH_SHADER_PROGRAM = new ShaderProgram();
        DEBUG_MESH_SHADER_PROGRAM.Initialise();
        DEBUG_MESH_SHADER_PROGRAM.AttachShader(debugVertShader);
        DEBUG_MESH_SHADER_PROGRAM.AttachShader(debugFragShader);
        DEBUG_MESH_SHADER_PROGRAM.Link();
    }

}

















