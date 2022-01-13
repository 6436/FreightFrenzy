package org.firstinspires.ftc.teamcode.tuning

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx

@TeleOp
class Carousel : OpMode() {
    private companion object {
        const val INCREMENT = 0.05
    }

    private lateinit var carousel: DcMotorEx

    override fun init() {
        carousel = hardwareMap.get(DcMotorEx::class.java, ::carousel.name)

        carousel.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        carousel.targetPosition = 0

        carousel.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        carousel.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    private var power = 0.0
    private var flag = true
    override fun loop() {
        if (gamepad1.a) {
            if (flag) {
                power += INCREMENT
                flag = false
            }
        } else if (gamepad1.b) {
            if (flag) {
                power -= INCREMENT
                flag = false
            }
        } else {
            flag = true
        }

        carousel.power = power

        telemetry.addData("power", power)
    }
}
