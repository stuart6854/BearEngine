package voxelgame.gameassets.blocks;

import voxelgame.data.Block;

public class BlockWood extends Block {

	public BlockWood() {
		super();
	}

	@Override
	public Tile TexturePosition(Direction direction) {
        Tile tile = new Tile();

        switch (direction) {
            case up:
                tile.x = 5;
                tile.y = 14;
                return tile;
            case down:
                tile.x = 5;
                tile.y = 14;
                return tile;
        }

        tile.x = 4;
        tile.y = 14;
        return tile;
    }

	@Override
    public byte blockType(){
    	return 5;
    }
}
