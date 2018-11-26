package core.Level.Map;

// the mapped sprite type in the map
class MappedTile {

    public int id, x, y;
    public boolean collidable;

    public MappedTile(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.collidable = false;
    }

    public MappedTile(int id, int x, int y, boolean collidable)
    {
        this.id = id;
        this.x = x;
        this.y = y;
        this.collidable = collidable;
    }
}
