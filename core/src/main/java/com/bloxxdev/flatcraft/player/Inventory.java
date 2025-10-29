package com.bloxxdev.flatcraft.player;

import com.bloxxdev.flatcraft.items.Item;
import com.bloxxdev.flatcraft.items.ItemData;

public class Inventory {

    public ItemData[] items;

    public Inventory (int size){
        items = new ItemData[size];
        items[0] = new ItemData(Item.STONE, 1);
        items[1] = new ItemData(Item.GRASS, 1);
        items[2] = new ItemData(Item.DIRT, 1);
        items[3] = new ItemData(Item.BEDROCK, 1);
    }

}
