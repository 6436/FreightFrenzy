package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.Mecanum

@TeleOp
class DrivetrainOnly : LinearOpMode() {
    private val drivetrain = Mecanum()

    fun initialize() {
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        org.firstinspires.ftc.teamcode.gamepad1 = gamepad1
        org.firstinspires.ftc.teamcode.gamepad2 = gamepad2
        org.firstinspires.ftc.teamcode.hardwareMap = hardwareMap
        org.firstinspires.ftc.teamcode.telemetry = telemetry

        drivetrain.initialize()
    }

    @Throws(InterruptedException::class)
    override fun runOpMode() {
        initialize()

        waitForStart()

        while (opModeIsActive()) {

            drivetrain.odometry()

//            telemetry.update()

            drivetrain.update()
        }
    }
}
