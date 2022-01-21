package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.*
import org.firstinspires.ftc.teamcode.gamepad1 as globalGamepad1
import org.firstinspires.ftc.teamcode.gamepad2 as globalGamepad2
import org.firstinspires.ftc.teamcode.hardwareMap as globalHardwareMap
import org.firstinspires.ftc.teamcode.telemetry as globalTelemetry

@TeleOp
open class TeleOp : OpMode() {
    private val drivetrain = Mecanum()
    private val intake = Intake()
    private val lift = Lift()
    private val scoring = Scoring()
    private val carousel = Carousel()

    override fun init() {
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        globalGamepad1 = gamepad1
        globalGamepad2 = gamepad2
        globalHardwareMap = hardwareMap
        globalTelemetry = telemetry

        drivetrain.initialize()
        intake.initialize()
        lift.initialize()
        scoring.initialize()
        carousel.initialize()
    }

    override fun loop() {
        drivetrain.update()
        intake.update()
        lift.update()
        scoring.update()
        carousel.update()

//        drivetrain.telemetry()
//        intake.telemetry()
        lift.telemetry()
        scoring.telemetry()
//        carousel.telemetry()
    }
}
