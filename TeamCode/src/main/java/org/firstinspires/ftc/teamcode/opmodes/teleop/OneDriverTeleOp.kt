package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.Alliance
import org.firstinspires.ftc.teamcode.alliance
import org.firstinspires.ftc.teamcode.gamepad1 as globalGamepad1
import org.firstinspires.ftc.teamcode.gamepad2 as globalGamepad2
import org.firstinspires.ftc.teamcode.gamepad3 as globalGamepad3

@TeleOp
class RedOneDriverTeleOp : OneDriverTeleOp() {
    override fun initialize() {
        super.initialize()

        alliance = Alliance.RED
    }
}

@TeleOp
class BlueOneDriverTeleOp : OneDriverTeleOp() {
    override fun initialize() {
        super.initialize()

        alliance = Alliance.BLUE
    }
}

abstract class OneDriverTeleOp : BaseTeleOp() {
    override fun initialize() {
        super.initialize()

        globalGamepad1 = gamepad1
        globalGamepad2 = gamepad1
        globalGamepad3 = gamepad2
    }
}
