package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.gamepad3
import org.firstinspires.ftc.teamcode.hardwareMap
import org.firstinspires.ftc.teamcode.telemetry

class Cap {
    private lateinit var horizontal: Servo
    private lateinit var vertical: Servo
    private lateinit var extend: CRServo

    fun initialize() {
        horizontal = hardwareMap.get(Servo::class.java, ::horizontal.name)
        vertical = hardwareMap.get(Servo::class.java, ::vertical.name)
        extend = hardwareMap.get(CRServo::class.java, ::extend.name)
    }

    fun update() {
        when {
            gamepad3.dpad_up -> horizontal.position += 0.02
            gamepad3.dpad_down -> horizontal.position -= 0.02
        }
        when {
            gamepad3.dpad_up -> vertical.position += 0.0002
            gamepad3.dpad_down -> vertical.position -= 0.0002
        }
        extend.power = when {
            gamepad3.left_bumper -> 1.0
            gamepad3.right_bumper -> 0.0
            else -> 0.5
        }
    }

    fun telemetry() {
        telemetry.addData("horizontal currentPosition", horizontal.position)
        telemetry.addData("vertical position", vertical.position)
        telemetry.addData("extend position", extend.power)
    }
}
