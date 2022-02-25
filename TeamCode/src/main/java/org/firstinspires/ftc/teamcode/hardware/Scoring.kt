package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.gamepad2
import org.firstinspires.ftc.teamcode.hardwareMap
import org.firstinspires.ftc.teamcode.telemetry

class Scoring {
    private companion object {
        const val OPEN_POSITION = 0.645
        const val DEFAULT_POSITION = 0.4
        const val CLOSE_POSITION = 0.023
    }

    private lateinit var scoring: Servo

    fun initialize() {
        scoring = hardwareMap.get(Servo::class.java, ::scoring.name)

        default()
    }

    fun update() {
        when {
//            gamepad2.b -> open()
//            gamepad2.a -> default()
//            gamepad2.x || gamepad2.y || gamepad2.right_bumper -> close()
            gamepad2.a -> scoring.position += 0.01
            gamepad2.b -> scoring.position -= 0.01
        }
    }

    fun open() {
        scoring.position = OPEN_POSITION
    }

    fun default() {
        scoring.position = DEFAULT_POSITION
    }

    fun close() {
        scoring.position = CLOSE_POSITION
    }

    fun telemetry() {
        telemetry.addData("scoring position", scoring.position)
    }
}
