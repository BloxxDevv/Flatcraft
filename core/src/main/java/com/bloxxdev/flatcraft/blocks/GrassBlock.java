package com.bloxxdev.flatcraft.blocks;

import com.bloxxdev.flatcraft.phys.AABB;

public class GrassBlock extends Block{
    public GrassBlock() {
        super(Block.GRASS);
    }

    @Override
    public AABB getHitbox(int x, int y) {
        return new AABB(x, y, 1, 1);
    }
}
