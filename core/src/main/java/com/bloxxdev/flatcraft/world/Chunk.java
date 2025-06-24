package com.bloxxdev.flatcraft.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bloxxdev.flatcraft.MainGameScreen;

import java.io.*;

public class Chunk implements Serializable{

    //(sx * 16) + sy
    public Subchunk[] subchunks = new Subchunk[4*16];

    public void updateSaveFile(int id) throws IOException {
        File f = new File(MainGameScreen.chunkFolder.getPath() + "/" + id + ".ck");

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))){
            oos.writeObject(this);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void render(SpriteBatch batch, int id){
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 16; y++) {
                if (subchunks[(x * 16) + y] != null) {
                    subchunks[(x * 16) + y].render(batch, id*4+x, y);
                }
            }
        }
    }
}
