package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.TeleOp as NeutralTeleOp

@TeleOp
open class BlueTeleOp(override val alliance: Alliance = Alliance.BLUE) : NeutralTeleOp()
