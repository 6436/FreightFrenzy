package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

abstract class TeleOpMode : LinearOpMode() {
    abstract fun initialize()

    abstract fun update()

    @Throws(InterruptedException::class)
    override fun runOpMode() {
        initialize()

        waitForStart()

        while (opModeIsActive()) {
            update()
        }
    }
}
