package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.Alliance
import org.firstinspires.ftc.teamcode.gamepad1
import org.firstinspires.ftc.teamcode.hardwareMap
import org.firstinspires.ftc.teamcode.telemetry
import kotlin.math.abs

class Carousel {
    private companion object {
        // chosen
        const val SLOW_POWER = 0.55
        const val FAST_POWER = 0.9
        const val SLOW_CAROUSEL_ROTATIONS = 1.0
        const val FAST_CAROUSEL_ROTATIONS = 1.0

        // measured

        const val COUNTS_PER_WHEEL_ROTATION = 537.6

        const val WHEEL_DIAMETER_INCHES = 4.0
        const val CAROUSEL_DIAMETER_INCHES = 15.0

        // derived

        const val COUNTS_PER_CAROUSEL_ROTATION =
            COUNTS_PER_WHEEL_ROTATION / WHEEL_DIAMETER_INCHES * CAROUSEL_DIAMETER_INCHES

        const val SLOW_COUNTS = SLOW_CAROUSEL_ROTATIONS * COUNTS_PER_CAROUSEL_ROTATION
        const val FAST_COUNTS =
            (SLOW_CAROUSEL_ROTATIONS + FAST_CAROUSEL_ROTATIONS) * COUNTS_PER_CAROUSEL_ROTATION
    }

    private lateinit var carousel: DcMotorEx

    fun initialize() {
        carousel = hardwareMap.get(DcMotorEx::class.java, ::carousel.name)

        carousel.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        carousel.targetPosition = 0

        carousel.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        carousel.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    private var startPosition = 0
    private var flag = true
    fun update() {
        when {
            gamepad1.dpad_left ->
                deliver(Alliance.RED)
            gamepad1.dpad_right ->
                deliver(Alliance.BLUE)
            else -> {
                off()

                flag = true
            }
        }
    }

    fun deliver(alliance: Alliance) {
        if (flag) {
            startPosition = carousel.currentPosition

            flag = false
        }

        val position = abs(carousel.currentPosition - startPosition)
        when {
            position < SLOW_COUNTS -> {
                slow(alliance)
            }
            position < FAST_COUNTS -> {
                fast(alliance)
            }
            else -> {
                slow(alliance)
            }
        }
    }

    private fun slow(alliance: Alliance) {
        carousel.power = alliance.value * SLOW_POWER
    }

    private fun fast(alliance: Alliance) {
        carousel.power = alliance.value * FAST_POWER
    }

    private fun off() {
        carousel.power = 0.0
    }

    fun telemetry() {
        telemetry.addData("carousel power", carousel.power)
        telemetry.addData("carousel position", carousel.currentPosition)
        telemetry.addData("slow counts", SLOW_COUNTS)
    }
}
