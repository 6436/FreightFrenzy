package org.firstinspires.ftc.teamcode.testing

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.CRServo
import org.firstinspires.ftc.teamcode.opmodes.teleop.TeleOpMode

@TeleOp
class ContinuousServo : TeleOpMode() {
    private lateinit var servo: CRServo

    override fun initialize() {
        servo = hardwareMap.get(CRServo::class.java, ::servo.name)
    }

    override fun update() {
        servo.power = when {
            gamepad1.x -> 0.25
            gamepad1.y -> 0.75
            else -> 0.5
        }

        telemetry.addData("servo position", servo.power)
    }
}
