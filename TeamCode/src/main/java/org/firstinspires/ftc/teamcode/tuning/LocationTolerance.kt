package org.firstinspires.ftc.teamcode.tuning

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.hardware.Mecanum
import kotlin.concurrent.thread
import org.firstinspires.ftc.teamcode.gamepad1 as globalGamepad1
import org.firstinspires.ftc.teamcode.gamepad2 as globalGamepad2
import org.firstinspires.ftc.teamcode.hardwareMap as globalHardwareMap
import org.firstinspires.ftc.teamcode.telemetry as globalTelemetry
import org.firstinspires.ftc.teamcode.isStopRequested as globalIsStopRequested

@Autonomous
class LocationTolerance : LinearOpMode() {
    private val drivetrain = Mecanum()

    override fun runOpMode() {
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        globalHardwareMap = hardwareMap
        globalTelemetry = telemetry
        globalIsStopRequested = ::isStopRequested

        drivetrain.initialize()

        thread {
            while (!isStopRequested) {
                drivetrain.odometry()

                telemetry.update()
            }
        }

        waitForStart()

        drivetrain.move(0, 96)
    }
}