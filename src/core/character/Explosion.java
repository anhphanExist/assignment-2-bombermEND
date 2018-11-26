package core.character;

import core.Game;
import core.Rectangle;
import core.RenderHandler;
import core.animation.GameObject;
import core.animation.Sprite;

public class Explosion implements GameObject {
    private Bomb bomb;
    private Sprite[] sprites = new Sprite[4]; //Sprite of flames
    private Rectangle[] rectangles = new Rectangle[4];//flame saving location rectangles on sheet
    private Rectangle[] flameLocation = new Rectangle[4]; //array saving saving location rectangles on map

    public Explosion(Bomb bomb) {
        this.bomb = bomb;

        rectangles[0] = new Rectangle(3 * 16, 5 * 16, 16, 16);//Position of right flame
        rectangles[1] = new Rectangle(1, 5 * 16, 16, 16);//Position for left flame
        rectangles[2] = new Rectangle(15 * 16, 2 * 16, 16, 16);//Position for downward flame
        rectangles[3] = new Rectangle(15 * 16, 0, 16, 16);

        for (int i = 0; i < sprites.length; i ++ ) {
            sprites[i] = new Sprite(bomb.getSheet(), rectangles[i].x, rectangles[i].y,
                    rectangles[i].w, rectangles[i].h);
        }
    }

    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        for (int i = 0; i < sprites.length; i++) {
            if (i < 2) {
                renderer.renderSprite(sprites[i], bomb.getBombRectangle().x - (i * 2 -1) * 16 * Game.MATERIAL_ZOOM,
                        bomb.getBombRectangle().y, xZoom, yZoom);
                flameLocation[i] = new Rectangle(bomb.getBombRectangle().x - (i * 2 -1) * 16 * Game.MATERIAL_ZOOM,
                        bomb.getBombRectangle().y, 16, 16);
            }

            else {
                int k = i % 2;
                renderer.renderSprite(sprites[i], bomb.getBombRectangle().x,bomb.getBombRectangle().y - (k * 2 - 1) * 16 * Game.MATERIAL_ZOOM,
                        xZoom, yZoom );
                flameLocation[i] = new Rectangle(bomb.getBombRectangle().x,
                        bomb.getBombRectangle().y - (k * 2 - 1) * 16 * Game.MATERIAL_ZOOM, 16,16);
            }

        }
    }

    /**
     * Getter
     * @return rectangles array of flames
     */
    public Rectangle[] getRectangles() {
        return rectangles;
    }

    @Override
    public void update(Game game) {

    }
}
