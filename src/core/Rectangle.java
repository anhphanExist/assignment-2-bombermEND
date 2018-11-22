package core;

public class Rectangle {
    public int x,y,w,h;
    private int[] pixels;

    public Rectangle(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public Rectangle() {
        this(0,0,0,0);
    }

    /**
     * Filling full rectangle
     * @param color
     */
    public void generateGraphic(int color) {
        pixels = new int[w*h];
        for (int y = 0; y<h; y++){
            for (int x = 0; x<w; x++){
                pixels[x + y*w] = color;
            }
        }
    }

    /**
     * Filling rectangle with border
     * @param borderWidth
     * @param color
     */
    public void generateGraphic(int borderWidth, int color){
        pixels = new int[w*h];

        //Fill all
        for (int i = 0 ; i<pixels.length; i++){
            pixels[i] = Game.randomColor;
        }

        //Fill top
        for (int y = 0; y<borderWidth; y++) {
            for (int x = 0 ; x<w ; x++){
                pixels[x + y*w] = color;
            }
        }

        //Fill left
        for (int y = 0 ; y < h; y++) {
            for (int x = 0; x<borderWidth; x++) {
                pixels[x + y*w] = color;
            }
        }

        //Fill right
        for (int y = 0 ; y < h; y++) {
            for (int x = w - borderWidth; x< w; x++) {
                pixels[x + y*w] = color;
            }
        }

        //Fill bottom
        for (int y = h - borderWidth ; y < h; y++) {
            for (int x = 0; x < w; x++) {
                pixels[x + y*w] = color;
            }
        }


    }

    public int[] getPixels() {

        return pixels;
    }

    /**
     * Check if two rectangle lay over each other
     * @param otherRect
     * @return
     */
    public boolean intersects(Rectangle otherRect) {
        if(x > otherRect.x + otherRect.w || otherRect.x > x + w)
            return false;

        if(y > otherRect.y + otherRect.h || otherRect.y > y + h)
            return false;

        return true;
    }
}
