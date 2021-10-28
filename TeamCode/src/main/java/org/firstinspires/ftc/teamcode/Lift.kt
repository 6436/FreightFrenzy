package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo

@TeleOp
class Lift : Motor() {
    private lateinit var servo: Servo

    override fun init() {
        super.init()

        servo = hardwareMap.get(Servo::class.java, ::servo.name)
    }

    override fun loop() {
        super.loop()

        if (gamepad1.x) {
            servo.position = 0.7
        } else if (gamepad1.y) {
            servo.position = 0.0
        } else if (gamepad1.dpad_up) {
            servo.position += 0.01
        } else if (gamepad1.dpad_down) {
            servo.position -= 0.01
        }
    }
}
