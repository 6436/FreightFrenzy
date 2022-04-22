package org.firstinspires.ftc.teamcode.hardware

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.teamcode.*
import kotlin.math.*

@Config
class Mecanum(private val POWER: Double = 0.95) {
    private companion object {
        // tuned

        const val TARGET_LOCATION_TOLERANCE_INCHES = 2.5
        const val TARGET_HEADING_TOLERANCE_DEGREES = 3.0

        const val X_ODOMETRY_COUNTS_PER_ROTATION =  45004.1666
        const val Y_ODOMETRY_COUNTS_PER_ROTATION = 61288.8295

        const val TRANSLATIONAL_DECELERATION_INCHES_PER_SECOND_PER_SECOND = 50.0
        const val ROTATIONAL_DECELERATION_DEGREES_PER_SECOND_PER_SECOND = 515.0
        const val TRANSLATIONAL_DECELERATION_MULTIPLIER = 0.7
        const val ROTATION_DECELERATION_MULTIPLIER = 1.0

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
    }

    private lateinit var alliance: Alliance

    private lateinit var hubs: List<LynxModule>

    private lateinit var fl: DcMotorEx
    private lateinit var fr: DcMotorEx
    private lateinit var bl: DcMotorEx
    private lateinit var br: DcMotorEx
    private lateinit var motors: Array<DcMotorEx>

    fun initialize(alliance: Alliance = Alliance.RED, brake: Boolean = true) {
        this.alliance = alliance

        hubs = hardwareMap.getAll(LynxModule::class.java)
        for (hub in hubs) hub.bulkCachingMode = LynxModule.BulkCachingMode.MANUAL

        fl = hardwareMap.get(DcMotorEx::class.java, ::fl.name)
        fr = hardwareMap.get(DcMotorEx::class.java, ::fr.name)
        bl = hardwareMap.get(DcMotorEx::class.java, ::bl.name)
        br = hardwareMap.get(DcMotorEx::class.java, ::br.name)
        motors = arrayOf(fl, fr, bl, br)

        for (motor in motors) {

            motor.zeroPowerBehavior =
                if (brake) DcMotor.ZeroPowerBehavior.BRAKE else DcMotor.ZeroPowerBehavior.FLOAT

            motor.targetPosition = 0

            motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

            motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        }

        fl.direction = DcMotorSimple.Direction.REVERSE
        bl.direction = DcMotorSimple.Direction.REVERSE
        fr.direction = DcMotorSimple.Direction.FORWARD
        br.direction = DcMotorSimple.Direction.FORWARD
    }

    fun reset() {
        for (motor in motors) {
            motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

            motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        }

        location = TwoDimensionalPoint()
        heading = 0.0
        locationChange = TwoDimensionalPoint()
        headingChange = 0.0
        locationChangeSpeedAverage = 0.0
        headingChangeSpeedAverage = 0.0

        lastTime = 0.0
        locationChangeSpeeds = DoubleArray(13)
        locationChangeSpeedsIndex = 0
        headingChangeSpeeds = DoubleArray(13)
        headingChangeSpeedsIndex = 0
        lastLeftPosition = 0
        lastRightPosition = 0
        lastBackPosition = 0
    }

