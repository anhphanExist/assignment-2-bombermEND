package core;

import core.animation.Sprite;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class RenderHandler {
    private BufferedImage view;
    private int[] pixels;
    private Rectangle camera;

    public RenderHandler(int width, int height) {
        //BufferImage for view
        view = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);

        //Camera - basically is our window rectangle
        camera = new Rectangle(0,0,width,height);

        //Array for pixels
        pixels = ((DataBufferInt) view.getRaster().getDataBuffer()).getData();
    }

    /**
     * render graphic window
     * @param graphics
     */
    public void render(Graphics graphics) {
        graphics.drawImage(view,0 ,0 , view.getWidth(), view.getHeight(), null);
    }

    /**
     * rendering Array of pixel method
     * @param renderPixels
     * @param renderWidth
     * @param renderHeight
     * @param xPos
     * @param yPos
     * @param xZoom
     * @param yZoom
     */
    private void renderArray(int[] renderPixels, int renderWidth, int renderHeight,
                            int xPos, int yPos, int xZoom, int yZoom) {
        for (int y = 0; y < renderHeight; y++) {
            for (int x = 0; x < renderWidth; x++) {
                for (int yZoomPosition = 0; yZoomPosition < yZoom; yZoomPosition++){
                    for (int xZoomPosition = 0 ; xZoomPosition < xZoom; xZoomPosition++) {
                        setPixels(renderPixels[x + y * renderWidth],
                                x * xZoom + xPos + xZoomPosition,
                                y * yZoom + yPos + yZoomPosition);
                    }
                }
            }
        }
    }

    /**
     * setting the pixel base on camera
     * @param pixel
     * @param x
     * @param y
     */
    private void setPixels(int pixel, int x, int y) {
        if (x >= camera.x
                && y >= camera.y
                && camera.x + camera.w >= x
                && camera.h + camera.y >= y) {
            int pixelIndex = (x - camera.x) + (y - camera.y) * view.getWidth();

            //Transparent randomColor
            if (pixels.length > pixelIndex && pixel != Game.randomColor)
                pixels[pixelIndex] = pixel;
        }
    }

    /**
     * render a rectangle
     * @param rectangle
     * @param xZoom
     * @param yZoom
     */
    public void renderRectangle(Rectangle rectangle, int xZoom, int yZoom) {
        int[] rectanglePixels = rectangle.getPixels();
        if (rectanglePixels != null){
            renderArray(rectanglePixels, rectangle.w,rectangle.h, rectangle.x, rectangle.y, xZoom, yZoom );
        }
    }

    public void clearPixel() {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = 0;
        }
    }

    public void renderSprite(Sprite sprite, int xPos, int yPos, int xZoom, int yZoom) {
        renderArray(sprite.getPixels(), sprite.getWidth(),
                sprite.getHeight(), xPos, yPos, xZoom, yZoom);
    }

    public Rectangle getCamera() {
        return camera;
    }
}
