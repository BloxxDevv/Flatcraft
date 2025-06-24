package com.bloxxdev.flatcraft.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bloxxdev.flatcraft.blocks.Block;

import java.io.Serializable;

public class Subchunk implements Serializable {
    //(y * 16) + x
    public Block[] blocks = new Block[16*16];

    public void render(SpriteBatch batch, int sx, int sy){
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                if (blocks[(y * 16) + x] != null) {
                    blocks[(y * 16) + x].render(batch, sx*16+x, sy*16+y);
                }
            }
        }
    }
}
