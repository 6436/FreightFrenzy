package org.firstinspires.ftc.teamcode

//@Autonomous
class RedFreight : BaseAutonomous() {
    override fun autonomous() {
        lift.bonus()
        repeat(1) {
            scoring.left()
            drivetrain.move(-24, 20, -90)
            scoring.up()
            lift.down()

            drivetrain.move(0, 0, stop = false)
            intake.suck()
            drivetrain.move(x = 24)
            lift.top()
            intake.off()
            drivetrain.move(x = 0, stop = false)
        }
    }
}
