package org.firstinspires.ftc.teamcode

import org.firstinspires.ftc.teamcode.hardware.Camera

@com.qualcomm.robotcore.eventloop.opmode.Autonomous
class Ff : Autonomous() {
    override fun autonomous() {
        when (camera.analysis) {
            Camera.SkystoneDeterminationPipeline.SkystonePosition.LEFT -> lift.bottom()
            Camera.SkystoneDeterminationPipeline.SkystonePosition.CENTER -> lift.middle()
            else ->lift.top()
        }
        spin.up()
        when (camera.analysis) {
            Camera.SkystoneDeterminationPipeline.SkystonePosition.LEFT -> drivetrain.move(-23, -19)
            Camera.SkystoneDeterminationPipeline.SkystonePosition.CENTER -> drivetrain.move(-24, -20)
            else -> drivetrain.move(-24.5, -20)
        }
        scoring.open()

        var startTime = System.nanoTime()
        while (System.nanoTime() - startTime < 0.4 * NANOSECONDS_PER_SECOND) {
            drivetrain.odometry()
        }

        val small = alliance.value * 0.3
        drivetrain.setPowers(small, -small, -small, small)
        startTime = System.nanoTime()
        while (System.nanoTime() - startTime < 8.0 * NANOSECONDS_PER_SECOND) {
            drivetrain.odometry()
        }

        spin.down()
        lift.down()
        drivetrain.move(drivetrain.location.x, y=-24)
    }
}