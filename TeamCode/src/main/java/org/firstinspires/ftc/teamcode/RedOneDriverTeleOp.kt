package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.RedTeleOp as RedTwoDriverTeleOp
import org.firstinspires.ftc.teamcode.gamepad2 as globalGamepad2
import org.firstinspires.ftc.teamcode.gamepad2 as globalGamepad3

@TeleOp
class RedOneDriverTeleOp : RedTwoDriverTeleOp() {
    override fun initialize() {
        super.initialize()

        globalGamepad2 = gamepad1
        globalGamepad3 = gamepad2
    }
}
