package com.redneckrobotics.zipti;

import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.Joystick;

import java.util.HashMap;

import com.redneckrobotics.zipti.drive.BaseDrive;
import com.redneckrobotics.math.Vec2;


public class ControlBase {
    /**
     * Interface for 'enabled/disabled' actions. This is used to store control bindings.
     */
    public interface Action {
        /**
         * Called once when the control is enabled
         */
        public void onActivate();
        /**
         * Called when the control is disabled
         */
        public void onDeactivate();

        /**
         * Called continuously when the control is enabled
         */
        public void whileActivated();
        /**
         * Called continuously when the control is disabled
         */
        public void whileDeactivated();
    }
    /**
     * CAN motor controller action.
     * This can use any control mode and goes to 2 specified powers/positions when enabled/disabled.
     */
    public class MotorAction implements Action {
        private BaseMotorController motor;
        private ControlMode mode;
        private double enabled, disabled;

        /**
         * Constructor
         * @param motor The motor to control
         * @param mode The ControlMode to use
         * @param enabled The value to set the motor to when the control is enabled
         * @param disabled The value to set the motor to when the control is disabled
         */
        public MotorAction(
            BaseMotorController motor,
            ControlMode mode,
            double enabled, double disabled) {
            this.motor = motor;
            this.mode = mode;
            this.enabled = enabled;
            this.disabled = disabled;
        }

        /**
         * Unused
         */
        public void onActivate() {}
        /**
         * Unused
         */
        public void onDeactivate() {}

        /**
         * Sets the motor to the 'enabled' state
         */
        public void whileActivated() {
            this.motor.set(this.mode, this.enabled);
        }
        /**
         * Sets the motor to the 'disabled states'
         */
        public void whileDeactivated() {
            this.motor.set(this.mode, this.disabled);
        }
    }

    private int xAx, yAx, tAx, trimAx;
    private double trimLow, trimHigh;
    private BaseDrive drv;
    private HashMap<Integer, Action> bindings;
    private Joystick joy;
    private boolean useCenterRot = false;

    /**
     * Constructor
     */
    public ControlBase(Joystick controller) {
        this.bindings = new HashMap<Integer, Action>();
        this.joy = controller;

        this.xAx = 0;
        this.yAx = 0;
        this.tAx = 0;
        this.trimAx = 0;
    }

    /**
     * Bind a button to an action
     * @param button The button to use
     * @param act The action to perform
     */
    public void bind(int button, Action act) {
        this.bindings.put(button, act);
    }

    /**
     * Bind a motor's power to a button
     * @param button The button to use
     * @param motor The motor controller to bind
     * @param enabled The enabled power
     * @param disabled The disabled power
     */
    public void bindMotorPower(int button, BaseMotorController motor, double enabled, double disabled) {
        Action act = new MotorAction(motor, ControlMode.PercentOutput, enabled, disabled);
        this.bind(button, act);
    }

    /**
     * Bind a new drivetrain. This overwrites the existing drive.
     * @param drv The drivetrain to use
     * @param xAxis The X-channel of the controller
     * @param yAxis The Y-channel of the controller
     * @param turnAxis The twist/turn channel of the controller
     * @param trimAxis The trim/throttle channel of the controller
     * @param useCenterRot Wether or not to use the HAT/POV button to change the center of rotation of the drive. This should probably be false for tank, but with swerve and sometimes mechanum, it's a useful feature.
     */
    public void bindDrive(BaseDrive drv, int xAxis, int yAxis, int turnAxis, int trimAxis, boolean useCenterRot) {
        this.xAx = xAxis;
        this.yAx = yAxis;
        this.tAx = turnAxis;
        this.trimAx = trimAxis;

        this.drv = drv;
        this.useCenterRot = useCenterRot;
    }
    
    /**
     * Set the range of the drive speed trim
     * @param bot The lower end of the trim range
     * @param top The higher end of the trim range
     */
    public void setTrimRange(double bot, double top) {
        this.trimLow = bot;
        this.trimHigh = top;
    }

    /**
     * This should be called in the robot's initialization code.
     */
    public void robotInit() {
        // Unused for now
    }

    /**
     * This should be called in the robot's teleop loop, it updates all buttons/actions
     */
    public void teleopPeriodic() {
        this.bindings.forEach((button, action) -> {
            if (this.joy.getRawButton(button))
                action.whileActivated();
            else
                action.whileDeactivated();
            
            if (this.joy.getRawButtonPressed(button))  action.onActivate();
            if (this.joy.getRawButtonReleased(button)) action.onDeactivate();
        });

        double trim = (this.joy.getRawAxis(this.trimAx) * -0.5 + 0.5) * (this.trimHigh - this.trimLow) + this.trimLow;
        double tX, tY, turn;
        tX   = this.joy.getRawAxis(this.xAx) * trim;
        tY   = -this.joy.getRawAxis(this.yAx) * trim;
        turn = this.joy.getRawAxis(this.tAx) * trim;

        if (this.drv != null) {
                // Change the center of rotation based on the HAT/POV switch (aka d-pad)
            int cRotAng = this.joy.getPOV();
            Vec2 normCenterRot = new Vec2(
                Math.cos(Math.toRadians(cRotAng-90)),
                Math.sin(Math.toRadians(cRotAng+90))
            );
            if (cRotAng == -1) {
                normCenterRot = new Vec2(0.0);
            }
            Vec2 trueCenterRot = normCenterRot.times(this.drv.halfSize);
            if (this.useCenterRot) this.drv.centerRot = trueCenterRot;
            else this.drv.centerRot = new Vec2(0.0);
            this.drv.set(new Vec2(tX, tY), turn);
        }
    }
}