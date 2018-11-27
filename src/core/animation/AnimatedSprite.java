package core.animation;

import core.Game;
import core.RenderHandler;
import core.Rectangle;

import java.awt.image.BufferedImage;

public class AnimatedSprite extends Sprite implements GameObject {
    private Sprite[] sprites;
    private  int currentSprite = 0;
    private int speed;
    private int counter = 0;
    private int startSprite = 0, endSprite;
    private boolean playSound;
    private String soundPath;


    public AnimatedSprite(SpriteSheet sheet, Rectangle[] positions,int speed) {
        sprites = new Sprite[positions.length];
        this.speed = speed;
        this.endSprite = positions.length - 1;

        for (int i = 0 ; i < positions.length; i++) {
            sprites[i] = new Sprite(sheet,positions[i].x, positions[i].y,
                    positions[i].w,positions[i].h);
        }

    }

    /**
     * reset to start sprite
     */
    public void reset() {
        counter = 0;
        currentSprite = startSprite;
    }

    public void setAnimationRange(int startSprite, int endSprite) {
        this.startSprite = startSprite;
        this.endSprite = endSprite;
        reset();
    }

    /**
     *
     * @param images
     * @param speed num of frames pass until the sprite changes
     */
    public AnimatedSprite(BufferedImage[] images, int speed) {
        sprites = new Sprite[images.length];
        this.speed = speed;
        this.endSprite = images.length - 1;

        for (int i = 0; i< images.length; i++)
            sprites[i] = new Sprite(images[i]);
    }

    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom) {

    }

    @Override
    public void update(Game game) {
        counter++;

        if (playSound && counter % speed == 0) {
            Game.playSound("soundFolder/Move.wav", false);
        }

        if (counter >= speed) {
            counter = 0;
            incrementSprite();
        }
    }

    private void incrementSprite() {
        currentSprite++;
        if (currentSprite > endSprite) {
            currentSprite = startSprite;
        }
    }

    public int getWidth() {
        return sprites[currentSprite].getWidth();
    }

    public int getHeight() {
        return sprites[currentSprite].getHeight();
    }

    public int[] getPixels() {
        return sprites[currentSprite].getPixels();
    }

    public void setPlaySound(boolean playSound, String soundPath) {
        this.playSound = playSound;
        this.soundPath = soundPath;
    }
}
