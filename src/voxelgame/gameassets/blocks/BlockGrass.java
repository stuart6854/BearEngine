package voxelgame.gameassets.blocks;

import org.voxelgame.data.Block;

@SuppressWarnings("incomplete-switch")
public class BlockGrass extends Block {

	public BlockGrass() {
		super();
	}
	
	@Override
	public Tile TexturePosition(Direction direction) {
        Tile tile = new Tile();

        switch (direction) {
            case up:
                tile.x = 0;
                tile.y = 15;
                return tile;
            case down:
                tile.x = 2;
                tile.y = 15;
                return tile;
        }

        tile.x = 3;
        tile.y = 15;

        return tile;
    }

	@Override
    public byte blockType(){
    	return 3;
    }
}
