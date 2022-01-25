package org.firstinspires.ftc.teamcode.tuning

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.Imu
import kotlin.math.absoluteValue

@TeleOp
class Distance : Drivetrain() {
    private companion object {
        const val POWER = 0.25
    }

    private var angle = 0.0
    private var flag = true
    override fun loop() {
        super.loop()

        for (motor in motors) motor.power = if (gamepad1.x) POWER else 0.0

        telemetry.addData("average", motors.map { it.currentPosition.absoluteValue }.average())
    }
}
