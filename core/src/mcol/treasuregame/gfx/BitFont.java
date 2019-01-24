package mcol.treasuregame.gfx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import mcol.treasuregame.assets.Assets;

public class BitFont {

    /** Characters stored in the font sheet. */
    private static final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ "
                                      + "0123456789.,:;'!?()-=+/_~<|";

    /** Alignment options. */
    public enum Align {
        LEFT, CENTRE, RIGHT
    }

    /** Available font sizes. */
    public enum Size {

        /** Available sizes */
        TITLE(3.0f), LARGE(2.0f), MEDIUM(1.0f), SMALL(0.5f), MICRO(0.3f), TINY(0.2f);

        /** Scaling factor. */
        private final float scale;

        /** Constructor. */
        Size(float scale) {
            this.scale = scale;
        }
    }

    /** Renders a message using the font characters. */
    public static void renderMessage(SpriteBatch sb, String msg, float x, float y,
                                     Size size, Align alignment) {

        // adjust the position if the message is centred or right aligned
        if (alignment == Align.CENTRE)
            x -= msg.length() * size.scale / 2;
        else if (alignment == Align.RIGHT)
            x -= msg.length() * size.scale;

        // convert to uppercase before matching
        msg = msg.toUpperCase();

        for (int i = 0; i < msg.length(); i++) {
            int charIndex = chars.indexOf(msg.charAt(i));

            // don't try to render a character not in the set
            if (charIndex < 0)
                continue;

            sb.draw(Assets.fontTexture[charIndex],
                    x + i * size.scale, y, size.scale, size.scale);
        }
    }
}
