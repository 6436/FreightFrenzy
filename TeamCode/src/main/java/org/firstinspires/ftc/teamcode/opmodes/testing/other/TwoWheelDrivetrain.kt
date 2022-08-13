package org.firstinspires.ftc.teamcode.opmodes.testing.other

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.opmodes.TeleOpMode
import kotlin.math.abs

@TeleOp
class TwoWheelDrivetrain : TeleOpMode() {
    private companion object {
        const val POWER = 0.9
    }

    private lateinit var left: DcMotorEx
    private lateinit var right: DcMotorEx
    private lateinit var motors: List<DcMotorEx>

    override fun initialize() {
        left = hardwareMap.get(DcMotorEx::class.java, ::left.name)
        right = hardwareMap.get(DcMotorEx::class.java, ::right.name)
        motors = listOf(left, right)

        motors.forEach {
            it.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

            it.targetPosition = 0

            it.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

            it.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        }
    }

    override fun update() {
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
