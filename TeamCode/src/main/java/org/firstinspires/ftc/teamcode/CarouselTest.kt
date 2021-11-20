package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit

@TeleOp
class CarouselTest : OpMode() {
    private lateinit var carousel: DcMotorEx
    override fun init() {
        carousel = hardwareMap.get(DcMotorEx::class.java, ::carousel.name)

        carousel.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        carousel.targetPosition = 0

        carousel.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        carousel.mode = DcMotor.RunMode.RUN_USING_ENCODER
    }

    private var velocity = 0.0
    override fun loop() {
        velocity += if (gamepad1.a) 1 else if (gamepad1.b) -1 else 0
        carousel.setVelocity(velocity, AngleUnit.DEGREES)
        telemetry.addData("velocity", velocity)
    }
}
