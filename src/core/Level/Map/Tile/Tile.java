package core.Level.Map.Tile;

import core.animation.Sprite;

// Sprite available
class Tile {

    public String tileName;
    public Sprite sprite;
    public boolean collidable = false;

    public Tile(String tileName, Sprite sprite, boolean collidable) {
        this.tileName = tileName;
        this.sprite = sprite;
        this.collidable = collidable;
    }
}
