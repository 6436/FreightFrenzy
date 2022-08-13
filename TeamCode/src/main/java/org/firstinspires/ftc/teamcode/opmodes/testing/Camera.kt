package org.firstinspires.ftc.teamcode.opmodes.testing

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.mechanisms.Camera
import org.firstinspires.ftc.teamcode.opmodes.TeleOpMode
import org.firstinspires.ftc.teamcode.hardwareMap as globalHardwareMap
import org.firstinspires.ftc.teamcode.telemetry as globalTelemetry

@TeleOp
class Camera : TeleOpMode() {
    private val camera = Camera()

    override fun initialize() {
        globalHardwareMap = hardwareMap
        globalTelemetry = telemetry

        camera.initialize()
    }

    override fun update() {

        camera.telemetry()
        telemetry.update()

        // Don't burn CPU cycles busy-looping in this sample
        sleep(50)
    }
}
