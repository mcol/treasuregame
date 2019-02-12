package mcol.treasuregame.gfx;

import com.badlogic.gdx.graphics.g2d.Batch;
import mcol.treasuregame.TreasureGame;
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

        /** Predefined sizes. */
        TITLE(6.0f),
        LARGE(4.0f),
        MEDIUM(2.0f),
        SMALL(1.0f),
        MICRO(0.6f),
        TINY(0.3f);

        /** Scaling factor. */
        private final float scale;

        /** Constructor. */
        Size(float scale) {
            this.scale = scale * TreasureGame.HEIGHT / 32;
        }
    }

    /** Renders a message using the font characters. */
    public static void renderMessage(Batch sb, String msg, float x, float y,
                                     float size, Align alignment) {

        // adjust the position if the message is centred or right aligned
        if (alignment == Align.CENTRE)
            x -= msg.length() * size / 2;
        else if (alignment == Align.RIGHT)
            x -= msg.length() * size;

        // convert to uppercase before matching
        msg = msg.toUpperCase();

        for (int i = 0; i < msg.length(); i++) {
            int charIndex = chars.indexOf(msg.charAt(i));

            // don't try to render a character not in the set
            if (charIndex < 0)
                continue;

            sb.draw(Assets.fontTexture[charIndex],
                    x + i * size, y, size, size);
        }
    }

    /** Renders a message using the font characters. */
    public static void renderMessage(Batch sb, String msg, float x, float y,
                                     Size size, Align alignment) {
        renderMessage(sb, msg, x, y, size.scale, alignment);
    }
}
