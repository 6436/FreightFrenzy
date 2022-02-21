package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous

@Autonomous(preselectTeleOp = "TeleOp")
class MoveTowardIntakeFourFeet : BaseAutonomous() {
    override fun autonomous() {
        drivetrain.move(y = -48)
    }
}
