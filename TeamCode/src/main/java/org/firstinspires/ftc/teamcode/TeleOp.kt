package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.gamepad1 as globalGamepad1
import org.firstinspires.ftc.teamcode.gamepad2 as globalGamepad2
import org.firstinspires.ftc.teamcode.hardwareMap as globalHardwareMap
import org.firstinspires.ftc.teamcode.telemetry as globalTelemetry

@TeleOp
class TeleOp : CarouselTest() {
    private val drivetrain = Mecanum()
    private val intake = Intake()
    private val lift = Lift()
    private val scoring = Scoring()

    override fun init() {
        super.init()

        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        globalGamepad1 = gamepad1
        globalGamepad2 = gamepad2
        globalHardwareMap = hardwareMap
        globalTelemetry = telemetry

        drivetrain.initialize()
        intake.initialize()
        lift.initialize()
        scoring.initialize()
    }

    override fun loop() {
        super.loop()

        drivetrain.update()
        intake.update()
        lift.update()
        scoring.update()

//        drivetrain.telemetry()
//        intake.telemetry()
        lift.telemetry()
        scoring.telemetry()
    }
}
