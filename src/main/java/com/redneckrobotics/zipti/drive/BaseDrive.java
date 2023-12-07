package com.redneckrobotics.zipti.drive;

import com.redneckrobotics.math.Vec2;

/**
 * Abstract base class for drive trains
 */
public abstract class BaseDrive {
    /**
     * Set the drivetrain to a specific drive and turn
     * @param drive The drive vector (power)
     * @param turn The turn vector (power)
     */
    public abstract void set(Vec2 drive, double turn);

    /**
     * Center of rotation (wheel space)
     */
    public Vec2 centerRot = new Vec2();
    /**
     * A vector that goes from the center of the robot to the front right wheel (in meters)
     */
    public Vec2 halfSize  = new Vec2(1.0);
}
