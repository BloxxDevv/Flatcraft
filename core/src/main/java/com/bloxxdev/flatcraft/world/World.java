package com.bloxxdev.flatcraft.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bloxxdev.flatcraft.MainGameScreen;
import com.bloxxdev.flatcraft.world.worldgen.Worldgen;

import java.io.*;
import java.nio.file.Files;

public class World {

    private Chunk[] loadedChunks = new Chunk[5];

    private int[] chunkIDs = new int[5];

    private int playerChunk;

    public static int[] maxChunks = new int[2];

    File f0;
    File f1;
    File f2;
    File f3;
    File f4;

    File worlddata = new File(MainGameScreen.worldFolder.getPath() + "/world.dat");

    public void loadWorld() {
        int currentPlayerChunk = (MainGameScreen.player.getX() < 0) ? ((int) MainGameScreen.player.getX() / 64)-1 : ((int) MainGameScreen.player.getX() / 64);

        if (maxChunks[0] == 0){
                try {
                    ObjectInputStream ois;
                    ois = new ObjectInputStream(new FileInputStream(worlddata));
                    maxChunks = ((int[])ois.readObject());
                    ois.close();
                }catch (FileNotFoundException e){
                    maxChunks[0] = -1;
                    maxChunks[1] = 1;
                    try {
                        updateMaxChunks();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }catch (ClassNotFoundException e) {}
        }

        chunkIDs[0] = currentPlayerChunk - 2;
        chunkIDs[1] = currentPlayerChunk - 1;
        chunkIDs[2] = currentPlayerChunk;
        chunkIDs[3] = currentPlayerChunk + 1;
        chunkIDs[4] = currentPlayerChunk + 2;

        if (f2 == null || playerChunk != chunkIDs[2]) {
            f0 = new File(MainGameScreen.chunkFolder.getPath() + "/" + chunkIDs[0] + ".ck");
            f1 = new File(MainGameScreen.chunkFolder.getPath() + "/" + chunkIDs[1] + ".ck");
            f2 = new File(MainGameScreen.chunkFolder.getPath() + "/" + chunkIDs[2] + ".ck");
            f3 = new File(MainGameScreen.chunkFolder.getPath() + "/" + chunkIDs[3] + ".ck");
            f4 = new File(MainGameScreen.chunkFolder.getPath() + "/" + chunkIDs[4] + ".ck");

            if (!f0.exists()) {
                Worldgen.generate(chunkIDs[0]);
            }
            if (!f1.exists()) {
                Worldgen.generate(chunkIDs[1]);
            }
            if (!f3.exists()) {
                Worldgen.generate(chunkIDs[3]);
            }
            if (!f4.exists()) {
                Worldgen.generate(chunkIDs[4]);
            }
            if (!f2.exists()) {
                Worldgen.generate(chunkIDs[2]);

                Worldgen.genTrees(chunkIDs[2]);
                Worldgen.genTrees(chunkIDs[1]);
                Worldgen.genTrees(chunkIDs[3]);

                Worldgen.genCaves(chunkIDs[2]);
                Worldgen.genCaves(chunkIDs[1]);
                Worldgen.genCaves(chunkIDs[3]);
            }

            if (currentPlayerChunk - 1 < maxChunks[0]) {
                maxChunks[0] = currentPlayerChunk - 1;
                try {
                    updateMaxChunks();
                    Worldgen.genTrees(maxChunks[0]);
                    Worldgen.finishCaves(true);
                    Worldgen.genCaves(maxChunks[0]);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (currentPlayerChunk + 1 > maxChunks[1]) {
                maxChunks[1] = currentPlayerChunk + 1;
                try {
                    updateMaxChunks();
                    Worldgen.genTrees(maxChunks[1]);
                    Worldgen.finishCaves(false);
                    Worldgen.genCaves(maxChunks[1]);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if (!Worldgen.dirtyChunks.isEmpty()) {

                for (Object i : Worldgen.dirtyChunks.keySet().toArray()) {
                    try {
                        if (Worldgen.dirtyChunks.get((int) i) != null) {
                            Worldgen.dirtyChunks.get((int) i).updateSaveFile((int) i);
                            Worldgen.dirtyChunks.remove((int) i);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            }

            readChunk(f0, chunkIDs[0], 0);
            readChunk(f1, chunkIDs[1], 1);
            readChunk(f2, chunkIDs[2], 2);
            readChunk(f3, chunkIDs[3], 3);
            readChunk(f4, chunkIDs[4], 4);

            playerChunk = chunkIDs[2];

            for (int i = 0; i < 5; i++) {
                MainGameScreen.previouslyLoadedChunks.put(chunkIDs[i], loadedChunks[i]);
            }

        }else {
            for (int i = 0; i < 5; i++) {
                MainGameScreen.previouslyLoadedChunks.put(chunkIDs[i], loadedChunks[i]);
            }
        }
    }

    private void updateMaxChunks() throws IOException {
        ObjectOutputStream oos;
        oos = new ObjectOutputStream(Files.newOutputStream(worlddata.toPath()));
        oos.writeObject(maxChunks);
        oos.close();
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
        int oldPlayerChunk = chunkIDs[2];

        if (currentPlayerChunk != oldPlayerChunk) {

            chunkIDs[0] = currentPlayerChunk - 2;
            chunkIDs[1] = currentPlayerChunk - 1;
            chunkIDs[2] = currentPlayerChunk;
            chunkIDs[3] = currentPlayerChunk + 1;
            chunkIDs[4] = currentPlayerChunk + 2;

            if (f2 == null || playerChunk != chunkIDs[2]) {
                f0 = new File(MainGameScreen.chunkFolder.getPath() + "/" + chunkIDs[0] + ".ck");
                f1 = new File(MainGameScreen.chunkFolder.getPath() + "/" + chunkIDs[1] + ".ck");
                f2 = new File(MainGameScreen.chunkFolder.getPath() + "/" + chunkIDs[2] + ".ck");
                f3 = new File(MainGameScreen.chunkFolder.getPath() + "/" + chunkIDs[3] + ".ck");
                f4 = new File(MainGameScreen.chunkFolder.getPath() + "/" + chunkIDs[4] + ".ck");
            }

            readChunk(f0, chunkIDs[0], 0);
            readChunk(f1, chunkIDs[1], 1);
            readChunk(f2, chunkIDs[2], 2);
            readChunk(f3, chunkIDs[3], 3);
            readChunk(f4, chunkIDs[4], 4);

        }
    }

    public void render(SpriteBatch batch){
        checkNewChunks();
        for (int i = 0; i < 3; i++) {
            if (loadedChunks[i+1] != null) {
                loadedChunks[i+1].render(batch, chunkIDs[i + 1]);
            }
        }
    }

}
