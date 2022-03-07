package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous

@Autonomous
class MoveTowardScoringSideFourFeet : BaseAutonomous() {
    override fun autonomous() {
        drivetrain.move(y = -48)
    }
}
