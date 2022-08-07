package org.firstinspires.ftc.teamcode.testing

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.opmodes.autonomous.BaseAutonomous as NeutralAutonomous

@Autonomous
class MoveTowardScoringSideFourFeet : NeutralAutonomous() {
    override fun autonomous() {
        drivetrain.move(y = -48)
    }
}
