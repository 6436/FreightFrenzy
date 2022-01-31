package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous

@Autonomous
class RedFreight : BaseAutonomous() {
    override fun autonomous() {
        lift.bonus()
        repeat(1) {
            scoring.left()
            drivetrain.move(-24, 20, -90)
            scoring.up()
            lift.down()

            drivetrain.move(0, 0, brake = false)
            intake.suck()
            drivetrain.move(x = 24)
            lift.top()
            intake.off()
            drivetrain.move(x = 0, brake = false)
        }
    }
}
