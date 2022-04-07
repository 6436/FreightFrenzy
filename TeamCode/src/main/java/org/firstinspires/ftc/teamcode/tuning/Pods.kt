package org.firstinspires.ftc.teamcode.tuning

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.BaseTeleOp
import org.firstinspires.ftc.teamcode.hardware.Pods

@TeleOp
class Pods : BaseTeleOp() {
    private val pods = Pods()

    override fun initialize() {
        org.firstinspires.ftc.teamcode.gamepad1 = gamepad1
        org.firstinspires.ftc.teamcode.hardwareMap = hardwareMap
        org.firstinspires.ftc.teamcode.telemetry = telemetry

        pods.initialize()
    }

    override fun update() {
        if (gamepad1.x) pods.up()
        else if (gamepad1.y) pods.down()

        pods.telemetry()
        telemetry.update()
    }
}
