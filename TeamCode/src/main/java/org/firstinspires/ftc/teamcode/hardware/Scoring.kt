package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.gamepad2
import org.firstinspires.ftc.teamcode.hardwareMap
import org.firstinspires.ftc.teamcode.telemetry

class Scoring {
    private companion object {
        const val CLOSE_POSITION = 0.0
        const val OPEN_POSITION = 0.0
    }

    private lateinit var scoring: Servo

    fun initialize() {
        scoring = hardwareMap.get(Servo::class.java, ::scoring.name)
    }

    fun update() {
        when {
            gamepad2.a -> scoring.position += 0.01
            gamepad2.b -> scoring.position -= 0.01
//            gamepad2.a -> close()
//            gamepad2.b -> open()
        }
    }

    fun close() {
        scoring.position = CLOSE_POSITION
    }

    fun open() {
        scoring.position = OPEN_POSITION
    }

    fun telemetry() {
        telemetry.addData("scoring position", scoring.position)
    }
}
