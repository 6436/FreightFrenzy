package org.firstinspires.ftc.teamcode

import kotlin.concurrent.thread

@com.qualcomm.robotcore.eventloop.opmode.Autonomous
class RedFreightAutonomous : Autonomous() {
    private companion object {
        // number of times Freight is scored, including Pre-Load box
        const val CYCLES = 5
    }

    override fun autonomous() {
        bonus()
        for (i in 1..CYCLES) {
            scoring.open()

            var startTime = System.nanoTime()
            while (System.nanoTime() - startTime < 0.4 * NANOSECONDS_PER_SECOND) {
                drivetrain.odometry()
            }

//            spin.down()
//            lift.down()
            intake.verySlowSpit()

            if (i == 1) {
                drivetrain.move(0, -1, 91, brake = false)
            } else {
                drivetrain.move(-3.0, -8.0 + (i - 1) * 1.75, 0, brake = false)
            }

            var small = alliance.value * 0.1
            drivetrain.setPowers(small, -small, -small, small)
            startTime = System.nanoTime()
            while (System.nanoTime() - startTime < 0.3 * NANOSECONDS_PER_SECOND) {
                drivetrain.odometry()
            }
            drivetrain.setPowers(0.0)

            startTime = System.nanoTime()

            while (System.nanoTime() - startTime < 0.1 * NANOSECONDS_PER_SECOND) {
                drivetrain.odometry()
            }
            drivetrain.reset()

            scoring.default()
            intake.fastSuck()

            drivetrain.move(
                0,
                12.0 + (i - 1) * 3.0,
                0,
                brake = false
            )

            if (i == CYCLES) {
                break
            }

            small = 0.2
            startTime = System.nanoTime()
            drivetrain.setPowers(small, small, small, small)
            while (System.nanoTime() - startTime < 1.1 * NANOSECONDS_PER_SECOND) {
                drivetrain.odometry()
            }

            thread {
                Thread.sleep(400)
                intake.spit()
            }

            drivetrain.move(y = 0, brake = false)
//break
//            scoring.close()
            intake.off()
//            lift.top()
//            spin.up()

            drivetrain.move(-24, -20, -60)
        }
    }
}
