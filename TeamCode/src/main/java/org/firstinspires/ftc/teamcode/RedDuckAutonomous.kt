package org.firstinspires.ftc.teamcode

import org.firstinspires.ftc.teamcode.hardware.Camera
import org.firstinspires.ftc.teamcode.hardware.Scoring

@com.qualcomm.robotcore.eventloop.opmode.Autonomous
class RedDuckAutonomous : Autonomous() {
    override fun autonomous() {
        drivetrain.move(17, 2.3, -90)
        while (!carousel.deliver(true)) {
            drivetrain.odometry()
        }
        scoring.state = when (camera.analysis) {
            Camera.SkystoneDeterminationPipeline.SkystonePosition.LEFT -> Scoring.Companion.Level.BOTTOM
            Camera.SkystoneDeterminationPipeline.SkystonePosition.CENTER -> Scoring.Companion.Level.MIDDLE
            else -> Scoring.Companion.Level.TOP
        }
        scoring.state2 = "start"
        drivetrain.move(y=-38.5, slot = { scoring.up() })
        drivetrain.move(x=1.5,  slot = { scoring.up() })
        var startTime = System.nanoTime()
        while (System.nanoTime() - startTime < 0.5 * NANOSECONDS_PER_SECOND) {
            drivetrain.odometry()
        }
        scoring.open()
        startTime = System.nanoTime()
        while (System.nanoTime() - startTime < 0.5 * NANOSECONDS_PER_SECOND) {
            drivetrain.odometry()
        }
        scoring.state = Scoring.Companion.Level.DOWN
        scoring.state2 = "start"
        drivetrain.move(x=23 ,slot = { scoring.up() })
        drivetrain.move(24, -23,slot = { scoring.up() })
        while(scoring.state2 != "done") {
            scoring.up()
            drivetrain.odometry()
        }
    }
}