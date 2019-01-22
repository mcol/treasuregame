package mcol.treasuregame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import mcol.treasuregame.TreasureGame;
import mcol.treasuregame.assets.Assets;
import mcol.treasuregame.gfx.Background;

public class MenuState extends State {

    /** Scene graph for the menu options. */
    private final Stage stage;

    /** Constructor. */
    public MenuState(final TreasureGame game, final SpriteBatch sb) {
        super(game, sb);
        stage = new Stage(viewport, sb);
        background = new Background(Assets.menubg, 0.03f, 0.3f);
        createUI();
    }

    /** Creates the user interface. */
    private void createUI() {
        float buttonSize = viewport.getWorldWidth() / 4;
        float buttonX = (viewport.getWorldWidth() - buttonSize) / 2;
        float buttonY = viewport.getWorldHeight() / 2;

        // play button
        Image play = new Image(Assets.iconTexture);
        play.setSize(buttonSize, buttonSize);
        play.setPosition(buttonX, buttonY);
        play.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new PlayState(game, sb));
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(play);

        // exit button
        Image exit = new Image(Assets.exitTexture);
        exit.setSize(buttonSize / 2, buttonSize / 2);
        exit.setPosition(buttonX + exit.getWidth() / 2, buttonY - viewport.getWorldHeight() / 4);
        exit.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(exit);
    }

    @Override
    protected void update(float dt) {
        background.update(dt);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        sb.begin();
        background.render(sb);
        sb.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }
}
