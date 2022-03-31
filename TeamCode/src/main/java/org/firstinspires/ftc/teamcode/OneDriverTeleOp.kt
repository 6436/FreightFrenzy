package org.firstinspires.ftc.teamcode

import org.firstinspires.ftc.teamcode.gamepad2 as globalGamepad2
import org.firstinspires.ftc.teamcode.gamepad3 as globalGamepad3

open class OneDriverTeleOp : TeleOp() {
    override fun initialize() {
        super.initialize()

        globalGamepad2 = gamepad1
        globalGamepad3 = gamepad2
    }
}
