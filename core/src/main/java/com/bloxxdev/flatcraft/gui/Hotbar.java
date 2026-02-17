package com.bloxxdev.flatcraft.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bloxxdev.flatcraft.Main;
import com.bloxxdev.flatcraft.MainGameScreen;

public class Hotbar {

    Texture hotbar;
    Texture hotbarSelection;

    float hotbarX;
    float hotbarY;

    private int selection = 0;

    public void show(){
        hotbar = new Texture(Gdx.files.internal("GUI/Hotbar.png"));
        hotbarSelection = new Texture(Gdx.files.internal("GUI/HotbarSelection.png"));

        hotbarX = (float) Gdx.graphics.getWidth()/2-hotbar.getWidth()*(float) Main.GUI_SCALE/2;
        hotbarY = hotbar.getHeight()*(float)Main.GUI_SCALE/2;
    }

    public void tick(){
        checkKeys();
    }

    public void mouseScrolled(float amountY){
        selection = (selection-(int)amountY+9)%9;
    }

    int keymask = 0b000000000;

    private void checkKeys() {
        if (!Gdx.input.isKeyPressed(Input.Keys.NUM_1)){
            keymask = keymask & 0b011111111;
        }
        if (!Gdx.input.isKeyPressed(Input.Keys.NUM_1)){
            keymask = keymask & 0b101111111;
        }
        if (!Gdx.input.isKeyPressed(Input.Keys.NUM_1)){
            keymask = keymask & 0b110111111;
        }
        if (!Gdx.input.isKeyPressed(Input.Keys.NUM_1)){
            keymask = keymask & 0b111011111;
        }
        if (!Gdx.input.isKeyPressed(Input.Keys.NUM_1)){
            keymask = keymask & 0b111101111;
        }
        if (!Gdx.input.isKeyPressed(Input.Keys.NUM_1)){
            keymask = keymask & 0b111110111;
        }
        if (!Gdx.input.isKeyPressed(Input.Keys.NUM_1)){
            keymask = keymask & 0b111111011;
        }
        if (!Gdx.input.isKeyPressed(Input.Keys.NUM_1)){
            keymask = keymask & 0b111111101;
        }
        if (!Gdx.input.isKeyPressed(Input.Keys.NUM_1)){
            keymask = keymask & 0b111111110;
        }

        if ((keymask & 0b100000000) == 0
            && Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
            selection = 0;
            keymask = keymask | 0b100000000;
        }
        if ((keymask & 0b010000000) == 0
            && Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            selection = 1;
            keymask = keymask | 0b010000000;
        }
        if ((keymask & 0b001000000) == 0
            && Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
            selection = 2;
            keymask = keymask | 0b001000000;
        }
        if ((keymask & 0b000100000) == 0
            && Gdx.input.isKeyPressed(Input.Keys.NUM_4)) {
            selection = 3;
            keymask = keymask | 0b000100000;
        }
        if ((keymask & 0b000010000) == 0
            && Gdx.input.isKeyPressed(Input.Keys.NUM_5)) {
            selection = 4;
            keymask = keymask | 0b000010000;
        }
        if ((keymask & 0b000001000) == 0
            && Gdx.input.isKeyPressed(Input.Keys.NUM_6)) {
            selection = 5;
            keymask = keymask | 0b000001000;
        }
        if ((keymask & 0b000000100) == 0
            && Gdx.input.isKeyPressed(Input.Keys.NUM_7)) {
            selection = 6;
            keymask = keymask | 0b000000100;
        }
        if ((keymask & 0b000000010) == 0
            && Gdx.input.isKeyPressed(Input.Keys.NUM_8)) {
            selection = 7;
            keymask = keymask | 0b000000010;
        }
        if ((keymask & 0b000000001) == 0
            && Gdx.input.isKeyPressed(Input.Keys.NUM_9)) {
            selection = 8;
            keymask = keymask | 0b000000001;
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(hotbar, hotbarX, hotbarY, hotbar.getWidth() * Main.GUI_SCALE, hotbar.getHeight() * Main.GUI_SCALE);
        batch.draw(hotbarSelection, hotbarX + selection*hotbar.getHeight()*Main.GUI_SCALE-Main.GUI_SCALE, hotbarY - Main.GUI_SCALE, hotbarSelection.getWidth() * Main.GUI_SCALE, hotbarSelection.getHeight() * Main.GUI_SCALE);

        for (int i = 0; i < 9; i++) {
            if (MainGameScreen.player.playerInventory.items[i] != null) {
                Texture tex = MainGameScreen.player.getPlayerInventory().items[i].getType().getTexture();
                if (tex != null) {
                    batch.draw(tex, hotbarX + (float) hotbar.getHeight() / 2 * Main.GUI_SCALE - (float) tex.getWidth() * Main.ITEM_SCALE / 2 + i*hotbar.getHeight()*Main.GUI_SCALE, hotbarY + (float) hotbar.getHeight() / 2 * Main.GUI_SCALE - (float) tex.getHeight() * Main.ITEM_SCALE / 2, tex.getWidth() * Main.ITEM_SCALE, tex.getHeight() * Main.ITEM_SCALE);
                }
            }
        }
    }

    public int getSelection() {
        return selection;
    }
}
