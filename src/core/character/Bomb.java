package core.character;

import core.Game;
import core.Level.Level;
import core.Level.Map.Brick;
import core.Rectangle;
import core.RenderHandler;
import core.animation.AnimatedSprite;
import core.animation.GameObject;
import core.animation.SpriteSheet;

import java.util.ArrayList;

public class Bomb implements GameObject {
    private Game game;
    private AnimatedSprite animatedSprite;
    private Rectangle bombRectangle;
    private Rectangle collisionCheckRectangle;
    private Player player;
    private int speed = 20;
    private int counter;
    private SpriteSheet sheet;

    public Bomb(Game game, SpriteSheet sheet, Player player) {
        //Loading the player who release the bomb
        this.game = game;
        this.player = player;
        this.sheet = sheet;

        //Create bunch of rectangle saving bomb location
        Rectangle[] spritePos = new Rectangle[4];

        //From the sheet we have
        for (int i = 0; i < spritePos.length; i++) {
            spritePos[i] = new Rectangle(12 * 16, (i+2) * 16, 16, 16);
        }
        spritePos[3] = new Rectangle(2*16 , 5 * 16, 16, 16);

        //Construct animate sprite for bomb
        animatedSprite = new AnimatedSprite(sheet, spritePos, speed);

        //Generate bomb rectangle
        bombRectangle = new Rectangle(player.getPlayerRectangle().x, player.getPlayerRectangle().y, Level.PLAYER_SPRITE_SIZE, Level.PLAYER_SPRITE_SIZE);
        collisionCheckRectangle = new Rectangle(bombRectangle.x, bombRectangle.y, bombRectangle.w * 3 / 2, bombRectangle.h * 3 / 2);

    }

    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        renderer.renderSprite(animatedSprite, bombRectangle.x, bombRectangle.y, 4,4);

        //Spawning the flames
        if (counter >= speed * 3) {
            Explosion explosion = new Explosion(game,this);
            explosion.render(renderer, 4, 4);
        }
    }

    @Override
    public void update(Game game) {
        animatedSprite.update(game);
        counter++;

        if (counter == speed * 4) {
            //Blowing the bomb
            ArrayList<Bomb> temporaryList = player.getBombs();

            this.player.setCurrentNumBom(this.player.getCurrentNumBom() - 1);

            temporaryList.remove(this);

            player.setBombs(temporaryList);

            Game.playSound("soundFolder/BombSound.wav", false);
        }

    }

    public SpriteSheet getSheet() {
        return sheet;
    }

    public Rectangle getBombRectangle() {
        return bombRectangle;
    }

    public Rectangle getCollisionCheckRectangle() {
        return collisionCheckRectangle;
    }
}
