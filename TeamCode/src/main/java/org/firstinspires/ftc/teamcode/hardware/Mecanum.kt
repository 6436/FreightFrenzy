package org.firstinspires.ftc.teamcode.hardware

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.teamcode.*
import kotlin.math.*

@Config
class Mecanum {

    companion object {
        // chosen

        const val POWER = 0.4

        // tuned

        const val TARGET_LOCATION_TOLERANCE_INCHES = 2.5
        const val TARGET_HEADING_TOLERANCE_DEGREES = 4.0
        const val X_ODOMETRY_COUNTS_PER_ROTATION = 74198.33941731641
        const val Y_ODOMETRY_COUNTS_PER_ROTATION = 66897.0861526

        @JvmField
        @Volatile
        var TRANSLATIONAL_FRICTION_DECELERATION_INCHES_PER_SECOND_PER_SECOND = 47.0

        @JvmField
        @Volatile
        var ROTATIONAL_FRICTION_DECELERATION_DEGREES_PER_SECOND_PER_SECOND = 1.62

        // measured

        const val ODOMETRY_WHEEL_COUNTS_PER_ROTATION = 8192.0
        const val ODOMETRY_WHEEL_DIAMETER_MILLIMETERS = 35.0

        // derived

        const val ODOMETRY_WHEEL_DIAMETER_INCHES =
            ODOMETRY_WHEEL_DIAMETER_MILLIMETERS / MILLIMETERS_PER_INCH
        const val ODOMETRY_WHEEL_INCHES_PER_ROTATION = ODOMETRY_WHEEL_DIAMETER_INCHES * PI
        const val ODOMETRY_WHEEL_COUNTS_PER_INCH =
            ODOMETRY_WHEEL_COUNTS_PER_ROTATION / ODOMETRY_WHEEL_INCHES_PER_ROTATION

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
            Y_ODOMETRY_COUNTS_PER_DEGREE / ODOMETRY_WHEEL_COUNTS_PER_INCH
    }

    private lateinit var hubs: List<LynxModule>

    private lateinit var fl: DcMotorEx
    private lateinit var fr: DcMotorEx
    private lateinit var bl: DcMotorEx
    private lateinit var br: DcMotorEx
    private lateinit var motors: Array<DcMotorEx>

    fun initialize(brake: Boolean = false) {
        hubs = hardwareMap.getAll(LynxModule::class.java)
        for (hub in hubs) hub.bulkCachingMode = LynxModule.BulkCachingMode.MANUAL

        fl = hardwareMap.get(DcMotorEx::class.java, ::fl.name)
        fr = hardwareMap.get(DcMotorEx::class.java, ::fr.name)
        bl = hardwareMap.get(DcMotorEx::class.java, ::bl.name)
        br = hardwareMap.get(DcMotorEx::class.java, ::br.name)
        motors = arrayOf(fl, fr, bl, br)

        for (motor in motors) {
            motor.direction = DcMotorSimple.Direction.REVERSE

            motor.zeroPowerBehavior =
                if (brake) DcMotor.ZeroPowerBehavior.BRAKE else DcMotor.ZeroPowerBehavior.FLOAT

            motor.targetPosition = 0

            motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

            motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        }
        fl.direction = DcMotorSimple.Direction.FORWARD
    }

    fun update() {
        val (xPower, yPower) = Vector(
            gamepad1.left_stick_x,
            -gamepad1.left_stick_y
        )
//            .rotatedAboutOrigin(-heading)

        val rotationalPower = gamepad1.right_stick_x.toDouble()

        setPowers(
            yPower + xPower + rotationalPower,
            yPower - xPower - rotationalPower,
            yPower - xPower + rotationalPower,
            yPower + xPower - rotationalPower
        )
    }

    private var location = Point()
    private var heading = 0.0
    private var locationChangeSpeed = 0.0
    private var headingChangeSpeed = 0.0

    private var headingChangeSpeedmv = DoubleArray(20)
    private var headingChangeSpeedidx = 0

