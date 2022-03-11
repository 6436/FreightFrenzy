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
        drivetrain.move(23, -19)
        for (i in 1..CYCLES) {
            scoring.open()

            var startTime = System.nanoTime()
            while (System.nanoTime() - startTime < 0.4 * NANOSECONDS_PER_SECOND) {
                drivetrain.odometry()
            }

            spin.down()
            lift.down()

            if (i == 1) {
                drivetrain.move(0.5, 0.5, 91, brake = false)
            } else {
                drivetrain.move(-3.0 - (i - 1) * 1.5 ,-3.0 - (i - 1) * 2.0,0, brake = false)
            }

            var small = alliance.value * 0.2
            drivetrain.setPowers(small, -small, -small, small)
            startTime = System.nanoTime()
            while (System.nanoTime() - startTime < 0.15 * NANOSECONDS_PER_SECOND) {
                drivetrain.odometry()
            }
            drivetrain.setPowers(0.0)

            drivetrain.reset()

            startTime = System.nanoTime()

            while (System.nanoTime() - startTime < 0.1 * NANOSECONDS_PER_SECOND) {
                drivetrain.odometry()
            }

            scoring.default()
            intake.fastSuck()

            drivetrain.move(
                0,
                24.0 + (i - 1) * 1.0,
                0,
                brake = false
            )

            if (i == CYCLES) {
                break
            }

            small=0.01
            startTime = System.nanoTime()
            drivetrain.setPowers(small, small, small, small)
            while (System.nanoTime() - startTime < 0.3 * NANOSECONDS_PER_SECOND) {
                drivetrain.odometry()
            }

            scoring.close()

            thread {
                Thread.sleep(300)

                intake.spit()
            }

            drivetrain.move(y = 0, brake = false)
//break
            intake.off()
            lift.top()
            spin.up()

            drivetrain.move(-24, -22, -60)
        }
    }
}
