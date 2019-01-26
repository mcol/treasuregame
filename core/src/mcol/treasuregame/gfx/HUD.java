package mcol.treasuregame.gfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import mcol.treasuregame.assets.Assets;
import mcol.treasuregame.assets.Map;
import mcol.treasuregame.assets.creatures.Player;
import mcol.treasuregame.utils.Utils;

public class HUD implements Disposable {

    /** Format string for the level. */
    private static final String levelFormat = "Level: %3d";

    /** Format string for the score. */
    private static final String scoreFormat = "Score: %3d";

    /** Format string for the number of bombs. */
    private static final String bombsFormat = "%3d";

    /** Format string for the map coverage. */
    private static final String coverFormat = "Cover: %3d";

    /** Scene graph for the HUD information. */
    private final Stage stage;

    /** The player object. */
    private final Player player;

    /** Label reporting the current level. */
    private Label levelLabel;

    /** Label reporting the current score. */
    private Label scoreLabel;

    /** Label reporting the current number of bombs. */
    private Label bombsLabel;

    /** Label reporting the current map coverage. */
    private Label coverLabel;

    /** Buttons in the HUD. */
    private Button playButton, bombButton, lambButton;

    /** Constructor. */
    public HUD(SpriteBatch sb, Map map, Player player) {
        this.player = player;
        stage = new Stage(new ScreenViewport(new OrthographicCamera()), sb);
        createUI();
    }

    /** Creates the user interface. */
    private void createUI() {

        // labels
        Label.LabelStyle labelStyle = new Label.LabelStyle(new Font(16).get(),
                                                           Color.WHITE);
        levelLabel = new Label(String.format(levelFormat, 1), labelStyle);
        scoreLabel = new Label(String.format(scoreFormat, 0), labelStyle);
        bombsLabel = new Label(String.format(bombsFormat, 0), labelStyle);
        coverLabel = new Label(String.format(coverFormat, 0), labelStyle);

        playButton = Utils.createButton(Assets.buttonPlayTexture);
        bombButton = Utils.createButton(Assets.buttonBombTexture);
        lambButton = Utils.createButton(Assets.buttonLambTexture);
        playButton.setChecked(true);

        // table to organize the buttons
        Table btns = new Table().padTop(20);
        btns.add(playButton).padRight(2);
        btns.add(bombButton).padRight(2);
        btns.add(lambButton);
        new ButtonGroup<Button>(playButton, bombButton, lambButton);

        // table to organize the labels
        Table table = new Table().right().padRight(20);
        table.setFillParent(true);
        table.add(levelLabel);
        table.row();
        table.add(scoreLabel).padTop(20);
        table.row();
        table.add(btns);
        table.row();
        table.add(coverLabel).padTop(20);

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

    /** Sets the current level. */
    public void setLevel(int level) {
        levelLabel.setText(String.format(levelFormat, level));
    }

    /** Sets the current score. */
    public void setScore() {
        scoreLabel.setText(String.format(scoreFormat, player.getScore()));
    }

    /** Sets the current number of bombs. */
    public void setBombs() {
        bombsLabel.setText(String.format(bombsFormat, player.getBombs()));
    }

    /** Sets the current map coverage. */
    public void setCoverage(int coverage) {
        coverLabel.setText(String.format(coverFormat, coverage));
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
