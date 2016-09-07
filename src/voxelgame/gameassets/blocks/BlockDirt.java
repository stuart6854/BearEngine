package voxelgame.gameassets.blocks;

import org.voxelgame.data.Block;

public class BlockDirt extends Block{

	public BlockDirt() {
		super();
	}
	
	@Override
	public Tile TexturePosition(Direction direction) {
        Tile tile = new Tile();
        tile.x = 2;
        tile.y = 15;
        return tile;
    }
	
	@Override
    public byte blockType(){
    	return 1;
    }
	
}