    private var lastTime = 0.0
    private var lastLeftPosition = 0
    private var lastRightPosition = 0
    private var lastBackPosition = 0
    fun odometry() {
        val time = System.nanoTime() / NANOSECONDS_PER_SECOND
        val timeChange = time - lastTime

        // bulk read
        for (hub in hubs) hub.clearBulkCache()

        val leftCurrentPosition = br.currentPosition
        val rightCurrentPosition = bl.currentPosition
        val backCurrentPosition = fl.currentPosition

        val newHeading =
            (-leftCurrentPosition + rightCurrentPosition) / 2.0 / Y_ODOMETRY_COUNTS_PER_DEGREE

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
            ).rotatedAboutOrigin(heading) / ODOMETRY_WHEEL_COUNTS_PER_INCH
        }

        // update
        lastTime = time
        location += locationChange
        heading = newHeading
        locationChangeSpeed = locationChange.magnitude / timeChange
        headingChangeSpeed = headingChange.absoluteValue / timeChange
        headingChangeSpeedmv[(++headingChangeSpeedidx) % headingChangeSpeedmv.size] =
            headingChangeSpeed
        lastLeftPosition = leftCurrentPosition
        lastRightPosition = rightCurrentPosition
        lastBackPosition = backCurrentPosition

        telemetry.addData("headings", headingChangeSpeed)
        telemetry.addData("time", timeChange)
        telemetry.addData("avg headings ", headingChangeSpeedmv.average())
    }

    private fun speedIsEnoughToReachTarget(
        speed: Double,
        deceleration: Double,
        remainingDistance: Double
    ) = speed.squared() / (2.0 * deceleration) >= remainingDistance.absoluteValue

    private var lastTargetLocation = Point()
    private var lastTargetHeading = 0.0
    fun move(
        x: Number = lastTargetLocation.x,
        y: Number = lastTargetLocation.y,
        heading: Number = lastTargetHeading
    ) {
        val targetLocation = Point(x, y)

        val targetHeading = run {
            val headingDifference = heading.toDouble() - this.heading
            val headingDisplacement =
                (headingDifference + 180.0) % 360.0 - 180.0

            this.heading + headingDisplacement
        }

        do {
            odometry()

            val remainingLocationDisplacement =
                (targetLocation - this.location)
                    // adjust to be relative to current robot position
                    .rotatedAboutOrigin(-this.heading)

            val remainingTranslationalDistance =
                remainingLocationDisplacement.magnitude

            // find translational powers
            val (aPower, bPower) =
                if (
//                    remainingTranslationalDistance < TARGET_LOCATION_TOLERANCE_INCHES ||
                    speedIsEnoughToReachTarget(
                        locationChangeSpeed,
                        TRANSLATIONAL_FRICTION_DECELERATION_INCHES_PER_SECOND_PER_SECOND,
                        remainingTranslationalDistance
                    )
                ) Vector()
                else {
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

            val remainingHeadingDisplacement = targetHeading - this.heading

            val remainingRotationalDistance = remainingHeadingDisplacement.absoluteValue

            // find rotational power
            val rotationalPower =
                if (
//                    remainingRotationalDistance < TARGET_HEADING_TOLERANCE_DEGREES ||
                    speedIsEnoughToReachTarget(
                        headingChangeSpeed,
                        ROTATIONAL_FRICTION_DECELERATION_DEGREES_PER_SECOND_PER_SECOND,
                        remainingRotationalDistance
                    )
                ) 0.0
                else (-sign(remainingHeadingDisplacement)
                        // weight of rotational powers
                        * remainingRotationalDistance * APPROXIMATE_DRIVETRAIN_INCHES_PER_DEGREE
                        )


            setPowers(
                aPower + rotationalPower,
                bPower - rotationalPower,
                bPower + rotationalPower,
                aPower - rotationalPower,
                true
            )

            telemetry()
            telemetry.update()
        } while (
//            (remainingTranslationalDistance > TARGET_LOCATION_TOLERANCE_INCHES || remainingRotationalDistance > TARGET_HEADING_TOLERANCE_DEGREES) &&
            !isStopRequested())

        setPowers(0.0)

        lastTargetLocation = targetLocation
        lastTargetHeading = targetHeading
    }

    /*
//    private var lastTargetLocation = Point()
//    private var lastTargetHeading = 0.0
//    fun move(
//        x: Number = lastTargetLocation.x,
//        y: Number = lastTargetLocation.y,
//        heading: Number = lastTargetHeading
//    ) {
//        fun speedIsEnoughToReachTarget(
//            speed: Double,
//            deceleration: Double,
//            remainingDistance: Double
//        ) =
//            speed.squared() / (2.0 * deceleration) > remainingDistance.absoluteValue
//
//        val startingHeading = this.heading
//
//        val targetLocation = Point(x, y)
//
//        val targetHeading = run {
//            val headingDifference = heading.toDouble() - startingHeading
//            val headingDisplacement =
//                (headingDifference + 180.0) % 360.0 - 180.0
//
//            startingHeading + headingDisplacement
//        }
//
//        var translationalFlag = true
//        var rotationalFlag = true
//        do {
//            // read
//            val (currentLocation, currentHeading) = this.location to this.heading
//
//            val remainingLocationDisplacement =
//                (targetLocation - currentLocation)
//                    // adjust to be relative to current robot position
//                    .rotatedAboutOrigin(-currentHeading)
//
//            val remainingTranslationalDistance =
//                remainingLocationDisplacement.magnitude
//
//            // find translational powers
//            val (aPower, bPower) = if (!translationalFlag || speedIsEnoughToReachTarget(
//                    locationChangeSpeed,
//                    TRANSLATIONAL_FRICTION_DECELERATION_INCHES_PER_SECOND_PER_SECOND,
//                    remainingTranslationalDistance
//                )
//            ) {
//                translationalFlag = false
//
//                Vector()
//            } else {
//                val aRemainingDisplacement =
//                    remainingLocationDisplacement.x + remainingLocationDisplacement.y
//                val bRemainingDisplacement =
//                    -remainingLocationDisplacement.x + remainingLocationDisplacement.y
//
//                val maxRemainingDisplacement =
//                    max(abs(aRemainingDisplacement), abs(bRemainingDisplacement))
//
//                (Vector(
//                    aRemainingDisplacement,
//                    bRemainingDisplacement
//                ) / maxRemainingDisplacement
//                        // weight of translational powers
//                        * remainingTranslationalDistance
//                        )
//            }
//
//            val remainingHeadingDisplacement = targetHeading - currentHeading
//
//            val remainingRotationalDistance =
//                (remainingHeadingDisplacement.absoluteValue) * APPROXIMATE_DRIVETRAIN_INCHES_PER_DEGREE
//
//            // find rotational power
//            val rotationalPower =
//                if (!rotationalFlag || speedIsEnoughToReachTarget(
//                        headingChangeSpeed * APPROXIMATE_DRIVETRAIN_INCHES_PER_DEGREE,
//                        ROTATIONAL_FRICTION_DECELERATION_INCHES_PER_SECOND_PER_SECOND,
//                        remainingRotationalDistance
//                    )
//                ) {
//                    rotationalFlag = false
//
//                    0.0
//                } else {
//                    (-sign(remainingHeadingDisplacement)
//                            // weight of rotational powers
//                            * remainingRotationalDistance
//                            )
//                }
//
//            setPowers(
//                aPower + rotationalPower,
//                bPower - rotationalPower,
//                bPower + rotationalPower,
//                aPower - rotationalPower,
//                true
//            )
//        } while (!(locationChangeSpeed == 0.0 && headingChangeSpeed == 0.0 && remainingTranslationalDistance < TARGET_LOCATION_TOLERANCE && remainingRotationalDistance < TARGET_HEADING_TOLERANCE) && !isStopRequested())
//
//        setPowers(0.0)
//
//        lastTargetLocation = targetLocation
//        lastTargetHeading = targetHeading
//    }
*/

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
            motor.power = power / maxPower * POWER
        }
    }

    fun telemetry() {
        telemetry.addData("x", location.x)
        telemetry.addData("y", location.y)
        telemetry.addData("heading", heading)
    }
}
