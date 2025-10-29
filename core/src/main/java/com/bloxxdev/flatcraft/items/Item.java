package com.bloxxdev.flatcraft.items;

import com.badlogic.gdx.graphics.Texture;
import com.bloxxdev.flatcraft.blocks.BlockTextures;

import java.util.HashSet;

public enum Item {

    DIRT,
    GRASS,
    STONE,
    BEDROCK;

    public static HashSet<Integer> validBlocks = new HashSet<Integer>();

    public int getId(){
        switch (this){
            case STONE:
                return 1;
            case GRASS:
                return 2;
            case DIRT:
                return 3;
            case BEDROCK:
                return 4;
            default:
                return 0;
        }
    }

    public Texture getTexture(){
        if (this == STONE){
            return BlockTextures.blockTextures[1];
        }
        if (this == GRASS){
            return BlockTextures.blockTextures[2];
        }
        if (this == DIRT){
            return BlockTextures.blockTextures[3];
        }
        if (this == BEDROCK) {
            return BlockTextures.blockTextures[4];
        }
        return null;
    }
}
