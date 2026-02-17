package com.bloxxdev.flatcraft.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bloxxdev.flatcraft.Main;
import com.bloxxdev.flatcraft.MainGameScreen;
import com.bloxxdev.flatcraft.blocks.Block;
import com.bloxxdev.flatcraft.gui.Hotbar;
import com.bloxxdev.flatcraft.items.ItemData;
import com.bloxxdev.flatcraft.phys.AABB;
import com.bloxxdev.flatcraft.world.Location;

public class Player {

    public static boolean sprinting;

    public static final int WIDTH = 12;
    public static final int HEIGHT = 30;

    private boolean onGround = false;

    public AABB hitbox;

    public Inventory playerInventory;

    //Top Left Pos
    private float x;
    private float y;

    private int displayX = Gdx.graphics.getWidth()/2 - WIDTH*Main.DEFAULT_SCALE/2;
    private int displayY = Gdx.graphics.getHeight()/2 + HEIGHT*Main.DEFAULT_SCALE/2;

    SpriteBatch batch = new SpriteBatch();

    private Texture head = new Texture("player/head.png");
    private Texture face1 = new Texture("player/face1.png");
    private Texture face2 = new Texture("player/face2.png");
    private Texture face3 = new Texture("player/face3.png");
    private Texture hair1 = new Texture("player/hair1.png");
    private Texture hair2 = new Texture("player/hair2.png");
    private Texture hair3 = new Texture("player/hair3.png");
    private Texture body = new Texture("player/body.png");
    private Texture arm = new Texture("player/arm.png");
    private Texture hand = new Texture("player/hand.png");
    private Texture leg = new Texture("player/leg.png");

    private TextureRegion leftArm = new TextureRegion(arm);
    private TextureRegion rightArm = new TextureRegion(arm);

    private TextureRegion leftHand = new TextureRegion(hand);
    private TextureRegion rightHand = new TextureRegion(hand);

    private TextureRegion leftLeg = new TextureRegion(leg);
    private TextureRegion rightLeg = new TextureRegion(leg);

    private boolean avoiding = false;

    public int mX = 0;
    public float mY = 0;

    private Hotbar hotbar;

    public Player(){
        x = 0;
        y = 70;
        leftArm.flip(true, false);
        leftHand.flip(true, false);
        leftLeg.flip(true, false);

        this.hitbox = new AABB(x, y, (float)WIDTH/16, (float)HEIGHT/16);

        this.playerInventory = new Inventory(9);
    }

    public void init(){
        hotbar = MainGameScreen.hotbar;
    }

