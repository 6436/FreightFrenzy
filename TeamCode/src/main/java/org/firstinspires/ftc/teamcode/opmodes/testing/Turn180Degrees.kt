package org.firstinspires.ftc.teamcode.opmodes.testing

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.opmodes.autonomous.BaseAutonomous

@Autonomous
class Turn180Degrees : BaseAutonomous() {
    override fun autonomous() {
        drivetrain.move(heading = 180)
    }
}
