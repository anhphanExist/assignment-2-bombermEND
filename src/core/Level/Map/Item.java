package core.Level.Map;

import core.Game;
import core.Level.Level;
import core.Level.Map.Tile.Tiles;
import core.Rectangle;
import core.RenderHandler;
import core.animation.GameObject;

public class Item implements GameObject {
    private Game game;
    private Rectangle itemRectangle;
    private Rectangle collisionCheckRectangle;

    private Tiles tiles;

    public Item(int x, int y, Tiles tile, Game game) {
        this.tiles = tiles;
        this.game = game;
        itemRectangle = new Rectangle(x * Level.MATERIALS_SPRITE_SIZE * Game.MATERIAL_ZOOM, y * Level.MATERIALS_SPRITE_SIZE * Game.MATERIAL_ZOOM, Level.PLAYER_SPRITE_SIZE, Level.PLAYER_SPRITE_SIZE);
        itemRectangle.generateGraphic(1, 0xFF00FF90);
        collisionCheckRectangle = new Rectangle(itemRectangle.x, itemRectangle.y, itemRectangle.w * 3 / 2, itemRectangle.h * 3 / 2);
        itemRectangle.generateGraphic(1, 0xFF00FF90);
    }

    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        tiles.renderTile(Tiles.ITEM_ID, renderer, itemRectangle.x, itemRectangle.y, xZoom, yZoom);
    }

    @Override
    public void update(Game game) {

    }

    public Rectangle getItemRectangle() {
        return itemRectangle;
    }

    public Rectangle getCollisionCheckRectangle() {
        return collisionCheckRectangle;
    }
}
