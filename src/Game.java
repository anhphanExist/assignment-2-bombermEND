import javax.swing.JFrame;

import java.awt.Canvas;

public class Game extends JFrame implements Runnable {
    private final static int width = 720, height = 640;
    private Canvas canvas = new Canvas();

    public Game() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0,0,width,height);
        setLocationRelativeTo(null);

        //Add graphic component
        add(canvas);

        //Make frame visible
        setVisible(true);

        //Create for buffer strategy
        canvas.createBufferStrategy(3);


    }

    /**
     * update method
     */
    public void update() {

    }

    /**
     * render method
     */
    public void render() {

    }

    /**
     * game loop
     */
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double nanoSecondConversion = 1000000000.0 / 60; //60 frames per sec
        double changeInSeconds = 0;

        while (true) {
            long now = System.nanoTime();

            changeInSeconds += (now - lastTime) / nanoSecondConversion;
            while (changeInSeconds >= 1 ){
                update();
                changeInSeconds--;
            }
            render();
            lastTime = now;
        }
    }
}