    public Inventory getPlayerInventory() {
        return playerInventory;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
    /**
     *
     * @return LEFT DISPLAY COORD OF PLAYER
     */
    public int getDisplayX() {
        return displayX;
    }

    /**
     *
     * @return TOP DISPLAY COORD OF PLAYER
     */
    public int getDisplayY() {
        return displayY;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setAvoiding(boolean avoiding) {
        this.avoiding = avoiding;
    }

    public boolean isAvoiding() {
        return avoiding;
    }

    private void move(){
        mY-=0.1F;
        float xa;

        if (!sprinting){
            xa = 0.1F*mX;
        }else{
            xa = 0.2F*mX;
        }

        float ya = 0.1F*mY;

        int checkR = 2;

        for (int cX = (int)x-checkR; cX <= (int)x+1+checkR; cX++) {
            for (int cY = (int)y-checkR; cY <= (int)y+2+checkR; cY++) {
                if (cY >= 0 && cY < 256) {
                    Block b = new Location(cX, cY).getBlock();
                    if (b != null) {
                        AABB blockBB = b.getHitbox(cX, cY);

                        if (blockBB != null) {
                            float adjxa = hitbox.adjustXMovement(xa, blockBB);
                            if (Math.abs(adjxa) < Math.abs(xa)) {
                                xa = adjxa;
                            }
                        }

                    }
                }
            }
        }

        x += xa;
        updateBB();

        for (int cX = (int)x-checkR; cX <= (int)x+1+checkR; cX++) {
            for (int cY = (int)y-checkR; cY <= (int)y+2+checkR; cY++) {
                if (cY >= 0 && cY < 256) {
                    Block b = new Location(cX, cY).getBlock();
                    if (b != null) {
                        AABB blockBB = b.getHitbox(cX, cY);

                        if (blockBB != null) {
                            float adjya = hitbox.adjustYMovement(ya, blockBB);
                            if (Math.abs(adjya) < Math.abs(ya)) {
                                ya = adjya;
                            }
                        }
                    }
                }
            }
        }
        if (ya == 0){
            mY = 0;
            if ((y - (int)y) < 0.01F){
                onGround = true;
            }
        }else{
            onGround = false;
        }

        y += ya;
        updateBB();
    }

    public void tick(){
        move();
    }

    public void render(){
        batch.begin();

        batch.draw(head, displayX+2*Main.DEFAULT_SCALE, displayY-head.getHeight()*Main.DEFAULT_SCALE, head.getWidth()*4, head.getHeight()*4);
        batch.draw(face2, displayX+2* Main.DEFAULT_SCALE, displayY-face1.getHeight()*Main.DEFAULT_SCALE, face1.getWidth()*4, face1.getHeight()*4);

        batch.setColor(101/255F, 67/255F, 33/255F, 1);
        batch.draw(hair3, displayX, displayY-Main.DEFAULT_SCALE*hair1.getHeight()+8*Main.DEFAULT_SCALE, hair1.getWidth()*4, hair1.getHeight()*4);
        batch.setColor(1, 1, 1, 1);

        batch.setColor(0/255F, 0/255F, 255/255F, 1);
        batch.draw(body, displayX+2*Main.DEFAULT_SCALE, displayY-Main.DEFAULT_SCALE*body.getHeight()-8*Main.DEFAULT_SCALE, body.getWidth()*4, body.getHeight()*4);

        batch.draw(leftArm, displayX, displayY-Main.DEFAULT_SCALE*arm.getHeight()-8*Main.DEFAULT_SCALE, arm.getWidth()*4, arm.getHeight()*4);
        batch.draw(rightArm, displayX+body.getWidth()*Main.DEFAULT_SCALE, displayY-Main.DEFAULT_SCALE*arm.getHeight()-8*Main.DEFAULT_SCALE, arm.getWidth()*4, arm.getHeight()*4);
        batch.setColor(1, 1, 1, 1);
        batch.draw(leftHand, displayX, displayY-Main.DEFAULT_SCALE*arm.getHeight()-8*Main.DEFAULT_SCALE, hand.getWidth()*4, hand.getHeight()*4);
        batch.draw(rightHand, displayX+body.getWidth()*Main.DEFAULT_SCALE, displayY-Main.DEFAULT_SCALE*arm.getHeight()-8*Main.DEFAULT_SCALE, hand.getWidth()*4, hand.getHeight()*4);

        batch.setColor(0, 1, 0.2F, 1);
        batch.draw(leftLeg, displayX, displayY-12*Main.DEFAULT_SCALE-Main.DEFAULT_SCALE*leg.getHeight()-8*Main.DEFAULT_SCALE, leg.getWidth()*4, leg.getHeight()*4);
        batch.draw(rightLeg, displayX+Main.DEFAULT_SCALE*leg.getWidth(), displayY-12*Main.DEFAULT_SCALE-Main.DEFAULT_SCALE*leg.getHeight()-8*Main.DEFAULT_SCALE, leg.getWidth()*4, leg.getHeight()*4);
        batch.setColor(1, 1, 1, 1);

        batch.end();
    }

    public ItemData getItemInHand(){
        return playerInventory.items[hotbar.getSelection()];
    }

    public Hotbar getHotbar() {
        return hotbar;
    }

    private void updateBB(){
        hitbox.setPos(x, y);
    }

}
