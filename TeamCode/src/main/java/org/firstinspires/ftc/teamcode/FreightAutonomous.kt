package org.firstinspires.ftc.teamcode

import kotlin.concurrent.thread

abstract class FreightAutonomous : Autonomous() {
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
            while (System.nanoTime() - startTime < 1.5 * NANOSECONDS_PER_SECOND) {
                drivetrain.odometry()
            }

            spin.down()
            lift.down()

            drivetrain.move(0.5 - (i - 1) * 1.5, 1.0, 91, brake = false)
break
            val small = alliance.value * 0.2
            drivetrain.setPowers(small, -small, -small, small)
            while (System.nanoTime() - startTime < 0.5 * NANOSECONDS_PER_SECOND) {
                drivetrain.odometry()
            }

            scoring.default()
            intake.suck()

            drivetrain.move(
                -34.0 - (i - 1) * 1.65,
                drivetrain.location.y,
                alliance.value * drivetrain.heading,
                brake = false
            )

            if (i == CYCLES) {
                break
            }

            startTime = System.nanoTime()
            drivetrain.setPowers(small, -small, -small, small)
            while (System.nanoTime() - startTime < 0.5 * NANOSECONDS_PER_SECOND) {
                drivetrain.odometry()
            }

            scoring.close()

            thread {
                Thread.sleep(200)

                intake.spit()
            }

            drivetrain.move(2, brake = false)

            intake.off()
            lift.top()
            spin.up()

            drivetrain.move(14, -26, 33)
        }
    }
}
