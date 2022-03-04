package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous

@Autonomous
class FreightAutonomous2 : BaseAutonomous() {
    override fun autonomous() {
        lift.top()
        spin.up()
        drivetrain.move2(24, -21)
        scoring.open()

        sleep(500)

        drivetrain.move2(0, 4,92)
        scoring.default()
        spin.down()
        lift.down()
        sleep(500)
        intake.suck()
        drivetrain.move2(-35)
        scoring.close()

        lift.top()
        spin.up()
        intake.spit()
        drivetrain.move2(2)

        intake.off()

        drivetrain.move2(6, -40)
        sleep(500)
        scoring.open()
        sleep(500)
        drivetrain.move2(2, 4,92)
        scoring.default()
        spin.down()
        lift.down()
        sleep(500)
        intake.suck()
        drivetrain.move2(-38)
        scoring.close()

        lift.top()
        spin.up()
        intake.spit()
        drivetrain.move2(2)

        intake.off()

        drivetrain.move2(6, -40)
        sleep(500)
        scoring.open()
    }
}
