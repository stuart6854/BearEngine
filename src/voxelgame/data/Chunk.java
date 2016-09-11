package voxelgame.data;

import java.util.LinkedList;
import java.util.Queue;

import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.graphics.types.Mesh;
import voxelgame.maths.Vector3i;
import main.java.org.bearengine.objects.GameObject;

public class Chunk extends GameObject{

	public Block[][][] blocks = new Block[chunkSizeXZ][chunkSizeY][chunkSizeXZ];
	private byte[][][] lightMap = new byte[chunkSizeXZ][chunkSizeY][chunkSizeXZ];

    public static final int chunkSizeXZ = 16, chunkSizeY = 256;
    public boolean update = false;
    public boolean isBuilt;
    public boolean rendered;
    private boolean isBusy = false;

    public boolean doneRemesh = false;

    private final World world;
    public final WorldPos pos;

    public Chunk(World world, WorldPos pos) {
		super();
		this.world = world;
		this.pos = pos;
		super.Name = "Chunk(" + GetPosAsString() + ")";
	}

	public void update(float deltaTime) {
        if (update && !isBusy) {
            UpdateChunk();
        }

        if(!doneRemesh && neighboursBuilt(pos.x, pos.z)){
        	doneRemesh = true;
        	update = true;
        }
	}

    public Block GetBlock(int x, int y, int z) {
        if (InRangeXZ(x) && InRangeY(y) && InRangeXZ(z))
    		return blocks[x][y][z];
        else
        	return world.GetBlock(pos.x + x, pos.y + y, pos.z + z);
    }

    public static boolean InRangeXZ(int index) {
        if (index < 0 || index >= chunkSizeXZ)
            return false;
        return true;
    }

    public static boolean InRangeY(int index) {
        if (index < 0 || index >= chunkSizeY)
            return false;
        return true;
    }

    public void SetBlock(int x, int y, int z, Block block) {
        if(InRangeXZ(x) && InRangeY(y) && InRangeXZ(z)) {
            blocks[x][y][z] = block;
        } else {
            world.SetBlock(pos.x + x, pos.y + y, pos.z + z, block);
        }
    }

    public static void SetBlock(int x, int y, int z, Block block, Chunk chunk, boolean replaceBlocks) {
        x -= chunk.pos.x;
        y -= chunk.pos.y;
        z -= chunk.pos.z;

        if(Chunk.InRangeXZ(x) && Chunk.InRangeY(y) && Chunk.InRangeXZ(z)) {
            if (replaceBlocks || chunk.blocks[x][y][z] == null)
                chunk.SetBlock(x, y, z, block);
        }
    }

    public byte getLightLvl(int x, int y, int z){
    	if(InRangeXZ(x) && InRangeY(y) && InRangeXZ(z))
    		return lightMap[x][y][z];
    	else
    		return world.getLightLevel(pos.x + x, pos.y + y, pos.z + z);
    }

    public void setLightLvl(int x, int y, int z, byte lvl) {
        if(InRangeXZ(x) && InRangeY(y) && InRangeXZ(z)) {
            lightMap[x][y][z] = lvl;
        } else {
            world.setLightLvl(pos.x + x, pos.y + y, pos.z + z, lvl);
        }
    }

    //Updates the chunk based on its contents
    public void UpdateChunk() {
    	isBusy = true;
    	update = false;
    	rendered = true;
//    	Debug.log("Chunk(" + GetPosAsString() + ") -> Regenerating Mesh.");
        MeshData meshData = new MeshData();

        updateLightMap(blocks);

        for (int x = 0; x < chunkSizeXZ; x++) {
            for (int y = 0; y < chunkSizeY; y++) {
                for (int z = 0; z < chunkSizeXZ; z++) {
                    meshData = blocks[x][y][z].BlockData(this, x, y, z, meshData);
                }
            }
        }

//        Debug.log("Chunk(" + GetPosAsString() + ") -> Mesh Regenerated.");
        
        updateMesh(meshData);
        isBusy = false;
    }

