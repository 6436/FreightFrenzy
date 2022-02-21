package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import kotlin.math.abs

@TeleOp
class TwoWheelDrivetrain : OpMode() {
    private companion object {
        const val POWER = 0.9
    }

    private lateinit var left: DcMotorEx
    private lateinit var right: DcMotorEx
    private lateinit var motors: Array<DcMotorEx>

    override fun init() {
        left = hardwareMap.get(DcMotorEx::class.java, ::left.name)
        right = hardwareMap.get(DcMotorEx::class.java, ::right.name)
        motors = arrayOf(left, right)

        for (motor in motors) {
            motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

            motor.targetPosition = 0

            motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

            motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        }
    }

    override fun loop() {
        val y = -gamepad1.left_stick_y

        val turn = gamepad1.right_stick_x.toDouble()

        val flPower = y + turn
        val frPower = y - turn
        val powers = doubleArrayOf(flPower, frPower)

        val max = (powers.map { abs(it) } + 1.0).maxOrNull()!!

        powers.zip(motors) { power, motor ->
            motor.power = power / max * POWER
        }
    }
}
