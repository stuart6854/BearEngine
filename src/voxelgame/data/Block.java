package voxelgame.data;

import java.awt.Window.Type;
import java.util.ArrayList;
import java.util.List;

import main.java.org.joml.Vector3f;

public class Block {

    //TODO: Add normal data

    public enum Direction { north, east, south, west, up, down }

    public class Tile { public int x, y; }
    final float textureTileSize = 0.0625f;// where 0 < x < 1

    public boolean changed = true;

    public Block() {  }

    public MeshData BlockData(Chunk chunk, int x, int y, int z, MeshData meshData) {
        meshData.useRenderDataForCol = true;

        Block block = chunk.GetBlock(x, y + 1, z);
        if (block == null || !block.IsSolid(Direction.down)) {
            meshData = FaceDataUp(chunk, x, y, z, meshData, chunk.getLightLvl(x, y + 1, z));
        }

        block = chunk.GetBlock(x, y - 1, z);
        if (block == null || !block.IsSolid(Direction.up)) {
            meshData = FaceDataDown(chunk, x, y, z, meshData, chunk.getLightLvl(x, y - 1, z));
        }

        block = chunk.GetBlock(x, y, z - 1);
        if (block == null || !block.IsSolid(Direction.south)) {
            meshData = FaceDataNorth(chunk, x, y, z, meshData, chunk.getLightLvl(x, y, z - 1));
        }

        block = chunk.GetBlock(x, y, z + 1);
        if (block == null || !block.IsSolid(Direction.north)) {
            meshData = FaceDataSouth(chunk, x, y, z, meshData, chunk.getLightLvl(x, y, z + 1));
        }

        block = chunk.GetBlock(x + 1, y, z);
        if (block == null || !block.IsSolid(Direction.west)) {
            meshData = FaceDataEast(chunk, x, y, z, meshData, chunk.getLightLvl(x + 1, y, z));
        }

        block = chunk.GetBlock(x - 1, y, z);
        if (block == null || !block.IsSolid(Direction.east)) {
            meshData = FaceDataWest(chunk, x, y, z, meshData, chunk.getLightLvl(x - 1, y, z));
        }

        return meshData;
    }
    
    protected MeshData FaceDataUp(Chunk chunk, int x, int y, int z, MeshData meshData, int lightLevel) {
        meshData.addVertex(new Vector3f(x - 0.5f, y + 0.5f, z - 0.5f), lightLevel);
        meshData.addVertex(new Vector3f(x - 0.5f, y + 0.5f, z + 0.5f), lightLevel);
        meshData.addVertex(new Vector3f(x + 0.5f, y + 0.5f, z + 0.5f), lightLevel);
        meshData.addVertex(new Vector3f(x + 0.5f, y + 0.5f, z - 0.5f), lightLevel);

        meshData.uv.addAll(FaceUVs(Direction.up));
        meshData.addQuadTriangles();

        if(!IsSolid(Direction.up)){
        	meshData.addVertex(new Vector3f(x - 0.5f, y + 0.5f, z - 0.5f), lightLevel);
            meshData.addVertex(new Vector3f(x + 0.5f, y + 0.5f, z - 0.5f), lightLevel);
            meshData.addVertex(new Vector3f(x + 0.5f, y + 0.5f, z + 0.5f), lightLevel);
            meshData.addVertex(new Vector3f(x - 0.5f, y + 0.5f, z + 0.5f), lightLevel);

            meshData.uv.addAll(FaceUVs(Direction.up));
            meshData.addQuadTriangles();
        }

        return meshData;
    }

