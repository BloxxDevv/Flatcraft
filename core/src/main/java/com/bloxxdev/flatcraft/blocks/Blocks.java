package com.bloxxdev.flatcraft.blocks;

import com.bloxxdev.flatcraft.items.Item;

public enum Blocks {
    STONE,
    GRASS,
    DIRT,
    BEDROCK,
    OAK_LEAVES,
    OAK_LOG(Property.AXIS);

    public Property[] properties;

    Blocks(Property... properties){
        this.properties = properties;
    }

    public int getId(){
        return Item.valueOf(this.name()).getId();
    }
}
