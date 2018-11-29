package core.animation;

import core.Game;
import core.Level.Level;
import core.Rectangle;
import core.RenderHandler;
import core.character.Enemy.Enemy;

public class DeathAnimation implements GameObject{

    private AnimatedSprite animatedSprite;
    private SpriteSheet sheet = new SpriteSheet(Game.loadImage("imageFolder/materials.png"));
    private Enemy e;
    private int counter;
    private int speed = 20;

    /**
     * Death Animation constructor
     * @param enemy - to get enemy.x and enemy.y
     */
    public DeathAnimation(Enemy enemy) {
        Rectangle[] deathSprites = new Rectangle[3];
        e = enemy;

        //Load from sheet
        for (int i = 0; i < deathSprites.length; i++) {
            deathSprites[i] = new Rectangle(15 * Level.MATERIALS_SPRITE_SIZE, i * Level.MATERIALS_SPRITE_SIZE, Level.MATERIALS_SPRITE_SIZE, Level.MATERIALS_SPRITE_SIZE);
        }

        animatedSprite = new AnimatedSprite(sheet, deathSprites, speed);
    }

    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        renderer.renderSprite(animatedSprite, e.getPlayerRectangle().x, e.getPlayerRectangle().y,4 ,4);
    }

    @Override
    public void update(Game game) {
        animatedSprite.update(game);
        counter++;

        //Remove death animation method
        if (counter == speed * 3) {
        }

    }
}
