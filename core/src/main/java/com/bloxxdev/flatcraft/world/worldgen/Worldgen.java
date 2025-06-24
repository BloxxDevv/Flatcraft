package com.bloxxdev.flatcraft.world.worldgen;

import com.bloxxdev.flatcraft.blocks.Block;
import com.bloxxdev.flatcraft.blocks.DirtBlock;
import com.bloxxdev.flatcraft.blocks.GrassBlock;
import com.bloxxdev.flatcraft.world.Chunk;
import com.bloxxdev.flatcraft.world.Subchunk;

import java.io.IOException;

public class Worldgen {

    public static double leftGradient;
    public static double rightGradient;

    public static void generate(int id){
        int[] heightmap = new int[64];
        int[] rockmap = new int[64];
        int[] rockmap2 = new int[64];
        int sign = (id == 0) ? 0 : id / Math.abs(id);

        PerlinNoise.generateOutputArray(heightmap, 2, 16, sign, 50);
        PerlinNoise.generateOutputArray(rockmap, 2, 16, sign, 45);
        PerlinNoise.generateOutputArray(rockmap2, 2, 16, sign, 45);

        Chunk chunk = new Chunk();

        for (int sx = 0; sx < 4; sx++) {
            for (int sy = 0; sy < 16; sy++) {
                Subchunk sc = new Subchunk();

                for (int x = 0; x < 16; x++) {
                    for (int y = 0; y < 16; y++) {
                        Block b = null;
                        if (heightmap[x + sx*16] >= y + sy * 16){
                            if (heightmap[x + sx*16] == y + sy * 16){
                                b = new GrassBlock();
                            }else if ((heightmap[x + sx*16] > y + sy * 16) &&
                                (Math.max(Math.min(Math.min(rockmap[x + sx * 16], rockmap2[x + sx * 16]), heightmap[x + sx * 16]-3), heightmap[x + sx * 16]-6)) < (y + sy * 16)){
                                b = new DirtBlock();
                            }else {
                                b = new Block(Block.STONE);
                            }
                        }

                        sc.blocks[(y * 16) + x] = b;
                    }
                }

                chunk.subchunks[(sx * 16) + sy] = sc;
            }
        }

        try {
            chunk.updateSaveFile(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
