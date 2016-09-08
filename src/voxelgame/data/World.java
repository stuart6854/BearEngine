package voxelgame.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import voxelgame.generation.TerrainGen;
import main.java.org.bearengine.graphics.types.Image;
import main.java.org.bearengine.graphics.types.Material;
import main.java.org.bearengine.graphics.types.Texture;
import main.java.org.bearengine.objects.GameObject;
import main.java.org.bearengine.utils.ResourceLoader;
import main.java.org.joml.Vector3f;

public class World {

	public String worldName = "world";

    public Map<WorldPos, Chunk> chunks = new HashMap<WorldPos, Chunk>();

    private TerrainGen terrainGen = new TerrainGen();

    private final Texture texture;

    public World(String textureFile){
    	texture = new Texture().UploadTexture(ResourceLoader.Load(textureFile, Image.class));
    }

    public void update(float deltaTime){
    	for(Chunk chunk : chunks.values()){
    		chunk.update(deltaTime);
    	}
    }

//    public void render(Renderer renderer){
//    	List<GameObject> chunkList = new ArrayList<>();
//    	chunkList.addAll(chunks.values());
//    	renderer.render(chunkList);
//    }

    public void CreateChunk(int x, int y, int z) {
    	//Logger.debug("World -> Creating chunk at " + x + ", " + y + ", " + z);
        WorldPos worldPos = new WorldPos(x, y, z);

        Chunk newChunk = new Chunk(this, worldPos);
        newChunk.SetPosition(x, y, z);

        Material material = new Material(texture, 0.0f, true);//NOTE: Use vertex Colors for lighting
        newChunk.GetMesh().material = material;

        chunks.put(worldPos, newChunk);

        terrainGen.ChunkGen(chunks.get(worldPos));
        chunks.get(worldPos).isBuilt = true;
        remeshNeighbourChunks(worldPos.x, worldPos.z);
    }

    private void remeshNeighbourChunks(int x, int z){
    	Chunk chunk = GetChunk(x + 16, 0, z);
    	if(chunk != null && chunk.isBuilt)
    		chunk.update = true;

    	chunk = GetChunk(x - 16, 0, z);
    	if(chunk != null && chunk.isBuilt)
    		chunk.update = true;

    	chunk = GetChunk(x, 0, z + 16);
    	if(chunk != null && chunk.isBuilt)
    		chunk.update = true;

    	chunk = GetChunk(x, 0, z - 16);
    	if(chunk != null && chunk.isBuilt)
    		chunk.update = true;
    }

    public Chunk GetChunk(int x, int y, int z) {
        WorldPos pos = new WorldPos();
        float multipleXZ = Chunk.chunkSizeXZ;
        float multipleY = Chunk.chunkSizeY;
        pos.x = (int) (Math.floor(x / multipleXZ) * Chunk.chunkSizeXZ);
        pos.y = (int) (Math.floor(y / multipleY) * Chunk.chunkSizeY);
        pos.z = (int) (Math.floor(z / multipleXZ) * Chunk.chunkSizeXZ);

        return chunks.get(pos);
    }

    public Block GetBlock(int x, int y, int z) {
        Chunk containerChunk = GetChunk(x, y, z);

        if(containerChunk != null) {
            Block block = containerChunk.GetBlock(x - containerChunk.pos.x, y - containerChunk.pos.y, z - containerChunk.pos.z);
            return block;
        } else {
            return new BlockAir();
        }
    }

    public void SetBlock(int x, int y, int z, Block block) {
        Chunk chunk = GetChunk(x, y, z);

        if(chunk != null) {
            chunk.SetBlock(x - chunk.pos.x, y - chunk.pos.y, z - chunk.pos.z, block);
            chunk.update = true;

            UpdateIfEqual(x - chunk.pos.x, 0, new WorldPos(x - 1, y, z));
            UpdateIfEqual(x - chunk.pos.x, Chunk.chunkSizeXZ - 1, new WorldPos(x + 1, y, z));
            UpdateIfEqual(y - chunk.pos.y, 0, new WorldPos(x, y - 1, z));
            UpdateIfEqual(y - chunk.pos.y, Chunk.chunkSizeXZ - 1, new WorldPos(x, y + 1, z));
            UpdateIfEqual(z - chunk.pos.z, 0, new WorldPos(x, y, z - 1));
            UpdateIfEqual(z - chunk.pos.z, Chunk.chunkSizeXZ - 1, new WorldPos(x, y, z + 1));
        }
    }

    public void setLightLvl(int x, int y, int z, byte lvl) {
        Chunk chunk = GetChunk(x, y, z);
        if(chunk != null) {
            chunk.setLightLvl(x - chunk.pos.x, y - chunk.pos.y, z - chunk.pos.z, lvl);
            chunk.update = true;
        }
    }

    public void DestroyChunk(int x, int y, int z) {
        Chunk chunk = chunks.get(new WorldPos(x, y, z));
        if(chunk != null) {
            //Serialization.SaveChunk(chunk);
            //Object.destroy(chunk);
        	chunk.GetMesh().Cleanup();
            chunks.remove(new WorldPos(x, y, z));
        }
    }

    public byte getLightLevel(int x, int y, int z){
    	Chunk containerChunk = GetChunk(x, y, z);

        if(containerChunk != null) {
            byte lightLvl = containerChunk.getLightLvl(x - containerChunk.pos.x, y - containerChunk.pos.y, z - containerChunk.pos.z);
            return lightLvl;
        } else {
            return 1;
        }
    }

    void UpdateIfEqual(int value1, int value2, WorldPos pos) {
        if(value1 == value2) {
            Chunk chunk = GetChunk(pos.x, pos.y, pos.z);
            if (chunk != null)
                chunk.update = true;
        }
    }

    public void cleanup(){
    	for(Chunk chunk : chunks.values()){
    		chunk.GetMesh().Cleanup();
    	}
    }

}