    protected MeshData FaceDataDown(Chunk chunk, int x, int y, int z, MeshData meshData, int lightLevel) {
        meshData.addVertex(new Vector3f(x - 0.5f, y - 0.5f, z - 0.5f), lightLevel);
        meshData.addVertex(new Vector3f(x + 0.5f, y - 0.5f, z - 0.5f), lightLevel);
        meshData.addVertex(new Vector3f(x + 0.5f, y - 0.5f, z + 0.5f), lightLevel);
        meshData.addVertex(new Vector3f(x - 0.5f, y - 0.5f, z + 0.5f), lightLevel);

        meshData.uv.addAll(FaceUVs(Direction.down));
        meshData.addQuadTriangles();

        if(!IsSolid(Direction.down)){
        	meshData.addVertex(new Vector3f(x - 0.5f, y - 0.5f, z - 0.5f), lightLevel);
            meshData.addVertex(new Vector3f(x - 0.5f, y - 0.5f, z + 0.5f), lightLevel);
            meshData.addVertex(new Vector3f(x + 0.5f, y - 0.5f, z + 0.5f), lightLevel);
            meshData.addVertex(new Vector3f(x + 0.5f, y - 0.5f, z - 0.5f), lightLevel);

            meshData.uv.addAll(FaceUVs(Direction.down));
            meshData.addQuadTriangles();
        }

        return meshData;
    }

    protected MeshData FaceDataNorth(Chunk chunk, int x, int y, int z, MeshData meshData, int lightLevel) {
        meshData.addVertex(new Vector3f(x + 0.5f, y + 0.5f, z - 0.5f), lightLevel);
        meshData.addVertex(new Vector3f(x + 0.5f, y - 0.5f, z - 0.5f), lightLevel);
        meshData.addVertex(new Vector3f(x - 0.5f, y - 0.5f, z - 0.5f), lightLevel);
        meshData.addVertex(new Vector3f(x - 0.5f, y + 0.5f, z - 0.5f), lightLevel);

        meshData.uv.addAll(FaceUVs(Direction.north));
        meshData.addQuadTriangles();

        if(!IsSolid(Direction.north)){
        	meshData.addVertex(new Vector3f(x - 0.5f, y + 0.5f, z - 0.5f), lightLevel);
            meshData.addVertex(new Vector3f(x - 0.5f, y - 0.5f, z - 0.5f), lightLevel);
            meshData.addVertex(new Vector3f(x + 0.5f, y - 0.5f, z - 0.5f), lightLevel);
            meshData.addVertex(new Vector3f(x + 0.5f, y + 0.5f, z - 0.5f), lightLevel);

            meshData.uv.addAll(FaceUVs(Direction.north));
            meshData.addQuadTriangles();
        }

        return meshData;
    }

    protected MeshData FaceDataEast(Chunk chunk, int x, int y, int z, MeshData meshData, int lightLevel) {
        meshData.addVertex(new Vector3f(x + 0.5f, y + 0.5f, z + 0.5f), lightLevel);
        meshData.addVertex(new Vector3f(x + 0.5f, y - 0.5f, z + 0.5f), lightLevel);
        meshData.addVertex(new Vector3f(x + 0.5f, y - 0.5f, z - 0.5f), lightLevel);
        meshData.addVertex(new Vector3f(x + 0.5f, y + 0.5f, z - 0.5f), lightLevel);

        meshData.uv.addAll(FaceUVs(Direction.east));
        meshData.addQuadTriangles();

        if(!IsSolid(Direction.east)){
        	meshData.addVertex(new Vector3f(x + 0.5f, y + 0.5f, z - 0.5f), lightLevel);
            meshData.addVertex(new Vector3f(x + 0.5f, y - 0.5f, z - 0.5f), lightLevel);
            meshData.addVertex(new Vector3f(x + 0.5f, y - 0.5f, z + 0.5f), lightLevel);
            meshData.addVertex(new Vector3f(x + 0.5f, y + 0.5f, z + 0.5f), lightLevel);

            meshData.uv.addAll(FaceUVs(Direction.east));
            meshData.addQuadTriangles();
        }

        return meshData;
    }

