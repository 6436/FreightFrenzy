package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.hardware.*
import kotlin.concurrent.thread
import org.firstinspires.ftc.teamcode.hardwareMap as globalHardwareMap
import org.firstinspires.ftc.teamcode.isStopRequested as globalIsStopRequested
import org.firstinspires.ftc.teamcode.telemetry as globalTelemetry

abstract class BaseAutonomous : LinearOpMode() {
    protected val drivetrain = Mecanum()
    protected val lift = Lift()
    protected val scoring = Scoring()
    protected val carousel = Carousel()
    protected val camera = Camera()
    protected val intake = Intake()

    override fun runOpMode() {
//        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        globalTelemetry = telemetry
        globalHardwareMap = hardwareMap
        globalIsStopRequested = ::isStopRequested

        drivetrain.initialize()
        lift.initialize()
        scoring.initialize()
        carousel.initialize()
        camera.initialize()
        intake.initialize()

        thread {
            while (!isStopRequested) {
                drivetrain.read()
                drivetrain.odometry()

                drivetrain.telemetry()
                lift.telemetry()
                scoring.telemetry()
                carousel.telemetry()
                intake.telemetry()

                telemetry.update()
            }
        }

        waitForStart()

        camera.off()

        autonomous()

        scoring.up()
        lift.down()
    }

    abstract fun autonomous()
}
