package voxelgame.gameassets.blocks;

import org.voxelgame.data.Block;

public class BlockCobblestone extends Block {

	public BlockCobblestone() {
		super();
	}
	
	@Override
	public Tile TexturePosition(Direction direction) {
        Tile tile = new Tile();
        tile.x = 0;
        tile.y = 14;
        return tile;
    }

	@Override
    public byte blockType(){
    	return 2;
    }
}
