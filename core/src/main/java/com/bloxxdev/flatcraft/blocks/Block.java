package com.bloxxdev.flatcraft.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bloxxdev.flatcraft.Main;
import com.bloxxdev.flatcraft.MainGameScreen;
import com.bloxxdev.flatcraft.items.Item;
import com.bloxxdev.flatcraft.phys.AABB;
import com.bloxxdev.flatcraft.player.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Block implements Serializable {

    public static final int STONE = 1;
    public static final int GRASS = 2;
    public static final int DIRT = 3;

    private int id;

    private boolean selected = false;

    public static final HashSet<Integer> blockIDs = new HashSet<>(Arrays.asList(
        1, 2, 3
    ));

    public Block(int id){
        this.id = id;
    }

    public int getType(){
        return id;
    }

    public AABB getHitbox(int x, int y){
        return new AABB(x, y, 1, 1);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void render(SpriteBatch batch, int x, int y){
        float displayX = MainGameScreen.player.getDisplayX() + (x-MainGameScreen.player.getX())*16*Main.DEFAULT_SCALE;
        float displayY = MainGameScreen.player.getDisplayY() + (y-MainGameScreen.player.getY())*16*Main.DEFAULT_SCALE - Player.HEIGHT* Main.DEFAULT_SCALE;

        if ((displayX > -16*Main.DEFAULT_SCALE && displayX < Gdx.graphics.getWidth()+10) && displayY > -16*Main.DEFAULT_SCALE && displayY < Gdx.graphics.getHeight()+10) {

            batch.begin();

            batch.draw(BlockTextures.blockTextures[id], displayX, displayY, 16 * Main.DEFAULT_SCALE, 16 * Main.DEFAULT_SCALE);

            batch.end();

            if (selected) {
                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                Gdx.gl.glEnable(GL20.GL_ALPHA);
                ShapeRenderer renderer = MainGameScreen.shapeRenderer;
                renderer.begin(ShapeRenderer.ShapeType.Filled);

                renderer.setColor(1.0F, 1.0F, 1.0F, 0.4F);

                renderer.rect(displayX, displayY, 16 * Main.DEFAULT_SCALE, 16 * Main.DEFAULT_SCALE);
                renderer.end();
                Gdx.gl.glDisable(GL20.GL_BLEND);

                renderer.begin(ShapeRenderer.ShapeType.Line);

                renderer.setColor(0F, 0F, 0F, 1F);

                renderer.rect(displayX, displayY, 16 * Main.DEFAULT_SCALE, 16 * Main.DEFAULT_SCALE);
                renderer.end();
            }
        }
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
