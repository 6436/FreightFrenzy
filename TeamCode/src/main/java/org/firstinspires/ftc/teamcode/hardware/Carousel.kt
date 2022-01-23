package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.teamcode.gamepad1
import org.firstinspires.ftc.teamcode.hardwareMap
import org.firstinspires.ftc.teamcode.telemetry

class Carousel {
    private companion object {
        //        // 365 degrees
//        const val WHEEL_CIRCUMFERENCE_INCHES = Math.PI * 4
//        const val CAROUSEL_CIRCUMFERENCE_INCHES = Math.PI * 15
//        const val COUNTS_PER_ROTATION = 537.6
//        const val COUNTS_PER_INCH = COUNTS_PER_ROTATION / WHEEL_CIRCUMFERENCE_INCHES
        const val POWER = 0.5
    }

    private lateinit var carousel: DcMotorEx

    fun initialize() {
        carousel = hardwareMap.get(DcMotorEx::class.java, ::carousel.name)

        carousel.direction = DcMotorSimple.Direction.REVERSE

        carousel.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        carousel.targetPosition = 0

        carousel.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        carousel.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    fun update() {
        when {
            gamepad1.dpad_left -> red()
            gamepad1.dpad_right -> blue()
            else -> off()
        }
    }

    fun red() {
        carousel.power = POWER
    }

    fun blue() {
        carousel.power = -POWER
    }

    fun off() {
        carousel.power = 0.0
    }

    fun telemetry() {
        telemetry.addData("carousel power", carousel.power)
    }
}
