package mcol.treasuregame.gfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class UIButton extends Button {

    /** Text to be displayed inside the button. */
    private String count;

    /** Constructor. */
    UIButton(TextureRegion[] texture) {
        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = new TextureRegionDrawable(texture[0]);
        style.down = new TextureRegionDrawable(texture[1]);
        style.disabled = new TextureRegionDrawable(texture[2]);
        setStyle(style);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setColor(isDisabled() ? Color.GRAY : Color.DARK_GRAY);
        BitFont.renderMessage(batch, count, getX() + 50, getY() + 9,
                              16, BitFont.Align.CENTRE);
        batch.setColor(Color.WHITE);
    }

    /** Sets the count. */
    public void setCount(int number) {
        count = Integer.toString(number);
    }
}
