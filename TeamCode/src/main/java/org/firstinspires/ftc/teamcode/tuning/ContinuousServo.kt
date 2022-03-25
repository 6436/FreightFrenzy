package org.firstinspires.ftc.teamcode.tuning

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.CRServo
import org.firstinspires.ftc.teamcode.BaseTeleOp

@TeleOp
class ContinuousServo : BaseTeleOp() {
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
