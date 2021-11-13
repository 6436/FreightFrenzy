package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp
class DrivetrainOnly : OpMode() {
    private val drivetrain = Mecanum()
    override fun init() {
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        org.firstinspires.ftc.teamcode.hardwareMap = hardwareMap
        org.firstinspires.ftc.teamcode.telemetry = telemetry as MultipleTelemetry
        org.firstinspires.ftc.teamcode.gamepad1 = gamepad1
        org.firstinspires.ftc.teamcode.gamepad2 = gamepad2
        drivetrain.initialize()
    }

    override fun loop() {
        drivetrain.update()
    }
}