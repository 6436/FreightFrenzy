package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.hardware.Carousel
import org.firstinspires.ftc.teamcode.hardware.Lift
import org.firstinspires.ftc.teamcode.hardware.Mecanum
import org.firstinspires.ftc.teamcode.hardware.Scoring
import kotlin.concurrent.thread
import org.firstinspires.ftc.teamcode.hardwareMap as globalHardwareMap
import org.firstinspires.ftc.teamcode.isStopRequested as globalIsStopRequested
import org.firstinspires.ftc.teamcode.telemetry as globalTelemetry

@Autonomous(preselectTeleOp = "TeleOp")
class BlueAuto : LinearOpMode() {
    private val drivetrain = Mecanum()
    private val lift = Lift()
    private val scoring = Scoring()
    private val carousel = Carousel()

    override fun runOpMode() {
//        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        globalTelemetry = telemetry
        globalHardwareMap = hardwareMap
        globalIsStopRequested = ::isStopRequested

        drivetrain.initialize()
        lift.initialize()
        scoring.initialize()
        carousel.initialize()

        thread {
            while (!isStopRequested) {
                drivetrain.read()
                drivetrain.odometry()

                drivetrain.telemetry()
                lift.telemetry()
                scoring.telemetry()
                carousel.telemetry()

                telemetry.update()
            }
        }

        waitForStart()

        drivetrain.move(41, 0)
        sleep(2000)
        lift.up()
        sleep(2000)
        scoring.right()
        sleep(2000)
        scoring.up()
        sleep(2000)
    }
}
