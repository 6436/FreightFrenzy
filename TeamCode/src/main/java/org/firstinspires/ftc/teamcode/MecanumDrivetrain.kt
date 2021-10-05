package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import kotlin.math.abs

class MecanumDrivetrain {
    private companion object {
        const val MAX_POWER = 0.7
    }

    private lateinit var fl: DcMotorEx
    private lateinit var fr: DcMotorEx
    private lateinit var bl: DcMotorEx
    private lateinit var br: DcMotorEx
    private lateinit var motors: Array<DcMotorEx>

    fun initialize() {
        fl = hardwareMap.get(DcMotorEx::class.java, ::fl.name)
        fr = hardwareMap.get(DcMotorEx::class.java, ::fr.name)
        bl = hardwareMap.get(DcMotorEx::class.java, ::bl.name)
        br = hardwareMap.get(DcMotorEx::class.java, ::br.name)
        motors = arrayOf(fl, fr, bl, br)

        for (i in motors.indices) motors[i].direction =
            if (i % 2 == 0) DcMotorSimple.Direction.REVERSE else DcMotorSimple.Direction.FORWARD

        for (i in motors) {
            i.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

            i.targetPosition = 0

            i.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

            i.mode = DcMotor.RunMode.RUN_USING_ENCODER
        }
    }

    fun update() {
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
