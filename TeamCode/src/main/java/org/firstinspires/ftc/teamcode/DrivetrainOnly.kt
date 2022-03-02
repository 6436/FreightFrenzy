package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.Mecanum
import org.firstinspires.ftc.teamcode.gamepad1 as globalGamepad1
import org.firstinspires.ftc.teamcode.hardwareMap as globalHardwareMap
import org.firstinspires.ftc.teamcode.telemetry as globalTelemetry

@TeleOp
class DrivetrainOnly : LinearOpMode() {
    private val drivetrain = Mecanum()

    fun initialize() {
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        globalGamepad1 = gamepad1
        globalHardwareMap = hardwareMap
        globalTelemetry = telemetry

        drivetrain.initialize()
    }

    @Throws(InterruptedException::class)
    override fun runOpMode() {
        initialize()

        waitForStart()

        while (opModeIsActive()) {

            drivetrain.odometry()

//            telemetry.update()

            drivetrain.update()
        }
    }
}
