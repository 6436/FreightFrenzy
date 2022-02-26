package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous

@Autonomous
class FreightAutonomous : BaseAutonomous() {
    override fun autonomous() {
        lift.bonus()
        spin.up()
        drivetrain.move(24, -20)
        scoring.open()
    }
}
