package voxelgame.gameassets.blocks;

import voxelgame.data.Block;

public class BlockLeaves extends Block {

	public BlockLeaves() {
		super();
	}

	@Override
	public Tile TexturePosition(Direction direction) {
        Tile tile = new Tile();

        tile.x = 4;
        tile.y = 12;

        return tile;
    }

	@Override
    public boolean IsSolid(Direction direction) {
        return false;
    }

	@Override
    public byte blockType(){
    	return 6;
    }
}
