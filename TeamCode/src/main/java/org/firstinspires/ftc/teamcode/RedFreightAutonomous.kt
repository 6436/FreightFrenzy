package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous

@Autonomous
class RedFreightAutonomous(override val alliance: Alliance = Alliance.RED) : BaseAutonomous() {
    private companion object {
        // number of times Freight is scored, including Pre-Load box
        const val CYCLES = 5
    }

    override fun autonomous() {
        lift.bonus()
        spin.up()
        drivetrain.move(24, -21)
        for (i in 1..CYCLES) {
            scoring.open()

            var startTime = System.nanoTime()
            while (System.nanoTime() - startTime < 0.5 * NANOSECONDS_PER_SECOND) {
                drivetrain.odometry()
            }

            spin.down()
            lift.down()

            drivetrain.move(0.5, 2.25, 91)

            scoring.default()
            intake.suck()

            drivetrain.move(-34.0 - (i - 1) * 2.0, drivetrain.location.y)

            if (i == CYCLES) {
                break
            }

            startTime = System.nanoTime()
            while (System.nanoTime() - startTime < 0.5 * NANOSECONDS_PER_SECOND) {
                drivetrain.odometry()
            }

            scoring.close()

            intake.spit()

            lift.top()
            spin.up()
            drivetrain.move(2)

            intake.off()

            drivetrain.move(14, -26, 33)
        }
    }
}
