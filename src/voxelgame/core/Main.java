package voxelgame.core;

import main.java.org.bearengine.core.*;
import main.java.org.bearengine.core.Game;

public class Main {

	public static void main(String[] args){
		try{
			Game game = new GameLogic_Test();
	        Engine engine = new Engine(120, 120, game);
	        engine.start();
		}catch(Exception e){
			System.err.println("Engine has failed to start with exception: ");
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
