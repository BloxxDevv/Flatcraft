package com.bloxxdev.flatcraft;

import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public static final int DEFAULT_SCALE = 4;
    public static final int GUI_SCALE = 3;
    public static final int ITEM_SCALE = 2;

    @Override
    public void create() {
        setScreen(new MainGameScreen());
    }
}
