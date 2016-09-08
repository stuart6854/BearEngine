package voxelgame.gameassets.blocks;

import voxelgame.data.Block;

public class BlockBedrock extends Block {
    
	public BlockBedrock() {
		super();
	}

	@Override
	public Tile TexturePosition(Direction direction) {
        Tile tile = new Tile();
        tile.x = 1;
        tile.y = 14;
        return tile;
    }

	@Override
    public byte blockType(){
    	return 7;
    }
}
