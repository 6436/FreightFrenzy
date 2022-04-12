package org.firstinspires.ftc.teamcode

import org.firstinspires.ftc.teamcode.hardware.Scoring
import kotlin.concurrent.thread

@com.qualcomm.robotcore.eventloop.opmode.Autonomous
class BlueFreightAutonomous(override val alliance: Alliance = Alliance.BLUE) : Autonomous() {
    private companion object {
        // number of times Freight is scored, including Pre-Load box
        const val CYCLES = 3
    }

    override fun autonomous() {
        bonus()
        for (i in 1..CYCLES) {
            scoring.open()

            intake.verySlowSpit()
            var startTime = System.nanoTime()
            while (System.nanoTime() - startTime < 0.4 * NANOSECONDS_PER_SECOND) {
                drivetrain.odometry()
            }

//            spin.down()
//            lift.down()

            if (i == 1) {
                drivetrain.move(0, -1, 91, brake = false)
            } else {
                drivetrain.move(-3.0, -8.0 + (i - 1) * 2.0, 0, brake = false)
            }

            var small = alliance.value * 0.3
            drivetrain.setPowers(small, -small, -small, small)
            startTime = System.nanoTime()
            while (System.nanoTime() - startTime < 0.9 * NANOSECONDS_PER_SECOND) {
                drivetrain.odometry()
            }
            drivetrain.setPowers(0.0)

            startTime = System.nanoTime()

            while (System.nanoTime() - startTime < 0.1 * NANOSECONDS_PER_SECOND) {
                drivetrain.odometry()
            }
            drivetrain.reset()

            scoring.up(Scoring.Companion.Level.DOWN)
            intake.fastSuck()

            drivetrain.move(
                0,
                24.5 + (i - 1) * 6.0,
                0,
                0.3,
                brake = false
            )

            if (i == CYCLES) {
                break
            }

            small = 0.2
            startTime = System.nanoTime()
            drivetrain.setPowers(small)
            while (System.nanoTime() - startTime < 1.1 * NANOSECONDS_PER_SECOND) {
                drivetrain.odometry()
            }

            drivetrain.setPowers(0.0)

            startTime = System.nanoTime()
            while (System.nanoTime() - startTime < 0.4 * NANOSECONDS_PER_SECOND) {
                drivetrain.odometry()
            }

            thread {
                Thread.sleep(400)
                intake.spit()
            }

            small = -0.2
            startTime = System.nanoTime()
            drivetrain.setPowers(small)
            while (System.nanoTime() - startTime < 0.8 * NANOSECONDS_PER_SECOND) {
                drivetrain.odometry()
            }

            drivetrain.move(0.15, 0, 0, 0.3, brake = false)
//break
//            scoring.close()
//            lift.top()
//            spin.up()

            drivetrain.move(-24, -17, -60)
            intake.off()
        }
    }
}
