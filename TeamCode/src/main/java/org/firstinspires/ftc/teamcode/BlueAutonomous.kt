package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.hardware.Camera.SkystoneDeterminationPipeline.SkystonePosition

@Autonomous(preselectTeleOp = "TeleOp")
class BlueAutonomous : BaseAutonomous() {
    override fun autonomous() {
        drivetrain.move(y = 13.5)
        sleep(1000)
        drivetrain.move(heading = -82)
        sleep(1000)
        drivetrain.move(x = -28.125, heading = -89)

        sleep(500)
        carousel.deliver(Alliance.BLUE)
        sleep(1000)
        drivetrain.move(x = 25)
        lift.bonus()
        sleep(1000)
        drivetrain.move(y = when (camera.analysis) {
            SkystonePosition.RIGHT -> -18
            SkystonePosition.CENTER -> -14.7
            else -> -13
        }
        )
        sleep(1000)
        scoring.right()
        sleep(2500)
        scoring.up()
        drivetrain.move(y = 30.5, powerx=0.4)
        lift.down()
        sleep(1000)
        drivetrain.move(y=22, powerx=0.25)
        sleep(1000)
        drivetrain.move(110, powerx=0.25)
        sleep(1000)
        drivetrain.move(heading=-170)
    }
}