    fun update() {
        val (xPower, yPower) = TwoDimensionalVector(
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

    var location = TwoDimensionalPoint()
    var heading = 0.0
    private var locationChange = TwoDimensionalPoint()
    private var headingChange = 0.0
    private var locationChangeSpeedAverage = 0.0
    private var headingChangeSpeedAverage = 0.0

    private var lastTime = 0.0
    private var locationChangeSpeeds = DoubleArray(13)
    private var locationChangeSpeedsIndex = 0
    private var headingChangeSpeeds = DoubleArray(13)
    private var headingChangeSpeedsIndex = 0
    private var lastLeftPosition = 0
    private var lastRightPosition = 0
    private var lastBackPosition = 0
    fun odometry() {
        val time = System.nanoTime() / NANOSECONDS_PER_SECOND
        val timeChange = time - lastTime

        // bulk read
        for (hub in hubs) hub.clearBulkCache()

        val leftCurrentPosition = -fr.currentPosition
        val rightCurrentPosition = fl.currentPosition
        val backCurrentPosition = -br.currentPosition

        val newHeading =
            (-leftCurrentPosition + rightCurrentPosition) / 2.0 / Y_ODOMETRY_COUNTS_PER_DEGREE

        headingChange = newHeading - heading

        /* assumes robot follows straight path between updates
           (as opposed to using "pose exponential" to correct for curvature), because
           loop time vs. error tradeoff is untested,
           it is simpler to implement,
           our loop time is short (improving curve approximation), and
           robot only needs to follow straight paths (for now) */
        locationChange = run {
            val leftPositionChange = leftCurrentPosition - lastLeftPosition
            val rightPositionChange = rightCurrentPosition - lastRightPosition
            val backPositionChange = backCurrentPosition - lastBackPosition

            TwoDimensionalVector(
                backPositionChange - (headingChange * X_ODOMETRY_COUNTS_PER_DEGREE),
                (leftPositionChange + rightPositionChange) / 2.0
            ).rotatedAboutOrigin(heading) / ODOMETRY_WHEEL_COUNTS_PER_INCH
        }

        // update
        lastTime = time

        location += locationChange

        heading = newHeading

        val locationChangeSpeed = locationChange.magnitude / timeChange
        locationChangeSpeeds[++locationChangeSpeedsIndex % locationChangeSpeeds.size] =
            locationChangeSpeed
        locationChangeSpeedAverage = locationChangeSpeeds.average()

        val headingChangeSpeed = headingChange / timeChange
        headingChangeSpeeds[++headingChangeSpeedsIndex % headingChangeSpeeds.size] =
            headingChangeSpeed
        headingChangeSpeedAverage = headingChangeSpeeds.average()

        lastLeftPosition = leftCurrentPosition
        lastRightPosition = rightCurrentPosition
        lastBackPosition = backCurrentPosition
    }

    private fun speedIsEnoughToReachTarget(
        speed: Double,
        deceleration: Double,
        remainingDistance: Double
    ) = speed.squared() / (2.0 * deceleration) >= remainingDistance.absoluteValue

    private var lastTargetLocation = TwoDimensionalPoint()
    private var lastTargetHeading = 0.0
    fun move(
        x: Number = lastTargetLocation.x,
        y: Number = lastTargetLocation.y,
        heading: Number = lastTargetHeading,
        power: Double = POWER,
        brake: Boolean = true,
        slot: () -> Unit = {}
    ) {
        var flag = true
        val targetLocation = TwoDimensionalPoint(alliance.value * x.toDouble(), y)

        val targetHeading = run {
            val headingDifference = alliance.value * heading.toDouble() - this.heading
            val headingDisplacement =
                (headingDifference + 180.0) % 360.0 - 180.0

            this.heading + headingDisplacement
        }

        var translationalFlag = true
        var rotationalFlag = true
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
                if (brake && (remainingTranslationalDistance < TARGET_LOCATION_TOLERANCE_INCHES ||
                            speedIsEnoughToReachTarget(
                                locationChangeSpeedAverage,
                                TRANSLATIONAL_DECELERATION_INCHES_PER_SECOND_PER_SECOND * TRANSLATIONAL_DECELERATION_MULTIPLIER,
                                remainingTranslationalDistance
                            ))
                ) {
                    translationalFlag = false

                    TwoDimensionalVector()
                } else {
                    val aRemainingDisplacement =
                        remainingLocationDisplacement.x + remainingLocationDisplacement.y
                    val bRemainingDisplacement =
                        -remainingLocationDisplacement.x + remainingLocationDisplacement.y

                    val maxRemainingDisplacement =
                        max(abs(aRemainingDisplacement), abs(bRemainingDisplacement))

                    (TwoDimensionalVector(
                        aRemainingDisplacement,
                        bRemainingDisplacement
                    ) / maxRemainingDisplacement
                            * if (translationalFlag) 1.0 else 0.3
                            )
                }

            val remainingHeadingDisplacement = targetHeading - this.heading

            val remainingRotationalDistance = remainingHeadingDisplacement.absoluteValue

            // find rotational power
            val rotationalPower =
                if (
                    remainingRotationalDistance < TARGET_HEADING_TOLERANCE_DEGREES ||
                    (sign(headingChange) == sign(remainingHeadingDisplacement) && speedIsEnoughToReachTarget(
                        headingChangeSpeedAverage,
                        ROTATIONAL_DECELERATION_DEGREES_PER_SECOND_PER_SECOND * ROTATION_DECELERATION_MULTIPLIER,
                        remainingRotationalDistance
                    ))
                ) {
                    rotationalFlag = false

                    0.0
                } else (-sign(remainingHeadingDisplacement)
                        * if (rotationalFlag) 1.0 else 0.3
                        )

            setPowers(
                aPower + rotationalPower,
                bPower - rotationalPower,
                bPower + rotationalPower,
                aPower - rotationalPower,
                power
            )

            slot()

            telemetry.addData("lcaot", locationChangeSpeedAverage)
//            telemetry.addData("a power", aPower)
//            telemetry.addData("b power", bPower)
//            telemetry.addData("rotational power", rotationalPower)
//
//            telemetry()
            telemetry.update()
            if (locationChangeSpeedAverage>10) {
                flag=false
            }
            if (!flag && locationChangeSpeedAverage<0.1 ) {
                break
            }
        } while (
            !(remainingTranslationalDistance < TARGET_LOCATION_TOLERANCE_INCHES && remainingRotationalDistance < TARGET_HEADING_TOLERANCE_DEGREES) &&
            !isStopRequested())

        setPowers(0.0)

        lastTargetLocation = TwoDimensionalPoint(x, y)
        lastTargetHeading = heading.toDouble()
    }

    fun setPowers(
        power: Double
    ) {
        setPowers(power, power, power, power)
    }

    fun setPowers(
        flPower: Double,
        frPower: Double,
        blPower: Double,
        brPower: Double,
        maxPower: Double = POWER,
        maximize: Boolean = false,
    ) {
        val powers = doubleArrayOf(flPower, frPower, blPower, brPower)

        val highestPower =
            powers.map { abs(it) }.run { if (!maximize) plus(1.0) else this }.maxOrNull()!!

        powers.zip(motors) { power, motor ->
            motor.power = power / highestPower * maxPower
        }
    }

    fun telemetry() {
        telemetry.addData("x", location.x)
        telemetry.addData("y", location.y)
        telemetry.addData("heading", heading)

        telemetry.addData(
            "location change speed average ${locationChangeSpeeds.size}",
            locationChangeSpeedAverage
        )
        telemetry.addData(
            "heading change speed average ${headingChangeSpeeds.size}",
            headingChangeSpeedAverage
        )
    }
}
