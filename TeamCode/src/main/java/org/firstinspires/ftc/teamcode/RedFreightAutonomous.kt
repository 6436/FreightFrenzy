package org.firstinspires.ftc.teamcode

import org.firstinspires.ftc.teamcode.hardware.Camera
import org.firstinspires.ftc.teamcode.hardware.Scoring
import kotlin.concurrent.thread

@com.qualcomm.robotcore.eventloop.opmode.Autonomous
open class RedFreightAutonomous : Autonomous() {

    override fun autonomous() {
        // number of times Freight is scored, INCLUDING Pre-Load box

        val CYCLES = when(camera.analysis) {
            Camera.SkystoneDeterminationPipeline.SkystonePosition.LEFT -> 4
            Camera.SkystoneDeterminationPipeline.SkystonePosition.CENTER -> 4
            else -> 4
        }

        bonus()
        drivetrain.move(23.3, -17.1, slot={scoring.up()}, power=0.4)
        while(scoring.state2 != "done") {
            scoring.up()
            drivetrain.odometry()
        }
        for (i in 1..CYCLES) {
            var startTime = System.nanoTime()
            while (System.nanoTime() - startTime < 0.1 * NANOSECONDS_PER_SECOND) {
                drivetrain.odometry()
            }
            if (i==1){when (camera.analysis) {
            Camera.SkystoneDeterminationPipeline.SkystonePosition.LEFT -> scoring.down()
            Camera.SkystoneDeterminationPipeline.SkystonePosition.CENTER -> scoring.mid()
                else -> scoring.open()
            }}else {
                scoring.open()
            }

            startTime = System.nanoTime()
            while (System.nanoTime() - startTime < 0.25 * NANOSECONDS_PER_SECOND) {
                drivetrain.odometry()
            }
            scoring.state = if (i == CYCLES) Scoring.Companion.Level.DEAD else Scoring.Companion.Level.DOWN
            scoring.state2 = "start"
            drivetrain.move(1, 5.2-i*0.242, 90-i*0.5, slot = {scoring.up()}, power=0.675 )
//break
            intake.suck()
            drivetrain.move(x=if (i != CYCLES) (-31.6-i*2) else -31.6, slot = {scoring.up()}, power=0.675)


            startTime = System.nanoTime()
            while (System.nanoTime() - startTime < 0.5 * NANOSECONDS_PER_SECOND) {
                drivetrain.odometry()
                scoring.up()
            }
            if (i == CYCLES) {
                while(scoring.state2 != "done") {
                    scoring.up()
                    drivetrain.odometry()
                }
                break
            }
            intake.off()
            scoring.state = Scoring.Companion.Level.TOP
            scoring.state2="start"
            drivetrain.move(x=1, y=4.8-i*0.4, slot = {scoring.up()}, brake=false)
            drivetrain.move(13.2, -17, 20+i*2.4, slot = {scoring.up()})


//
////            spin.down()
////            lift.down()
//            intake.verySlowSpit()
//
//            if (i == 1) {
//                drivetrain.move(0, -1, 91, brake = false)
//            } else {
//                drivetrain.move(-3.0, -8.0 + (i - 1) * 1.75, 0, brake = false)
//            }
//
//            var small = alliance.value * 0.1
//            drivetrain.setPowers(small, -small, -small, small)
//            startTime = System.nanoTime()
//            while (System.nanoTime() - startTime < 0.3 * NANOSECONDS_PER_SECOND) {
//                drivetrain.odometry()
//            }
//            drivetrain.setPowers(0.0)
//
//            startTime = System.nanoTime()
//
//            while (System.nanoTime() - startTime < 0.1 * NANOSECONDS_PER_SECOND) {
//                drivetrain.odometry()
//            }
//            drivetrain.reset()
//
//            intake.fastSuck()
//
//            drivetrain.move(
//                0,
//                12.0 + (i - 1) * 3.0,
//                0,
//                brake = false
//            )
//
//            if (i == CYCLES) {
//                break
//            }
//
//            small = 0.2
//            startTime = System.nanoTime()
//            drivetrain.setPowers(small, small, small, small)
//            while (System.nanoTime() - startTime < 1.1 * NANOSECONDS_PER_SECOND) {
//                drivetrain.odometry()
//            }
//
//            thread {
//                Thread.sleep(400)
//                intake.spit()
//            }
//
//            drivetrain.move(y = 0, brake = false)
////break
////            scoring.close()
//            intake.off()
////            lift.top()
////            spin.up()
//
//            drivetrain.move(-24, -20, -60)
        }
    }
}
