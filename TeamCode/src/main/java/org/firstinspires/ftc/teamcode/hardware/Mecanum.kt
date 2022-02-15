package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.teamcode.*
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sign

class Mecanum {
    private companion object {
        const val POWER = 0.9

        const val X_ODOMETRY_COUNTS_PER_ROTATION = 81411.14764756098
        const val Y_ODOMETRY_COUNTS_PER_ROTATION = 133794.1723051728

        const val MOTOR_COUNTS_PER_ROTATION = 8192.0
        const val WHEEL_DIAMETER_MILLIMETERS = 96.0

        const val WHEEL_DIAMETER_INCHES = WHEEL_DIAMETER_MILLIMETERS / MILLIMETERS_PER_INCH
        const val WHEEL_INCHES_PER_ROTATION = WHEEL_DIAMETER_INCHES * PI
        const val WHEEL_COUNTS_PER_INCH =
            MOTOR_COUNTS_PER_ROTATION / WHEEL_INCHES_PER_ROTATION

        const val X_ODOMETRY_COUNTS_PER_DEGREE =
            X_ODOMETRY_COUNTS_PER_ROTATION / DEGREES_PER_ROTATION
        const val Y_ODOMETRY_COUNTS_PER_DEGREE =
            Y_ODOMETRY_COUNTS_PER_ROTATION / DEGREES_PER_ROTATION
        const val Y_ODOMETRY_INCHES_PER_DEGREE =
            Y_ODOMETRY_COUNTS_PER_DEGREE / WHEEL_COUNTS_PER_INCH
    }

    private lateinit var hubs: List<LynxModule>

    private lateinit var fl: DcMotorEx
    private lateinit var fr: DcMotorEx
    private lateinit var bl: DcMotorEx
    private lateinit var br: DcMotorEx
    lateinit var motors: Array<DcMotorEx>

    fun initialize() {
        hubs = hardwareMap.getAll(LynxModule::class.java)
        for (hub in hubs) hub.bulkCachingMode = LynxModule.BulkCachingMode.MANUAL

        fl = hardwareMap.get(DcMotorEx::class.java, ::fl.name)
        fr = hardwareMap.get(DcMotorEx::class.java, ::fr.name)
        bl = hardwareMap.get(DcMotorEx::class.java, ::bl.name)
        br = hardwareMap.get(DcMotorEx::class.java, ::br.name)
        motors = arrayOf(fl, fr, bl, br)

        for (motor in motors) {
            motor.direction = DcMotorSimple.Direction.REVERSE

            motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT

            motor.targetPosition = 0

            motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

            motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        }
        fl.direction = DcMotorSimple.Direction.FORWARD
    }

    @Volatile
    private var location = Point()

    @Volatile
    private var heading = 0.0

    private var lastHeading = heading
    private var leftLastPosition = 0
    private var rightLastPosition = 0
    private var backLastPosition = 0
    fun odometry() {
        for (hub in hubs) hub.clearBulkCache()

        val leftCurrentPosition = -fl.currentPosition
        val rightCurrentPosition = fr.currentPosition
        val backCurrentPosition = bl.currentPosition

        val leftPositionChange = leftCurrentPosition - leftLastPosition
        val rightPositionChange = rightCurrentPosition - rightLastPosition
        val backPositionChange = backCurrentPosition - backLastPosition

        heading =
            (-leftCurrentPosition + rightCurrentPosition) / Y_ODOMETRY_COUNTS_PER_DEGREE

        val headingChange = heading - lastHeading
        val locationChange = Vector(
            backPositionChange - (headingChange * X_ODOMETRY_COUNTS_PER_DEGREE),
            (leftPositionChange + rightPositionChange) / 2.0
        ).rotatedAboutOrigin(lastHeading) / WHEEL_COUNTS_PER_INCH

        location += locationChange

        lastHeading = heading
        leftLastPosition = leftCurrentPosition
        rightLastPosition = rightCurrentPosition
        backLastPosition = backCurrentPosition

        telemetry.addData("heading", heading)
        telemetry.addData("headingChange", headingChange)
        telemetry.addData("x", location.x)
        telemetry.addData("y", location.y)
    }

    fun update() {
        val translate = Vector(
            gamepad1.left_stick_x,
            -gamepad1.left_stick_y
        ).rotatedAboutOrigin(heading)
        val x = translate.x
        val y = translate.y

        val turn = -gamepad1.right_stick_x.toDouble()

        val flPower = y + x + turn
        val frPower = y - x - turn
        val blPower = y - x + turn
        val brPower = y + x - turn
        val powers = doubleArrayOf(flPower, frPower, blPower, brPower)

        val max = (powers.map { abs(it) } + 1.0).maxOrNull()!!

        powers.zip(motors) { power, motor ->
            motor.power = power / max * POWER
        }
    }

    private var lastTargetLocation = Point()
    private var lastTargetHeading = 0.0
    fun move(
        x: Number = lastTargetLocation.x,
        y: Number = lastTargetLocation.y,
        heading: Number = lastTargetHeading,
        brake: Boolean = true,
        powerx: Double = POWER
    ) {
        for (motor in motors) {
            motor.zeroPowerBehavior =
                if (brake) DcMotor.ZeroPowerBehavior.BRAKE else DcMotor.ZeroPowerBehavior.FLOAT
        }

        val targetLocation = Point(x, y)

        val differenceAngle = heading.toDouble() - this.heading
        val displacementAngle = (differenceAngle + 180.0) % 360.0 - 180.0
        val targetHeading = this.heading + displacementAngle

        do {
            var remainingLocationDisplacement = targetLocation - this.location

            remainingLocationDisplacement =
                remainingLocationDisplacement.rotatedAboutOrigin(-this.heading)

            val aRemainingDisplacement =
                remainingLocationDisplacement.x + remainingLocationDisplacement.y
            val bRemainingDisplacement =
                -remainingLocationDisplacement.x + remainingLocationDisplacement.y

            val maxRemainingDisplacement =
                max(abs(aRemainingDisplacement), abs(bRemainingDisplacement))

            val aPower = aRemainingDisplacement / maxRemainingDisplacement
            val bPower = bRemainingDisplacement / maxRemainingDisplacement

            val remainingAngleDisplacement = targetHeading - this.heading

            val rightPower = sign(remainingAngleDisplacement)
            val leftPower = -rightPower

            val flPower = aPower + leftPower
            val frPower = bPower + rightPower
            val blPower = bPower + leftPower
            val brPower = aPower + rightPower
            val powers = doubleArrayOf(flPower, frPower, blPower, brPower)

            val maxPower = powers.map { abs(it) }.maxOrNull()!!

            powers.zip(motors) { power, motor ->
                motor.power = power / maxPower * powerx
            }
        } while (!(maxPower == 0.0 || isStopRequested()))

        lastTargetLocation = targetLocation
        lastTargetHeading = targetHeading
    }

    fun telemetry() {
        telemetry.addData("fl", fl.currentPosition)
        telemetry.addData("fr", fr.currentPosition)
        telemetry.addData("bl", bl.currentPosition)

//        telemetry.addLine("drivetrain location\n")
//            .addFormattedData("x", location.x)
//            .addFormattedData("y", location.y)
//        telemetry.addFormattedData("drivetrain heading", heading)
    }
}
