package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.teamcode.gamepad2
import org.firstinspires.ftc.teamcode.hardwareMap
import org.firstinspires.ftc.teamcode.telemetry

class Spin {
    private companion object {
        const val POWER = 0.85
        const val UP_POSITION = 2100
    }

    private lateinit var spin: DcMotorEx

    fun initialize() {
        spin = hardwareMap.get(DcMotorEx::class.java, ::spin.name)

        spin.direction = DcMotorSimple.Direction.REVERSE

        spin.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        spin.targetPosition = 0

        spin.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        spin.mode = DcMotor.RunMode.RUN_TO_POSITION

        spin.power = POWER
    }

    fun update() {
        when {
            gamepad2.left_bumper -> spin.targetPosition += 10
            gamepad2.right_bumper -> spin.targetPosition -= 10
//            gamepad2.right_bumper -> top()
        }
    }

    fun up() {
        spin.targetPosition = UP_POSITION
    }

    fun bottom() {
        spin.targetPosition = 0
    }

    fun telemetry() {
        telemetry.addData("spin currentPosition", spin.currentPosition)
    }
}
