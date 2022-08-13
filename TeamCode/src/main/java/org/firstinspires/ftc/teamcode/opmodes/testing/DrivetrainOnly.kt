package org.firstinspires.ftc.teamcode.opmodes.testing

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.mechanisms.Mecanum
import org.firstinspires.ftc.teamcode.mechanisms.Pods
import org.firstinspires.ftc.teamcode.opmodes.TeleOpMode
import org.firstinspires.ftc.teamcode.gamepad1 as globalGamepad1
import org.firstinspires.ftc.teamcode.hardwareMap as globalHardwareMap
import org.firstinspires.ftc.teamcode.telemetry as globalTelemetry

@TeleOp
class DrivetrainOnly : TeleOpMode() {
    private val drivetrain = Mecanum()
    private val pods = Pods()

    override fun initialize() {
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        globalGamepad1 = gamepad1
        globalHardwareMap = hardwareMap
        globalTelemetry = telemetry

        drivetrain.initialize()
        pods.initialize()
    }

    override fun update() {
        drivetrain.odometry()

        drivetrain.update()
        if (gamepad1.x) pods.up()
        else if (gamepad1.y) pods.down()

        pods.telemetry()
        drivetrain.telemetry()
        telemetry.update()
    }
}
