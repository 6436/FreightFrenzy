package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous

@Autonomous(preselectTeleOp = "TeleOp")
class RedAuto : BaseAutonomous() {
    override fun autonomous() {
        drivetrain.move(0, 50)
        sleep(1000)
        lift.up()
        sleep(2000)
        scoring.right()
        sleep(2000)
        scoring.up()
        sleep(1000)
        drivetrain.move(0, 4.5)
        sleep(1000)
        drivetrain.move(-40.5, 4.5)
        sleep(1000)
        carousel.red()
        sleep(5000)
        carousel.off()
        drivetrain.move(-40.5, 35)
        sleep(1000)
    }
}