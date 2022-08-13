package org.firstinspires.ftc.teamcode.opmodes.testing

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.opmodes.autonomous.BaseAutonomous

@Autonomous
class Turn90Degrees : BaseAutonomous() {
    override fun autonomous() {
        drivetrain.move(heading = 90)
    }
}
