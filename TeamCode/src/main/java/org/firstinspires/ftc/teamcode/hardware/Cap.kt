package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.gamepad3
import org.firstinspires.ftc.teamcode.hardwareMap
import org.firstinspires.ftc.teamcode.telemetry

class Cap {
    private lateinit var nissan: Servo
    private lateinit var maximus: Servo

    fun initialize() {
        nissan = hardwareMap.get(Servo::class.java, ::nissan.name)
        maximus = hardwareMap.get(Servo::class.java, ::maximus.name)

        maximus.direction = Servo.Direction.REVERSE
    }

    fun update() {
        when {
            gamepad3.left_bumper && gamepad3.dpad_up -> nissan.position += 0.02
            gamepad3.left_bumper && gamepad3.dpad_down -> nissan.position -= 0.02
            gamepad3.dpad_up -> maximus.position += 0.0008
            gamepad3.dpad_down -> maximus.position -= 0.0008
        }
    }

    fun telemetry() {
        telemetry.addData("nissan currentPosition", nissan.position)
        telemetry.addData("maximus currentPosition", maximus.position)
    }
}
