package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo

@TeleOp
class ServoAdjust : OpMode() {

    private lateinit var servo: Servo

    override fun init() {
        servo = hardwareMap.get(Servo::class.java, ::servo.name)
        servo.position = 0.0

    }

    override fun loop() {

        when {
            gamepad1.dpad_up -> {
                servo.position += 0.001
            }
            gamepad1.dpad_down -> {
                servo.position -= 0.001
            }
        }

        telemetry.addData("Servo Pos: ", servo.position)
    }
}