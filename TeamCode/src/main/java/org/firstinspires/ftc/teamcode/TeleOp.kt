package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.Mecanum
import org.firstinspires.ftc.teamcode.gamepad1 as globalGamepad1
import org.firstinspires.ftc.teamcode.gamepad2 as globalGamepad2
import org.firstinspires.ftc.teamcode.hardwareMap as globalHardwareMap
import org.firstinspires.ftc.teamcode.telemetry as globalTelemetry

@TeleOp
open class TeleOp : LinearOpMode() {
    private val drivetrain = Mecanum()
//    private val intake = Intake()
//    private val lift = Lift()
//    private val scoring = Scoring()
//    private val carousel = Carousel()

    override fun runOpMode() {
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        globalGamepad1 = gamepad1
        globalGamepad2 = gamepad2
        globalHardwareMap = hardwareMap
        globalTelemetry = telemetry

        drivetrain.initialize()
//        intake.initialize()
//        lift.initialize()
//        scoring.initialize()
//        carousel.initialize()
        waitForStart()

        while (opModeIsActive()) {
            drivetrain.odometry()

            drivetrain.update()
//        intake.update()
//        lift.update()
//        scoring.update()
//        carousel.update()

            drivetrain.telemetry()
//        intake.telemetry()
//        lift.telemetry()
//        scoring.telemetry()
//        carousel.telemetry()
            telemetry.update()
        }
    }
//
//    override fun loop() {
//        drivetrain.read()
////        drivetrain.odometry()
//
//        drivetrain.update()
////        intake.update()
////        lift.update()
////        scoring.update()
////        carousel.update()
//
//        drivetrain.telemetry()
////        intake.telemetry()
////        lift.telemetry()
////        scoring.telemetry()
////        carousel.telemetry()
//    }
}
