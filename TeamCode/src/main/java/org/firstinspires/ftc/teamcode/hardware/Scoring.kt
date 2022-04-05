package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.gamepad1
import org.firstinspires.ftc.teamcode.gamepad2
import org.firstinspires.ftc.teamcode.hardwareMap
import org.firstinspires.ftc.teamcode.telemetry

class Scoring {
    companion object {
        private const val LIFT_POWER = 0.95

        enum class LiftLevel(val position: Int) {
            OK(200),
            BOTTOM(630),
            MIDDLE(1440),
            TOP(2700)
        }

        private const val SPIN_UP_POSITION = 0.38677777777
        private const val SPIN_DOWN_POSITION = 0.273

        private const val SCORING_OPEN_POSITION = 0.91
        private const val SCORING_DEFAULT_POSITION = 0.7572222222
        private const val SCORING_CLOSE_POSITION = 0.6077777777
    }

    private lateinit var lift: DcMotorEx
    private lateinit var spin: Servo
    private lateinit var scoring: Servo

    fun initialize() {
        lift = hardwareMap.get(DcMotorEx::class.java, ::lift.name)

        spin = hardwareMap.get(Servo::class.java, ::spin.name)

        scoring = hardwareMap.get(Servo::class.java, ::scoring.name)

        lift.direction = DcMotorSimple.Direction.FORWARD

        lift.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        lift.targetPosition = 0

        lift.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        lift.mode = DcMotor.RunMode.RUN_TO_POSITION

        lift.power = LIFT_POWER

        default()
    }

    fun update() {
//        if (gamepad2.a) default()
//        if (gamepad2.x) up(LiftLevel.BOTTOM)
//        if (gamepad2.y) up(LiftLevel.MIDDLE)
//        if (gamepad2.b) up(LiftLevel.TOP)
//        if (gamepad1.right_trigger > 0.0) open()
        if (gamepad1.left_stick_button) lift.targetPosition -= 10
        if (gamepad1.right_stick_button) lift.targetPosition += 10
        if (gamepad2.x) spin.position += 0.0001
        if (gamepad2.y) spin.position -= 0.0001
        if (gamepad2.a) scoring.position += 0.0001
        if (gamepad2.b) scoring.position -= 0.0001
    }

    fun up(liftLevel: LiftLevel) {
        lift.targetPosition = liftLevel.position
        spin.position =
            if (lift.currentPosition > LiftLevel.OK.position) SPIN_UP_POSITION else SPIN_DOWN_POSITION
        scoring.position = SCORING_CLOSE_POSITION
    }

    fun open() {
        scoring.position = SCORING_OPEN_POSITION
    }

    fun default() {
        lift.targetPosition = 0
        spin.position = SPIN_DOWN_POSITION
        scoring.position = SCORING_DEFAULT_POSITION
    }

    fun telemetry() {
        telemetry.addData("lift position", lift.currentPosition)
        telemetry.addData("scoring position", scoring.position)
        telemetry.addData("spin position", spin.position)
    }
}
