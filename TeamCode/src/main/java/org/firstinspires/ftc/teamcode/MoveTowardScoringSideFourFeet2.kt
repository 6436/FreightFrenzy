package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous

@Autonomous(preselectTeleOp = "TeleOp")
class MoveTowardScoringSideFourFeet2 : BaseAutonomous() {
    override fun autonomous() {
        drivetrain.move2(y = -48)
    }
}
