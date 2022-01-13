package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.teamcode.gamepad2
import org.firstinspires.ftc.teamcode.hardwareMap

class Lift {
    private companion object {
        const val POWER = 0.8
    }

    private lateinit var lift: DcMotorEx

    fun initialize() {
        lift = hardwareMap.get(DcMotorEx::class.java, ::lift.name)

        lift.direction = DcMotorSimple.Direction.REVERSE

        lift.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        lift.targetPosition = 0

        lift.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        lift.mode = DcMotor.RunMode.RUN_TO_POSITION

        lift.power = POWER
    }

    fun update() {
        when {
            gamepad2.x -> down()
            gamepad2.y -> middle()
            gamepad2.right_bumper -> up()
        }
    }

    fun down() {
        lift.targetPosition = 0
    }

    fun middle() {
        lift.targetPosition = 812
    }

    fun up() {
        lift.targetPosition = 2050
    }

    fun telemetry() {
        org.firstinspires.ftc.teamcode.telemetry.addData("lift targetPosition", lift.targetPosition)
    }
}
