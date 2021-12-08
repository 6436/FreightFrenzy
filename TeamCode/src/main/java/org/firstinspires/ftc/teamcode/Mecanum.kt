package org.firstinspires.ftc.teamcode

import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import kotlin.math.*

class Mecanum {
    private companion object {
        const val INCHES_PER_FEET = 12
        const val MILLIMETERS_PER_INCH = 25.4
        const val DEGREES_PER_ROTATION = 360

        const val COUNTS_PER_ROTATION = 537.6
        const val WHEEL_DIAMETER_MILLIMETERS = 100.0

        const val WHEEL_DIAMETER_INCHES = WHEEL_DIAMETER_MILLIMETERS / MILLIMETERS_PER_INCH
        const val WHEEL_CIRCUMFERENCE_INCHES = WHEEL_DIAMETER_INCHES * PI
        const val COUNTS_PER_INCH = COUNTS_PER_ROTATION / WHEEL_CIRCUMFERENCE_INCHES
        const val COUNTS_PER_FEET = COUNTS_PER_INCH * INCHES_PER_FEET

        const val ROBOT_CIRCUMFERENCE_COUNTS = 3400.0

        const val MAX_POWER = 0.9
    }

//    private val imu = Imu()

    private lateinit var hubs: List<LynxModule>

    private lateinit var fl: DcMotorEx
    private lateinit var fr: DcMotorEx
    private lateinit var bl: DcMotorEx
    private lateinit var br: DcMotorEx
    private lateinit var motors: Array<DcMotorEx>

    fun initialize() {
//        imu.initialize()
        hubs = hardwareMap.getAll(LynxModule::class.java)
        for (hub in hubs) hub.bulkCachingMode = LynxModule.BulkCachingMode.MANUAL

        fl = hardwareMap.get(DcMotorEx::class.java, ::fl.name)
        fr = hardwareMap.get(DcMotorEx::class.java, ::fr.name)
        bl = hardwareMap.get(DcMotorEx::class.java, ::bl.name)
        br = hardwareMap.get(DcMotorEx::class.java, ::br.name)
        motors = arrayOf(fl, fr, bl, br)

        for (i in motors.indices) motors[i].direction =
            if (i % 2 == 0) DcMotorSimple.Direction.REVERSE else DcMotorSimple.Direction.FORWARD

        for (motor in motors) {
            motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

            motor.targetPosition = 0

            motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

            motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        }
    }

    fun update() {
        read()
        odometry()
        drive()
    }

    fun read() {
        for (motor in motors) {
            motor.currentPosition

//            i.velocity

            motor.isBusy
        }
    }

    @Volatile
    private var location = Point()

    @Volatile
    private var heading = 0.0

    private var lastAPosition = 0.0
    private var lastBPosition = 0.0
    fun odometry() {
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
        ) / sqrt(2.0)
        changePosition = changePosition.rotatedAboutOrigin(heading)

        location += changePosition / COUNTS_PER_INCH

        lastAPosition = aPosition
        lastBPosition = bPosition
    }

    fun tempauto() {
        for (motor in motors) {
            motor.targetPosition = (12 * COUNTS_PER_FEET).roundToInt()
            motor.mode = DcMotor.RunMode.RUN_TO_POSITION
            motor.power = 0.25
        }
    }

    private fun drive() {
//        val translate = Vector(
//            -gamepad1.left_stick_x.toDouble(),
//            gamepad1.left_stick_y.toDouble()
//        ).rotatedAboutOrigin(heading)
//        val x = translate.x
//        val y = translate.y

        val x = -gamepad1.left_stick_x.toDouble() * 0.8
        val y = gamepad1.left_stick_y.toDouble() * 0.8

        val turn = -gamepad1.right_stick_x.toDouble() * 0.7

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

    private var lastTargetHeading = 0.0
    private var lastTargetLocation = Point()
    fun move(
        x: Number = lastTargetLocation.x,
        y: Number = lastTargetLocation.y,
        heading: Number = lastTargetHeading
    ) {
        val targetLocation = Point(x, y)

        val differenceAngle = heading.toDouble() - this.heading
        val displacementAngle = (differenceAngle + 180.0) % 360.0 - 180.0
        val targetHeading = this.heading + displacementAngle

        do {
            var remainingLocationDisplacement = targetLocation - this.location

            val aPower: Double
            val bPower: Double
            if (remainingLocationDisplacement.magnitude < 1.0) {
                aPower = 0.0
                bPower = 0.0
            } else {
                remainingLocationDisplacement =
                    remainingLocationDisplacement.rotatedAboutOrigin(-this.heading)

                val aRemainingDisplacement =
                    remainingLocationDisplacement.x + remainingLocationDisplacement.y
                val bRemainingDisplacement =
                    -remainingLocationDisplacement.x + remainingLocationDisplacement.y

                val maxRemainingDisplacement =
                    max(abs(aRemainingDisplacement), abs(bRemainingDisplacement))

                aPower = aRemainingDisplacement / maxRemainingDisplacement
                bPower = bRemainingDisplacement / maxRemainingDisplacement
            }

            val remainingAngleDisplacement = targetHeading - this.heading

            val leftPower: Double
            val rightPower: Double
            if (remainingAngleDisplacement < 5.0) {
                leftPower = 0.0
                rightPower = 0.0
            } else {
                rightPower = sign(remainingAngleDisplacement)
                leftPower = -rightPower
            }

            val flPower = aPower + leftPower
            val frPower = bPower + rightPower
            val blPower = bPower + leftPower
            val brPower = aPower + rightPower
            val powers = doubleArrayOf(flPower, frPower, blPower, brPower)

            val max = powers.map { abs(it) }.maxOrNull()!!

            powers.zip(motors) { power, motor ->
                motor.power = power / max * MAX_POWER
            }
        } while (true)
    }

    fun telemetry() {
        telemetry.addData("x", location.x)
        telemetry.addData("y", location.y)
        telemetry.addData("heading", heading)
    }
}
