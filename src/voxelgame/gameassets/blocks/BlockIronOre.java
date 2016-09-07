package voxelgame.gameassets.blocks;//package org.voxelgame.gameassets.blocks;
//
//import org.voxelgame.data.Block;
//import org.voxelgame.data.Chunk;
//import org.voxelgame.data.MeshData;
//
//public class BlockIronOre extends Block{
//
//	public BlockIronOre() {
//		super();
//	}
//	
//	@Override
//	public MeshData BlockData(Chunk chunk, int x, int y, int z, MeshData meshData) {
//        meshData.useRenderDataForCol = true;
//        meshData = FaceDataUp(chunk, x, y, z, meshData);
//        meshData = FaceDataDown(chunk, x, y, z, meshData);
//        meshData = FaceDataNorth(chunk, x, y, z, meshData);
//        meshData = FaceDataEast(chunk, x, y, z, meshData);
//        meshData = FaceDataSouth(chunk, x, y, z, meshData);
//        meshData = FaceDataWest(chunk, x, y, z, meshData);
//
//        return super.BlockData(chunk, x, y, z, meshData);
//    }
//
//	@Override
//    public Tile TexturePosition(Direction direction) {
//        Tile tile = new Tile();
//        tile.x = 1;
//        tile.y = 13;
//        return tile;
//    }
//	
//}
