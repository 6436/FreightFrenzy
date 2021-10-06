package org.firstinspires.ftc.teamcode

import kotlin.math.abs

class MecanumDrivetrain : FourWheelDrivetrain() {
    override fun update() {
        val x = gamepad1.left_stick_x.toDouble()
        val y = gamepad1.left_stick_y.toDouble()
        val turn = gamepad1.right_stick_x.toDouble()

        val flPower = y + x + turn
        val frPower = y - x - turn
        val blPower = y - x + turn
        val brPower = y + x - turn
        val powers = doubleArrayOf(flPower, frPower, blPower, brPower)

        val max = (powers.map { abs(it) } + 1.0).maxOrNull()!!

        powers.zip(motors) { power, motor ->
            motor.power = power / max * MAX_POWER
        }
    }
}