    private void updateLightMap(Block[][][] blocks){
    	//Debug.log("Chunk(" + GetPosAsString() + ") -> Updating Voxel Lightmap.");
        long startTimeLoopPoints = System.nanoTime();

    	lightMap = new byte[chunkSizeXZ][chunkSizeY][chunkSizeXZ];
        Queue<Vector3i> litPoints = new LinkedList<Vector3i>();

        for (int x = 0; x < chunkSizeXZ; x++) {
            for (int y = 0; y < chunkSizeY; y++) {
                for (int z = 0; z < chunkSizeXZ; z++) {
                    Block block = blocks[x][y][z];

                    if(block.LightOutput() > 0){

                    	lightMap[x][y][z] = block.LightOutput();
                    	litPoints.add(new Vector3i(x, y, z));
                    }else if(x == chunkSizeXZ - 1 || z == chunkSizeXZ - 1 || x == 0 || z == 0){

                    	lightMap[x][y][z] = (byte)(getHighestNeighbourLightLevel(x, y, z) - 1);
                    	lightMap[x][y][z] = (byte)Math.max(lightMap[x][y][z], 0);
                        litPoints.add(new Vector3i(x, y, z));
                    }
                    lightMap[x][y][z] = 15; //Lights everything up
                }
            }
        }

        Vector3i point;
        int x, y, z;
        while(!litPoints.isEmpty()){
            point = litPoints.poll();

            x = point.x;
            y = point.y;
    		z = point.z;

            byte lightLvl = getLightLvl(x, y, z);

            if(GetBlock(x + 1, y, z).blockType() <= 0 && getLightLvl(x + 1, y, z) + 2 <= lightLvl){
            	setLightLvl(x + 1, y, z, (byte)(lightLvl - 1));

            	litPoints.add(new Vector3i(x + 1, y, z));
            }
            if(GetBlock(x - 1, y, z).blockType() <= 0 && getLightLvl(x - 1, y, z) + 2 <= lightLvl){
            	setLightLvl(x - 1, y, z, (byte)(lightLvl - 1));

            	litPoints.add(new Vector3i(x - 1, y, z));
            }
            if(GetBlock(x, y + 1, z).blockType() <= 0 && getLightLvl(x, y + 1, z) + 2 <= lightLvl){
            	setLightLvl(x, y + 1, z, (byte)(lightLvl - 1));

            	litPoints.add(new Vector3i(x, y + 1, z));
            }
            if(GetBlock(x, y - 1, z).blockType() <= 0 && getLightLvl(x, y - 1, z) + 2 <= lightLvl){
            	setLightLvl(x, y - 1, z, (byte)(lightLvl - 1));

            	litPoints.add(new Vector3i(x, y - 1, z));
            }
            if(GetBlock(x, y, z + 1).blockType() <= 0 && getLightLvl(x, y, z + 1) + 2 <= lightLvl){
            	setLightLvl(x, y, z + 1, (byte)(lightLvl - 1));

            	litPoints.add(new Vector3i(x, y, z + 1));
            }
            if(GetBlock(x, y, z - 1).blockType() <= 0 && getLightLvl(x, y, z - 1) + 2 <= lightLvl){
            	setLightLvl(x, y, z - 1, (byte)(lightLvl - 1));

            	litPoints.add(new Vector3i(x, y, z - 1));
            }
        }

        long estimatedTimeLoopPoints = System.nanoTime() - startTimeLoopPoints;
        //Debug.log("Chunk(" + pos + ") -> updateLightMap -> Loop Points -> Execution Time: " + (estimatedTimeLoopPoints / 1000000) + "ms");
        //Debug.log("Complete Light Map Algorithm!");
    }

    //Sends the calculated mesh information
    //to the mesh and collision components
    public void updateMesh(MeshData meshData) {
//    	Debug.log("Chunk(" + GetPosAsString() + ") -> Updating Mesh.");

        super.GetMesh().Cleanup();
        
        Mesh mesh = new Mesh();
        mesh.Mesh_Name = "Chunk(" + GetPosAsString() + ")";
        mesh.material.SetTexture(World.Texture_Sheet);
        
        mesh.SetVertices(meshData.vertices);
        mesh.SetUVs(meshData.uv);
        mesh.SetIndices(meshData.indices);
        mesh.SetColors(meshData.vertexColors);
    
        mesh.CreateRenderModel();
        super.SetMesh(mesh);
        
//        getBoundingBox().updateBounds(getMesh());//TODO: Add bounding boxes to Engine
        
//        Debug.log("Chunk(" + GetPosAsString() + ") -> Mesh Updated.");
    }

    private boolean neighboursBuilt(int x, int z){
    	Chunk chunk = world.GetChunk(x + 16, 0, z);
    	if(chunk == null || !chunk.isBuilt || !chunk.rendered)
    		return false;

    	chunk = world.GetChunk(x - 16, 0, z);
    	if(chunk == null || !chunk.isBuilt || !chunk.rendered)
    		return false;

    	chunk = world.GetChunk(x, 0, z + 16);
    	if(chunk == null || !chunk.isBuilt || !chunk.rendered)
    		return false;

    	chunk = world.GetChunk(x, 0, z - 16);
    	if(chunk == null || !chunk.isBuilt || !chunk.rendered)
    		return false;

    	return true;
    }

    ///LIGHTING METHODS\\\

    private byte getHighestNeighbourLightLevel(int x, int y, int z){
    	long startTime = System.nanoTime();
    	byte lightLvl = 0;

    	byte tmpLvl = getLightLvl(x + 1, y, z);
    	if(tmpLvl > lightLvl)
    		lightLvl = tmpLvl;

    	tmpLvl = getLightLvl(x - 1, y, z);
    	if(tmpLvl > lightLvl)
    		lightLvl = tmpLvl;

    	tmpLvl = getLightLvl(x, y + 1, z);
    	if(tmpLvl > lightLvl)
    		lightLvl = tmpLvl;

    	tmpLvl = getLightLvl(x, y - 1, z);
    	if(tmpLvl > lightLvl)
    		lightLvl = tmpLvl;

    	tmpLvl = getLightLvl(x, y, z + 1);
    	if(tmpLvl > lightLvl)
    		lightLvl = tmpLvl;

    	tmpLvl = getLightLvl(x, y, z - 1);
    	if(tmpLvl > lightLvl)
    		lightLvl = tmpLvl;

		long endTime = System.nanoTime() - startTime;
		//if(endTime / 1000000 > 0) Debug.log("Chunk(" + pos + ") -> getHighestLightLevel() -> Execution Time: " + (endTime / 1000000) + "ms");

    	return lightLvl;
    }
    
    public String GetPosAsString(){
        return pos.x + ", " + pos.z;
    }
    
}
