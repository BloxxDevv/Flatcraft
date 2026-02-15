package com.bloxxdev.flatcraft.items;

import com.badlogic.gdx.graphics.Texture;
import com.bloxxdev.flatcraft.blocks.BlockTextures;

import java.util.HashSet;

public enum Item {
    STONE,
    GRASS,
    DIRT,
    BEDROCK,
    OAK_LEAVES,
    OAK_LOG;

    public int getId(){
        return this.ordinal() + 1;
    }

    public Texture getTexture(){
        return BlockTextures.blockTextures[getId()];
    }
}
