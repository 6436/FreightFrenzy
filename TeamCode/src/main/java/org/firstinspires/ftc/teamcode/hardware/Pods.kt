package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.hardwareMap
import org.firstinspires.ftc.teamcode.telemetry

class Pods {
    private lateinit var left: Servo
    private lateinit var right: Servo
    private lateinit var back: Servo

    fun initialize() {
        left = hardwareMap.get(Servo::class.java, ::left.name)
        right = hardwareMap.get(Servo::class.java, ::right.name)
        back = hardwareMap.get(Servo::class.java, ::back.name)
    }

    fun up() {
        left.position = 0.0
        right.position = 0.3005555555
        back.position = 0.0
    }

    fun down() {
        left.position = 0.0
        right.position = 0.0
        back.position = 0.0
    }

    fun telemetry() {
        telemetry.addData("left currentPosition", left.position)
        telemetry.addData("right position", right.position)
        telemetry.addData("back position", back.position)
    }
}
