package voxelgame.gameassets.blocks;

import org.voxelgame.data.Block;
import org.voxelgame.data.Chunk;
import org.voxelgame.data.MeshData;

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
