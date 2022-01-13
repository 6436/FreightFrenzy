package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.gamepad2
import org.firstinspires.ftc.teamcode.hardwareMap

class Scoring {
    private lateinit var scoring: Servo

    fun initialize() {
        scoring = hardwareMap.get(Servo::class.java, ::scoring.name)

        up()
    }

    fun update() {
        when {
            gamepad2.dpad_up || gamepad2.x -> up()
            gamepad2.a -> left()
            gamepad2.b -> right()
        }
    }

    fun up() {
        scoring.position = 0.56
    }

    fun left() {
        scoring.position = 0.98
    }

    fun right() {
        scoring.position = 0.1
    }

    fun telemetry() {
        org.firstinspires.ftc.teamcode.telemetry.addData("scoring position", scoring.position)
    }
}
