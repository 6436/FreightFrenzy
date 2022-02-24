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
        const val UP_POSITION = 560
    }

    private lateinit var spin: DcMotorEx

    fun initialize() {
        spin = hardwareMap.get(DcMotorEx::class.java, ::spin.name)

        spin.direction = DcMotorSimple.Direction.FORWARD

        spin.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        spin.targetPosition = 0

        spin.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        spin.mode = DcMotor.RunMode.RUN_TO_POSITION

        spin.power = POWER
    }

    fun update() {
        when {
            gamepad2.dpad_left -> spin.targetPosition += 10
            gamepad2.dpad_right -> spin.targetPosition -= 10
//            gamepad2.x -> down()
//            gamepad2.y || gamepad2.right_bumper -> up()
        }
    }

    fun up() {
        spin.targetPosition = UP_POSITION
    }

    fun down() {
        spin.targetPosition = 0
    }

    fun telemetry() {
        telemetry.addData("spin currentPosition", spin.currentPosition)
    }
}
