package org.firstinspires.ftc.teamcode.tuning

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.Camera
import org.firstinspires.ftc.teamcode.hardwareMap as globalHardwareMap

@TeleOp
class Camera : LinearOpMode() {
    private val camera = Camera()

    override fun runOpMode() {
        globalHardwareMap = hardwareMap

        camera.initialize()

        waitForStart()

        while (opModeIsActive()) {
            telemetry.addData("Analysis", camera.analysis)
            telemetry.update()

            // Don't burn CPU cycles busy-looping in this sample
            sleep(50)
        }
    }
}
