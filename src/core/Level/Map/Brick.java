package core.Level.Map;

import core.Game;
import core.Level.Level;
import core.Level.Map.Tile.Tiles;
import core.Rectangle;
import core.RenderHandler;
import core.animation.GameObject;
import core.animation.Sprite;

public class Brick implements GameObject {

    private Rectangle brickRectangle;
    private Rectangle collisionCheckRectangle;

    private Tiles tiles;

    public Brick(int x, int y, Tiles tiles) {

        this.tiles = tiles;

        brickRectangle = new Rectangle(x * Level.MATERIALS_SPRITE_SIZE * Game.MATERIAL_ZOOM, y * Level.MATERIALS_SPRITE_SIZE * Game.MATERIAL_ZOOM, Level.PLAYER_SPRITE_SIZE, Level.PLAYER_SPRITE_SIZE);
        brickRectangle.generateGraphic(1,0xFF00FF90);

        collisionCheckRectangle = new Rectangle(brickRectangle.x, brickRectangle.y, brickRectangle.w * 3/2, brickRectangle.h * 3/2);
        collisionCheckRectangle.generateGraphic(1,0xFF00FF90);
    }


    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        tiles.renderTile(Tiles.BRICK_ID, renderer, brickRectangle.x, brickRectangle.y, xZoom, yZoom);

        renderer.renderRectangle(collisionCheckRectangle, Game.PLAYER_ZOOM, Game.PLAYER_ZOOM);
    }

    @Override
    public void update(Game game) {

    }

    public Rectangle getBrickRectangle() {
        return brickRectangle;
    }

    public Rectangle getCollisionCheckRectangle() {
        return collisionCheckRectangle;
    }
}
