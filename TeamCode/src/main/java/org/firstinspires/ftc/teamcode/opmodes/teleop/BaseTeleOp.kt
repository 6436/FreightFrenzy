package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import org.firstinspires.ftc.teamcode.Alliance
import org.firstinspires.ftc.teamcode.hardware.*
import org.firstinspires.ftc.teamcode.hardwareMap as globalHardwareMap
import org.firstinspires.ftc.teamcode.telemetry as globalTelemetry

open class BaseTeleOp : TeleOpMode() {
    open val alliance = Alliance.RED

    private val drivetrain = Mecanum()
    private val intake = Intake()
    private val scoring = Scoring()
    private val carousel = Carousel()
//    private val cap = Cap()
    private val pods = Pods()

    override fun initialize() {
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)


        globalHardwareMap = hardwareMap
        globalTelemetry = telemetry

        drivetrain.initialize()
        intake.initialize()
        scoring.initialize()
        carousel.initialize(alliance)
//        cap.initialize()
        pods.initialize()

        pods.up()
    }

    override fun update() {
        drivetrain.odometry()

        drivetrain.update()
        intake.update()
        scoring.update()
        carousel.update()
//        cap.update()

//        drivetrain.telemetry()
//        intake.telemetry()
//        scoring.telemetry()
//        carousel.telemetry()
//        cap.telemetry()

        telemetry.update()
    }
}
