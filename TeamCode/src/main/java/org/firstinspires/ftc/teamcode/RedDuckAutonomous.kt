package org.firstinspires.ftc.teamcode

import org.firstinspires.ftc.teamcode.hardware.Scoring

@com.qualcomm.robotcore.eventloop.opmode.Autonomous
class RedDuckAutonomous : Autonomous() {
    override fun autonomous() {
        drivetrain.move(23, 0, -90)
        while (!carousel.deliver(true)) {
            drivetrain.odometry()
        }

        drivetrain.move(y=-36,brake=false)
        drivetrain.move(0, heading=-100)
        scoring.state = Scoring.Companion.Level.TOP
        scoring.state2 = "start"
        while(scoring.state2 != "done") {
            scoring.up(scoring.state)
            drivetrain.odometry()
        }
        scoring.open()

    }
}