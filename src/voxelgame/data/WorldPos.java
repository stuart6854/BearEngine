package voxelgame.data;

public class WorldPos {
	
	public int x, y, z;

    public WorldPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public WorldPos() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	@Override
    public boolean equals(Object obj) {
        if (hashCode() == obj.hashCode())
            return true;
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 47;

        hash = hash * 227 + x;
        hash = hash * 227 + y;
        hash = hash * 227 + z;

        return hash;
    }
	
    public String toString(){
    	return x + ", " + y + ", " + z;
    }
    
}
