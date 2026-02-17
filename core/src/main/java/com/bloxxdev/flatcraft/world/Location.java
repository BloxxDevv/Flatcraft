package com.bloxxdev.flatcraft.world;

import com.bloxxdev.flatcraft.MainGameScreen;
import com.bloxxdev.flatcraft.blocks.Block;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;

public class Location {
    private int x;
    private int y;

    public Location(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Block getBlock(){
        if (y < 0)
            return null;

        int chunkID = x/64;
        if (x < 0){
            chunkID = -((Math.abs(x)-1)/64+1);
        }

        Chunk chunk;

        if (MainGameScreen.previouslyLoadedChunks.containsKey(chunkID)){
            chunk = MainGameScreen.previouslyLoadedChunks.get(chunkID);
        }else{
            File chunkFile = new File(MainGameScreen.chunkFolder.getPath() + "/" + chunkID + ".ck");
            chunk = getChunkData(chunkFile);
            MainGameScreen.previouslyLoadedChunks.put(chunkID, chunk);
        }

        if (chunk != null){
            int chunkX = x-chunkID*64;

            int sX = chunkX/16;
            int sY = y/16;

            Subchunk subchunk = chunk.subchunks[(sX * 16) + sY];

            if (subchunk != null){
                int subchunkX = chunkX-sX*16;
                int subchunkY = y-sY*16;

                return subchunk.blocks[(subchunkY * 16) + subchunkX];
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    public boolean setBlock(Block type){
        int chunkID = x/64;
        if (x < 0){
            chunkID = (x+1)/64-1;
        }

        Chunk chunk;

        if (MainGameScreen.previouslyLoadedChunks.containsKey(chunkID)){
            chunk = MainGameScreen.previouslyLoadedChunks.get(chunkID);
        }else{
            File chunkFile = new File(MainGameScreen.chunkFolder.getPath() + "/" + chunkID + ".ck");
            chunk = getChunkData(chunkFile);
            MainGameScreen.previouslyLoadedChunks.put(chunkID, chunk);
        }

        if (chunk != null){
            int chunkX = x-chunkID*64;

            int sX = chunkX/16;
            int sY = y/16;

            Subchunk subchunk = chunk.subchunks[(sX * 16) + sY];

            if (subchunk != null){
                int subchunkX = chunkX-sX*16;
                int subchunkY = y-sY*16;

                subchunk.blocks[(subchunkY * 16) + subchunkX] = type;
                try {
                    chunk.updateSaveFile(chunkID);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                MainGameScreen.previouslyLoadedChunks.put(chunkID, chunk);
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    private Chunk getChunkData(File chunkFile){
        if (chunkFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(chunkFile.toPath()))) {
                return (Chunk) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        return this.getX() == ((Location)obj).getX() && this.getY() == ((Location)obj).getY();
    }
}
