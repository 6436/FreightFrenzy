package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.teamcode.*
import kotlin.math.*

class Mecanum {
    private companion object {
        // chosen

        const val POWER = 0.9

        // tuned

        const val TARGET_LOCATION_TOLERANCE = 1.0
        const val TARGET_HEADING_TOLERANCE = 3.0
        const val X_ODOMETRY_COUNTS_PER_ROTATION = 81411.14764756098
        const val Y_ODOMETRY_COUNTS_PER_ROTATION = 133794.1723051728
        const val FRICTION_DECELERATION_INCHES_PER_SECOND_PER_SECOND = 3.0

        // measured

        const val MOTOR_COUNTS_PER_ROTATION = 8192.0
        const val WHEEL_DIAMETER_MILLIMETERS = 96.0

        // derived

        const val WHEEL_DIAMETER_INCHES = WHEEL_DIAMETER_MILLIMETERS / MILLIMETERS_PER_INCH
        const val WHEEL_INCHES_PER_ROTATION = WHEEL_DIAMETER_INCHES * PI
        const val WHEEL_COUNTS_PER_INCH =
            MOTOR_COUNTS_PER_ROTATION / WHEEL_INCHES_PER_ROTATION

        const val X_ODOMETRY_COUNTS_PER_DEGREE =
            X_ODOMETRY_COUNTS_PER_ROTATION / DEGREES_PER_ROTATION
        const val Y_ODOMETRY_COUNTS_PER_DEGREE =
            Y_ODOMETRY_COUNTS_PER_ROTATION / DEGREES_PER_ROTATION

        /** derived from [Y_ODOMETRY_COUNTS_PER_ROTATION]
         * (as opposed to a tuned constant DRIVETRAIN_COUNTS_PER_ROTATION), because
         * we can't add encoders to our drivetrain wheels to tune,
         * y-odometry wheel track is bigger than drivetrain (which should cause no errors but slows turning), and
         * y-odometry wheel track is similar in size to drivetrain wheel track
         */
        const val APPROXIMATE_DRIVETRAIN_INCHES_PER_DEGREE =
            Y_ODOMETRY_COUNTS_PER_DEGREE / WHEEL_COUNTS_PER_INCH
    }

    private lateinit var hubs: List<LynxModule>

    private lateinit var fl: DcMotorEx
    private lateinit var fr: DcMotorEx
    private lateinit var bl: DcMotorEx
    private lateinit var br: DcMotorEx
    private lateinit var motors: Array<DcMotorEx>

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

        /* assumes robot follows straight path between updates
           (as opposed to using "pose exponential" to correct for curvature), because
           loop time vs. error tradeoff is untested,
           it is simpler to implement,
           our loop time is short (improving curve approximation), and
           robot only needs to follow straight paths (for now) */
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
        telemetry.addData("heading change magnitude", headingChange.absoluteValue)
    }

    fun update() {
        val (xPower, yPower) = Vector(
            gamepad1.left_stick_x,
            -gamepad1.left_stick_y
        ).rotatedAboutOrigin(-heading)

        val rotationalPower = gamepad1.right_stick_x.toDouble()

        setPowers(
            yPower + xPower + rotationalPower,
            yPower - xPower - rotationalPower,
            yPower - xPower + rotationalPower,
            yPower + xPower - rotationalPower
        )
    }

    private var lastTargetLocation = Point()
    private var lastTargetHeading = 0.0
    fun move(
        x: Number = lastTargetLocation.x,
        y: Number = lastTargetLocation.y,
        heading: Number = lastTargetHeading,
        stop: Boolean = true
    ) {
        fun speedIsEnoughToReachTarget(speed: Double, remainingDistance: Double) =
            speed.squared() / (2.0 * FRICTION_DECELERATION_INCHES_PER_SECOND_PER_SECOND) > remainingDistance.absoluteValue

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

            val remainingLocationDisplacement =
                (targetLocation - currentLocation)
                    // adjust to be relative to current robot position
                    .rotatedAboutOrigin(-currentHeading)

            // find translational powers
            val (aPower, bPower) = run {
                val remainingTranslationalDistance = remainingLocationDisplacement.magnitude
                if (stop && speedIsEnoughToReachTarget(
                        locationChangeSpeed,
                        remainingTranslationalDistance
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

                    (Vector(
                        aRemainingDisplacement,
                        bRemainingDisplacement
                    ) / maxRemainingDisplacement
                            // weight of translational powers
                            * remainingTranslationalDistance
                            )
                }
            }

            val remainingHeadingDisplacement = targetHeading - currentHeading

            // find rotational power
            val rotationalPower = run {
                val remainingRotationalDistance =
                    remainingHeadingDisplacement.absoluteValue * APPROXIMATE_DRIVETRAIN_INCHES_PER_DEGREE
                if (stop && speedIsEnoughToReachTarget(
                        headingChangeSpeed * APPROXIMATE_DRIVETRAIN_INCHES_PER_DEGREE,
                        remainingRotationalDistance
                    )
                ) {
                    0.0
                } else {
                    (-sign(remainingHeadingDisplacement)
                            // weight of rotational powers
                            * remainingRotationalDistance
                            )
                }
            }

            setPowers(
                aPower + rotationalPower,
                bPower - rotationalPower,
                bPower + rotationalPower,
                aPower - rotationalPower,
                true
            )
        } while ((remainingLocationDisplacement.magnitude > TARGET_LOCATION_TOLERANCE || remainingHeadingDisplacement.absoluteValue > TARGET_HEADING_TOLERANCE) && !isStopRequested())

        lastTargetLocation = targetLocation
        lastTargetHeading = targetHeading
    }

    fun setPowers(
        power: Double
    ) {
        setPowers(power, power, power, power, false)
    }

    private fun setPowers(
        flPower: Double,
        frPower: Double,
        blPower: Double,
        brPower: Double,
        maximize: Boolean = false
    ) {
        val powers = doubleArrayOf(flPower, frPower, blPower, brPower)

        val maxPower =
            powers.map { abs(it) }.run { if (!maximize) plus(1.0) else this }.maxOrNull()!!

        powers.zip(motors) { power, motor ->
            motor.power = power / maxPower
        }
    }
}
