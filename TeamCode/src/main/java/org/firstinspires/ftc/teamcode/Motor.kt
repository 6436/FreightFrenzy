package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple

@TeleOp
open class Motor : OpMode() {
    private companion object {
        const val POWER = 0.5
    }

    private lateinit var motor: DcMotorEx

    override fun init() {
        motor = hardwareMap.get(DcMotorEx::class.java, ::motor.name)

        motor.direction = DcMotorSimple.Direction.REVERSE

        motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        motor.targetPosition = 0

        motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    override fun loop() {
        motor.power = gamepad1.right_stick_y * POWER
    }
}
