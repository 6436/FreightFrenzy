package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.Alliance
import org.firstinspires.ftc.teamcode.gamepad1 as globalGamepad1
import org.firstinspires.ftc.teamcode.gamepad2 as globalGamepad2
import org.firstinspires.ftc.teamcode.gamepad3 as globalGamepad3

@TeleOp
class RedOneDriverTeleOp(override val alliance: Alliance = Alliance.RED) : OneDriverTeleOp()

@TeleOp
class BlueOneDriverTeleOp(override val alliance: Alliance = Alliance.BLUE) : OneDriverTeleOp()

open class OneDriverTeleOp : BaseTeleOp() {
    override fun initialize() {
        super.initialize()

        globalGamepad1 = gamepad1
        globalGamepad2 = gamepad1
        globalGamepad3 = gamepad2
    }
}
