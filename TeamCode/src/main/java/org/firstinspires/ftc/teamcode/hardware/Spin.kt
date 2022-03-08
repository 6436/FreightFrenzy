package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.gamepad2
import org.firstinspires.ftc.teamcode.hardwareMap
import org.firstinspires.ftc.teamcode.telemetry

class Spin {
    private companion object {
        const val UP_POSITION = 0.38777777777

        //        const val DUMP_POSITION = 0.42
        const val DOWN_POSITION = 0.274
    }

    private lateinit var spin: Servo

    fun initialize() {
        spin = hardwareMap.get(Servo::class.java, ::spin.name)

        down()
    }

    fun update() {
        when {
            gamepad2.a -> down()
//            gamepad2.x -> dump()
            gamepad2.x || gamepad2.y || gamepad2.right_bumper -> up()
//            gamepad2.dpad_left -> spin.position += 0.002
//            gamepad2.dpad_right -> spin.position -= 0.002
        }
    }

    fun up() {
        spin.position = UP_POSITION
    }
//
//    fun dump() {
//        spin.position = DUMP_POSITION
//    }

    fun down() {
        spin.position = DOWN_POSITION
    }

    fun telemetry() {
        telemetry.addData("spin currentPosition", spin.position)
    }
}
