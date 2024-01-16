package com.redneckrobotics.math;

/**
 * 2-dimensional vector class
 */
public class Vec2 {
    /**
     * Components of the vector
     */
    public double x,y;

    /**
     * Construct a new Vec2 with (0,0)
     */
    public Vec2() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * Construct a new Vec2 with (x,x)
     * @param x The value to set the Vec2's x and y component to
     */
    public Vec2(double x) {
        this.x = x;
        this.y = x;
    }

    /**
     * Construct a new Vec2 with (x,y)
     * @param x The value to set the Vec2's x component to
     * @param y The value to set the Vec2's y component to
     */
    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Add 2 vectors
     * @param x The left operand
     * @param y The right operand
     * @return The element-wise sum of the operands
     */
    public static Vec2 add(Vec2 x, Vec2 y) {
        return new Vec2(x.x + y.x, x.y + y.y);
    }
    /**
     * Add 2 vectors
     * @param other The right operand
     * @return The element-wise sum of this and other
     */
    public Vec2 plus(Vec2 other) {
        return new Vec2(this.x + other.x, this.y + other.y);
    }

    /**
     * Subtract 2 vectors
     * @param x The left operand
     * @param y The right operand
     * @return The element-wise difference of the operands
     */
    public static Vec2 subtract(Vec2 x, Vec2 y) {
        return new Vec2(x.x - y.x, x.y - y.y);
    }

    /**
     * Subtract 2 vectors
     * @param other The right operand
     * @return The elemen-wise difference of this and other
     */
    public Vec2 minus(Vec2 other) {
        return new Vec2(this.x - other.x, this.y - other.y);
    }

    /**
     * Multiply two vectors
     * @param x The left operand
     * @param y The right operand
     * @return The element-wise multiplication of the operands
     */
    public static Vec2 multiply(Vec2 x, Vec2 y) {
        return new Vec2(x.x * y.x, x.y * y.y);
    }

    /**
     * Multiply two vectors
     * @param other The right operand
     * @return The element-wise multiplication of this and other
     */
    public Vec2 times(Vec2 other) {
        return new Vec2(this.x * other.x, this.y * other.y);
    }

    public Vec2 times(double other) {
        return new Vec2(this.x * other, this.y * other);
    }

    /**
     * Divide two vectors
     * @param x The left operand
     * @param y The right operand
     * @return The element-wise division of the operands
     */
    public static Vec2 divide(Vec2 x, Vec2 y) {
        return new Vec2(x.x / y.x, x.y / y.y);
    }

    /**
     * Divide two vectors
     * @param other The right operand
     * @return The element-wise division of this and other
     */
    public Vec2 divided(Vec2 other) {
        return new Vec2(this.x / other.x, this.y / other.y);
    }

    /**
     * Dot product of two vectors
     * @param x The left operand
     * @param y The right operand
     * @return The dot product of x and y
     */
    public static double dot(Vec2 x, Vec2 y) {
        return x.x * y.x + x.y * y.y;
    }

    /**
     * Length of a vector
     * @param x The vector to length
     * @return The length of x
     */
    public static double length(Vec2 x) {
        return Math.sqrt(x.x*x.x + x.y*x.y);
    }

    /**
     * Length of this
     * @return The length of this
     */
    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    /**
     * Normalize a vector
     * @param x The vector to normalize
     * @return The normalized vector
     */
    public static Vec2 normalize(Vec2 x) {
        return x.divided(new Vec2(x.length()));
    }

    /**
     * Normalize a vector
     * @return this, normalized
     */
    public Vec2 normalized() {
        return this.divided(new Vec2(this.length()));
    }

    /**
     * Rotate a vector around the origin.
     * @param angleRadians The angle to rotate by
     * @return
     */
    public Vec2 rotate(double angleRadians) {
        return new Vec2(this.x * Math.cos(angleRadians) - this.y * Math.sin(angleRadians), this.y * Math.cos(angleRadians) + this.x * Math.sin(angleRadians));
    }
}