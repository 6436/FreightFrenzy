package org.firstinspires.ftc.teamcode.tuning

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
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
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        globalHardwareMap = hardwareMap
        globalTelemetry = telemetry

        drivetrain.initialize()
    }

    override fun loop() {
        drivetrain.odometry()

        drivetrain.setPowers(if (gamepad1.x) POWER else 0.0)
    }
}
