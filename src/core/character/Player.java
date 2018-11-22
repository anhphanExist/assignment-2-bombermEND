package core.character;

import core.animation.*;
import core.*;

import java.util.ArrayList;

public class Player implements GameObject{
    private Rectangle playerRectangle;
    private int speed = 10;
    private AnimatedSprite animatedSprite;
    private int xLocCharacter;
    private int yLocCharacter;

    private int bomLimit = 1;
    private int currentNumBom = 0;

    /**
     * 0 = up, 1 = right, 2 = down, 3 = left
     */
    private int direction = 0;

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

        this.animatedSprite = constructSprite();

        updateDirection();
        playerRectangle = new Rectangle(32,16,32,32);
        playerRectangle.generateGraphic(1,0xFF00FF90);
    }

    /**
     * update the direction every time we move
     */
    private void updateDirection() {
        if (animatedSprite != null) {
            //Up line 4
            if (direction == 0) {
                animatedSprite.setAnimationRange(9,11);
            }

            //Right line 3
            if (direction == 1) {
                animatedSprite.setAnimationRange(6,8);
            }

            //Left line 2
            if (direction == 3) {
                animatedSprite.setAnimationRange(3,5);
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
        renderer.renderRectangle(playerRectangle,xZoom,yZoom);
    }

    @Override
    public void update(Game game) {
        KeyBoard keyBoard = game.getKeyBoard();
        boolean isMove = false;
        int newDirection = direction;

        //Update direction if turn left
        if (keyBoard.left()) {
            playerRectangle.x -= speed;
            newDirection = 3;
            isMove = true;
        }

        //Update direction if turn right
        if (keyBoard.right()) {
            playerRectangle.x += speed;
            newDirection = 1;
            isMove = true;
        }

        //Update direction if turn up
        if (keyBoard.up()) {
            playerRectangle.y -= speed;
            newDirection = 0;
            isMove = true;
        }

        //Update direction if turn down
        if (keyBoard.down()) {
            playerRectangle.y += speed;
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

        movingWithCam(game.getRenderer().getCamera());

        if (isMove) {
            animatedSprite.update(game);
        }

        //Release the bomb
        if (keyBoard.space() && currentNumBom < bomLimit) {
            ArrayList<GameObject> temp = game.getGameObjects();

            Bomb bomb = new Bomb(game.getBombSheet(), this);

            temp.add(bomb);

            game.setGameObjects(temp);

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
}
