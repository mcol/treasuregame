package mcol.treasuregame.gfx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Background {

    /** Image to display in the background. */
    private final Sprite background;

    /** Speed of movement of the background. */
    private final float speed;

    /** Width of the background image after scaling. */
    private final float scaledWidth;

    /** Vertical coordinate. */
    private final float y;

    /** Current horizontal coordinate. */
    private float x;

    /** Constructor. */
    public Background(Texture texture, float scale, float bgSpeed) {
        background = new Sprite(texture);
        background.setScale(scale);
        background.setOrigin(0, 0);
        speed = bgSpeed;
        scaledWidth = background.getWidth() * scale;
        x = -scaledWidth / 2;
        y = 0;
    }

    public void update(float dt) {
        x = (x - speed * dt) % scaledWidth;
    }

    public void render(Batch sb) {
        sb.disableBlending();
        background.setPosition(x, y);
        background.draw(sb);
        background.setPosition(x + scaledWidth, y);
        background.draw(sb);
        sb.enableBlending();
    }
}
