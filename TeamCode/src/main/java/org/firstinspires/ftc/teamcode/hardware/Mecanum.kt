package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.teamcode.*
import kotlin.math.*

class Mecanum {
    private companion object {
        const val POWER = 0.9
        const val TARGET_LOCATION_TOLERANCE = 1.0
        const val TARGET_HEADING_TOLERANCE = 3.0

        const val X_ODOMETRY_COUNTS_PER_ROTATION = 81411.14764756098
        const val Y_ODOMETRY_COUNTS_PER_ROTATION = 133794.1723051728
        const val FRICTION_DECELERATION_INCHES_PER_SECOND_PER_SECOND = 3

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

    @Volatile
    private var locationChangeSpeed = 0.0

    @Volatile
    private var headingChangeSpeed = 0.0

    private var lastTime = 0.0
    private var lastLeftPosition = 0
    private var lastRightPosition = 0
    private var lastBackPosition = 0
    fun odometry() {
        val time = System.nanoTime() / NANOSECONDS_PER_SECOND
        val timeChange = time - lastTime

        // bulk read
        for (hub in hubs) hub.clearBulkCache()
        fl.currentPosition
        fr.currentPosition
        bl.currentPosition

        val leftCurrentPosition = -fl.currentPosition
        val rightCurrentPosition = fr.currentPosition
        val backCurrentPosition = bl.currentPosition

        val newHeading =
            (-leftCurrentPosition + rightCurrentPosition) / Y_ODOMETRY_COUNTS_PER_DEGREE

        val headingChange = newHeading - heading

        val locationChange = run {
            val leftPositionChange = leftCurrentPosition - lastLeftPosition
            val rightPositionChange = rightCurrentPosition - lastRightPosition
            val backPositionChange = backCurrentPosition - lastBackPosition

            Vector(
                backPositionChange - (headingChange * X_ODOMETRY_COUNTS_PER_DEGREE),
                (leftPositionChange + rightPositionChange) / 2.0
            ).rotatedAboutOrigin(heading) / WHEEL_COUNTS_PER_INCH
        }

        // update
        location += locationChange
        heading = newHeading
        locationChangeSpeed = run {
            val newLocationChangeSpeed = locationChange.magnitude / timeChange
            telemetry.addData(
                "location change speed change velocity",
                (newLocationChangeSpeed - locationChangeSpeed) / timeChange
            )
            newLocationChangeSpeed
        }
        headingChangeSpeed = headingChange / timeChange

        lastTime = time
        lastLeftPosition = leftCurrentPosition
        lastRightPosition = rightCurrentPosition
        lastBackPosition = backCurrentPosition

        telemetry.addData("loop time milliseconds", timeChange * MILLISECONDS_PER_SECOND)
        telemetry.addData("x", location.x)
        telemetry.addData("y", location.y)
        telemetry.addData("heading", heading)
        telemetry.addData("location change magnitude", locationChange.magnitude)
        telemetry.addData("heading change absolute value", headingChange.absoluteValue)
    }

    fun update() {
        val (x, y) = Vector(
            gamepad1.left_stick_x,
            -gamepad1.left_stick_y
        ).rotatedAboutOrigin(-heading)

        val turn = gamepad1.right_stick_x.toDouble()

        val powers = run {
            val flPower = y + x + turn
            val frPower = y - x - turn
            val blPower = y - x + turn
            val brPower = y + x - turn

            doubleArrayOf(flPower, frPower, blPower, brPower)
        }

        val maxPower = (powers.map { abs(it) } + 1.0).maxOrNull()!!

        powers.zip(motors) { power, motor ->
            motor.power = power / maxPower * POWER
        }
    }

    private var lastTargetLocation = Point()
    private var lastTargetHeading = 0.0
    fun move(
        x: Number = lastTargetLocation.x,
        y: Number = lastTargetLocation.y,
        heading: Number = lastTargetHeading,
        stop: Boolean = true
    ) {
        fun speedIsEnoughToReachTarget(speed: Double, remainingDisplacement: Double) =
            speed.squared() / (2.0 * FRICTION_DECELERATION_INCHES_PER_SECOND_PER_SECOND) > remainingDisplacement.absoluteValue

        val startingHeading = this.heading

        val targetLocation = Point(x, y)

        val targetHeading = run {
            val headingDifference = heading.toDouble() - startingHeading
            val headingDisplacement =
                (headingDifference + 180.0) % 360.0 - 180.0

            startingHeading + headingDisplacement
        }

        do {
            // read
            val (currentLocation, currentHeading) = this.location to this.heading

            // adjusted by current heading to be relative to robot
            val remainingLocationDisplacement =
                (targetLocation - currentLocation).rotatedAboutOrigin(-currentHeading)

            // find translational powers
            val (aPower, bPower) =
                if (stop && speedIsEnoughToReachTarget(
                        locationChangeSpeed,
                        remainingLocationDisplacement.magnitude
                    )
                ) {
                    Vector()
                } else {
                    val aRemainingDisplacement =
                        remainingLocationDisplacement.x + remainingLocationDisplacement.y
                    val bRemainingDisplacement =
                        -remainingLocationDisplacement.x + remainingLocationDisplacement.y

                    val maxRemainingDisplacement =
                        max(abs(aRemainingDisplacement), abs(bRemainingDisplacement))

                    Vector(
                        aRemainingDisplacement,
                        bRemainingDisplacement
                    ) / maxRemainingDisplacement * remainingLocationDisplacement.magnitude
                }

            val remainingHeadingDisplacement = targetHeading - currentHeading

            // find rotational powers
            val (leftPower, rightPower) =
                if (stop && speedIsEnoughToReachTarget(
                        headingChangeSpeed * Y_ODOMETRY_INCHES_PER_DEGREE,
                        remainingHeadingDisplacement * Y_ODOMETRY_INCHES_PER_DEGREE
                    )
                ) {
                    Vector()
                } else {
                    Vector(
                        -sign(remainingHeadingDisplacement),
                        sign(remainingHeadingDisplacement)
                    ) * remainingHeadingDisplacement.absoluteValue * Y_ODOMETRY_INCHES_PER_DEGREE
                }

            // find aggregate powers
            val powers = run {
                val flPower = aPower + leftPower
                val frPower = bPower + rightPower
                val blPower = bPower + leftPower
                val brPower = aPower + rightPower

                doubleArrayOf(flPower, frPower, blPower, brPower)
            }

            val maxPower = powers.map { abs(it) }.maxOrNull()!!

            powers.zip(motors) { power, motor ->
                motor.power = power / maxPower
            }
        } while ((remainingLocationDisplacement.magnitude > TARGET_LOCATION_TOLERANCE || remainingHeadingDisplacement.absoluteValue > TARGET_HEADING_TOLERANCE) && !isStopRequested())

        lastTargetLocation = targetLocation
        lastTargetHeading = targetHeading
    }
}
