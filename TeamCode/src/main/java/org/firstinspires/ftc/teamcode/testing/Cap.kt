package org.firstinspires.ftc.teamcode.testing

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.opmodes.teleop.TeleOpMode
import org.firstinspires.ftc.teamcode.gamepad3
import org.firstinspires.ftc.teamcode.hardware.Cap
import org.firstinspires.ftc.teamcode.hardwareMap as globalHardwareMap
import org.firstinspires.ftc.teamcode.telemetry as globalTelemetry

@TeleOp
class Cap : TeleOpMode() {
    private val cap = Cap()

    override fun initialize() {
        gamepad3 = gamepad1
        globalHardwareMap = hardwareMap
        globalTelemetry = telemetry

        cap.initialize()
    }

    override fun update() {
        cap.update()

        cap.telemetry()
        telemetry.update()
    }
}
