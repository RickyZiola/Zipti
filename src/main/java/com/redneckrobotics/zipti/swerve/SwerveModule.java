package com.redneckrobotics.zipti.swerve;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

public class SwerveModule {
    public WPI_TalonFX drive;
    public WPI_TalonFX turn;

    private double _getOptimizedAngle(double angle) {
        // Convert to encoder units TODO: make the units/revolution changeable
        double units = angle * (4096.0 * 6.4 / (2.0 * Math.PI));

        // Avoid annoying "lugnut issues"
        // named by our mechanic, not the programmers
        double delta = units - this.turn.getSelectedSensorPosition();
            // 12.0 is the turning gear ratio
        while (delta >  2048.0 * 6.4) delta -= 4096.0 * 6.4;
        while (delta < -2048.0 * 6.4) delta += 4096.0 * 6.4;

        // Final angle
        return this.turn.getSelectedSensorPosition() + delta;

        /*
         * Lugnut isses:
         *  The motor reaches its limit at 2048 units, then snaps back to -2048 units, causing the motor to turn
         *  360 degrees at high velocity. The name comes from trying to drive in this direction, the motors end up
         *  going back and forth very quickly, sounding like an air impact taking off lugnuts.
         * 
         *  Like I said, named by the mechanic.
         */
    }

    public SwerveModule(WPI_TalonFX drive, WPI_TalonFX turn) {
        this.drive = drive;
        this.turn  = turn;

        this.turn.config_kP(0, 0.10000);
        this.turn.config_kI(0, 0.00007);
        this.turn.config_kD(0, 0.00020);
    }

    /**
     * Set the speed and turn of a module
     * @param drive The speed of the module ([0-1))
     * @param turn The direction of the module ([0-2π), radians, 0 is forward)
     */
    public void set(double drive, double turn) {
        this.drive.set(ControlMode.PercentOutput, drive);
        this.turn.set(ControlMode.Position, this._getOptimizedAngle(turn));
    }
}
