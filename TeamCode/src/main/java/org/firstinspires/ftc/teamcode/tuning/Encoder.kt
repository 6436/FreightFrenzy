package org.firstinspires.ftc.teamcode.tuning

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.teamcode.hardware.Scoring

@TeleOp
class Encoder : OpMode() {
    private companion object {
        const val POWER = 0.5
        const val INCREMENT = 100
    }

    private lateinit var motor: DcMotorEx

    override fun init() {
        motor = hardwareMap.get(DcMotorEx::class.java, ::motor.name)

        motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        motor.targetPosition = 0

        motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        motor.mode = DcMotor.RunMode.RUN_TO_POSITION

        motor.power = POWER
    }

    private var power = 0.0
    override fun loop() {
        if (gamepad1.x) {
                motor.targetPosition += INCREMENT

        } else if (gamepad1.y) {
                motor.targetPosition -= INCREMENT
            }


        telemetry.addData("position", motor.targetPosition)
    }
}
