package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.teamcode.gamepad1
import org.firstinspires.ftc.teamcode.hardwareMap
import org.firstinspires.ftc.teamcode.telemetry

class Carousel {
    private companion object {
        const val WHEEL_DIAMETER_INCHES = 4.0
        const val CAROUSEL_DIAMETER_INCHES = 15.0

        const val COUNTS_PER_WHEEL_ROTATION = 537.6

        const val COUNTS_PER_CAROUSEL_ROTATION =
            COUNTS_PER_WHEEL_ROTATION / WHEEL_DIAMETER_INCHES * CAROUSEL_DIAMETER_INCHES

        const val POWER = 0.5
    }

    private lateinit var carousel: DcMotorEx

    fun initialize() {
        carousel = hardwareMap.get(DcMotorEx::class.java, ::carousel.name)

        carousel.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        carousel.targetPosition = 0

        carousel.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        carousel.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    fun update() {
        when {
            gamepad1.dpad_left -> redOn()
            gamepad1.dpad_right -> blueOn()
            else -> off()
        }
    }

    fun redDeliver() {
    }

    fun redOn() {
        carousel.power = POWER
    }

    fun blueOn() {
        carousel.power = -POWER
    }

    fun off() {
        carousel.power = 0.0
    }

    fun telemetry() {
        telemetry.addData("carousel power", carousel.power)
    }
}
