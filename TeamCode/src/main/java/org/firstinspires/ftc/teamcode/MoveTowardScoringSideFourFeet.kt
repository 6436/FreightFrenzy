package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.Autonomous as NeutralAutonomous

@Autonomous
class MoveTowardScoringSideFourFeet : NeutralAutonomous() {
    override fun autonomous() {
        drivetrain.move(y = -48)
    }
}
