package org.firstinspires.ftc.teamcode.opmodes.testing

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.opmodes.TeleOpMode
import org.firstinspires.ftc.teamcode.mechanisms.Mecanum
import org.firstinspires.ftc.teamcode.hardwareMap as globalHardwareMap
import org.firstinspires.ftc.teamcode.telemetry as globalTelemetry

@TeleOp
class Distance : TeleOpMode() {
    private companion object {
        const val POWER = 0.2
    }

    private val drivetrain = Mecanum()

    override fun initialize() {
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        globalHardwareMap = hardwareMap
        globalTelemetry = telemetry

        drivetrain.initialize()
    }

    override fun update() {
        drivetrain.odometry()

        drivetrain.setPowers(if (gamepad1.x) POWER else 0.0)
    }
}
