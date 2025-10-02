package com.bloxxdev.flatcraft.player;

import com.badlogic.gdx.InputProcessor;
import com.bloxxdev.flatcraft.gui.Hotbar;

public class PlayerInputHandler implements InputProcessor {

    private Hotbar hotbar;

    public PlayerInputHandler(Hotbar hotbar){
        this.hotbar = hotbar;
    }

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
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
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
    public boolean scrolled(float amountX, float amountY) {
        if (amountY > 0){
            hotbar.mouseScrolled(-1);
        }else if (amountY < 0){
            hotbar.mouseScrolled(1);
        }
        return false;
    }
}
