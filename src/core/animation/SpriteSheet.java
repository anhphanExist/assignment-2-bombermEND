package core.animation;

import java.awt.image.BufferedImage;

public class SpriteSheet {
    private int[] pixels;
    private BufferedImage image;
    public final int SIZEX, SIZEY;
    private Sprite[] loadedSprite = null;
    private boolean checkLoadSprite = false;
    private int spriteSizeX;

    public SpriteSheet(BufferedImage sheetImage) {
        this.image = sheetImage;
        SIZEX = sheetImage.getWidth();
        SIZEY = sheetImage.getHeight();

        pixels = new int[SIZEY * SIZEX];
        pixels = sheetImage.getRGB(0,0,SIZEX,SIZEY,pixels,0,SIZEX);
    }

    /**
     * Create array of Sprites contain each sprite with size from param
     * @param spriteSizeX
     * @param spriteSizeY
     */
    public void loadSprites(int spriteSizeX, int spriteSizeY){
        this.spriteSizeX = spriteSizeX;
        loadedSprite = new Sprite[(SIZEX / spriteSizeX) * (SIZEY / spriteSizeY)];

        int spriteID = 0;
        for (int y = 0; y < SIZEY; y += spriteSizeY ){
            for (int x = 0 ; x < SIZEX; x+= spriteSizeX){
                loadedSprite[spriteID] = new Sprite(this,x,y,spriteSizeX,spriteSizeY);
                spriteID++;
            }
        }
        checkLoadSprite = true;
    }

    /**
     * get a sprite located by x and y
     * @param x
     * @param y
     * @return
     */
    public Sprite getSprite(int x, int y) {
        if (checkLoadSprite) {
            int spriteID = x + y * (SIZEX / spriteSizeX);

            if (spriteID < loadedSprite.length) {
                return loadedSprite[spriteID];
            }
            else {
                System.out.println("SpriteID: " + spriteID + " out of range");
            }
        }
        else {
            System.out.println("Problem with load sprite");
        }
        return null;
    }

    public int[] getPixels() {
        return pixels;
    }

    public BufferedImage getImage() {
        return image;
    }
}