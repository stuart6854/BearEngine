package voxelgame.gameassets.blocks;

import voxelgame.data.Block;
import voxelgame.data.Chunk;
import voxelgame.data.MeshData;

public class BlockAir extends Block {

	public BlockAir() {
		super();
	}

	@Override
	public MeshData BlockData(Chunk chunk, int x, int y, int z, MeshData meshData) {
        return meshData;
    }

    public boolean IsSolid(Direction direction) {
        return false;
    }

    @Override
    public byte blockType(){
    	return 0;
    }

}
