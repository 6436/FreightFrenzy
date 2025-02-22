package org.firstinspires.ftc.teamcode.opmodes.testing

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.opmodes.TeleOpMode

@TeleOp
class Servo : TeleOpMode() {
    private lateinit var servo: Servo

    override fun initialize() {
        servo = hardwareMap.get(Servo::class.java, ::servo.name)

        servo.position = 0.0
    }

    override fun update() {
        when {
            gamepad1.x -> {
                servo.position += 0.001
            }
            gamepad1.y -> {
                servo.position -= 0.001
            }
        }

        telemetry.addData("servo position", servo.position)
        telemetry.update()
    }
}
