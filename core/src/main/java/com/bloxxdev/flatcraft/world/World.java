package com.bloxxdev.flatcraft.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bloxxdev.flatcraft.MainGameScreen;
import com.bloxxdev.flatcraft.world.worldgen.Worldgen;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;

public class World {

    private Chunk[] loadedChunks = new Chunk[3];

    private int[] chunkIDs = new int[3];

    private int playerChunk;

    File f0;
    File f1;
    File f2;

    public void loadWorld() {
        int currentPlayerChunk = (MainGameScreen.player.getX() < 0) ? ((int) MainGameScreen.player.getX() / 64)-1 : ((int) MainGameScreen.player.getX() / 64);

        chunkIDs[0] = currentPlayerChunk - 1;
        chunkIDs[1] = currentPlayerChunk;
        chunkIDs[2] = currentPlayerChunk + 1;

        if (f1 == null || playerChunk != chunkIDs[1]) {
            f0 = new File(MainGameScreen.chunkFolder.getPath() + "/" + chunkIDs[0] + ".ck");
            f1 = new File(MainGameScreen.chunkFolder.getPath() + "/" + chunkIDs[1] + ".ck");
            f2 = new File(MainGameScreen.chunkFolder.getPath() + "/" + chunkIDs[2] + ".ck");


            if (!f0.exists()) {
                Worldgen.generate(chunkIDs[0]);
            }
            if (!f1.exists()) {
                Worldgen.generate(chunkIDs[1]);
            }
            if (!f2.exists()) {
                Worldgen.generate(chunkIDs[2]);
            }

            readChunk(f0, chunkIDs[0], 0);
            readChunk(f1, chunkIDs[1], 1);
            readChunk(f2, chunkIDs[2], 2);
        }
        playerChunk = chunkIDs[1];

        for (int i = 0; i < 3; i++) {
            MainGameScreen.previouslyLoadedChunks.put(chunkIDs[i], loadedChunks[i]);
        }
    }

    private void readChunk(File chunkFile, int chunkID, int loadID){
        if (MainGameScreen.previouslyLoadedChunks.containsKey(chunkID)){
            loadedChunks[loadID] = MainGameScreen.previouslyLoadedChunks.get(chunkID);
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(chunkFile.toPath()))) {
            loadedChunks[loadID] = (Chunk) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return;
        }
    }

    public void checkNewChunks(){
        int currentPlayerChunk = (MainGameScreen.player.getX() < 0) ? ((int) MainGameScreen.player.getX() / 64)-1 : ((int) MainGameScreen.player.getX() / 64);
        int oldPlayerChunk = chunkIDs[1];

        if (currentPlayerChunk != oldPlayerChunk) {

            chunkIDs[0] = currentPlayerChunk - 1;
            chunkIDs[1] = currentPlayerChunk;
            chunkIDs[2] = currentPlayerChunk + 1;

            if (f1 == null || playerChunk != chunkIDs[1]) {
                f0 = new File(MainGameScreen.chunkFolder.getPath() + "/" + chunkIDs[0] + ".ck");
                f1 = new File(MainGameScreen.chunkFolder.getPath() + "/" + chunkIDs[1] + ".ck");
                f2 = new File(MainGameScreen.chunkFolder.getPath() + "/" + chunkIDs[2] + ".ck");
            }

            readChunk(f0, chunkIDs[0], 0);
            readChunk(f1, chunkIDs[1], 1);
            readChunk(f2, chunkIDs[2], 2);

        }
    }

    public void render(SpriteBatch batch){
        checkNewChunks();
        for (int i = -1; i < 2; i++) {
            if (loadedChunks[i+1] != null) {
                loadedChunks[i + 1].render(batch, chunkIDs[i + 1]);
            }
        }
    }

}
