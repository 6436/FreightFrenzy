package org.firstinspires.ftc.teamcode.opmodes.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.Alliance
import org.firstinspires.ftc.teamcode.NANOSECONDS_PER_SECOND
import org.firstinspires.ftc.teamcode.mechanisms.Camera
import org.firstinspires.ftc.teamcode.mechanisms.Scoring
import org.firstinspires.ftc.teamcode.alliance as globalAlliance

@Autonomous
class RedDuckAutonomous : DuckAutonomous() {
    override fun runOpMode() {
        super.runOpMode()

        globalAlliance = Alliance.RED
    }
}

@Autonomous
class BlueDuckAutonomous : DuckAutonomous() {
    override fun runOpMode() {
        super.runOpMode()

        globalAlliance = Alliance.BLUE
    }
}

abstract class DuckAutonomous : BaseAutonomous() {
    override fun autonomous() {
        drivetrain.move(y = -5)
        drivetrain.move(19.82, 1.82, heading = -90)
        while (!carousel.deliver(true)) {
            drivetrain.odometry()
        }
        bonus()
        drivetrain.move(y = -38.5, slot = { scoring.up() })
        drivetrain.move(x = 0, slot = { scoring.up() })
        var startTime = System.nanoTime()
        while (System.nanoTime() - startTime < 0.5 * NANOSECONDS_PER_SECOND) {
            drivetrain.odometry()
        }
        when (camera.analysis) {
            Camera.SkystoneDeterminationPipeline.SkystonePosition.LEFT -> scoring.down()
            Camera.SkystoneDeterminationPipeline.SkystonePosition.CENTER -> scoring.mid()
            else -> scoring.open()
        }
        startTime = System.nanoTime()
        while (System.nanoTime() - startTime < 0.5 * NANOSECONDS_PER_SECOND) {
            drivetrain.odometry()
        }
        scoring.state = Scoring.Companion.Level.DOWN
        scoring.state2 = "start"
        drivetrain.move(x = 23, slot = { scoring.up() })
        drivetrain.move(24, -23, slot = { scoring.up() })
        while (scoring.state2 != "done") {
            scoring.up()
            drivetrain.odometry()
        }
        scoring.leftLift.targetPosition -= 200
        scoring.rightLift.targetPosition -= 200
        sleep(1000)
    }
}
