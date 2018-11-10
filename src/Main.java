import core.Game;

public class Main {
    public static void main(String[] args) {
    	try {
			Game game = new Game();
			Thread gameThread = new Thread(game);
			gameThread.start();
		}
		catch (Exception e) {
    		e.printStackTrace();
		}
    }
}
