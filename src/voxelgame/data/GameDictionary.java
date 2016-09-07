package voxelgame.data;

import java.util.HashMap;
import java.util.Map;

import org.voxelgame.gameassets.blocks.*;

public class GameDictionary {
	
	public static final Map<Byte, Block> blockDictionary = new HashMap<Byte, Block>();
	
	static{
		blockDictionary.put((byte)0, new BlockAir());
		blockDictionary.put((byte)1, new BlockDirt());
		blockDictionary.put((byte)2, new BlockCobblestone());
		blockDictionary.put((byte)3, new BlockGrass());
		blockDictionary.put((byte)4, new BlockStone());
		blockDictionary.put((byte)5, new BlockWood());
		blockDictionary.put((byte)6, new BlockLeaves());
		blockDictionary.put((byte)7, new BlockBedrock());
		blockDictionary.put((byte)8, new BlockLight());
	}
	
}
