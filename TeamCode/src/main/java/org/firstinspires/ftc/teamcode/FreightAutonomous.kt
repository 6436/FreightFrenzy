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
        for (i in 1..CYCLES) {
            scoring.open()

            val time = System.nanoTime()
            while (System.nanoTime() - time < 0.5 * NANOSECONDS_PER_SECOND) {
                drivetrain.odometry()
            }

            spin.down()
            lift.down()

            drivetrain.move(1, 2, 90)

            scoring.default()
            intake.suck()

            drivetrain.move(-28 - (i - 1) * 2.5, drivetrain.location.y)
            if (i == CYCLES) {
                break
            }
            scoring.close()

            intake.spit()

            lift.top()
            spin.up()
            drivetrain.move(2)

            intake.off()

            drivetrain.move(12, -24, 25)
        }
    }
}
