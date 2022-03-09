package org.firstinspires.ftc.teamcode.tuning

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.Autonomous as NeutralAutonomous

@Autonomous
class Turn90Degrees : NeutralAutonomous() {
    override fun autonomous() {
        drivetrain.move(heading = 90)
    }
}
