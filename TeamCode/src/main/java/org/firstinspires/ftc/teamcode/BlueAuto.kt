package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous

@Autonomous(preselectTeleOp = "TeleOp")
class BlueAuto : BaseAutonomous() {
    override fun autonomous() {
        drivetrain.move(y = 13)
        sleep(1000)
        drivetrain.move(heading = -84)
        sleep(1000)
        drivetrain.move(x = -26, heading = -90)

        sleep(100)
        carousel.deliver(Alliance.BLUE)
        sleep(1000)
        drivetrain.move(x = 30)
    }
}