    protected MeshData FaceDataSouth(Chunk chunk, int x, int y, int z, MeshData meshData, int lightLevel) {
        meshData.addVertex(new Vector3f(x - 0.5f, y + 0.5f, z + 0.5f), lightLevel);
        meshData.addVertex(new Vector3f(x - 0.5f, y - 0.5f, z + 0.5f), lightLevel);
        meshData.addVertex(new Vector3f(x + 0.5f, y - 0.5f, z + 0.5f), lightLevel);
        meshData.addVertex(new Vector3f(x + 0.5f, y + 0.5f, z + 0.5f), lightLevel);

        meshData.uv.addAll(FaceUVs(Direction.south));
        meshData.addQuadTriangles();

        if(!IsSolid(Direction.south)){
        	meshData.addVertex(new Vector3f(x + 0.5f, y + 0.5f, z + 0.5f), lightLevel);
            meshData.addVertex(new Vector3f(x + 0.5f, y - 0.5f, z + 0.5f), lightLevel);
            meshData.addVertex(new Vector3f(x - 0.5f, y - 0.5f, z + 0.5f), lightLevel);
            meshData.addVertex(new Vector3f(x - 0.5f, y + 0.5f, z + 0.5f), lightLevel);

            meshData.uv.addAll(FaceUVs(Direction.south));
            meshData.addQuadTriangles();
        }

        return meshData;
    }

    protected MeshData FaceDataWest(Chunk chunk, int x, int y, int z, MeshData meshData, int lightLevel) {
        meshData.addVertex(new Vector3f(x - 0.5f, y + 0.5f, z - 0.5f), lightLevel);
        meshData.addVertex(new Vector3f(x - 0.5f, y - 0.5f, z - 0.5f), lightLevel);
        meshData.addVertex(new Vector3f(x - 0.5f, y - 0.5f, z + 0.5f), lightLevel);
        meshData.addVertex(new Vector3f(x - 0.5f, y + 0.5f, z + 0.5f), lightLevel);

        meshData.uv.addAll(FaceUVs(Direction.west));
        meshData.addQuadTriangles();

        if(!IsSolid(Direction.west)){
        	meshData.addVertex(new Vector3f(x - 0.5f, y + 0.5f, z + 0.5f), lightLevel);
            meshData.addVertex(new Vector3f(x - 0.5f, y - 0.5f, z + 0.5f), lightLevel);
            meshData.addVertex(new Vector3f(x - 0.5f, y - 0.5f, z - 0.5f), lightLevel);
            meshData.addVertex(new Vector3f(x - 0.5f, y + 0.5f, z - 0.5f), lightLevel);

            meshData.uv.addAll(FaceUVs(Direction.west));
            meshData.addQuadTriangles();
        }

        return meshData;
    }

    public byte blockType(){
    	return -1;
    }

    public boolean IsSolid(Direction direction) {
        switch (direction) {
            case north:
                return true;
            case east:
                return true;
            case south:
                return true;
            case west:
                return true;
            case up:
                return true;
            case down:
                return true;
        }

        return false;
    }

    public Tile TexturePosition(Direction direction) {
        Tile tile = new Tile();
        tile.x = 0;
        tile.y = 0;
        return tile;
    }

    public List<Float> FaceUVs(Direction direction) {
        Tile tilePos = TexturePosition(direction);
        List<Float> UVs = new ArrayList<>(4 * 2); // 4 coord sets with each being 2d

        UVs.add(textureTileSize * tilePos.x);
        UVs.add(textureTileSize * tilePos.y + textureTileSize);

        UVs.add(textureTileSize * tilePos.x);
        UVs.add(textureTileSize * tilePos.y);

        UVs.add(textureTileSize * tilePos.x + textureTileSize);
        UVs.add(textureTileSize * tilePos.y);

        UVs.add(textureTileSize * tilePos.x + textureTileSize);
        UVs.add(textureTileSize * tilePos.y + textureTileSize);

        return UVs;
    }

    protected String getIdentifier(Type type) {
        String identifier = type.toString();
        char c[] = identifier.toCharArray();
        c[0] += 32;
        identifier = new String(c);
        return identifier;
    }

    public byte LightOutput(){
    	return 0;
    }

}
