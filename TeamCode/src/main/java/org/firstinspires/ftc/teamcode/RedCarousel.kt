package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.hardware.Camera.SkystoneDeterminationPipeline.SkystonePosition

@Autonomous(preselectTeleOp = "TeleOp")
class RedCarousel : BaseAutonomous() {
    override fun autonomous() {
        original()
//        fast()
    }

    fun original() {
        drivetrain.move(-32, 7)
        sleep(100)
        carousel.deliver(Alliance.RED)
        drivetrain.move(
            y = when (camera.analysis) {
                SkystonePosition.LEFT -> 48.5
                else -> 46.2
            }
        )
        lift.bonus()
        sleep(1000)
        drivetrain.move(
            x = 20.7 - when (camera.analysis) {
                SkystonePosition.RIGHT -> 0.0
                else -> 2.3
            }
        )
        sleep(1000)
        scoring.right()
        sleep(2500)
        scoring.up()
        sleep(500)
        lift.down()
        when (camera.analysis) {
            SkystonePosition.LEFT -> drivetrain.move(x = -7.7)
            SkystonePosition.CENTER -> drivetrain.move(x = 2.5)
            else -> {}
        }
        intake.duckSuck()
        sleep(1000)
        drivetrain.move(y = 35)
        sleep(1000)
        intake.off()
        lift.top()
        sleep(500)
        drivetrain.move(19, 45.5)
        sleep(500)
        scoring.right()
        sleep(1800)

        drivetrain.move(
            -32, 30.7
        )
    }

    fun fast() {
        drivetrain.move(-32, 7)
        sleep(100)
        carousel.deliver(Alliance.RED)

        drivetrain.move(y = 41, brake = false)
        lift.bonus()
        scoring.right()
        drivetrain.move(19.5, 46)
        scoring.up()
        lift.down()

        intake.duckSuck()
        when (camera.analysis) {
            SkystonePosition.LEFT -> drivetrain.move(x = 6, brake = false)
            SkystonePosition.CENTER -> drivetrain.move(x = -6, brake = false)
            else -> {}
        }
        drivetrain.move(y = 35)
        intake.off()
        lift.top()
        scoring.right()
        drivetrain.move(19, 46)
        sleep(1000)

        drivetrain.move(
            -32, when (camera.analysis) {
                SkystonePosition.RIGHT -> 28.5
                else -> 30
            }
        )
    }
}
