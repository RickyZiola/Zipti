package com.redneckrobotics.zipti.drive;

import com.redneckrobotics.math.Vec2;

public interface BaseDrive {
    public void set(Vec2 movement, double turn);
}
