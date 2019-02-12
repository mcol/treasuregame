package mcol.treasuregame.assets;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import mcol.treasuregame.assets.creatures.Player;
import mcol.treasuregame.assets.items.*;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    /** Container for all items. */
    private final List<Item> items;

    /** List of items to remove. */
    private final List<Item> toRemove;

    /** Number of collectable items in the list. */
    private int numberOfCollectableItems;

    /** Constructor. */
    public ItemManager() {
        items = new ArrayList<>(50);
        toRemove = new ArrayList<>();
        numberOfCollectableItems = 0;
    }

    /** Adds an item to the current list. */
    public void add(Item item) {
        items.add(item);
        if (item.isCollectable())
            numberOfCollectableItems++;
    }

    /** Checks for collisions with the player. */
    public void checkCollisions(Player player) {
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item.isCollectable() && item.getBounds().overlaps(player.getBounds())) {
                int value = item.getValue();
                if (value > 0)
                    player.addScore(value);
                if (item instanceof Hurricane)
                    items.add(new MovingHurricane(item.getX(), item.getY()));
                else if (item instanceof Bomb)
                    player.addBomb();
                items.remove(item);
                numberOfCollectableItems--;
                break;
            }
        }
    }

    /** Destroys elements of the map. */
    public void destroy(Map map) {
        for (Item item : items)
            item.destroy(map);
    }

    public void update(float dt) {
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            item.update(dt);
            if (item.shouldRemove()) {
                toRemove.add(item);
                if (item instanceof ArmedBomb)
                    items.add(new Explosion(item.getX(), item.getY(), 3));
            }
        }
        for (Item item : toRemove) {
            items.remove(item);
            if (item instanceof CollectableItem)
                numberOfCollectableItems--;
        }
        toRemove.clear();
    }

    public void render(Batch sb) {
        for (Item item : items)
            item.render(sb);
    }

    // getters and setters

    /** Returns the coordinates of a random collectable item. */
    public void getRandomCollectableItemCoordinates(Vector3 coords) {
        if (numberOfCollectableItems > 0) {
            Item item = items.get(MathUtils.random(items.size() - 1));
            while (!item.isCollectable())
                item = items.get(MathUtils.random(items.size() - 1));
            coords.x = item.getX();
            coords.y = item.getY();
        }
    }
}
