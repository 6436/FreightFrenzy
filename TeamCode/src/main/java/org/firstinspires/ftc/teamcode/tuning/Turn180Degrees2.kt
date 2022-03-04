package org.firstinspires.ftc.teamcode.tuning

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.BaseAutonomous

@Autonomous
class Turn180Degrees2 : BaseAutonomous() {
    override fun autonomous() {
        drivetrain.move2(heading = 180)
    }
}
