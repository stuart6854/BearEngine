package main.java.org.bearengine.graphics.shaders;

import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.graphics.types.VertexAttribute;
import main.java.org.joml.Matrix4d;
import main.java.org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.*;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {
	
	public static ShaderProgram CURRENT;
	public static ShaderProgram DEFAULT;
	
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

	public int getUniform(String name){
        Bind();

        if(Uniforms.containsKey(name))
            return Uniforms.get(name);

        int location = glGetUniformLocation(ProgramID, name);
        if(location == -1)
            Debug.error("ShaderProgram -> getUniform() -> Could not find Uniform: " + name);

        Uniforms.put(name, location);

        return location;
    }

    public void setUniform(int location, int[] values){
        if(values.length > 4)
            Debug.exception("Uniform component can not have more than 4 components!");

        Bind();

        switch(values.length){
            case 1:
                glUniform1i(ProgramID, values[0]);
                break;
            case 2:
                glUniform2i(ProgramID, values[0], values[1]);
                break;
            case 3:
                glUniform3i(ProgramID, values[0], values[1], values[2]);
                break;
            case 4:
                glUniform4i(ProgramID, values[0], values[1], values[2], values[3]);
                break;
        }
    }

    public void setUniform(String location, int[] values){
        setUniform(getUniform(location), values);
    }

    public void setUniform(int location, float[] values){
        if(values.length > 4)
            Debug.exception("Uniform component can not have more than 4 components!");

        Bind();

        switch(values.length){
            case 1:
                glUniform1f(ProgramID, values[0]);
                break;
            case 2:
                glUniform2f(ProgramID, values[0], values[1]);
                break;
            case 3:
                glUniform3f(ProgramID, values[0], values[1], values[2]);
                break;
            case 4:
                glUniform4f(ProgramID, values[0], values[1], values[2], values[3]);
                break;
        }
    }

    public void setUniform(String location, float[] values){
        setUniform(getUniform(location), values);
    }

    public void setUniform(String name, Matrix4d value){
        Bind();

        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        glUniformMatrix4fv(getUniform(name), false, value.get(buffer));
    }

    public void setUniform(String name, Matrix4f value){
        Bind();

        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        glUniformMatrix4fv(getUniform(name), false, value.get(buffer));
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

}

















