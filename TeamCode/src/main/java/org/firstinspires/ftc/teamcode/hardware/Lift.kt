package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.teamcode.gamepad2
import org.firstinspires.ftc.teamcode.hardwareMap
import org.firstinspires.ftc.teamcode.telemetry

class Lift {
    private companion object {
        const val POWER = 0.85
        const val TOP_POSITION = 2600
        const val MIDDLE_POSITION = 1440
        const val BOTTOM_POSITION = 630
    }

    private lateinit var lift: DcMotorEx

    fun initialize() {
        lift = hardwareMap.get(DcMotorEx::class.java, ::lift.name)

        lift.direction = DcMotorSimple.Direction.FORWARD

        lift.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        lift.targetPosition = 0

        lift.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        lift.mode = DcMotor.RunMode.RUN_TO_POSITION

        lift.power = POWER
    }

    fun update() {
        when {
            gamepad2.a -> down()
            gamepad2.x -> bottom()
            gamepad2.y -> middle()
            gamepad2.right_bumper -> top()
//            gamepad2.dpad_up -> lift.targetPosition += 10
//            gamepad2.dpad_down -> lift.targetPosition -= 10
        }
    }

    fun top() {
        lift.targetPosition = TOP_POSITION
    }

    fun middle() {
        lift.targetPosition = MIDDLE_POSITION
    }

    fun bottom() {
        lift.targetPosition = BOTTOM_POSITION
    }

    fun down() {
        lift.targetPosition = 0
    }

    fun telemetry() {
        telemetry.addData("lift currentPosition", lift.currentPosition)
    }
}
