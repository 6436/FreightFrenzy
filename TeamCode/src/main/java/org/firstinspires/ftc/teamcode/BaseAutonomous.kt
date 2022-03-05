package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.hardware.*
import kotlin.concurrent.thread
import org.firstinspires.ftc.teamcode.hardwareMap as globalHardwareMap
import org.firstinspires.ftc.teamcode.isStopRequested as globalIsStopRequested
import org.firstinspires.ftc.teamcode.telemetry as globalTelemetry

abstract class BaseAutonomous : LinearOpMode() {
    protected val drivetrain = Mecanum()
    protected val intake = Intake()
    protected val lift = Lift()
    protected val scoring = Scoring()
    protected val carousel = Carousel()
//    protected val camera = Camera()
    protected val spin = Spin()

    @Throws(InterruptedException::class)
    override fun runOpMode() {
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        globalTelemetry = telemetry
        globalHardwareMap = hardwareMap
        globalIsStopRequested = ::isStopRequested

        drivetrain.initialize()
        intake.initialize()
        lift.initialize()
        scoring.initialize()
        scoring.close()
        carousel.initialize()
//        camera.initialize()
        spin.initialize()

        thread {
            while (!isStopRequested) {
                drivetrain.odometry()

                drivetrain.telemetry()
//                intake.telemetry()
//                lift.telemetry()
//                scoring.telemetry()
//                carousel.telemetry()
//                camera.telemetry()
//                spin.telemetry()

                telemetry.update()
            }
        }

        waitForStart()

//        camera.off()

        autonomous()

        reset()
    }

    fun Lift.bonus() {
//        when (camera.analysis) {
//            Camera.SkystoneDeterminationPipeline.SkystonePosition.LEFT -> lift.bottom()
//            Camera.SkystoneDeterminationPipeline.SkystonePosition.CENTER -> lift.middle()
//            else ->
        lift.top()
//        }
    }

    abstract fun autonomous()

    private fun reset() {
        sleep(2500)

        lift.down()
        spin.down()
        scoring.default()

        sleep(5000)
    }
}
