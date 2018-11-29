package core.character;

import core.Game;
import core.Level.Map.Brick;
import core.Level.Map.Map;
import core.Rectangle;
import core.RenderHandler;
import core.animation.DeathAnimation;
import core.animation.GameObject;
import core.animation.Sprite;
import core.character.Enemy.Enemy;

import java.util.ArrayList;

public class Explosion implements GameObject {
    private Game game;
    private ArrayList<Brick> bricks;
    private ArrayList<Enemy> enemies;
    private ArrayList<GameObject> gameObjects;
    private Bomb bomb;
    private Sprite[] sprites = new Sprite[4]; //Sprite of flames
    private Rectangle[] rectangles = new Rectangle[4];//flame saving location rectangles on sheet
    private Rectangle[] flameLocation = new Rectangle[5]; //array saving saving location rectangles on map

    public Explosion(Game game, Bomb bomb) {
        this.game = game;
        this.bomb = bomb;
        this.bricks = game.getLevel1().getMap().getBricks();
        this.enemies = game.getLevel1().getMap().getEnemies();
        this.gameObjects = game.getLevel1().getMap().getGameObjects();

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
        flameLocation[4] = bomb.getBombRectangle(); //Central flame
        for (int i = 0; i < sprites.length; i++) {
            if (i < 2) {

                flameLocation[i] = new Rectangle(bomb.getBombRectangle().x - (i * 2 -1) * 16 * Game.MATERIAL_ZOOM + 16,
                        bomb.getBombRectangle().y + 16, 16, 16);
                flameLocation[i].generateGraphic(2, 0xFF00FF90);
                //renderer.renderRectangle(flameLocation[i], xZoom, yZoom);

                // Remove brick if collide
                brickCollision(flameLocation[i]);
                // Remove enemy if collide
                enemyCollision(flameLocation[i]);
                enemyCollision(flameLocation[4]);

                // if no collision then render
                if (!game.getLevel1().getMap().checkCollision(flameLocation[i], xZoom, yZoom)) {
                    renderer.renderSprite(sprites[i], bomb.getBombRectangle().x - (i * 2 - 1) * 16 * Game.MATERIAL_ZOOM,
                            bomb.getBombRectangle().y, xZoom, yZoom);
                    // End game if player collide
                    playerCollision(flameLocation[i]);
                    playerCollision(flameLocation[4]);
                }
            }

            else {
                int k = i % 2;

                flameLocation[i] = new Rectangle(bomb.getBombRectangle().x + 16,
                        bomb.getBombRectangle().y - (k * 2 - 1) * 16 * Game.MATERIAL_ZOOM + 16, 16,16);
                flameLocation[i].generateGraphic(2, 0xFF00FF90);
                //renderer.renderRectangle(flameLocation[i], xZoom, yZoom);

                // Remove brick if collide
                brickCollision(flameLocation[i]);
                // Remove enemy if collide
                enemyCollision(flameLocation[i]);
                enemyCollision(flameLocation[4]);

                // if no collision then render
                if (!game.getLevel1().getMap().checkCollision(flameLocation[i], xZoom, yZoom)) {
                    renderer.renderSprite(sprites[i], bomb.getBombRectangle().x, bomb.getBombRectangle().y - (k * 2 - 1) * 16 * Game.MATERIAL_ZOOM,
                            xZoom, yZoom);
                    // End game if player collide
                    playerCollision(flameLocation[i]);
                    playerCollision(flameLocation[4]);
                }
            }

        }
    }

    /**
     * remove brick if flame collide with brick
     * @param curFlameLocation
     */
    private void brickCollision(Rectangle curFlameLocation) {
        boolean brickCollide = false;
        int j = 0;
        while (j < bricks.size()) {
            if (curFlameLocation.intersects(bricks.get(j).getBrickRectangle())) {
                brickCollide = true;
                break;
            }
            j++;
        }
        if (brickCollide) {
            Brick brickToRemove = bricks.get(j);
            bricks.remove(j);
            gameObjects.remove(brickToRemove);
        }
    }

    /**
     * Remove enemy if flame burns enemy
     * @param curFlameLocation
     */
    private void enemyCollision(Rectangle curFlameLocation) {
        boolean enemyCollide = false;
        int j = 0;
        while (j < enemies.size()) {
            if (curFlameLocation.intersects(enemies.get(j).getCollisionCheckRectangle())) {
                enemyCollide = true;
                break;
            }
            j++;
        }
        if (enemyCollide) {
            Enemy enemyToRemove = enemies.get(j);
            DeathAnimation deathAnimation = new DeathAnimation(enemyToRemove);
            Map.deathAnimations.add(deathAnimation);
            enemies.remove(j);
            gameObjects.remove(enemyToRemove);
        }
    }

    private void playerCollision(Rectangle curFlameLocation) {

        if (curFlameLocation.intersects(game.getLevel1().getMap().getPlayer().getCollisionCheckRectangle())) {
            game.setRunning(false);
        }
    }



    @Override
    public void update(Game game) {

    }

    /**
     * Getter
     * @return rectangles array of flames
     */
    public Rectangle[] getRectangles() {
        return rectangles;
    }
}
