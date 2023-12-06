package com.redneckrobotics.zipti;

import java.util.HashMap;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.ctre.phoenix.motorcontrol.ControlMode;

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

    private HashMap<Integer, Action> bindings;

    /**
     * Constructor
     */
    public ControlBase() {
        this.bindings = new HashMap<Integer, Action>();
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
}