package org.bearengine.graphics.shaders;

import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.bearengine.debug.Debug;
import org.bearengine.utils.GLError;
import org.lwjgl.opengl.GL11;

public class Shader {

    public static final int VERTEX_SHADER = GL_VERTEX_SHADER, FRAGMENT_SHADER = GL_FRAGMENT_SHADER;

	public int ShaderID; // OpenGL's Shader ID
	
	public final int ShaderType; // GL_VERTEX_SHADER or GL_FRAGMENT_SHADER
	
	public final String ShaderSourcePath; // Path to Shader Source
	
	public String ShaderSource; // Shaders Source Code
	
	public boolean Compiled = false;
	
	public Shader(String shaderSourcePath, int shaderType){
		this.ShaderSourcePath = shaderSourcePath;
		this.ShaderType = shaderType;
	}
	
	public void LoadSourceCode(){
		Compiled = false;
		StringBuilder source = new StringBuilder();

        String type = (ShaderType == VERTEX_SHADER) ? "VERTEX" : "FRAGMENT";

		try {
			BufferedReader reader = new BufferedReader(new FileReader(ShaderSourcePath));
			String line;
			while((line = reader.readLine()) != null){
				source.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Shader -> Could not read Shader(" + type + ") Source File!");
			System.err.println("Shader -> Shader(" + type + ") Source File: " + ShaderSourcePath);
			e.printStackTrace();
			System.exit(-1);
		}
		
		ShaderSource = source.toString();
		Debug.log("Shader -> (" + type + ") Source Code loaded.");
	}
	
	public void CompileShader(){
        if(Compiled) return;

		ShaderID = glCreateShader(ShaderType);
		if(ShaderID == 0){
			Debug.error("Shader -> Could not create Shader!");
		}
		
		glShaderSource(ShaderID, ShaderSource);
		glCompileShader(ShaderID);

        GLError.PrintShaderInfoLog(ShaderID);

        String type = (ShaderType == VERTEX_SHADER) ? "VERTEX" : "FRAGMENT";

		if(glGetShaderi(ShaderID, GL_COMPILE_STATUS) == 0){
			Debug.error("Shader -> Error Compiling Shader(" + type + ") Source: " + glGetShaderInfoLog(ShaderID));
		}else{
			Compiled = true;
            Debug.log("Shader -> (" + type + ") Source Code Compiled.");
		}

        GLError.Check("Shader -> CompileShader() -> End");
	}
		
}









