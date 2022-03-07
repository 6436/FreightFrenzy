package org.firstinspires.ftc.teamcode.tuning

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.BaseAutonomous

@Autonomous
class MoveAndTurn : BaseAutonomous() {
    override fun autonomous() {
        drivetrain.move(24, 24, 180)
    }
}
