package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
//import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.hardwareMap as globalHardwareMap
import org.firstinspires.ftc.teamcode.telemetry as globalTelemetry
import org.firstinspires.ftc.teamcode.gamepad1 as globalGamepad1
import org.firstinspires.ftc.teamcode.gamepad2 as globalGamepad2

@Autonomous
class Auto : LinearOpMode() {
//    private lateinit var hubs: List<LynxModule>

    private val drivetrain = Mecanum()

    override fun runOpMode() {
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        globalHardwareMap = hardwareMap
        globalTelemetry = telemetry as MultipleTelemetry
        globalGamepad1 = gamepad1
        globalGamepad2 = gamepad2

//        hubs = org.firstinspires.ftc.teamcode.hardwareMap.getAll(LynxModule::class.java)
//        for (i in hubs) i.bulkCachingMode = LynxModule.BulkCachingMode.MANUAL

        drivetrain.initialize()

        waitForStart()
        drivetrain.move()

    }
}
