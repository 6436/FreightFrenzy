package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous

@Autonomous(preselectTeleOp = "TeleOp")
class BlueAuto : BaseAutonomous() {
    override fun autonomous() {
        drivetrain.move(41, 0)
        sleep(2000)
        lift.up()
        sleep(2000)
        scoring.right()
        sleep(2000)
        scoring.up()
        sleep(2000)
    }
}
