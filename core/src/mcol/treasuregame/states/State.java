package mcol.treasuregame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import mcol.treasuregame.TreasureGame;
import mcol.treasuregame.gfx.Background;
import mcol.treasuregame.gfx.Camera;

public abstract class State implements Screen, InputProcessor {

    /** Main game object. */
    protected final TreasureGame game;

    /** Object to batch the drawing of the game graphics. */
    protected final Batch sb;

    /** Active camera. */
    protected final Camera camera;

    /** Game viewport. */
    protected final Viewport viewport;

    /** Background image. */
    protected Background background;

    /** Constructor. */
    protected State(TreasureGame game, Batch sb) {
        this.game = game;
        this.sb = sb;
        camera = new Camera();
        viewport = new ExtendViewport(TreasureGame.WIDTH, TreasureGame.HEIGHT, camera);
        viewport.apply(true);
    }

    protected abstract void update(float dt);

    // Screen interface

    @Override
    public void render(float dt) {

        update(dt);

        // clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.setProjectionMatrix(camera.combined);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

    // InputProcessor interface

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
