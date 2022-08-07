package org.firstinspires.ftc.teamcode.opmodes.autonomous

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.Alliance
import org.firstinspires.ftc.teamcode.hardware.*
import org.firstinspires.ftc.teamcode.hardwareMap as globalHardwareMap
import org.firstinspires.ftc.teamcode.isStopRequested as globalIsStopRequested
import org.firstinspires.ftc.teamcode.telemetry as globalTelemetry

abstract class BaseAutonomous : LinearOpMode() {
    open val alliance = Alliance.RED

    protected val drivetrain = Mecanum(0.9)
    protected val intake = Intake()
    protected val scoring = Scoring()
    protected val carousel = Carousel()
    private val pods = Pods()
    protected val camera = Camera()


    @Throws(InterruptedException::class)
    override fun runOpMode() {
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        globalTelemetry = telemetry
        globalHardwareMap = hardwareMap
        globalIsStopRequested = ::isStopRequested

        drivetrain.initialize(alliance, false)
        intake.initialize()
        scoring.initialize()
        carousel.initialize(alliance)
        pods.initialize()
        camera.initialize()

        pods.down()
        scoring.scoring.position = Scoring.Companion.Level.TOP.scoringPosition

        waitForStart()

        camera.off()

        autonomous()

//        val startTime = System.nanoTime()
//        while (System.nanoTime() - startTime < 5.0 * NANOSECONDS_PER_SECOND && opModeIsActive()) {
//            drivetrain.odometry()
//            telemetry.addData("a power", 0.0)
//            telemetry.addData("b power", 0.0)
//            telemetry.addData("rotational power", 0.0)
//            drivetrain.telemetry()
//            telemetry.update()
//        }
        //        reset()
    }

    fun bonus() {
//        when (camera.analysis) {
////            Camera.SkystoneDeterminationPipeline.SkystonePosition.LEFT -> lift.bottom()
////            Camera.SkystoneDeterminationPipeline.SkystonePosition.CENTER -> lift.middle()
////            else -> lift.top()
//        }
////        spin.up()
//        when (camera.analysis) {
//            Camera.SkystoneDeterminationPipeline.SkystonePosition.LEFT -> drivetrain.move(23, -19)
//            Camera.SkystoneDeterminationPipeline.SkystonePosition.CENTER -> drivetrain.move(24, -20)
//            else -> drivetrain.move(24.5, -20)
//        }
        scoring.state = when (camera.analysis) {
            Camera.SkystoneDeterminationPipeline.SkystonePosition.LEFT -> Scoring.Companion.Level.BOTTOM
            Camera.SkystoneDeterminationPipeline.SkystonePosition.CENTER -> Scoring.Companion.Level.MIDDLE
            else -> Scoring.Companion.Level.TOP
        }
        scoring.state2 = "start"
    }

    abstract fun autonomous()

    private fun reset() {
        sleep(2500)

//        lift.down()
//        spin.down()
        scoring.up()

        sleep(5000)
    }
}
