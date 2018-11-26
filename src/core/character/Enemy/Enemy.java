package core.character.Enemy;

import core.Game;
import core.Level.Level;
import core.Rectangle;
import core.animation.AnimatedSprite;
import core.animation.SpriteSheet;
import core.character.Player;

import java.util.Random;

public class Enemy extends Player {
    private int directCounting = 0;
    private int enemySpeed = 5;
    private int enemyID;
    private int enemyType;

    /**
     * construct sprites of character from the sheet
     * @return Animated Spire for character
     */
    private AnimatedSprite constructSprite() {
        String path = "imageFolder/materials.png";
        SpriteSheet characterSheet = new SpriteSheet(Game.loadImage(path));

        characterSheet.loadSprites(Level.MATERIALS_SPRITE_SIZE,Level.MATERIALS_SPRITE_SIZE);

        //Create bunch of Rectangle saving location of character sprites
        Rectangle[] spritePos = new Rectangle[6];
        for (int x = 0; x < 2 ;x++){
            for (int y = 0; y < 3; y++) {
                spritePos[2 * y + x] = new Rectangle( (x + 6 + enemyType * 2) * Level.MATERIALS_SPRITE_SIZE,  (y + 5)  * Level.MATERIALS_SPRITE_SIZE, Level.MATERIALS_SPRITE_SIZE, Level.MATERIALS_SPRITE_SIZE );
            }
        }

        return new AnimatedSprite(characterSheet,spritePos,30);
    }

    /**
     * update the direction every time we move
     */
    protected void updateDirection() {
        if (animatedSprite != null) {
            animatedSprite.setAnimationRange(0,5);
        }
    }


    /**
     * Create enemy
     * @param xLoc - x location
     * @param yLoc - y location
     */
    public Enemy(int xLoc, int yLoc, int enemyType) {
        super();
        this.enemyType = enemyType;
        this.animatedSprite = constructSprite();

        updateDirection();

        Random random = new Random();
        direction = random.nextInt(4);

        //Set enemyRectangle
        this.playerRectangle = new Rectangle(xLoc * Game.MATERIAL_ZOOM * Level.MATERIALS_SPRITE_SIZE, yLoc * Game.MATERIAL_ZOOM * Level.MATERIALS_SPRITE_SIZE, Level.PLAYER_SPRITE_SIZE, Level.PLAYER_SPRITE_SIZE);
        this.playerRectangle.generateGraphic(1, 0xFF00FF90);

        this.xCollisionOffset = 10;
        this.yCollisionOffset = 10;

        // Init collisionCheckRectangle and generate graphic, size = 16 * 3 / 2
        this.collisionCheckRectangle = new Rectangle(xLoc * Game.MATERIAL_ZOOM * Level.MATERIALS_SPRITE_SIZE, yLoc * Game.MATERIAL_ZOOM * Level.MATERIALS_SPRITE_SIZE, this.playerRectangle.w * 3 / 2,this.playerRectangle.h * 3 / 2);
        this.collisionCheckRectangle.generateGraphic(1, 0xFF00FF90);
    }


    public void update(Game game) {
    }

    /**
     * collision of enemy
     * @param game
     */
    protected void enemyCollision(Game game) {
        // Increase checkrectangle by offset to not collide at the very beginning
        collisionCheckRectangle.x += xCollisionOffset;
        collisionCheckRectangle.y += yCollisionOffset;

        // check x collision
        Rectangle xAxisCheck = new Rectangle(collisionCheckRectangle.x, playerRectangle.y + yCollisionOffset, collisionCheckRectangle.w, collisionCheckRectangle.h);
        if (!game.getLevel1().getMap().checkCollision(xAxisCheck, Game.MATERIAL_ZOOM, Game.MATERIAL_ZOOM)) {
            if (!game.getLevel1().getMap().checkCollisionEnemyVsPlayer(xAxisCheck)) {
                if (!game.getLevel1().getMap().checkCollisionEnemyVsEnemy(this, xAxisCheck)) {
                    if (!game.getLevel1().getMap().checkCollisionMobVsBrick(xAxisCheck)) {
                        if (!game.getLevel1().getMap().checkCollisionEnemyVsBomb(this, xAxisCheck)) {
                            playerRectangle.x = collisionCheckRectangle.x - xCollisionOffset;
                        }
                    }
                }
            }
        }

        // check y collision
        Rectangle yAxisCheck = new Rectangle(playerRectangle.x + xCollisionOffset, collisionCheckRectangle.y, collisionCheckRectangle.w, collisionCheckRectangle.h);
        if (!game.getLevel1().getMap().checkCollision(yAxisCheck, Game.MATERIAL_ZOOM, Game.MATERIAL_ZOOM)) {
            if (!game.getLevel1().getMap().checkCollisionEnemyVsPlayer(yAxisCheck)) {
                if (!game.getLevel1().getMap().checkCollisionEnemyVsEnemy(this, yAxisCheck)) {
                    if (!game.getLevel1().getMap().checkCollisionMobVsBrick(yAxisCheck)) {
                        if (!game.getLevel1().getMap().checkCollisionEnemyVsBomb(this, yAxisCheck)) {
                            playerRectangle.y = collisionCheckRectangle.y - yCollisionOffset;
                        }
                    }
                }
            }
        }
    }

    public int getEnemyID() {
        return enemyID;
    }

    public void setEnemyID(int enemyID) {
        this.enemyID = enemyID;
    }

    public int getDirectCounting() {
        return directCounting;
    }

    public void setDirectCounting(int directCounting) {
        this.directCounting = directCounting;
    }

    public int getEnemySpeed() {
        return enemySpeed;
    }

    public void setEnemySpeed(int enemySpeed) {
        this.enemySpeed = enemySpeed;
    }
}
