package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous

@Autonomous
class FreightAutonomous : BaseAutonomous() {
    private companion object {
        // number of times Freight is scored, including Pre-Load box
        const val CYCLES = 1
    }

    override fun autonomous() {
        lift.bonus()
        spin.up()
        drivetrain.move(24, -21)
        sleep(5000)
//        for (i in 1..CYCLES) {
//            scoring.open()
//
//            sleep(250)
//            spin.down()
//            lift.down()
//
//            drivetrain.move(1, 1.5, 90)
//
//            scoring.default()
//            intake.suck()
//
//            drivetrain.move(-35 - (i - 1) * 2.5)
//            if (i == CYCLES) {
//                break
//            }
//            scoring.close()
//
//            intake.spit()
//
//            lift.top()
//            spin.up()
//            drivetrain.move(2)
//
//            intake.off()
//
//            drivetrain.move(12, -25, 25)
//        }
    }
}
