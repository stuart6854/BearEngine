package voxelgame.core;

import org.engine.core.GameEngine;
import org.engine.core.IGameLogic;
import org.engine.core.Window.WindowMode;
import org.game.graphics.Camera.ProjectionMode;
import org.joml.Vector2f;

public class Game {
	
	public static void main(String[] args){
		try{
			IGameLogic logic = new GameLogic_Test();
			Vector2f screenRes = new Vector2f(1280, 720);
			GameEngine engine = new GameEngine("Bear Engine", screenRes, WindowMode.WINDOWMODE_NORMAL, ProjectionMode.Perspective, logic);
			engine.start();
		}catch(Exception e){
			System.err.println("Engine has failed to start with exception: ");
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
}
