package core.character;

import core.Level.Level;
import core.Level.Map.Door;
import core.Level.Map.Item;
import core.animation.*;
import core.*;

import java.util.ArrayList;

public class Player implements GameObject {
    private static final int BOMB_LIMIT = 10;

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
    private int bomLimit = BOMB_LIMIT;
    private int currentNumBom = 0;

    private int counter = 0;


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

    public Player() {}

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

        //While moving adding sound for character
        animatedSprite.setPlaySound(true, "soundFolder/Move.wav") ;

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

//        renderer.renderRectangle(playerRectangle, Game.PLAYER_ZOOM, Game.PLAYER_ZOOM);
    }

    @Override
    public void update(Game game) {
        counter++;

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
                if (!game.getLevel1().getMap().checkCollisionMobVsBrick(xAxisCheck)) {
                    playerRectangle.x = collisionCheckRectangle.x - xCollisionOffset;
                }
            }

            // check y collision
            Rectangle yAxisCheck = new Rectangle(playerRectangle.x + xCollisionOffset, collisionCheckRectangle.y, collisionCheckRectangle.w, collisionCheckRectangle.h);
            if (!game.getLevel1().getMap().checkCollision(yAxisCheck, Game.MATERIAL_ZOOM, Game.MATERIAL_ZOOM)) {
                if (!game.getLevel1().getMap().checkCollisionMobVsBrick(yAxisCheck)) {
                    playerRectangle.y = collisionCheckRectangle.y - yCollisionOffset;
                }
            }

            // check die when collide vs enemy
            if (game.getLevel1().getMap().checkCollisionPlayerVsEnemy(collisionCheckRectangle)) {
                game.setRunning(false);
            }

            // Update animated sprite
            animatedSprite.update(game);


        }

        // moving camera
        movingWithCam(game.getRenderer().getCamera());

        //Release the bomb
        if ( keyBoard.spacePress() &&  currentNumBom < bomLimit) {
            Bomb bomb = new Bomb(game, game.getBombSheet(), this);
            boolean isBombCollideWithBombs = false;
            for (Bomb checkBomb : bombs) {
                if (checkBomb.getCollisionCheckRectangle().intersects(bomb.getCollisionCheckRectangle())) {
                    isBombCollideWithBombs = true;
                }
            }
            if (!isBombCollideWithBombs) {
                bombs.add(bomb);
                currentNumBom++;
            }
        }

        handleCollisionVsItems(game);
        if (counter == speed * 300) {
            bomLimit = BOMB_LIMIT;
            counter = 0;
        }
        handleCollisionVsDoor(game);
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

    private void handleCollisionVsItems(Game game) {
        int itemsToRemoveID = 0;
        boolean itemCollide = false;
        ArrayList<Item> items = game.getLevel1().getMap().getItems();
        ArrayList<GameObject> gameObjects = game.getLevel1().getMap().getGameObjects();
        while (itemsToRemoveID < items.size()) {
            if (items.get(itemsToRemoveID).getItemRectangle().intersects(this.playerRectangle)) {
                bomLimit = BOMB_LIMIT * 10;
                itemCollide = true;
                break;
            }
            itemsToRemoveID++;
        }
        if(itemCollide) {
            Item itemToRemove = items.get(itemsToRemoveID);
            items.remove(itemsToRemoveID);
            gameObjects.remove(itemToRemove);
        }
    }

    private void handleCollisionVsDoor(Game game) {
        Door door = game.getLevel1().getMap().getDoor();
        Player player = game.getLevel1().getMap().getPlayer();
        boolean doorCollide = false;
        if (game.getLevel1().getMap().getEnemies().size() == 0 && player.getPlayerRectangle().intersects(door.getItemRectangle())) {
            doorCollide = true;
        }
        if (doorCollide) {
            game.setRunning(false);
        }
    }

    public Rectangle getPlayerRectangle() {
        return playerRectangle;
    }

    public Rectangle getCollisionCheckRectangle() {
        return collisionCheckRectangle;
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
