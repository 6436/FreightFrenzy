package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.hardwareMap
import org.firstinspires.ftc.teamcode.telemetry

class Pods {
    companion object {
        const val DOWN_POSITION = 1.0
        const val UP_POSITION = 0.0
    }

    private lateinit var left: Servo
    private lateinit var right: Servo
    private lateinit var back: Servo

    fun initialize() {
        left = hardwareMap.get(Servo::class.java, ::left.name)
        right = hardwareMap.get(Servo::class.java, ::right.name)
        back = hardwareMap.get(Servo::class.java, ::back.name)
    }

    fun up() {
        left.position = UP_POSITION
        right.position = UP_POSITION
        back.position = UP_POSITION
    }

    fun down() {
        left.position = DOWN_POSITION
        right.position = DOWN_POSITION
        back.position = DOWN_POSITION
    }

    fun telemetry() {
        telemetry.addData("left position", left.position)
        telemetry.addData("right position", right.position)
        telemetry.addData("back position", back.position)
    }
}
