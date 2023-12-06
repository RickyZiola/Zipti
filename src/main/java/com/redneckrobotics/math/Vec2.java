package com.redneckrobotics.math ;

/**
 * TODO: replace this with the actually good implementation from the SteelSwerve prototype
 */

public class Vec2 {
    public double x, y;

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public Vec2(double x) {
        this(x,x);
    }
    public Vec2() {
        this(0,0);
    }

    // Static mathematics
    public static Vec2 add(Vec2 vec1, Vec2 vec2) {
        return new Vec2(vec1.x + vec2.x, vec1.y + vec2.y);
    }
    public static Vec2 sub(Vec2 vec1, Vec2 vec2) {
        return new Vec2(vec1.x - vec2.x, vec1.y - vec2.y);
    }
    public static Vec2 mul(Vec2 vec1, Vec2 vec2) {
        return new Vec2(vec1.x * vec2.x, vec1.y * vec2.y);
    }
    public static Vec2 div(Vec2 vec1, Vec2 vec2) {
        return new Vec2(vec1.x / vec2.x, vec1.y / vec2.y);
    }

    // Instancs maths
    public Vec2 plus(Vec2 other) {
        return new Vec2(this.x + other.x, this.y + other.y);
    }
    public Vec2 minus(Vec2 other) {
        return new Vec2(this.x - other.x, this.y - other.y);
    }
    public Vec2 times(Vec2 other) {
        return new Vec2(this.x * other.x, this.y * other.y);
    }
    public Vec2 divided(Vec2 other) {
        return new Vec2(this.x / other.x, this.y / other.y);
    }
    public Vec2 plus(double other) {
        return new Vec2(this.x + other, this.y + other);
    }
    public Vec2 minus(double other) {
        return new Vec2(this.x - other, this.y - other);
    }
    public Vec2 times(double other) {
        return new Vec2(this.x * other, this.y * other);
    }
    public Vec2 divided(double other) {
        return new Vec2(this.x / other, this.y / other);
    }

    public double length() {
        return Math.sqrt(this.x*this.x + this.y*this.y);
    }

    public Vec2 norm() {
        double len = this.length();
        if(len == 0)
            len = 1;
        return this.divided(new Vec2(len, len));
    }

    public static Vec2 rotate(Vec2 v, double radians) {
        return new Vec2(
            Math.cos(radians)*v.x + Math.sin(radians)*v.y,
            Math.cos(radians)*v.y - Math.sin(radians)*v.x );
    }
    public Vec2 rotate(double radians) {
        return rotate(this, radians);
    }

}