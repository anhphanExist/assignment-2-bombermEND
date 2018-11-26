package core.character.Enemy;

import core.Game;

import java.util.Random;

public class Water extends Enemy {
    /**
     * Create enemy
     * @param xLoc    - x location
     * @param yLoc    - y location
     */
    public Water(int xLoc, int yLoc) {
        super(xLoc, yLoc, 2);
    }

    public void update(Game game) {
        boolean isMove = false;
        int newDirection = direction;

        collisionCheckRectangle.x = playerRectangle.x;
        collisionCheckRectangle.y = playerRectangle.y;

        //Set new direction - random every 4 * 60 counting
        if (this.getDirectCounting()== 45) {
            this.setDirectCounting(0);
            Random random = new Random();
            newDirection = random.nextInt(4);

        } else {
            this.setDirectCounting(this.getDirectCounting() + 1);
        }

        //Moving base on direction
        switch (this.direction) {
            case 1:
                isMove = true;
                collisionCheckRectangle.x += this.getEnemySpeed();
                break;
            case 0:

                isMove = true;
                collisionCheckRectangle.y -= this.getEnemySpeed();
                break;
            case 2:

                isMove = true;
                collisionCheckRectangle.y += this.getEnemySpeed();
                break;
            case 3:

                isMove = true;
                collisionCheckRectangle.x -= this.getEnemySpeed();
                break;
        }



        //New direction checking
        if (newDirection != direction) {
            direction = newDirection;
            updateDirection();
        }

        if (!isMove) {
            this.animatedSprite.reset();
        }
        else  {
            enemyCollision(game);
            this.animatedSprite.update(game);
        }
    }
}
