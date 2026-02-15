package com.bloxxdev.flatcraft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.bloxxdev.flatcraft.blocks.Block;
import com.bloxxdev.flatcraft.blocks.Blocks;
import com.bloxxdev.flatcraft.blocks.Property;
import com.bloxxdev.flatcraft.gui.Hotbar;
import com.bloxxdev.flatcraft.player.Player;
import com.bloxxdev.flatcraft.player.PlayerInputHandler;
import com.bloxxdev.flatcraft.world.Chunk;
import com.bloxxdev.flatcraft.world.Location;
import com.bloxxdev.flatcraft.world.World;
import com.bloxxdev.flatcraft.world.worldgen.Worldgen;

import java.io.File;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Deflater;

/** First screen of the application. Displayed after the application is created. */
public class MainGameScreen implements Screen {

    public static final float MAX_BLOCK_INTERACT_RADIUS = 4;
    public static final float MAX_BLOCK_INTERACT_RADIUS_SQ = MAX_BLOCK_INTERACT_RADIUS*MAX_BLOCK_INTERACT_RADIUS;

    public static AdvancedLocation selection = null;

    public static HashMap<Integer, Chunk> previouslyLoadedChunks = new HashMap<>();

    public static Player player;
    public static Hotbar hotbar;

    public static World world;

    Runnable loadWorld;

    SpriteBatch batch = new SpriteBatch();
    public static ShapeRenderer shapeRenderer = new ShapeRenderer();

    public static File worldFolder = new File(System.getenv("APPDATA") + "/.flatcraft/world");
    public static File chunkFolder = new File(System.getenv("APPDATA") + "/.flatcraft/world/chunks");

    public static final int MAX_ID = 3;

    @Override
    public void show() {
        if (!chunkFolder.exists()){
            chunkFolder.mkdirs();
        }

        world = new World();
        player = new Player();
        hotbar = new Hotbar();

        Worldgen.genRootTable();

        hotbar.show();
        Gdx.input.setInputProcessor(new PlayerInputHandler(hotbar));

        player.init();

        world.loadWorld();

        loadWorld = world::loadWorld;
    }

    boolean a = false;
    boolean d = false;

