package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous

@Autonomous(preselectTeleOp = "TeleOp")
class RedAuto : BaseAutonomous() {
    override fun autonomous() {
        drivetrain.move(-32, 7)
        sleep(100)
        carousel.redOn()
        sleep(2500)
        carousel.off()
        drivetrain.move(-32, 40)
        lift.up()
        sleep(1000)
        drivetrain.move(20, 46)
        sleep(1000)
        scoring.right()
        sleep(2000)
        scoring.up()
        sleep(1000)
        lift.down()
        sleep(1000)
        intake.suck()
        sleep(1000)
        drivetrain.move(20, 35)
        sleep(1000)
        lift.up()
        sleep(1000)
        drivetrain.move(20, 46)
        sleep(1000)
        scoring.right()
        sleep(2000)
        drivetrain.move(-32, 40)
        sleep(1000)
        drivetrain.move(-32, 30)
//        drivetrain.move(-40.5, 4.5)
//        sleep(1000)

//        drivetrain.move(-40.5, 35)
//        sleep(1000)
    }
}