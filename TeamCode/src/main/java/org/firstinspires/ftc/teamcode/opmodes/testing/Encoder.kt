package org.firstinspires.ftc.teamcode.opmodes.testing

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.opmodes.TeleOpMode

@TeleOp
class Encoder : TeleOpMode() {
    private companion object {
        const val POWER = 0.5
        const val INCREMENT = 100
    }

    private lateinit var motor: DcMotorEx

    override fun initialize() {
        motor = hardwareMap.get(DcMotorEx::class.java, ::motor.name)

        motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        motor.targetPosition = 0

        motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        motor.mode = DcMotor.RunMode.RUN_TO_POSITION

        motor.power = POWER
    }

    private var power = 0.0
    override fun update() {
        if (gamepad1.x) {
            motor.targetPosition += INCREMENT
        } else if (gamepad1.y) {
            motor.targetPosition -= INCREMENT
        }

        telemetry.addData("position", motor.targetPosition)
    }
}
