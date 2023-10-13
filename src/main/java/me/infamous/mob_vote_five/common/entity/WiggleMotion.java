package me.infamous.mob_vote_five.common.entity;

public enum WiggleMotion {
    CENTER_TO_LEFT(1.0F),
    LEFT_TO_CENTER(-1.0F),
    CENTER_TO_RIGHT(-1.0F),
    RIGHT_TO_CENTER(1.0F);

    private final float impulse;

    WiggleMotion(float impulse) {
        this.impulse = impulse;
    }

    public float getImpulse() {
        return this.impulse;
    }

    public WiggleMotion getNext(){
        switch (this){
            case CENTER_TO_LEFT -> {
                return LEFT_TO_CENTER;
            }
            case LEFT_TO_CENTER -> {
                return CENTER_TO_RIGHT;
            }
            case CENTER_TO_RIGHT -> {
                return RIGHT_TO_CENTER;
            }
            case RIGHT_TO_CENTER -> {
                return CENTER_TO_LEFT;
            }
            default -> throw new IllegalStateException("Invalid WiggleMotion " + this.name());
        }
    }
}
