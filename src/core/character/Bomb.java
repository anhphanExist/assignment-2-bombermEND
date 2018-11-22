package core.character;

import core.Game;
import core.Rectangle;
import core.RenderHandler;
import core.animation.AnimatedSprite;
import core.animation.GameObject;
import core.animation.SpriteSheet;

import java.util.ArrayList;

public class Bomb implements GameObject {
    private AnimatedSprite animatedSprite;
    private Rectangle bombRectangle;
    private Player player;
    private int speed = 20;
    private int counter;

    public Bomb(SpriteSheet sheet, Player player) {
        //Loading the player who release the bomb
        this.player = player;

        //Create bunch of rectangle saving bomb location
        Rectangle[] spritePos = new Rectangle[4];

        //From the sheet we have
        for (int i = 0; i < spritePos.length; i++) {
            spritePos[i] = new Rectangle(12 * 16, (i+2) * 16, 16, 16);
        }
        spritePos[3] = new Rectangle(15*16 , 5 * 16, 16, 16);

        //Construct animate sprite for bomb
        animatedSprite = new AnimatedSprite(sheet, spritePos, speed);

        //Generate bomb rectangle
        bombRectangle = new Rectangle(player.getPlayerRectangle().x, player.getPlayerRectangle().y, 16, 16);

    }

    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        renderer.renderSprite(animatedSprite, bombRectangle.x, bombRectangle.y, 4,4);
    }

    @Override
    public void update(Game game) {
        animatedSprite.update(game);
        counter++;

        if (counter == speed * 4) {
            //Blowing the bomb
            ArrayList<GameObject> temporaryList = game.getGameObjects();

            temporaryList.remove(temporaryList.size() - 1);

            game.setGameObjects(temporaryList);

            this.player.setCurrentNumBom(this.player.getCurrentNumBom() - 1);
        }

    }
}
