package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import org.firstinspires.ftc.teamcode.mechanisms.*
import org.firstinspires.ftc.teamcode.opmodes.TeleOpMode
import org.firstinspires.ftc.teamcode.hardwareMap as globalHardwareMap
import org.firstinspires.ftc.teamcode.telemetry as globalTelemetry

abstract class BaseTeleOp : TeleOpMode() {
    private val drivetrain = Mecanum()
    private val intake = Intake()
    private val scoring = Scoring()
    private val carousel = Carousel()

    //    private val cap = Cap()

    private val pods = Pods()
    private val mechanisms = setOf(drivetrain, intake, scoring, carousel, pods)

    override fun initialize() {
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        globalHardwareMap = hardwareMap
        globalTelemetry = telemetry

        mechanisms.forEach { it.initialize() }

        pods.up()
    }

    override fun update() {
        drivetrain.odometry()

        mechanisms.forEach {
            it.update()
            it.telemetry()
        }

        telemetry.update()
    }
}
