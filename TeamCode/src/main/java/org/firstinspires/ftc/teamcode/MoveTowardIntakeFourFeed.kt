package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous

@Autonomous(preselectTeleOp = "TeleOp")
class MoveTowardIntakeFourFeed : BaseAutonomous() {
    override fun autonomous() {
        drivetrain.move(0, -48)
    }
}
