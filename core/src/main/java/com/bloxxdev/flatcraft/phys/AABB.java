package com.bloxxdev.flatcraft.phys;

public class AABB {

    private float x0;
    private float y0;
    private float x1;
    private float y1;

    private float width;
    private float height;

    public AABB(float x0, float y0, float width, float height){
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x0+width;
        this.y1 = y0+height;

        this.width = width;
        this.height = height;
    }

    public void setPos(float x, float y){
        this.x0 = x;
        this.y0 = y;
        this.x1 = x+width;
        this.y1 = y+height;
    }

    public float adjustXMovement(float movement, AABB checkBB){
        if (y0 < checkBB.y1 && y1 > checkBB.y0){
            if (movement < 0){
                if (x0 >= checkBB.x1){
                    float maxMovement = checkBB.x1-x0;

                    return Math.max(maxMovement, movement);
                }
            }else if (movement > 0){
                if (x1 <= checkBB.x0){
                    float maxMovement = checkBB.x0-x1;

                    return Math.min(maxMovement, movement);
                }
            }
        }

        return movement;
    }

    public float adjustYMovement(float movement, AABB checkBB){
        if (x0 < checkBB.x1 && x1 > checkBB.x0){
            if (movement < 0){
                if (y0 >= checkBB.y1){
                    float maxMovement = checkBB.y1-y0;

                    return Math.max(maxMovement, movement);
                }
            }else if (movement > 0){
                if (y1 <= checkBB.y0){
                    float maxMovement = checkBB.y0-y1;

                    return Math.min(maxMovement, movement);
                }
            }
        }

        return movement;
    }

    public float[] getPos(){
        return new float[]{
            x0, y0, x1, y1
        };
    }

}
