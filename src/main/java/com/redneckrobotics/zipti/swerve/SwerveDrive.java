package com.redneckrobotics.zipti.swerve;

import com.redneckrobotics.zipti.drive.BaseDrive;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.redneckrobotics.math.Vec2;


public class SwerveDrive implements BaseDrive {
    private static Vec2 _proj(double t, Vec2 drv, double turn, Vec2 modPos, Vec2 CoR) {
        Vec2 rot = modPos.minus(CoR).rotate(turn*t).plus(CoR);
        return rot.plus(drv.times(t));
    }

    public SwerveModule fl, fr, bl, br;
    public Vec2 centerRot;

    public SwerveDrive(SwerveModule fl, SwerveModule fr, SwerveModule bl, SwerveModule br) {
        this.fl = fl;
        this.fr = fr;
        this.bl = bl;
        this.br = br;

            // TODO: make this changeable
        this.centerRot = new Vec2(0.0);
    }
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

    public void set(Vec2 drive, double turn) {
        double eps = 0.001;

        Vec2 flPos = new Vec2(-9.0, 12.0); // TODO: find the actual positions
        Vec2 flCart =_proj(eps, drive, turn, flPos, this.centerRot);

        Vec2 frPos = new Vec2(9.0, 12.0); // TODO: find the actual positions
        Vec2 frCart =_proj(eps, drive, turn, frPos, this.centerRot);

        Vec2 blPos = new Vec2(-9.0, -12.0); // TODO: find the actual positions
        Vec2 blCart =_proj(eps, drive, turn, blPos, this.centerRot);

        Vec2 brPos = new Vec2(9.0, -12.0); // TODO: find the actual positions
        Vec2 brCart =_proj(eps, drive, turn, brPos, this.centerRot);

        this.fl.set(flCart.length(), Math.atan2(flCart.y, flCart.x));
        this.fr.set(frCart.length(), Math.atan2(frCart.y, frCart.x));
        this.bl.set(blCart.length(), Math.atan2(blCart.y, blCart.x));
        this.br.set(brCart.length(), Math.atan2(brCart.y, brCart.x));
    }
}
