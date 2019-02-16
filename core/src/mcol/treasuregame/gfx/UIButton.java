package mcol.treasuregame.gfx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class UIButton extends Button {

    /** Constructor. */
    UIButton(TextureRegion[] texture) {
        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = new TextureRegionDrawable(texture[0]);
        style.down = new TextureRegionDrawable(texture[1]);
        style.disabled = new TextureRegionDrawable(texture[3]);
        setStyle(style);
    }
}
