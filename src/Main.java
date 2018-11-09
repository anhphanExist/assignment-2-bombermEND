public class Main {

    public static void main(String[] args) {
	    Game game = new Game();
	    Thread gameThread = new Thread(game);
	    gameThread.start();
    }
}
