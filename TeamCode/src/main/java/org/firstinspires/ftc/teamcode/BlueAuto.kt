package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.hardware.Camera.SkystoneDeterminationPipeline.SkystonePosition

@Autonomous(preselectTeleOp = "TeleOp")
class BlueAuto : BaseAutonomous() {
    override fun autonomous() {
        drivetrain.move(y = 14)
        sleep(1000)
        drivetrain.move(heading = -83)
        sleep(1000)
        drivetrain.move(x = -26, heading = -89)

        sleep(100)
        carousel.deliver(Alliance.BLUE)
        sleep(1000)
        drivetrain.move(x = 25)
        lift.bonus()
        sleep(1000)
        drivetrain.move(y = -18 + if (camera.analysis ===
            SkystonePosition.RIGHT) 0 else 3)
        sleep(1000)
        scoring.right()
        sleep(2500)
        drivetrain.move(y = 20)
    }
}
