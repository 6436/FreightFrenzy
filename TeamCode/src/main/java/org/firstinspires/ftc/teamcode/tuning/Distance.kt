package org.firstinspires.ftc.teamcode.tuning

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import kotlin.math.PI
import kotlin.math.roundToInt

@TeleOp
class Distance : Drivetrain() {
    private companion object {
        const val POWER = 0.25

        const val MILLIMETERS_PER_INCH = 25.4

        const val COUNTS_PER_ROTATION = 537.6
        const val WHEEL_DIAMETER_MILLIMETERS = 100.0

        const val WHEEL_DIAMETER_INCHES = WHEEL_DIAMETER_MILLIMETERS / MILLIMETERS_PER_INCH
        const val WHEEL_CIRCUMFERENCE_INCHES = WHEEL_DIAMETER_INCHES * PI
        const val COUNTS_PER_INCH = COUNTS_PER_ROTATION / WHEEL_CIRCUMFERENCE_INCHES
    }

    override fun init() {
        super.init()

        for (motor in motors) {
            motor.mode = DcMotor.RunMode.RUN_TO_POSITION

            motor.targetPosition = (120.0 * COUNTS_PER_INCH).roundToInt()
        }
    }

    override fun loop() {
        super.loop()

        if (gamepad1.x) {
            for (motor in motors) {
                motor.power = POWER
            }
        }

        telemetry.addData(
            "average inches",
            motors.map { it.currentPosition }.average() / COUNTS_PER_INCH
        )
    }
}
