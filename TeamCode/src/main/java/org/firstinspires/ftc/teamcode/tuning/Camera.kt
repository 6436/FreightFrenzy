package org.firstinspires.ftc.teamcode.tuning

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.Camera
import org.firstinspires.ftc.teamcode.hardwareMap as globalHardwareMap
import org.firstinspires.ftc.teamcode.telemetry as globalTelemetry

@TeleOp
class Camera : LinearOpMode() {
    private val camera = Camera()

    override fun runOpMode() {
        globalHardwareMap = hardwareMap
        globalTelemetry = telemetry

        camera.initialize()

        waitForStart()

        while (opModeIsActive()) {
            camera.telemetry()
            telemetry.update()

            // Don't burn CPU cycles busy-looping in this sample
            sleep(50)
        }
    }
}
