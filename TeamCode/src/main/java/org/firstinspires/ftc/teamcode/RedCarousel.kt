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
        sleep(2500)
        carousel.off()
        drivetrain.move(y = 40)
        lift.bonus()
        sleep(500)
        drivetrain.move(19.5, 46)
        sleep(500)
        scoring.right()
        sleep(1500)
        scoring.up()
        sleep(200)
        lift.down()
        when (camera.analysis) {
            SkystonePosition.LEFT -> drivetrain.move(x = 6)
            SkystonePosition.CENTER -> drivetrain.move(x = -6)
            else -> {}
        }
        intake.duckSuck()
        sleep(500)
        drivetrain.move(y = 35)
        sleep(500)
        intake.off()
        lift.top()
        sleep(100)
        drivetrain.move(19, 47)
        sleep(500)
        scoring.right()
        sleep(1800)
        drivetrain.move(-33, 30)
    }

    fun fast() {
        drivetrain.move(-32, 7)
        sleep(100)
        carousel.deliver(Alliance.RED)
        sleep(1800)
        carousel.off()

        drivetrain.move(y = 40, brake = false)
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

        drivetrain.move(-32, 30)
    }
}
