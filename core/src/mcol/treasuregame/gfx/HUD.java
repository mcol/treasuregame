package mcol.treasuregame.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import mcol.treasuregame.assets.Assets;
import mcol.treasuregame.assets.creatures.Player;

public class HUD implements Disposable {

    /** Scene graph for the HUD information. */
    private final Stage stage;

    /** The player object. */
    private final Player player;

    /** The minimap widget. */
    private final MiniMap miniMap;

    /** Buttons in the HUD. */
    private UIButton playButton;
    private UIButton hurrButton;
    private UIButton bombButton;
    private UIButton lambButton;

    /** Constructor. */
    public HUD(Batch sb, MiniMap miniMap, Player player) {
        this.miniMap = miniMap;
        this.player = player;
        stage = new Stage(new ScreenViewport(new OrthographicCamera()), sb);
        createUI();
    }

    /** Creates the user interface. */
    private void createUI() {

        playButton = new UIButton(Assets.buttonPlayTexture);
        bombButton = new UIButton(Assets.buttonBombTexture);
        hurrButton = new UIButton(Assets.buttonHurrTexture);
        lambButton = new UIButton(Assets.buttonLambTexture);
        playButton.setChecked(true);

        // table to organize the buttons
        Table table = new Table();
        table.add(playButton);
        table.row();
        table.add(bombButton).padTop(3);
        table.row();
        table.add(hurrButton).padTop(3);
        table.row();
        table.add(lambButton).padTop(3);
        table.row();
        table.add(miniMap).padTop(5);
        new ButtonGroup<>(playButton,
                          bombButton,
                          hurrButton,
                          lambButton);

        // set the background
        table.setWidth(playButton.getPrefWidth() + 20);
        table.setHeight(Gdx.graphics.getHeight());
        table.setX(Gdx.graphics.getWidth() - table.getWidth());
        table.setBackground(new NinePatchDrawable(Assets.buttonPatch));

        // ignore inputs over the table background area
        table.setTouchable(Touchable.enabled);
        table.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(table);
    }

    public void update(float dt) {
        playButton.setCount(player.getScore());
        bombButton.setCount(player.getBombs());
        hurrButton.setCount(player.getHurricanes());
        lambButton.setCount(player.getLambs());
        bombButton.setDisabled(player.getBombs() == 0);
        hurrButton.setDisabled(player.getHurricanes() == 0);
        lambButton.setDisabled(player.getLambs() == 0);
        miniMap.update(dt);
    }

    public void render() {
        playButton.setChecked(true);
        stage.act();
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        Table table = (Table) stage.getActors().first();
        table.setHeight(Gdx.graphics.getHeight());
        table.setX(Gdx.graphics.getWidth() - table.getWidth());
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    // getters and setters

    /** Returns the stage. */
    public Stage getStage() {
        return stage;
    }

    /** Returns whether the bomb button is active. */
    public boolean isHoldingBomb() {
        return bombButton.isChecked();
    }

    /** Returns whether the hurricane button is active. */
    public boolean isHoldingHurricane() {
        return hurrButton.isChecked();
    }

    /** Returns whether the lamb button is active. */
    public boolean isHoldingLamb() {
        return lambButton.isChecked();
    }
}
