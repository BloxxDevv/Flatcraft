package com.bloxxdev.flatcraft.world.worldgen;

import com.bloxxdev.flatcraft.MainGameScreen;
import com.bloxxdev.flatcraft.blocks.Block;
import com.bloxxdev.flatcraft.blocks.DirtBlock;
import com.bloxxdev.flatcraft.blocks.GrassBlock;
import com.bloxxdev.flatcraft.world.Chunk;
import com.bloxxdev.flatcraft.world.Subchunk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Worldgen {

    public static double leftGradient;
    public static double rightGradient;

    //Speed in which the circles shrink
    private static final double CARVE_SPEED = 0.05;

    private static int[] rootTable = new int[50];
    private static Random random = new Random();

    public static HashMap<Integer, Chunk> dirtyChunks = new HashMap<>();

    private static ArrayList<CaveData> leftPoints = new ArrayList<>();
    private static ArrayList<CaveData> rightPoints = new ArrayList<>();

    public static void genRootTable(){
        for (int i = 0; i < 50; i++) {
            rootTable[i] = (int) Math.floor(Math.sqrt(i));
        }
    }

    public static void generate(int id){
        int[] heightmap = new int[64];
        int[] rockmap = new int[64];
        int[] rockmap2 = new int[64];
        int sign = (id == 0) ? 0 : id / Math.abs(id);

        PerlinNoise.generateOutputArray(heightmap, 2, 16, sign, 65);
        PerlinNoise.generateOutputArray(rockmap, 2, 16, sign, 60);
        PerlinNoise.generateOutputArray(rockmap2, 2, 16, sign, 60);

        Chunk chunk = new Chunk();

        for (int sx = 0; sx < 4; sx++) {
            for (int sy = 0; sy < 16; sy++) {
                Subchunk sc = new Subchunk();

                for (int x = 0; x < 16; x++) {
                    for (int y = 0; y < 16; y++) {
                        Block b = null;
                        if (heightmap[x + sx*16] >= y + sy * 16){
                            if (y == 0 && sy == 0) {
                                b = new Block(Block.BEDROCK);
                            }else{
                                if (heightmap[x + sx * 16] == y + sy * 16) {
                                    b = new GrassBlock();
                                } else if ((heightmap[x + sx * 16] > y + sy * 16) &&
                                    (Math.max(Math.min(Math.min(rockmap[x + sx * 16], rockmap2[x + sx * 16]), heightmap[x + sx * 16] - 3), heightmap[x + sx * 16] - 6)) < (y + sy * 16)) {
                                    b = new DirtBlock();
                                } else {
                                    b = new Block(Block.STONE);
                                }
                            }
                        }

                        sc.blocks[(y * 16) + x] = b;
                    }
                }

                chunk.subchunks[(sx * 16) + sy] = sc;
            }
        }
        try{
            chunk.updateSaveFile(id);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void genCaves(int id){
        System.out.println(id);
        int ox = random.nextInt(64)+id*64;
        int oy = random.nextInt(40)+11;
        int size = 12;
        int x = ox;
        int y = oy;

        System.out.println("x = " + x + "\ny = " + y);

        if(id == 0) {
            while (size > 3) {
                genCircle(x, y, size);

                if (x % 64 == 0) {
                    continueCaves(x - 1, y, size, true, false);
                    break;
                }

                x = x - 1;
                y = random.nextInt(3) - 1 + y;

                size = Math.random() > (1 - CARVE_SPEED) ? size - 2 : size;
            }
            size = 12;

            x = ox + 1;
            y = oy;

            while (size > 3) {
                genCircle(x, y, size);

                if (x % 64 == 0) {
                    continueCaves(x + 1, y, size, false, false);
                    break;
                }

                x = x + 1;
                y = random.nextInt(3) - 1 + y;

                size = Math.random() > (1 - CARVE_SPEED) ? size - 2 : size;
            }
        }else if(id < 0){
            size = 4;

            while (size < 13){
                genCircle(x, y, size);

                if (x % 64 == 0) {
                    continueCaves(x - 1, y, size, true, true);
                    break;
                }

                x = x - 1;
                y = random.nextInt(3) - 1 + y;

                size = Math.random() > (1 - CARVE_SPEED) ? size + 2 : size;
            }

            if (size > 12) {
                size = 12;

                while (size > 3) {
                    genCircle(x, y, size);

                    if (x % 64 == 0) {
                        continueCaves(x - 1, y, size, true, false);
                        break;
                    }

                    x = x - 1;
                    y = random.nextInt(3) - 1 + y;

                    size = Math.random() > (1 - CARVE_SPEED) ? size - 2 : size;
                }
            }
        }else{
            size = 4;

            while (size < 13){
                genCircle(x, y, size);

                if (x % 64 == 0) {
                    continueCaves(x + 1, y, size, false, true);
                    break;
                }

                x = x + 1;
                y = random.nextInt(3) - 1 + y;

                size = Math.random() > (1 - CARVE_SPEED) ? size + 2 : size;
            }

            if (size > 12) {
                size = 12;

                while (size > 3) {
                    genCircle(x, y, size);

                    if (x % 64 == 0) {
                        continueCaves(x + 1, y, size, false, false);
                        break;
                    }

                    x = x + 1;
                    y = random.nextInt(3) - 1 + y;

                    size = Math.random() > (1 - CARVE_SPEED) ? size - 2 : size;
                }
            }
        }
    }

    //Side: true = left, false = right
    public static void finishCaves(boolean side){
        if (side){
            System.out.println("left Points: " + leftPoints);
            for (Object cd : leftPoints.toArray()){
                continueCaves(((CaveData)cd).x, ((CaveData)cd).y, ((CaveData)cd).size, true, ((CaveData) cd).grow);
                leftPoints.remove(cd);
            }
        }else {
            System.out.println("right Points: " + rightPoints);
            for (Object cd : rightPoints.toArray()){
                continueCaves(((CaveData)cd).x, ((CaveData)cd).y, ((CaveData)cd).size, false, ((CaveData) cd).grow);
                rightPoints.remove(cd);
            }
        }
    }

    //Side: true = left, false = right
    private static void continueCaves(int x, int y, int size, boolean side, boolean grow){
        int chunkid = (x < 0) ? (x / 64)-1 : (x / 64);

        if (!grow) {
            if (side && new File(MainGameScreen.chunkFolder.getPath() + "/" + (chunkid - 1) + ".ck").exists()) {
                while (size > 3) {
                    genCircle(x, y, size);

                    if (x % 64 == 0) {
                        continueCaves(x - 1, y, size, true, false);
                        break;
                    }

                    x = x - 1;
                    y = random.nextInt(3) - 1 + y;

                    size = Math.random() > (1 - CARVE_SPEED) ? size - 2 : size;
                }
            } else if (!side && new File(MainGameScreen.chunkFolder.getPath() + "/" + (chunkid + 1) + ".ck").exists()) {
                while (size > 3) {
                    genCircle(x, y, size);

                    if (x % 64 == 0) {
                        continueCaves(x + 1, y, size, false, false);
                        break;
                    }

                    x = x + 1;
                    y = random.nextInt(3) - 1 + y;

                    size = Math.random() > (1 - CARVE_SPEED) ? size - 2 : size;
                }
            } else {
                if (side) {
                    leftPoints.add(new CaveData(x, y, size, false));
                } else {
                    rightPoints.add(new CaveData(x, y, size, false));
                }
            }
        }else{
            if (side && new File(MainGameScreen.chunkFolder.getPath() + "/" + (chunkid - 1) + ".ck").exists()) {
                while (size < 13) {
                    genCircle(x, y, size);

                    if (x % 64 == 0) {
                        continueCaves(x - 1, y, size, true, true);
                        break;
                    }

                    x = x - 1;
                    y = random.nextInt(3) - 1 + y;

                    size = Math.random() > (1 - CARVE_SPEED) ? size + 2 : size;
                }
                if (size > 12) {
                    continueCaves(x, y, 12, true, false);
                }
            } else if (!side && new File(MainGameScreen.chunkFolder.getPath() + "/" + (chunkid + 1) + ".ck").exists()) {
                while (size < 13) {
                    genCircle(x, y, size);

                    if (x % 64 == 0) {
                        continueCaves(x + 1, y, size, false, true);
                        break;
                    }

                    x = x + 1;
                    y = random.nextInt(3) - 1 + y;

                    size = Math.random() > (1 - CARVE_SPEED) ? size + 2 : size;
                }
                if (size > 12) {
                    continueCaves(x, y, 12, false, false);
                }
            } else {
                if (side) {
                    leftPoints.add(new CaveData(x, y, size, true));
                } else {
                    rightPoints.add(new CaveData(x, y, size, true));
                }
            }
        }
    }

    private static void genCircle(int x, int y, int size){
        int scanRange = rootTable[size];

        for (int i = x-scanRange; i <= x+scanRange; i++) {
            for (int j = y-scanRange; j <= y+scanRange; j++) {
                if (((x-i)*(x-i) + (y-j)*(y-j)) < size){
                    setBlockNoSave(i, j, null);
                }
            }
        }
    }

    private static void setBlockNoSave(int x, int y, Block type){
        int chunkID = x/64;
        if (x < 0){
            chunkID = (x+1)/64-1;
        }

        Chunk chunk;

        if (MainGameScreen.previouslyLoadedChunks.containsKey(chunkID)){
            chunk = MainGameScreen.previouslyLoadedChunks.get(chunkID);
        }else{
            File chunkFile = new File(MainGameScreen.chunkFolder.getPath() + "/" + chunkID + ".ck");
            chunk = Chunk.fromFile(chunkFile);
            MainGameScreen.previouslyLoadedChunks.put(chunkID, chunk);
        }

        if (chunk != null) {
            int chunkX = x - chunkID * 64;

            int sX = chunkX / 16;
            int sY = y / 16;

            Subchunk subchunk = chunk.subchunks[(sX * 16) + sY];

            if (subchunk != null) {
                int subchunkX = chunkX - sX * 16;
                int subchunkY = y - sY * 16;

                if (subchunkY >= 0)
                    subchunk.blocks[(subchunkY * 16) + subchunkX] = type;
            }
        }
        dirtyChunks.put(chunkID, chunk);
    }

    static class CaveData{
        public int x;
        public int y;
        public int size;
        public boolean grow;

        public CaveData(int x, int y, int size, boolean grow){
            this.x=x;
            this.y=y;
            this.size=size;
            this.grow=grow;
        }

        @Override
        public String toString() {
            return "x=" + x + " y=" + y + " size=" + size + " grow=" + grow;
        }
    }
}

