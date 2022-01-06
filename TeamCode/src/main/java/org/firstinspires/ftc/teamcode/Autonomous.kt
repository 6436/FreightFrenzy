package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import kotlin.concurrent.thread
//import com.qualcomm.hardware.lynx.LynxModule
import org.firstinspires.ftc.teamcode.hardwareMap as globalHardwareMap
import org.firstinspires.ftc.teamcode.telemetry as globalTelemetry
import org.firstinspires.ftc.teamcode.gamepad1 as globalGamepad1
import org.firstinspires.ftc.teamcode.gamepad2 as globalGamepad2
import org.firstinspires.ftc.teamcode.isStopRequested as globalIsStopRequested

@Autonomous(preselectTeleOp = "TeleOp")
class Autonomous : LinearOpMode() {

    private val drivetrain = Mecanum()

    override fun runOpMode() {
        globalGamepad1 = gamepad1
        globalGamepad2 = gamepad2

        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)
        globalTelemetry = telemetry as MultipleTelemetry

        globalHardwareMap = hardwareMap

        globalIsStopRequested = ::isStopRequested

        drivetrain.initialize()
        thread {
            while (!isStopRequested) {
                drivetrain.read()

                drivetrain.odometry()

                drivetrain.telemetry()

                telemetry.update()
            }
        }

        waitForStart()

//        drivetrain.tempauto()
        drivetrain.move(36, 36, 45)
    }
}
