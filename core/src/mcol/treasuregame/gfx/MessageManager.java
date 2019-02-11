package mcol.treasuregame.gfx;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import mcol.treasuregame.TreasureGame;

import java.util.ArrayList;

public class MessageManager {

    private static class Message {

        /** Message to be displayed. */
        public final String message;

        /** Coordinates where the message should be displayed. */
        public float x, y;

        /** Time when the message was added to the list. */
        public float stateTime;

        /** Constructor. */
        public Message(String message, float x, float y) {
            this.message = message;
            this.x = x;
            this.y = y;
        }
    }

    /** Interval for which the message is displayed in seconds. */
    private static final float MESSAGE_DISPLAY_INTERVAL = 1.5f;

    /** List of messages. */
    private static final ArrayList<Message> messages = new ArrayList<>();

    /** Adds a message to the list. */
    public void addMessage(String message, float x, float y) {
        messages.add(new Message(message, x + TreasureGame.TILESIZE * 0.5f,
                                          y + TreasureGame.TILESIZE));
    }

    public void update(float dt) {
        for (int i = 0; i < messages.size(); i++) {
            Message msg = messages.get(i);
            msg.stateTime += dt;
            if (msg.stateTime > MESSAGE_DISPLAY_INTERVAL) {
                messages.remove(i);
                i--;
                continue;
            }
            msg.x += MathUtils.random(-2, 2) * TreasureGame.TILESIZE * dt;
            msg.y += MathUtils.random(0, 3) * TreasureGame.TILESIZE * dt;
        }
    }

    public void render(Batch sb) {
        for (Message msg : messages) {
            BitFont.renderMessage(sb, msg.message, msg.x, msg.y,
                                  BitFont.Size.MICRO, BitFont.Align.CENTRE);
        }
    }
}
