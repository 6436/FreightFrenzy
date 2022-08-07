package org.firstinspires.ftc.teamcode.testing

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.opmodes.autonomous.BaseAutonomous as NeutralAutonomous

@Autonomous
class Turn90Degrees : NeutralAutonomous() {
    override fun autonomous() {
        drivetrain.move(heading = 90)
    }
}
