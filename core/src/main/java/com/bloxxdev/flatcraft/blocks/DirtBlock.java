package com.bloxxdev.flatcraft.blocks;

import com.bloxxdev.flatcraft.phys.AABB;

public class DirtBlock extends Block{
    public DirtBlock() {
        super(Block.DIRT);
    }

    @Override
    public AABB getHitbox(int x, int y) {
        return new AABB(x, y, 1, 1);
    }
}
