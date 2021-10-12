package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import kotlin.math.PI
import kotlin.math.abs

open class Mecanum {
    private companion object {
        const val MILLIMETERS_PER_INCH = 25.4
        const val DEGREES_PER_ROTATION = 360.0

        const val COUNTS_PER_ROTATION = 537.6
        const val WHEEL_DIAMETER_MILLIMETERS = 100.0

        const val WHEEL_DIAMETER_INCHES = WHEEL_DIAMETER_MILLIMETERS / MILLIMETERS_PER_INCH
        const val WHEEL_CIRCUMFERENCE_INCHES = WHEEL_DIAMETER_INCHES * PI
        const val COUNTS_PER_INCH = COUNTS_PER_ROTATION / WHEEL_CIRCUMFERENCE_INCHES

        const val ROBOT_CIRCUMFERENCE_COUNTS = 3000

        const val MAX_POWER = 0.5
    }

    private lateinit var fl: DcMotorEx
    private lateinit var fr: DcMotorEx
    private lateinit var bl: DcMotorEx
    private lateinit var br: DcMotorEx
    private lateinit var motors: Array<DcMotorEx>

    fun initialize() {
        fl = hardwareMap.get(DcMotorEx::class.java, ::fl.name)
        fr = hardwareMap.get(DcMotorEx::class.java, ::fr.name)
        bl = hardwareMap.get(DcMotorEx::class.java, ::bl.name)
        br = hardwareMap.get(DcMotorEx::class.java, ::br.name)
        motors = arrayOf(fl, fr, bl, br)

        for (i in motors.indices) motors[i].direction =
            if (i % 2 == 0) DcMotorSimple.Direction.REVERSE else DcMotorSimple.Direction.FORWARD

        for (i in motors) {
            i.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

            i.targetPosition = 0

            i.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

            i.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        }
    }

    fun update() {
        read()
        odometry()
        drive()
    }

    private fun read() {
        for (i in motors) {
            i.currentPosition

//            i.velocity

            i.isBusy
        }
    }

    private var heading = 0.0
    private var location = Point()
    private var lastAPosition = 0.0
    private var lastBPosition = 0.0
    private fun odometry() {
        heading = intArrayOf(
            -fl.currentPosition,
            fr.currentPosition,
            -bl.currentPosition,
            br.currentPosition
        ).average() / ROBOT_CIRCUMFERENCE_COUNTS * DEGREES_PER_ROTATION

        val aPosition = intArrayOf(fl.currentPosition, br.currentPosition).average()
        val bPosition = intArrayOf(fr.currentPosition, bl.currentPosition).average()
        val aPositionChange = aPosition - lastAPosition
        val bPositionChange = bPosition - lastBPosition

        var changePosition = Vector(
            aPositionChange - bPositionChange,
            aPositionChange + bPositionChange
        ) / 2.0
        changePosition = changePosition.rotatedAboutOrigin(heading)

        location += changePosition / COUNTS_PER_INCH

        lastAPosition = aPosition
        lastBPosition = bPosition
    }

    private fun drive() {
        val translate = Vector(
            gamepad1.left_stick_x.toDouble(),
                    gamepad1.left_stick_y.toDouble()
        ).rotatedAboutOrigin(-heading)
        val x = translate.x
        val y = translate.y

        val turn = gamepad1.right_stick_x.toDouble()

        val flPower = y + x + turn
        val frPower = y - x - turn
        val blPower = y - x + turn
        val brPower = y + x - turn
        val powers = doubleArrayOf(flPower, frPower, blPower, brPower)

        val max = (powers.map { abs(it) } + 1.0).maxOrNull()!!

        powers.zip(motors) { power, motor ->
            motor.power = power / max * MAX_POWER
        }
    }
}
