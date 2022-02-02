package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.Alliance
import org.firstinspires.ftc.teamcode.gamepad1
import org.firstinspires.ftc.teamcode.hardwareMap
import org.firstinspires.ftc.teamcode.telemetry
import java.lang.Thread.sleep

class Carousel {
    private companion object {
        const val POWER = 0.5
        const val MAX_POWER = 0.9

        const val WHEEL_DIAMETER_INCHES = 4.0
        const val CAROUSEL_DIAMETER_INCHES = 15.0

        const val COUNTS_PER_WHEEL_ROTATION = 537.6

        const val COUNTS_PER_CAROUSEL_ROTATION =
            COUNTS_PER_WHEEL_ROTATION / WHEEL_DIAMETER_INCHES * CAROUSEL_DIAMETER_INCHES
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
            gamepad1.dpad_left -> on(Alliance.RED)
            gamepad1.dpad_right -> on(Alliance.BLUE)
            else -> off()
        }
    }

    private fun on(alliance: Alliance) {
        carousel.power = alliance.value * POWER
    }

    private fun launch(alliance: Alliance) {
        carousel.power = alliance.value * MAX_POWER
    }

    fun deliver(alliance: Alliance) {
        on(alliance)
        sleep(2400)
        off()
    }

    fun off() {
        carousel.power = 0.0
    }

    fun telemetry() {
        telemetry.addData("carousel power", carousel.power)
    }
}
