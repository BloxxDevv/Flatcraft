package com.bloxxdev.flatcraft.items;

import com.bloxxdev.flatcraft.blocks.Block;

public class ItemData {

    private Item type;
    private int amount;

    public ItemData(Item type, int amount){

        this.type = type;
        this.amount = amount;

    }

    public boolean isBlock(){
        return Block.blockIDs.contains(getType().getId());
    }

    public Item getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }
}
