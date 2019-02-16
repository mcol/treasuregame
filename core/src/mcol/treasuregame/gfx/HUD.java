package mcol.treasuregame.gfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import mcol.treasuregame.assets.Assets;
import mcol.treasuregame.assets.Map;
import mcol.treasuregame.assets.creatures.Player;

public class HUD implements Disposable {

    /** Format string for the score. */
    private static final String scoreFormat = "Score: %3d";

    /** Scene graph for the HUD information. */
    private final Stage stage;

    /** The player object. */
    private final Player player;

    /** Label reporting the current score. */
    private Label scoreLabel;

    /** Buttons in the HUD. */
    private UIButton playButton;
    private UIButton bombButton;
    private UIButton lambButton;

    /** Constructor. */
    public HUD(Batch sb, Map map, Player player) {
        this.player = player;
        stage = new Stage(new ScreenViewport(new OrthographicCamera()), sb);
        createUI();
    }

    /** Creates the user interface. */
    private void createUI() {

        // labels
        Label.LabelStyle labelStyle = new Label.LabelStyle(new Font(16).get(),
                                                           Color.WHITE);
        scoreLabel = new Label(String.format(scoreFormat, 0), labelStyle);

        playButton = new UIButton(Assets.buttonPlayTexture);
        bombButton = new UIButton(Assets.buttonBombTexture);
        lambButton = new UIButton(Assets.buttonLambTexture);
        playButton.setChecked(true);

        // table to organize the buttons
        Table btns = new Table().padTop(20);
        btns.add(playButton).padRight(2);
        btns.add(bombButton).padRight(2);
        btns.add(lambButton);
        new ButtonGroup<>(playButton,
                          bombButton,
                          lambButton);

        // table to organize the labels
        Table table = new Table().right().padRight(20);
        table.setFillParent(true);
        table.add(scoreLabel).padTop(20);
        table.row();
        table.add(btns);

        stage.addActor(table);
    }

    /** Reassigns the active mode to the player. */
    public void resetCheckedButton() {
        playButton.setChecked(true);
    }

    public void render() {
        bombButton.setDisabled(player.getBombs() == 0);
        lambButton.setDisabled(player.getLambs() == 0);
        stage.act();
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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

    /** Sets the current score. */
    public void setScore() {
        scoreLabel.setText(String.format(scoreFormat, player.getScore()));
    }

    /** Returns whether the bomb button is active. */
    public boolean isHoldingBomb() {
        return bombButton.isChecked();
    }

    /** Returns whether the lamb button is active. */
    public boolean isHoldingLamb() {
        return lambButton.isChecked();
    }
}
