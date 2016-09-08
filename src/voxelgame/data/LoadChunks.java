//package voxelgame.data;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import org.bearengine.math.VectorMath;
//import org.bearengine.objects.GameObject;
//import org.game.graphics.Camera;
//import org.joml.Vector3f;
//
//public class LoadChunks extends GameObject{
//
//	private final World world;
//
//	List<WorldPos> updateList = new ArrayList<WorldPos>();
//    List<WorldPos> buildList = new ArrayList<WorldPos>();
//
//    static WorldPos[] chunkPositions = {   new WorldPos( 0, 0,  0), new WorldPos(-1, 0,  0), new WorldPos( 0, 0, -1), new WorldPos( 0, 0,  1), new WorldPos( 1, 0,  0),
//                             new WorldPos(-1, 0, -1), new WorldPos(-1, 0,  1), new WorldPos( 1, 0, -1), new WorldPos( 1, 0,  1), new WorldPos(-2, 0,  0),
//                             new WorldPos( 0, 0, -2), new WorldPos( 0, 0,  2), new WorldPos( 2, 0,  0), new WorldPos(-2, 0, -1), new WorldPos(-2, 0,  1),
//                             new WorldPos(-1, 0, -2), new WorldPos(-1, 0,  2), new WorldPos( 1, 0, -2), new WorldPos( 1, 0,  2), new WorldPos( 2, 0, -1),
//                             new WorldPos( 2, 0,  1), new WorldPos(-2, 0, -2), new WorldPos(-2, 0,  2), new WorldPos( 2, 0, -2), new WorldPos( 2, 0,  2),
//                             new WorldPos(-3, 0,  0), new WorldPos( 0, 0, -3), new WorldPos( 0, 0,  3), new WorldPos( 3, 0,  0), new WorldPos(-3, 0, -1),
//                             new WorldPos(-3, 0,  1), new WorldPos(-1, 0, -3), new WorldPos(-1, 0,  3), new WorldPos( 1, 0, -3), new WorldPos( 1, 0,  3),
//                             new WorldPos( 3, 0, -1), new WorldPos( 3, 0,  1), new WorldPos(-3, 0, -2), new WorldPos(-3, 0,  2), new WorldPos(-2, 0, -3),
//                             new WorldPos(-2, 0,  3), new WorldPos( 2, 0, -3), new WorldPos( 2, 0,  3), new WorldPos( 3, 0, -2), new WorldPos( 3, 0,  2),
//                             new WorldPos(-4, 0,  0), new WorldPos( 0, 0, -4), new WorldPos( 0, 0,  4), new WorldPos( 4, 0,  0), new WorldPos(-4, 0, -1),
//                             new WorldPos(-4, 0,  1), new WorldPos(-1, 0, -4), new WorldPos(-1, 0,  4), new WorldPos( 1, 0, -4), new WorldPos( 1, 0,  4),
//                             new WorldPos( 4, 0, -1), new WorldPos( 4, 0,  1), new WorldPos(-3, 0, -3), new WorldPos(-3, 0,  3), new WorldPos( 3, 0, -3),
//                             new WorldPos( 3, 0,  3), new WorldPos(-4, 0, -2), new WorldPos(-4, 0,  2), new WorldPos(-2, 0, -4), new WorldPos(-2, 0,  4),
//                             new WorldPos( 2, 0, -4), new WorldPos( 2, 0,  4), new WorldPos( 4, 0, -2), new WorldPos( 4, 0,  2), new WorldPos(-5, 0,  0),
//                             new WorldPos(-4, 0, -3), new WorldPos(-4, 0,  3), new WorldPos(-3, 0, -4), new WorldPos(-3, 0,  4), new WorldPos( 0, 0, -5),
//                             new WorldPos( 0, 0,  5), new WorldPos( 3, 0, -4), new WorldPos( 3, 0,  4), new WorldPos( 4, 0, -3), new WorldPos( 4, 0,  3),
//                             new WorldPos( 5, 0,  0), new WorldPos(-5, 0, -1), new WorldPos(-5, 0,  1), new WorldPos(-1, 0, -5), new WorldPos(-1, 0,  5),
//                             new WorldPos( 1, 0, -5), new WorldPos( 1, 0,  5), new WorldPos( 5, 0, -1), new WorldPos( 5, 0,  1), new WorldPos(-5, 0, -2),
//                             new WorldPos(-5, 0,  2), new WorldPos(-2, 0, -5), new WorldPos(-2, 0,  5), new WorldPos( 2, 0, -5), new WorldPos( 2, 0,  5),
//                             new WorldPos( 5, 0, -2), new WorldPos( 5, 0,  2), new WorldPos(-4, 0, -4), new WorldPos(-4, 0,  4), new WorldPos( 4, 0, -4),
//                             new WorldPos( 4, 0,  4), new WorldPos(-5, 0, -3), new WorldPos(-5, 0,  3), new WorldPos(-3, 0, -5), new WorldPos(-3, 0,  5),
//                             new WorldPos( 3, 0, -5), new WorldPos( 3, 0,  5), new WorldPos( 5, 0, -3), new WorldPos( 5, 0,  3), new WorldPos(-6, 0,  0),
//                             new WorldPos( 0, 0, -6), new WorldPos( 0, 0,  6), new WorldPos( 6, 0,  0), new WorldPos(-6, 0, -1), new WorldPos(-6, 0,  1),
//                             new WorldPos(-1, 0, -6), new WorldPos(-1, 0,  6), new WorldPos( 1, 0, -6), new WorldPos( 1, 0,  6), new WorldPos( 6, 0, -1),
//                             new WorldPos( 6, 0,  1), new WorldPos(-6, 0, -2), new WorldPos(-6, 0,  2), new WorldPos(-2, 0, -6), new WorldPos(-2, 0,  6),
//                             new WorldPos( 2, 0, -6), new WorldPos( 2, 0,  6), new WorldPos( 6, 0, -2), new WorldPos( 6, 0,  2), new WorldPos(-5, 0, -4),
//                             new WorldPos(-5, 0,  4), new WorldPos(-4, 0, -5), new WorldPos(-4, 0,  5), new WorldPos( 4, 0, -5), new WorldPos( 4, 0,  5),
//                             new WorldPos( 5, 0, -4), new WorldPos( 5, 0,  4), new WorldPos(-6, 0, -3), new WorldPos(-6, 0,  3), new WorldPos(-3, 0, -6),
//                             new WorldPos(-3, 0,  6), new WorldPos( 3, 0, -6), new WorldPos( 3, 0,  6), new WorldPos( 6, 0, -3), new WorldPos( 6, 0,  3),
//                             new WorldPos(-7, 0,  0), new WorldPos( 0, 0, -7), new WorldPos( 0, 0,  7), new WorldPos( 7, 0,  0), new WorldPos(-7, 0, -1),
//                             new WorldPos(-7, 0,  1), new WorldPos(-5, 0, -5), new WorldPos(-5, 0,  5), new WorldPos(-1, 0, -7), new WorldPos(-1, 0,  7),
//                             new WorldPos( 1, 0, -7), new WorldPos( 1, 0,  7), new WorldPos( 5, 0, -5), new WorldPos( 5, 0,  5), new WorldPos( 7, 0, -1),
//                             new WorldPos( 7, 0,  1), new WorldPos(-6, 0, -4), new WorldPos(-6, 0,  4), new WorldPos(-4, 0, -6), new WorldPos(-4, 0,  6),
//                             new WorldPos( 4, 0, -6), new WorldPos( 4, 0,  6), new WorldPos( 6, 0, -4), new WorldPos( 6, 0,  4), new WorldPos(-7, 0, -2),
//                             new WorldPos(-7, 0,  2), new WorldPos(-2, 0, -7), new WorldPos(-2, 0,  7), new WorldPos( 2, 0, -7), new WorldPos( 2, 0,  7),
//                             new WorldPos( 7, 0, -2), new WorldPos( 7, 0,  2), new WorldPos(-7, 0, -3), new WorldPos(-7, 0,  3), new WorldPos(-3, 0, -7),
//                             new WorldPos(-3, 0,  7), new WorldPos( 3, 0, -7), new WorldPos( 3, 0,  7), new WorldPos( 7, 0, -3), new WorldPos( 7, 0,  3),
//                             new WorldPos(-6, 0, -5), new WorldPos(-6, 0,  5), new WorldPos(-5, 0, -6), new WorldPos(-5, 0,  6), new WorldPos( 5, 0, -6),
//                             new WorldPos( 5, 0,  6), new WorldPos( 6, 0, -5), new WorldPos( 6, 0,  5) };
//
//    int timer = 0;
//
//	public LoadChunks(World world) {
//		this.world = world;
//	}
//
//	public void update(float deltaTime, Camera camera) {
//		//getTransform().setPosition(camera.getTransform().getPosition());
//		if (DeleteChunks())
//            return;
//        FindChunksToLoad();
//        LoadAndRenderChunks();
//	}
//
//    void FindChunksToLoad() {
//        //Get the position of this gameobject to load around
//        WorldPos playerPos = new WorldPos((int)Math.floor(getTransform().getPosition().x / Chunk.chunkSizeXZ) * Chunk.chunkSizeXZ,
//                                            (int)Math.floor(getTransform().getPosition().y / Chunk.chunkSizeY) * Chunk.chunkSizeY,
//                                            -(int)Math.floor(getTransform().getPosition().z / Chunk.chunkSizeXZ) * Chunk.chunkSizeXZ);
//
//        //if there arn't already chunks to generate
//        if(updateList.size() == 0) {
//            //Cycle through the array of positions
//            for (int i = 0; i < 4; i++) {
//                WorldPos newChunkPos = new WorldPos(chunkPositions[i].x * Chunk.chunkSizeXZ + playerPos.x,
//                                                    0,
//                                                    -chunkPositions[i].z * Chunk.chunkSizeXZ + playerPos.z);
//
//                //Get the chunk in the defined position
//                Chunk newChunk = world.GetChunk(newChunkPos.x, newChunkPos.y, newChunkPos.z);
//
//                //If chunk already exists and its already rendered or in queue to be rendered continue
//                if (newChunk != null && (newChunk.rendered || updateList.contains(newChunkPos)))
//                    continue;
//
//                for (int x = newChunkPos.x - Chunk.chunkSizeXZ; x <= newChunkPos.x + Chunk.chunkSizeXZ; x += Chunk.chunkSizeXZ) {
//                    for (int z = newChunkPos.z - Chunk.chunkSizeXZ; z <= newChunkPos.z + Chunk.chunkSizeXZ; z += Chunk.chunkSizeXZ) {
//                        buildList.add(new WorldPos(x, 0, z));
//                    }
//                }
//                //buildList.add(new WorldPos(newChunkPos.x, 0, newChunkPos.z)); //Test Code
//                updateList.add(new WorldPos(newChunkPos.x, 0, newChunkPos.z));
//
//                return;
//            }
//        }
//    }
//
//    void BuildChunk(WorldPos pos) {
//        if (world.GetChunk(pos.x, pos.y, pos.z) == null){
//        	world.CreateChunk(pos.x, pos.y, pos.z);
//        }
//    }
//
//    void LoadAndRenderChunks() {
//        if (buildList.size() != 0) {
//            for (int i = 0; i < buildList.size() && i < 1; i++) {
////	            Logger.info("Building Chunk: " + buildList.get(0).x + ", " + buildList.get(0).z);
//	            BuildChunk(buildList.get(0));
//                buildList.remove(0);
//            }
//            //if chunks were built return early
//            return;
//        }
//
//        if(updateList.size() != 0) {
//            Chunk chunk = world.GetChunk(updateList.get(0).x, updateList.get(0).y, updateList.get(0).z);
//            if (chunk != null){
//                chunk.update = true;
//            }
//            updateList.remove(0);
//        }
//    }
//
//    private boolean DeleteChunks() {
//        if(timer == 10) {
//            List<WorldPos> chunksToDelete = new ArrayList<WorldPos>();
//            for (Map.Entry<WorldPos, Chunk> chunk : world.chunks.entrySet()) {
//                float distance = VectorMath.Distance(new Vector3f(chunk.getValue().pos.x, 0, chunk.getValue().pos.z), new Vector3f(getTransform().getPosition().x, 0, getTransform().getPosition().z));
//
//                if (distance > 256) {
//                    chunksToDelete.add(chunk.getKey());
//                }
//            }
//
//            for(WorldPos chunk : chunksToDelete){
//                world.DestroyChunk(chunk.x, chunk.y, chunk.z);
//            }
//            timer = 0;
//            return true;
//        }
//
//        timer++;
//        return false;
//    }
//
//    public void cleanup(){
//
//    }
//
//}
