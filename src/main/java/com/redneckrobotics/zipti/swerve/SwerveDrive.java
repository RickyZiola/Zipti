package com.redneckrobotics.zipti.swerve;

import com.redneckrobotics.zipti.drive.BaseDrive;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.redneckrobotics.math.Vec2;

/**
 * 4-wheel Swerve drive train for use with ZIPTI or standalone.
 * It uses a simple IK model that supports changing the center of rotation, and can work with real data (target m/s and rads/s)
 */
public class SwerveDrive extends BaseDrive {
    private static Vec2 _proj(double t, Vec2 drv, double turn, Vec2 modPos, Vec2 CoR) {
        Vec2 rot = modPos.minus(CoR).rotate(turn*t).plus(CoR);
        return rot.plus(drv.times(t));
    }

    public SwerveModule fl, fr, bl, br;
    public Vec2 centerRot;
    public Vec2 halfSize;

    /**
     * Constructor
     * @param fl The front left swerve module
     * @param fr The front right swerve module
     * @param bl The back left swerve module
     * @param br The back right swerve module
     * @param halfSize A vector that goes from the center of the robot to the front right wheel (in inches)
     */
    public SwerveDrive(SwerveModule fl, SwerveModule fr, SwerveModule bl, SwerveModule br, Vec2 halfSize) {
        this.fl = fl;
        this.fr = fr;
        this.bl = bl;
        this.br = br;

        this.centerRot = new Vec2(0.0);
        this.halfSize = halfSize;
    }

    /**
     * Constructor
     * @param fl The front left swerve module
     * @param fr The front right swerve module
     * @param bl The back left swerve module
     * @param br The back right swerve module
     */
    public SwerveDrive(SwerveModule fl, SwerveModule fr, SwerveModule bl, SwerveModule br) {
        this(fl, fr, bl, br, new Vec2(9.0, 12.0)); // "standard" chassis size (inches)
    }

    /**
     * Constructor
     * @param fld Front left drive motor
     * @param flt Front left turn motor
     * @param frd Front right drive motor
     * @param frt Front right turn motor
     * @param bld Back left drive motor
     * @param blt Back left turn motor
     * @param brd Back right drive motor
     * @param brt Back right turn motor
     */
    public SwerveDrive(
        WPI_TalonFX fld, WPI_TalonFX flt,
        WPI_TalonFX frd, WPI_TalonFX frt,
        WPI_TalonFX bld, WPI_TalonFX blt,
        WPI_TalonFX brd, WPI_TalonFX brt
    ) {
        this(
            new SwerveModule(fld, flt),
            new SwerveModule(frd, frt),
            new SwerveModule(bld, blt),
            new SwerveModule(brd, brt)
        );
    }

    /**
     * Set the drive to a given drive and turn value
     * @param drive The drive vector (power)
     * @param turn The turn value (power)
     */
    public void set(Vec2 drive, double turn) {
        double eps = 0.02; // Robot runs at 50 loops/sec. This is pretty arbitrary as long as it's < 0.05

        Vec2 flPos = new Vec2(-this.halfSize.x, this.halfSize.y);
        Vec2 flCart =_proj(eps, drive, turn, flPos, this.centerRot);

        Vec2 frPos = new Vec2(this.halfSize.x, this.halfSize.y);
        Vec2 frCart =_proj(eps, drive, turn, frPos, this.centerRot);

        Vec2 blPos = new Vec2(-this.halfSize.x, -this.halfSize.y);
        Vec2 blCart =_proj(eps, drive, turn, blPos, this.centerRot);

        Vec2 brPos = new Vec2(this.halfSize.x, -this.halfSize.y);
        Vec2 brCart =_proj(eps, drive, turn, brPos, this.centerRot);

        this.fl.set(flCart.length(), Math.atan2(flCart.y, flCart.x));
        this.fr.set(frCart.length(), Math.atan2(frCart.y, frCart.x));
        this.bl.set(blCart.length(), Math.atan2(blCart.y, blCart.x));
        this.br.set(brCart.length(), Math.atan2(brCart.y, brCart.x));
    }
}
