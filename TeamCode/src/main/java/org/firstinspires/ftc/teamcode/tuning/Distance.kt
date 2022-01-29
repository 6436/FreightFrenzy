package org.firstinspires.ftc.teamcode.tuning

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.Mecanum
import org.firstinspires.ftc.teamcode.gamepad1 as globalGamepad1
import org.firstinspires.ftc.teamcode.gamepad2 as globalGamepad2
import org.firstinspires.ftc.teamcode.hardwareMap as globalHardwareMap
import org.firstinspires.ftc.teamcode.telemetry as globalTelemetry

@TeleOp
class Distance : OpMode() {
    private companion object {
        const val POWER = 0.2
    }

    private val drivetrain = Mecanum()

    override fun init() {
        globalGamepad1 = gamepad1
        globalGamepad2 = gamepad2
        globalHardwareMap = hardwareMap
        globalTelemetry = telemetry

        drivetrain.initialize()
    }

    override fun loop() {
        drivetrain.read()
        drivetrain.odometry()

        for (motor in drivetrain.motors) {
            motor.power = if (gamepad1.x && drivetrain.location.magnitude < 120.0) POWER else 0.0
        }

        drivetrain.telemetry()
    }
}

