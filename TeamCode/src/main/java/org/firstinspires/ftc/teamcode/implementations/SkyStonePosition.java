package org.firstinspires.ftc.teamcode.implementations;

/**
 * DO NOT CHANGE ORDER OR ELSE EVERYTHING WILL BREAK
 */
public enum SkyStonePosition {
    // why put math when just put values
    THREE_SIX(4), TWO_FIVE(12), ONE_FOUR(20);
    private double distance;
    SkyStonePosition(double pos) { distance = pos; }

    /**
     * @return double distance
     */
    public double getDistance() {
        return distance;
    }
}
