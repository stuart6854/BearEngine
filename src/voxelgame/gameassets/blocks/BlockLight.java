package voxelgame.gameassets.blocks;

import voxelgame.data.Block;

public class BlockLight extends Block{

	public BlockLight() {
		super();
	}

	@Override
	public Tile TexturePosition(Direction direction) {
        Tile tile = new Tile();
        tile.x = 9;
        tile.y = 9;
        return tile;
    }

	@Override
    public byte blockType(){
    	return 9;
    }

	@Override
	public byte LightOutput(){
		return 15;
	}

}
