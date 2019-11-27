package org.firstinspires.ftc.teamcode.implementations;

public enum SkyStonePosition {
    THREE_SIX(0), TWO_FIVE(1), ONE_FOUR(2);
    private double distance;
    SkyStonePosition(int pos) {
        distance = pos * 8 + 4;
    }

    public double getDistance() {
        return distance;
    }
}
