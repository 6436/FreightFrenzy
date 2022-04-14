package org.firstinspires.ftc.teamcode.tuning

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.Autonomous as NeutralAutonomous

@Autonomous
class MoveAndTurn : NeutralAutonomous() {
    override fun autonomous() {
        drivetrain.move(36, 36, 180)
    }
}
