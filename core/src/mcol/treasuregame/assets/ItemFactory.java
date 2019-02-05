package mcol.treasuregame.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapProperties;
import mcol.treasuregame.TreasureGame;
import mcol.treasuregame.assets.items.Bomb;
import mcol.treasuregame.assets.items.Hurricane;
import mcol.treasuregame.assets.items.Item;
import mcol.treasuregame.assets.items.Sweet;

public class ItemFactory {

    /** Create an item according to its properties. */
    public static Item createItem(MapProperties mp) {
        String type = mp.get("type", String.class);
        float x = -1, y = -1;
        try {
            x = mp.get("x", float.class) * TreasureGame.SCALE;
            y = mp.get("y", float.class) * TreasureGame.SCALE;
            switch (type) {
                case "sweet":
                    return new Sweet(x, y, mp.get("value", int.class));
                case "bomb":
                    return new Bomb(x, y);
                case "hurricane":
                    return new Hurricane(x, y);
                default:
                    Gdx.app.log("createItem", "Ignored " + type + " " + x + " " + y);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Gdx.app.log("createItem", type + " " + x + " " + y);
        }
        return null;
    }
}
