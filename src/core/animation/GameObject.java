package core.animation;

import core.*;

public interface GameObject {
    void render(RenderHandler renderer, int xZoom, int yZoom);
    void update(Game game);
}

