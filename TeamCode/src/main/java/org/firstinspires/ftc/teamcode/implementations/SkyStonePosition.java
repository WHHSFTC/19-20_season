package org.firstinspires.ftc.teamcode.implementations;

/**
 * DO NOT CHANGE ORDER OR ELSE EVERYTHING WILL BREAK
 */
public enum SkyStonePosition {
    // 4, 12, 20
    THREE_SIX(3), TWO_FIVE(2), ONE_FOUR(1);
    private double position;
    private double distance;
    SkyStonePosition(double pos) {
        position = pos;
        distance = (3 - pos) * 8 + 4;
    }

    /**
     * @return double distance
     */
    public double getDistance() {
        return distance;
    }

    /**
     * @return Stone numbered position
     */
    public double getPosition() {
        return position;
    }

}
