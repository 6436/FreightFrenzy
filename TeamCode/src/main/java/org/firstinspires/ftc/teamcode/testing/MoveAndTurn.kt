package org.firstinspires.ftc.teamcode.testing

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.opmodes.autonomous.BaseAutonomous as NeutralAutonomous

@Autonomous
class MoveAndTurn : NeutralAutonomous() {
    override fun autonomous() {
        drivetrain.move(36, 36, 180)
    }
}
