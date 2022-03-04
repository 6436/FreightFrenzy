package org.firstinspires.ftc.teamcode.tuning

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.BaseAutonomous

@Autonomous
class Turn90Degrees : BaseAutonomous() {
    override fun autonomous() {
        drivetrain.move(heading = 90)
    }
}
