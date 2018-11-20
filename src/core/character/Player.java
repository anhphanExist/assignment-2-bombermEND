package core.character;

import core.animation.*;
import core.*;

public class Player implements GameObject{
    private Rectangle playerRectangle;
    private int speed = 10;
    private AnimatedSprite animatedSprite = null;
    private int characterNum = 0, xLocCharacter = 0, yLocCharacter = 0;
    /**
     * 0 = up, 1 = right, 2 = down, 3 = left
     */
    private int direction = 0;

    /**
     * construct sprites of character from the sheet
     * @return
     */
    private AnimatedSprite constructSprite() {
        String path = new String("imageFolder/character.png");
        SpriteSheet characterSheet = new SpriteSheet(Game.loadImage(path));

        characterSheet.loadSprites(32,32);

        //Create bunch of Rectangle saving location of character sprites
        Rectangle[] spritePos = new Rectangle[12];
        for (int x = 0; x < 3 ;x++){
            for (int y = 0; y < 4; y++) {
                spritePos[3 * y + x] = new Rectangle( (x + xLocCharacter) * 32,  (y + yLocCharacter) * 32, 32, 32 );
            }
        }

        AnimatedSprite animatedSprite = new AnimatedSprite(characterSheet,spritePos,5);

        return animatedSprite;
    }

    public Player(int _CharacterNum) {
        characterNum = _CharacterNum;

        if (characterNum < 6) {
            xLocCharacter = (characterNum % 3) * 3;

            if (characterNum < 3)
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
//        playerRectangle.generateGraphic(1,0xFF00FF90);
    }

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
        //renderer.renderRectangle(playerRectangle,xZoom,yZoom);
    }

    @Override
    public void update(Game game) {
        KeyBoard keyBoard = game.getKeyBoard();
        boolean isMove = false;
        int newDirection = direction;

        if (keyBoard.left()) {
            playerRectangle.x -= speed;
            newDirection = 3;
            isMove = true;
        }

        if (keyBoard.right()) {
            playerRectangle.x += speed;
            newDirection = 1;
            isMove = true;
        }

        if (keyBoard.up()) {
            playerRectangle.y -= speed;
            newDirection = 0;
            isMove = true;
        }

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
    }

    public void movingWithCam(Rectangle camera) {
        //Let player position at the center of the camera
        camera.x = playerRectangle.x - (camera.w / 2);
        camera.y = playerRectangle.y - (camera.h / 2);
    }

    public Rectangle getPlayerRectangle() {
        return playerRectangle;
    }
}
