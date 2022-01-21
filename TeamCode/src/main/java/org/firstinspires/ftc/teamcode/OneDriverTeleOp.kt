package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.TeleOp as TwoDriverTeleOp
import org.firstinspires.ftc.teamcode.gamepad2 as globalGamepad2

@TeleOp
class OneDriverTeleOp : TwoDriverTeleOp() {
    override fun init() {
        super.init()

        globalGamepad2 = gamepad1
    }
}