    private void checkPlayerKeys() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (!a) {
                player.mX -= 1;
                a = true;
            }
        } else {
            if (a) {
                player.mX += 1;
                a = false;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (!d) {
                player.mX += 1;
                d = true;
            }
        } else {
            if (d) {
                player.mX -= 1;
                d = false;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (player.isOnGround()){
                player.mY = 1.75F;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)){
            Player.sprinting = true;
        }else if (!Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)){
            Player.sprinting = false;
        }
    }

    BitmapFont debugFont = new BitmapFont();

    private class AdvancedLocation{
        public Location loc;
        public char axis;

        public AdvancedLocation(Location loc, char axis){
            this.loc = loc;
            this.axis = axis;
        }
    }

    private AdvancedLocation findIntersectLoc(float mouseX, float mouseY) {
        float originX = player.getDisplayX() + (Main.DEFAULT_SCALE / 2F) * Player.WIDTH;
        float originY = player.getDisplayY() - (Main.DEFAULT_SCALE / 2F) * Player.HEIGHT;

        float mouseDistWorld = (float) Math.sqrt((mouseX - originX) * (mouseX - originX) + (mouseY - originY) * (mouseY - originY)) / (16 * Main.DEFAULT_SCALE);

        float originWorldX = player.getX() + (Player.WIDTH / 16F) / 2F;
        float originWorldY = player.getY() + (Player.HEIGHT / 16F) / 2F;

        //DEBUG LINES
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.RED);
//        shapeRenderer.line(originX, originY, mouseX, mouseY);
//        shapeRenderer.setColor(Color.WHITE);
//        shapeRenderer.end();

        float slopeX = (mouseY - originY) / (mouseX - originX);
        float slopeY = (mouseX - originX) / (mouseY - originY);

        float gridDistX, gridDistY;

        //Calculate initial x distance from player to the next grid cell
        if (mouseX < originX) {
            gridDistX = (float) Math.floor(originWorldX) - originWorldX;
        } else {
            gridDistX = (float) Math.ceil(originWorldX) - originWorldX;
        }

        //Calculate initial y distance from player to the next grid cell
        if (mouseY < originY) {
            gridDistY = (float) Math.floor(originWorldY) - originWorldY;
        } else {
            gridDistY = (float) Math.ceil(originWorldY) - originWorldY;
        }

        //Distance to the intersections
        double distToXInt = 0;
        double distToYInt = 0;

        int count = 0;

        //as long as the distance to any next intersection is less than the dist to the cursor, scan
        while (distToXInt < mouseDistWorld || distToYInt < mouseDistWorld){
            //Calculate the distance to the next intersection point on X and Y
            distToXInt = Math.sqrt(gridDistY * gridDistY + gridDistY * slopeY * gridDistY * slopeY);
            distToYInt = Math.sqrt(gridDistX * gridDistX + gridDistX * slopeX * gridDistX * slopeX);

            //if intersect on the x axis is closer
            if (distToXInt < distToYInt){
                float xcoord = originWorldX+gridDistY*slopeY;
                float ycoord = originWorldY+gridDistY;
                if (mouseY < originY){
                    ycoord--;
                }
                Location loc = new Location((int)Math.floor(xcoord), (int)Math.floor(ycoord));
                if (loc.getBlock() != null){
                    return new AdvancedLocation(loc, ' ');
                }else if (loc.equals(getLocAtScreen(mouseX, mouseY))){
                    if (gridDistY > 0) {
                        gridDistY++;
                    }else{
                        gridDistY--;
                    }

                    distToXInt = Math.sqrt(gridDistY * gridDistY + gridDistY * slopeY * gridDistY * slopeY);
                    distToYInt = Math.sqrt(gridDistX * gridDistX + gridDistX * slopeX * gridDistX * slopeX);

                    if (distToXInt < distToYInt){
                        return new AdvancedLocation(null, 'y');
                    }else{
                        return new AdvancedLocation(null, 'x');
                    }
                }
                if (gridDistY > 0) {
                    gridDistY++;
                }else{
                    gridDistY--;
                }
            //if intersect on the y axis is closer
            }else{
                float xcoord = originWorldX+gridDistX;
                float ycoord = originWorldY+gridDistX*slopeX;
                if (mouseX < originX){
                    xcoord--;
                }
                //Get the grid point and its data and check if there is a block if yes return
                Location loc = new Location((int)Math.floor(xcoord), (int)Math.floor(ycoord));
                if (loc.getBlock() != null){
                    return new AdvancedLocation(loc, ' ');
                }else if (loc.equals(getLocAtScreen(mouseX, mouseY))){
                    if (gridDistX > 0) {
                        gridDistX++;
                    }else{
                        gridDistX--;
                    }

                    distToXInt = Math.sqrt(gridDistY * gridDistY + gridDistY * slopeY * gridDistY * slopeY);
                    distToYInt = Math.sqrt(gridDistX * gridDistX + gridDistX * slopeX * gridDistX * slopeX);

                    if (distToXInt < distToYInt){
                        return new AdvancedLocation(null, 'y');
                    }else{
                        return new AdvancedLocation(null, 'x');
                    }
                }
                if (gridDistX > 0) {
                    gridDistX++;
                }else{
                    gridDistX--;
                }
            }
        }

        return null;
    }

    private Block blockAt(int x, int y){
        return new Location(x, y).getBlock();
    }

    private boolean hasSupportBlock(Location loc){
        int x = loc.getX();
        int y = loc.getY();

        return
            blockAt(x-1, y) != null ||
            blockAt(x+1, y) != null ||
                (y > 0) && blockAt(x, y-1) != null ||
            blockAt(x, y+1) != null;
    }

    private Location getLocAtScreen(float x, float y){
        float xDist = x - player.getDisplayX();
        float yDist = y - player.getDisplayY()+Player.HEIGHT*Main.DEFAULT_SCALE;

        float xBlockDist = xDist/(16*Main.DEFAULT_SCALE);
        float yBlockDist = yDist/(16*Main.DEFAULT_SCALE);

        float xCoord = player.getX()+xBlockDist;
        float yCoord = player.getY()+yBlockDist;

        return new Location((int) Math.floor(xCoord), (int) Math.floor(yCoord));
    }

    private void setBlockPick(){
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        float xDist = mouseX - player.getDisplayX();
        float yDist = mouseY - player.getDisplayY()+Player.HEIGHT*Main.DEFAULT_SCALE;

        float xBlockDist = xDist/(16*Main.DEFAULT_SCALE);
        float yBlockDist = yDist/(16*Main.DEFAULT_SCALE);

        float xCoord = player.getX()+xBlockDist;
        float yCoord = player.getY()+yBlockDist;

        if ((yCoord >= 0 && yCoord < 256) &&

            (((int)Math.ceil(xCoord) <= player.hitbox.getPos()[0] ||
                (int)Math.floor(xCoord) >= player.hitbox.getPos()[2]) ||
            ((int)Math.ceil(yCoord) <= player.hitbox.getPos()[1] ||
                (int)Math.floor(yCoord) >= player.hitbox.getPos()[3]))
        && (distanceToPlayerSQ((int)Math.floor(xCoord), (int)Math.floor(yCoord)) <= MAX_BLOCK_INTERACT_RADIUS_SQ)) {

            AdvancedLocation loc = findIntersectLoc(mouseX, mouseY);

            if (loc == null){
                loc = new AdvancedLocation(new Location((int) Math.floor(xCoord), (int) Math.floor(yCoord)), ' ');
            }else if (loc.loc == null){
                loc.loc = new Location((int) Math.floor(xCoord), (int) Math.floor(yCoord));
            }

            if (loc.loc.getBlock() != null) {
                if (selection != null && selection.loc.getBlock() != null) {
                    selection.loc.getBlock().setSelected(false);
                }
                loc.loc.getBlock().setSelected(true);
                selection = loc;
            } else {
                if (selection != null && selection.loc.getBlock() != null) {
                    selection.loc.getBlock().setSelected(false);
                }

                if (hasSupportBlock(loc.loc)) {
                    float displayX = MainGameScreen.player.getDisplayX() + ((int) Math.floor(xCoord) - MainGameScreen.player.getX()) * 16 * Main.DEFAULT_SCALE;
                    float displayY = MainGameScreen.player.getDisplayY() + ((int) Math.floor(yCoord) - MainGameScreen.player.getY()) * 16 * Main.DEFAULT_SCALE - Player.HEIGHT * Main.DEFAULT_SCALE;

                    Gdx.gl.glEnable(GL20.GL_BLEND);
                    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                    Gdx.gl.glEnable(GL20.GL_ALPHA);
                    ShapeRenderer renderer = MainGameScreen.shapeRenderer;
                    renderer.begin(ShapeRenderer.ShapeType.Filled);

                    renderer.setColor(1.0F, 1.0F, 1.0F, 0.4F);

                    renderer.rect(displayX, displayY, 16 * Main.DEFAULT_SCALE, 16 * Main.DEFAULT_SCALE);
                    renderer.end();
                    Gdx.gl.glDisable(GL20.GL_BLEND);

                    renderer.begin(ShapeRenderer.ShapeType.Line);

                    renderer.setColor(0F, 0F, 0F, 1F);

                    renderer.rect(displayX, displayY, 16 * Main.DEFAULT_SCALE, 16 * Main.DEFAULT_SCALE);
                    renderer.end();

                    selection = loc;
                }else{
                    selection = null;
                }
            }
        }else{
            if (selection != null && selection.loc.getBlock() != null) {
                selection.loc.getBlock().setSelected(false);
            }
            selection = null;
        }
    }

    private float distanceToPlayerSQ(int x, int y){
        float playerX = player.getX()+(Player.WIDTH/2F)/16F;
        float playerY = player.getY()+(Player.HEIGHT/2F)/16F;

        float blockCenterX = x+0.5F;
        float blockCenterY = y+0.5F;

        float xDist = blockCenterX-playerX;
        float yDist = blockCenterY-playerY;

        return xDist*xDist+yDist*yDist;
    }

    boolean cooldown = false;

    Timer timer = new Timer();

    private boolean containsProperty(Property[] properties, Property property){
        for (Property p : properties){
            if (p == property){
                return true;
            }
        }

        return false;
    }

    private void checkMouseClick(){
        int RIGHT = Input.Buttons.RIGHT;
        int LEFT = Input.Buttons.LEFT;

        TimerTask resetCD = new TimerTask() {
            @Override
            public void run() {
                cooldown = false;
            }
        };

        if (!cooldown) {
            if (selection != null) {
                if (Gdx.input.isButtonPressed(RIGHT) && player.getItemInHand() != null) {
                    if (
                        (player.hitbox.getPos()[3] <= selection.loc.getY() ||
                            player.hitbox.getPos()[1] >= selection.loc.getY() + 1 ||
                            player.hitbox.getPos()[2] <= selection.loc.getX() ||
                            player.hitbox.getPos()[0] >= selection.loc.getX() + 1) &&
                                selection.loc.getBlock() == null
                    ) {

                        if (player.getItemInHand().isBlock()) {
                            Block b = new Block(player.getItemInHand().getType().getId());
                            if (containsProperty(Blocks.valueOf(player.getItemInHand().getType().name()).properties, Property.AXIS)){
                                b.blockdata.setAxis(selection.axis);
                            }
                            selection.loc.setBlock(b);
                            cooldown = true;
                            timer.schedule(resetCD, 200);
                        }
                    }

                } else if (Gdx.input.isButtonPressed(LEFT)) {
                    if (selection.loc.getBlock() != null && selection.loc.getBlock().getType() != Block.BEDROCK){
                        selection.loc.setBlock(null);
                    }
                    cooldown = true;
                    timer.schedule(resetCD, 200);
                }
            }
        }
    }

    boolean F7Pressed = false;

    public void tick(){
        checkPlayerKeys();
        player.tick();
        setBlockPick();
        checkMouseClick();
        hotbar.tick();
        if (Gdx.input.isKeyPressed(Input.Keys.F7)){
            F7Pressed = true;
            takeScreenshot();
        }else if (!Gdx.input.isKeyPressed(Input.Keys.F7)){
            F7Pressed = false;
        }
    }

    private void takeScreenshot() {
        Gdx.gl.glFlush(); // Ensure rendering is done

        int width = Gdx.graphics.getBackBufferWidth();
        int height = Gdx.graphics.getBackBufferHeight();

        byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, width, height, false); // don't flip
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);

        FileHandle file = Gdx.files.absolute(System.getenv("APPDATA") + "/AtomicChambers/latestScreenshot.png");
        PixmapIO.writePNG(file, pixmap, Deflater.DEFAULT_COMPRESSION, true);

        pixmap.dispose();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(new Color(0, 200, 200, 1));
        tick();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(0, 0, 0, 0);

        int rectheight = 3000;
        shapeRenderer.rect(player.getDisplayX()-1500, (-rectheight+player.getDisplayY()-Player.HEIGHT*Main.DEFAULT_SCALE)-player.getY()*16*Main.DEFAULT_SCALE, 3000, rectheight);

        shapeRenderer.end();

        player.render();

        Thread worldGenerator = new Thread(loadWorld);
        worldGenerator.start();

        world.render(batch);

        batch.begin();

        debugFont.getData().setScale(3);
        debugFont.draw(batch, "Position: " + player.getX() + " " + player.getY(), 10, 50);

        hotbar.render(batch);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        selection.loc.getBlock().setSelected(false);
    }
}
