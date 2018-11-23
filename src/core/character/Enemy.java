package core.character;

import core.Game;
import core.Level.Level;
import core.Rectangle;
import core.animation.AnimatedSprite;
import core.animation.SpriteSheet;

import java.util.Random;

public class Enemy extends Player {
    private int directCounting = 0;
    private int enemySpeed = 1;

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
                spritePos[2 * y + x] = new Rectangle( (x + 6) * Level.MATERIALS_SPRITE_SIZE,  (y + 5)  * Level.MATERIALS_SPRITE_SIZE, Level.MATERIALS_SPRITE_SIZE, Level.MATERIALS_SPRITE_SIZE );
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
    public Enemy(int xLoc, int yLoc) {
        super();
        this.animatedSprite = constructSprite();

        updateDirection();

        Random random = new Random();
        direction = random.nextInt(4);

        //Set enemyRectangle
        this.playerRectangle = new Rectangle(xLoc,yLoc,Level.MATERIALS_SPRITE_SIZE,Level.MATERIALS_SPRITE_SIZE);

        // Init collisionCheckRectangle and generate graphic, size = 16 * 3 / 2
        this.collisionCheckRectangle = new Rectangle(xLoc,yLoc, playerRectangle.w * 3 / 2,playerRectangle.h * 3 / 2);
        this.collisionCheckRectangle.generateGraphic(1, 0xFF00FF90);
    }


    public void update(Game game) {
        boolean isMove = false;
        int newDirection = direction;

        collisionCheckRectangle.x = playerRectangle.x;
        collisionCheckRectangle.y = playerRectangle.y;

        //Set new direction - random every 4 * 60 counting
        if (directCounting == 240) {
            directCounting = 0;
            Random random = new Random();
            newDirection = random.nextInt(4);

        } else {
            directCounting++;
        }

        //Moving base on direction
        switch (this.direction) {
            case 1:
                playerRectangle.x += enemySpeed;
                isMove = true;
                collisionCheckRectangle.x += enemySpeed;
                break;
            case 0:
                playerRectangle.y -= enemySpeed;
                isMove = true;
                collisionCheckRectangle.y -= enemySpeed;
                break;
            case 2:
                playerRectangle.y += enemySpeed;
                isMove = true;
                collisionCheckRectangle.y += enemySpeed;
                break;
            case 3:
                playerRectangle.x -= enemySpeed;
                isMove = true;
                collisionCheckRectangle.x -= enemySpeed;
                break;
        }

        collisionCheckRectangle.x = this.playerRectangle.x;
        collisionCheckRectangle.y = this.playerRectangle.y;

        //New direction checking
        if (newDirection != direction) {
            direction = newDirection;
            updateDirection();
        }

        if (!isMove) {
            this.animatedSprite.reset();
        }
        else  {
            this.animatedSprite.update(game);
            //Collision handle here
        }
    }
}
