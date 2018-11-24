package core.character;

import core.Level.Level;
import core.animation.*;
import core.*;

import java.util.ArrayList;

public class Player implements GameObject{
    protected Rectangle playerRectangle;
    protected Rectangle collisionCheckRectangle;

    //Player speed
    private int speed = 10;

    //Spire animate for player
    protected AnimatedSprite animatedSprite;
    //Location on the spire
    private int xLocCharacter;
    private int yLocCharacter;


    //Bomb list
    private ArrayList<Bomb> bombs = new ArrayList<>();
    private int bomLimit = 1;
    private int currentNumBom = 0;


    // Collision offset
    protected int xCollisionOffset = 10;
    protected int yCollisionOffset = 10;

    /**
     * 0 = up, 1 = right, 2 = down, 3 = left
     */
    protected int direction = 0;

    /**
     * construct sprites of character from the sheet
     * @return Animated Spire for character
     */
    private AnimatedSprite constructSprite() {
        String path = "imageFolder/character.png";
        SpriteSheet characterSheet = new SpriteSheet(Game.loadImage(path));

        characterSheet.loadSprites(32,32);

        //Create bunch of Rectangle saving location of character sprites
        Rectangle[] spritePos = new Rectangle[12];
        for (int x = 0; x < 3 ;x++){
            for (int y = 0; y < 4; y++) {
                spritePos[3 * y + x] = new Rectangle( (x + xLocCharacter) * 32,  (y + yLocCharacter) * 32, 32, 32 );
            }
        }

        return new AnimatedSprite(characterSheet,spritePos,5);
    }

    public Player() {

    }

    public Player(int _CharacterNum) {

        if (_CharacterNum < 6) {
            xLocCharacter = (_CharacterNum % 3) * 3;

            if (_CharacterNum < 3)
                yLocCharacter = 0;

            else
                yLocCharacter = 4;
        }

        else {
            xLocCharacter = 9;
            yLocCharacter = 0;
        }

        animatedSprite = constructSprite();

        updateDirection();
        // Init player rectangle, sprite size = 32
        playerRectangle = new Rectangle(Game.MATERIAL_ZOOM * Level.MATERIALS_SPRITE_SIZE,Game.MATERIAL_ZOOM * Level.MATERIALS_SPRITE_SIZE,Level.PLAYER_SPRITE_SIZE,Level.PLAYER_SPRITE_SIZE);
        playerRectangle.generateGraphic(2,0xFF00FF90);

        // Init collisionCheckRectangle and generate graphic, size = 48
        collisionCheckRectangle = new Rectangle(Game.MATERIAL_ZOOM * Level.MATERIALS_SPRITE_SIZE,Game.MATERIAL_ZOOM * Level.MATERIALS_SPRITE_SIZE,playerRectangle.w * 3 / 2,playerRectangle.h * 3 / 2);
        collisionCheckRectangle.generateGraphic(2, 0xFF00FF90);
    }

    /**
     * update the direction every time we move
     */
    protected void updateDirection() {
        if (animatedSprite != null) {

            //Right line 3
            if (direction == 1) {
                animatedSprite.setAnimationRange(6,8);
            }
            //Left line 2
            if (direction == 3) {
                animatedSprite.setAnimationRange(3,5);
            }
            //Up line 4
            if (direction == 0) {
                animatedSprite.setAnimationRange(9,11);
            }
            //Down line 0
            if (direction == 2) {
                animatedSprite.setAnimationRange(0,2);
            }

        }
    }

    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        renderer.renderSprite(animatedSprite, playerRectangle.x, playerRectangle.y, xZoom, yZoom);
        renderer.renderRectangle(collisionCheckRectangle,xZoom,yZoom);
    }

    @Override
    public void update(Game game) {
        KeyBoard keyBoard = game.getKeyBoard();
        boolean isMove = false;
        int newDirection = direction;

        collisionCheckRectangle.x = playerRectangle.x;
        collisionCheckRectangle.y = playerRectangle.y;

        //Update direction if turn left
        if (keyBoard.left()) {
            collisionCheckRectangle.x -= speed;
            newDirection = 3;
            isMove = true;
        }

        //Update direction if turn right
        if (keyBoard.right()) {
            collisionCheckRectangle.x += speed;
            newDirection = 1;
            isMove = true;
        }

        //Update direction if turn up
        if (keyBoard.up()) {
            collisionCheckRectangle.y -= speed;
            newDirection = 0;
            isMove = true;
        }

        //Update direction if turn down
        if (keyBoard.down()) {
            collisionCheckRectangle.y += speed;
            newDirection = 2;
            isMove = true;
        }

        if (newDirection != direction) {
            direction = newDirection;
            updateDirection();
        }

        if (!isMove) {
            animatedSprite.reset();
        }
        else  {
            // Increase checkrectangle by offset to not collide at the very beginning
            collisionCheckRectangle.x += xCollisionOffset;
            collisionCheckRectangle.y += yCollisionOffset;

            // check x collision
            Rectangle xAxisCheck = new Rectangle(collisionCheckRectangle.x, playerRectangle.y + yCollisionOffset, collisionCheckRectangle.w, collisionCheckRectangle.h);
            if (!game.getLevel1().getMap().checkCollision(xAxisCheck, Game.MATERIAL_ZOOM, Game.MATERIAL_ZOOM)) {
                playerRectangle.x = collisionCheckRectangle.x - xCollisionOffset;
            }

            // check y collision
            Rectangle yAxisCheck = new Rectangle(playerRectangle.x + xCollisionOffset, collisionCheckRectangle.y, collisionCheckRectangle.w, collisionCheckRectangle.h);
            if (!game.getLevel1().getMap().checkCollision(yAxisCheck, Game.MATERIAL_ZOOM, Game.MATERIAL_ZOOM)) {
                playerRectangle.y = collisionCheckRectangle.y - yCollisionOffset;
            }
            // Update animated sprite
            animatedSprite.update(game);
        }

        // moving camera
        movingWithCam(game.getRenderer().getCamera());

        //Release the bomb
        if (keyBoard.space() && currentNumBom < bomLimit) {
            Bomb bomb = new Bomb(game.getBombSheet(), this);

            bombs.add(bomb);

            currentNumBom++;
        }
    }

    /**
     * We always set our character at the middle
     * Everything else render with camera location
     * @param camera
     */
    public void movingWithCam(Rectangle camera) {
        //Let player position at the center of the camera
        camera.x = playerRectangle.x - (camera.w / 2);
        camera.y = playerRectangle.y - (camera.h / 2);
    }

    public Rectangle getPlayerRectangle() {
        return playerRectangle;
    }


    /**
     * current Number of bomb getter
     * @return number of bomb currently
     */
    public int getCurrentNumBom() {
        return currentNumBom;
    }

    /**
     * current Number of bomb setter
     * @param currentNumBom
     */
    public void setCurrentNumBom(int currentNumBom) {
        this.currentNumBom = currentNumBom;
    }

    public ArrayList<Bomb> getBombs() {
        return bombs;
    }

    public void setBombs(ArrayList<Bomb> bombs) {
        this.bombs = bombs;
    }
}
