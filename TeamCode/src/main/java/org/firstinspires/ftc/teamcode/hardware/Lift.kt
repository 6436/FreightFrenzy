package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.teamcode.*

class Lift {
    private companion object {
        const val POWER = 0.85
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

        liftIsUp = {lift.currentPosition > 712}
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

    private fun middle() {
        lift.targetPosition = 812
    }

    fun up() {
        lift.targetPosition = 2100
    }

    fun telemetry() {
        telemetry.addData("lift currentPosition", lift.currentPosition)
    }
}
