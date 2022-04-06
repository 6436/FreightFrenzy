package org.firstinspires.ftc.teamcode.tuning

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.BaseTeleOp
import org.firstinspires.ftc.teamcode.hardware.Pods

@TeleOp
class Pods : BaseTeleOp() {
    private val pods = Pods()

    override fun initialize() {
        pods.initialize()
    }

    override fun update() {
        if (gamepad1.x) pods.up()
        else if (gamepad1.y) pods.down()

        pods.telemetry()
        telemetry.update()
    }
}
-276080
242739
-163520
92.5625+4*360

-332436
286855
195080
24.4375+5*360
