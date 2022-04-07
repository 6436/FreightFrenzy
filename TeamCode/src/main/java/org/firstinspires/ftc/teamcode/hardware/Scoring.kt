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

    private lateinit var leftLift: DcMotorEx
    private lateinit var rightLift: DcMotorEx
    private lateinit var leftSpin: Servo
    private lateinit var rightSpin: Servo
    private lateinit var scoring: Servo

    fun initialize() {
        leftLift = hardwareMap.get(DcMotorEx::class.java, ::leftLift.name)
        rightLift = hardwareMap.get(DcMotorEx::class.java, ::rightLift.name)

        leftSpin = hardwareMap.get(Servo::class.java, ::leftSpin.name)
        rightSpin = hardwareMap.get(Servo::class.java, ::rightSpin.name)

        scoring = hardwareMap.get(Servo::class.java, ::scoring.name)

        leftLift.direction = DcMotorSimple.Direction.FORWARD

        leftLift.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        leftLift.targetPosition = 0

        leftLift.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        leftLift.mode = DcMotor.RunMode.RUN_TO_POSITION

        leftLift.power = LIFT_POWER

        rightLift.direction = DcMotorSimple.Direction.FORWARD

        rightLift.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        rightLift.targetPosition = 0

        rightLift.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        rightLift.mode = DcMotor.RunMode.RUN_TO_POSITION

        rightLift.power = LIFT_POWER

        default()
    }

    fun update() {
//        if (gamepad2.a) default()
//        if (gamepad2.x) up(LiftLevel.BOTTOM)
//        if (gamepad2.y) up(LiftLevel.MIDDLE)
//        if (gamepad2.b) up(LiftLevel.TOP)
//        if (gamepad1.right_trigger > 0.0) open()
        if (gamepad1.left_stick_button) {
            leftLift.targetPosition -= 10
            rightLift.targetPosition -= 10
        }
        if (gamepad1.right_stick_button) {
            leftLift.targetPosition += 10
            rightLift.targetPosition += 10
        }
        if (gamepad2.x) {
            leftSpin.position += 0.001
            rightSpin.position -= 0.001
        }
        if (gamepad2.y) {
            leftSpin.position -= 0.001
            rightSpin.position += 0.001
        }
        if (gamepad2.a) scoring.position += 0.0001
        if (gamepad2.b) scoring.position -= 0.0001
    }

    fun up(liftLevel: LiftLevel) {
        leftLift.targetPosition = liftLevel.position
        rightLift.targetPosition = liftLevel.position
        leftSpin.position =
            if (leftLift.currentPosition > LiftLevel.OK.position) 1.0 - SPIN_UP_POSITION else 1.0 - SPIN_DOWN_POSITION
        rightSpin.position =
            if (leftLift.currentPosition > LiftLevel.OK.position) SPIN_UP_POSITION else SPIN_DOWN_POSITION
        scoring.position = SCORING_CLOSE_POSITION
    }

    fun open() {
        scoring.position = SCORING_OPEN_POSITION
    }

    fun default() {
        leftLift.targetPosition = 0
        rightLift.targetPosition = 0
        leftSpin.position = 1.0 - SPIN_DOWN_POSITION
        rightSpin.position = SPIN_DOWN_POSITION
        scoring.position = SCORING_DEFAULT_POSITION
    }

    fun telemetry() {
        telemetry.addData("left lift position", leftLift.currentPosition)
        telemetry.addData("right lift position", rightLift.currentPosition)
        telemetry.addData("scoring position", scoring.position)
        telemetry.addData("left spin position", leftSpin.position)
        telemetry.addData("right spin position", rightSpin.position)
    }
}
