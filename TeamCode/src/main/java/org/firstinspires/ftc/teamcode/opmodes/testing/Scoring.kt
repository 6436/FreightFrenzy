package org.firstinspires.ftc.teamcode.opmodes.testing

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.opmodes.TeleOpMode
import org.firstinspires.ftc.teamcode.NANOSECONDS_PER_SECOND

@TeleOp
class Scoring : TeleOpMode()  {
    companion object {
        private const val LIFT_POWER = 0.95

        enum class Level(val liftPosition: Int, val spinPosition: Double, val scoringPosition: Double) {
            DOWN(LIFT_DOWN_POSITION, SPIN_DOWN_POSITION, SCORING_DEFAULT_POSITION),
            BOTTOM(LIFT_BOTTOM_POSITION, SPIN_BOTTOM_POSITION, SCORING_CLOSE_POSITION),
            MIDDLE(LIFT_MIDDLE_POSITION, SPIN_MIDDLE_POSITION, SCORING_CLOSE_POSITION),
            TOP(LIFT_TOP_POSITION, SPIN_TOP_POSITION, SCORING_CLOSE_POSITION)
        }

        private const val LIFT_OK_POSITION = 1800
        private const val LIFT_DOWN_POSITION = 200
        private const val LIFT_BOTTOM_POSITION = 100
        private const val LIFT_MIDDLE_POSITION = 100
        private const val LIFT_TOP_POSITION = 2030

        private const val SPIN_DOWN_POSITION = 0.9594
        private const val SPIN_BOTTOM_POSITION = 0.0694444
        private const val SPIN_MIDDLE_POSITION = 0.1572222
        private const val SPIN_TOP_POSITION = 0.17611111

        private const val SCORING_DEFAULT_POSITION = 0.5
        private const val SCORING_CLOSE_POSITION = 0.35111111
        private const val SCORING_OPEN_POSITION = 0.84777777
        private const val SCORING_BOTTOM_POSITION = 0.471666666
    }

    private lateinit var leftLift: DcMotorEx
    private lateinit var rightLift: DcMotorEx
    private lateinit var spin: Servo
    //    private lateinit var rightSpin: Servo
    private lateinit var scoring: Servo

    override fun initialize() {
        leftLift = hardwareMap.get(DcMotorEx::class.java, ::leftLift.name)
        rightLift = hardwareMap.get(DcMotorEx::class.java, ::rightLift.name)

        spin = hardwareMap.get(Servo::class.java, ::spin.name)
//        rightSpin = hardwareMap.get(Servo::class.java, ::rightSpin.name)

        scoring = hardwareMap.get(Servo::class.java, ::scoring.name)

        leftLift.direction = DcMotorSimple.Direction.FORWARD

        leftLift.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        leftLift.targetPosition = 0

        leftLift.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        leftLift.mode = DcMotor.RunMode.RUN_TO_POSITION

        leftLift.power = LIFT_POWER

        rightLift.direction = DcMotorSimple.Direction.REVERSE

        rightLift.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        rightLift.targetPosition = 0

        rightLift.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        rightLift.mode = DcMotor.RunMode.RUN_TO_POSITION

        rightLift.power = LIFT_POWER

        leftLift.targetPosition = -200
        rightLift.targetPosition = -200
        spin.position = Level.DOWN.spinPosition
        scoring.position = Level.DOWN.scoringPosition
    }

    private var state = Level.DOWN
    private var state2 = "done"
    override fun update() {
//        state = when {
//            gamepad2.a -> {state2 = "start";Level.DOWN}
//            gamepad2.x -> {state2 = "start";Level.BOTTOM}
//            gamepad2.y -> {state2 = "start";Level.MIDDLE}
//            gamepad2.b -> {state2 = "start";Level.TOP}
//            else -> state
//        }
//        up(state)
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
            spin.position += 0.001
//            rightSpin.position -= 0.001
        }
        if (gamepad2.y) {
            spin.position -= 0.001
//            rightSpin.position += 0.001
        }
        if (gamepad2.a) scoring.position += 0.001
        if (gamepad2.b) scoring.position -= 0.001
        telemetry()
        telemetry.update()
    }

    private var startTime = System.nanoTime()
    private var flag = false
    private var flag2 = false
    fun up(level: Level) {
        if (state2 == "start") {
            leftLift.targetPosition = LIFT_OK_POSITION
            rightLift.targetPosition = LIFT_OK_POSITION
            state2= "next"
        }
        if (state2 == "next") {
            if (!leftLift.isBusy) {
                spin.position = level.spinPosition
                startTime = System.nanoTime()
                state2 = "next2"
            }
        }
        if (state2 == "next2") {if (System.nanoTime() - startTime > (if (level == Level.DOWN) 1.4 else 0.6) * NANOSECONDS_PER_SECOND) {
            leftLift.targetPosition = level.liftPosition
            rightLift.targetPosition = level.liftPosition
            state2 = "done"
        }}
        if (state2 == "done") {

        }



        scoring.position = level.scoringPosition
    }

    fun open() {
        scoring.position = SCORING_OPEN_POSITION
    }

    fun telemetry() {
        telemetry.addData("left lift position", leftLift.currentPosition)
        telemetry.addData("right lift position", rightLift.currentPosition)
        telemetry.addData("stick position", scoring.position)
        telemetry.addData("spin position", spin.position)
    }
}
