package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.gamepad2
import org.firstinspires.ftc.teamcode.hardwareMap
import org.firstinspires.ftc.teamcode.telemetry

class Cap {
    private lateinit var nissan: Servo
    private lateinit var maximus: Servo

    fun initialize() {
        nissan = hardwareMap.get(Servo::class.java, ::nissan.name)
        maximus = hardwareMap.get(Servo::class.java, ::maximus.name)
    }

    fun update() {
        when {
            gamepad2.left_bumper && gamepad2.dpad_up -> nissan.position += 0.002
            gamepad2.left_bumper && gamepad2.dpad_down -> nissan.position -= 0.002
            gamepad2.dpad_up -> maximus.position += 0.002
            gamepad2.dpad_down -> maximus.position -= 0.002
        }
    }

    fun telemetry() {
        telemetry.addData("nissan currentPosition", nissan.position)
        telemetry.addData("maximus currentPosition", maximus.position)
    }
}
