package voxelgame.generation;

import main.java.org.bearengine.debug.Debug;
import voxelgame.data.Chunk;
import voxelgame.gameassets.blocks.*;

public class TerrainGen {

	float stoneBaseHeight = 60;
    float stoneBaseNoise = 0.05f;
    float stoneBaseNoiseHeight = 4;

    float stoneMountainHeight = 128;
    float stoneMountainFrequency = 0.008f;
    float stoneMinHeight = 1;

    float dirtBaseHeight = 4;
    float dirtNoise = 0.04f;
    float dirtNoiseHeight = 3;

    float caveFrequency = 0.025f;
    int caveSize = 7;

    float treeFrequency = 0.2f;
    int treeDensity = 3;

	public Chunk ChunkGen(Chunk chunk) {
        for (int x = chunk.pos.x - 3; x < chunk.pos.x + Chunk.chunkSizeXZ + 3; x++) {
            for(int z = chunk.pos.z - 3; z < chunk.pos.z + Chunk.chunkSizeXZ + 3; z++) {
                chunk = ChunkColumnGen(chunk, x, z);

                //chunk = ChunkOreGen(chunk);
            }
        }

        Debug.log("TerrainGen -> Generated chunk at " + chunk.pos.x + ", " + chunk.pos.z);
        return chunk;
    }

    public Chunk ChunkColumnGen(Chunk chunk, int x, int z) {
        int stoneHeight = (int) Math.floor(stoneBaseHeight);
        stoneHeight += GetNoise(x, 0, z, stoneMountainFrequency, (int)Math.floor(stoneMountainHeight));

        if (stoneHeight < stoneMinHeight)
            stoneHeight = (int) Math.floor(stoneMinHeight);

        stoneHeight += GetNoise(x, 0, z, stoneBaseNoise, (int)Math.floor(stoneBaseNoiseHeight));

        int dirtHeight = (int) (stoneHeight + Math.floor(dirtBaseHeight));
        dirtHeight += GetNoise(x, 100, z, dirtNoise, (int)Math.floor(dirtNoiseHeight));

        for(int y = 0; y < Chunk.chunkSizeY; y++) {
            //get a value to base cave generation on
            int caveChance = GetNoise(x, y, z, caveFrequency, 100);

            if(y <= stoneHeight && caveSize < caveChance) {
                Chunk.SetBlock(x, y, z, new BlockStone(), chunk, true);

            } else if(y <= dirtHeight && caveSize < caveChance) {
                if (y == dirtHeight) {
                    Chunk.SetBlock(x, y, z, new BlockGrass(), chunk, true);

                    if (GetNoise(x, 0, z, treeFrequency, 100) < treeDensity){
                        //CreateTree(x, y + 1, z, chunk);
                    }
                } else {
                    Chunk.SetBlock(x, y, z, new BlockDirt(), chunk, true);
                }

            } else {
                Chunk.SetBlock(x, y, z, new BlockAir(), chunk, true);
            }

            if(y == dirtHeight + 1){
            	////TEMP!!!!
            	if(x == 0 && z == 0) Chunk.SetBlock(x, y, z, new BlockLight(), chunk, true);
            	if(x == Chunk.chunkSizeXZ - 1&& z == Chunk.chunkSizeXZ - 1) Chunk.SetBlock(x, y, z, new BlockLight(), chunk, true);
                if(x == Chunk.chunkSizeXZ / 2 && z == Chunk.chunkSizeXZ / 2)Chunk.SetBlock(x, y, z, new BlockLight(), chunk, true);
                ////
            }

            if(y <= 5) Chunk.SetBlock(x, y, z, new BlockBedrock(), chunk, true);
        }

        return chunk;
    }

    public static int GetNoise(int x, int y, int z, float scale, int max) {
        return (int) Math.floor((SimplexNoise.noise(x * scale, y * scale, z * scale) + 1f) * (max / 2f));
    }

    void CreateTree(int x, int y, int z, Chunk chunk) {
        //create leaves
        for (int xi = -2; xi <= 2; xi++) {
            for (int yi = 4; yi <= 8; yi++) {
                for (int zi = -2; zi <= 2; zi++) {
                    Chunk.SetBlock(x + xi, y + yi, z + zi, new BlockLeaves(), chunk, true);
                }
            }
        }

        //create trunk
        for (int yt = 0; yt < 6; yt++) {
            Chunk.SetBlock(x, y + yt, z, new BlockWood(), chunk, true);
        }
    }

}
