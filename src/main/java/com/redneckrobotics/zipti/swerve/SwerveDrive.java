package com.redneckrobotics.zipti.swerve;

import com.redneckrobotics.zipti.drive.BaseDrive;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.redneckrobotics.math.Vec2;

/**
 * 4-wheel Swerve drive train for use with ZIPTI or standalone.
 * It uses a simple IK model that supports changing the center of rotation, and can work with real data (target m/s and rads/s)
 */
public class SwerveDrive extends BaseDrive {
    private static Vec2 _proj(double t, Vec2 drv, double turn, Vec2 modPos, Vec2 CoR) {
        Vec2 rot = modPos.rotate(turn*t);
        return rot.minus(modPos).minus(drv.times(t));
    }

    public SwerveModule fl, fr, bl, br;
    public Vec2 centerRot;
    public Vec2 halfSize;
    public String dashboardKey = "swerve"; // TODO: constructors

    /**
     * Constructor
     * @param fl The front left swerve module
     * @param fr The front right swerve module
     * @param bl The back left swerve module
     * @param br The back right swerve module
     * @param halfSize A vector that goes from the center of the robot to the front right wheel (in meters)
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
        this(fl, fr, bl, br, new Vec2(0.229, 0.305)); // "standard" chassis size (meters)
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
        if (drive.length() < 0.1 && Math.abs(turn) < 0.01) {
            this.fl.drive.set(0.0);
            this.fr.drive.set(0.0);
            this.bl.drive.set(0.0);
            this.br.drive.set(0.0);
            this.fl.turn.set(TalonFXControlMode.PercentOutput, 0.0); // get rid of annoying clicking noises
            this.fr.turn.set(TalonFXControlMode.PercentOutput, 0.0);
            this.bl.turn.set(TalonFXControlMode.PercentOutput, 0.0);
            this.br.turn.set(TalonFXControlMode.PercentOutput, 0.0);

            return;
        }

        double eps = 0.0001;
        double turnRads = 0.5 * Math.PI * turn;

        Vec2 flPos = new Vec2(-this.halfSize.x, this.halfSize.y);
        Vec2 flCart = _proj(eps, drive, turnRads, flPos, this.centerRot).times(1.0 / eps);

        Vec2 frPos = new Vec2(this.halfSize.x, this.halfSize.y);
        Vec2 frCart = _proj(eps, drive, turnRads, frPos, this.centerRot).times(1.0 / eps);

        Vec2 blPos = new Vec2(-this.halfSize.x, -this.halfSize.y);
        Vec2 blCart = _proj(eps, drive, turnRads, blPos, this.centerRot).times(1.0 / eps);

        Vec2 brPos = new Vec2(this.halfSize.x, -this.halfSize.y);
        Vec2 brCart = _proj(eps, drive, turnRads, brPos, this.centerRot).times(1.0 / eps);

        this.fl.set(flCart.length(), Math.atan2(flCart.y, flCart.x) - .5 * Math.PI);
        this.fr.set(frCart.length(), Math.atan2(frCart.y, frCart.x) - .5 * Math.PI);
        this.bl.set(blCart.length(), Math.atan2(blCart.y, blCart.x) - .5 * Math.PI);
        this.br.set(brCart.length(), Math.atan2(brCart.y, brCart.x) - .5 * Math.PI);

        SmartDashboard.putNumber(this.dashboardKey + "_flX", flCart.x); 
        SmartDashboard.putNumber(this.dashboardKey + "_flY", flCart.y);

        SmartDashboard.putNumber(this.dashboardKey + "_frX", frCart.x); 
        SmartDashboard.putNumber(this.dashboardKey + "_frY", frCart.y);

        SmartDashboard.putNumber(this.dashboardKey + "_blX", blCart.x); 
        SmartDashboard.putNumber(this.dashboardKey + "_blY", blCart.y);

        SmartDashboard.putNumber(this.dashboardKey + "_brX", brCart.x); 
        SmartDashboard.putNumber(this.dashboardKey + "_brY", brCart.y); 
    }
}
