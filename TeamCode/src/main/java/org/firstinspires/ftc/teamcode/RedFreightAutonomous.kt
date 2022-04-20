package org.firstinspires.ftc.teamcode

import org.firstinspires.ftc.teamcode.hardware.Camera
import org.firstinspires.ftc.teamcode.hardware.Scoring
import kotlin.concurrent.thread

@com.qualcomm.robotcore.eventloop.opmode.Autonomous
class RedFreightAutonomous : Autonomous() {
    private companion object {
        // number of times Freight is scored, including Pre-Load box
        const val CYCLES = 5
    }

    override fun autonomous() {
        bonus()

        when (camera.analysis) {
//            Camera.SkystoneDeterminationPipeline.SkystonePosition.LEFT -> drivetrain.move(23, -19)
//            Camera.SkystoneDeterminationPipeline.SkystonePosition.CENTER -> drivetrain.move(24, -20)
            else -> drivetrain.move(23, -15, slot={scoring.up()})
        }
        while(scoring.state2 != "done") {
            scoring.up()
            drivetrain.odometry()
        }
        for (i in 1..CYCLES) {
            var startTime = System.nanoTime()
            while (System.nanoTime() - startTime < 1.0 * NANOSECONDS_PER_SECOND) {
                drivetrain.odometry()
            }
            when (camera.analysis) {
            Camera.SkystoneDeterminationPipeline.SkystonePosition.LEFT -> scoring.down()
            Camera.SkystoneDeterminationPipeline.SkystonePosition.CENTER -> scoring.mid()
                else -> scoring.open()
            }

            startTime = System.nanoTime()
            while (System.nanoTime() - startTime < 0.4 * NANOSECONDS_PER_SECOND) {
                drivetrain.odometry()
            }
            scoring.state = Scoring.Companion.Level.DOWN
            scoring.state2 = "start"
            drivetrain.move(1, 5, 90, slot = {scoring.up()})
//break
//            intake.suck()
            drivetrain.move(x=-28, slot = {scoring.up()})
            break
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
