package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import kotlin.concurrent.thread
import org.firstinspires.ftc.teamcode.hardwareMap as globalHardwareMap
import org.firstinspires.ftc.teamcode.isStopRequested as globalIsStopRequested
import org.firstinspires.ftc.teamcode.telemetry as globalTelemetry

@Autonomous(preselectTeleOp = "TeleOp")
class Autonomous : LinearOpMode() {

    private val drivetrain = Mecanum()
    private val lift = Lift()
    private val scoring = Scoring()

    override fun runOpMode() {
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        globalTelemetry = telemetry
        globalHardwareMap = hardwareMap
        globalIsStopRequested = ::isStopRequested

        drivetrain.initialize()
        lift.initialize()
        scoring.initialize()

        thread {
            while (!isStopRequested) {
                drivetrain.read()

                drivetrain.odometry()

                drivetrain.telemetry()

                telemetry.update()
            }
        }

        waitForStart()

        drivetrain.move(0, 31, 0)
        lift.up()
        scoring.right()
        drivetrain.move(-20, 11, 0)
    }
}
