package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous

@Autonomous
class FreightAutonomous : BaseAutonomous() {
    override fun autonomous() {
        lift.bonus()
        drivetrain.move(y=-20)

    }
}