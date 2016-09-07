package voxelgame.gameassets.blocks;

import org.voxelgame.data.Block;

public class BlockStone extends Block{
	
	public BlockStone() {
		super();
	}

	@Override
    public Tile TexturePosition(Direction direction) {
        Tile tile = new Tile();
        tile.x = 1;
        tile.y = 15;
        return tile;
    }
	
	@Override
    public byte blockType(){
    	return 4;
    }
}
