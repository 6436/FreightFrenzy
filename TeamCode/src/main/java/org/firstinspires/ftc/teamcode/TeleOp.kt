package org.firstinspires.ftc.teamcode

import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardwareMap as globalHardwareMap
import org.firstinspires.ftc.teamcode.gamepad1 as globalGamepad1
import org.firstinspires.ftc.teamcode.gamepad2 as globalGamepad2

@TeleOp
class TeleOp : OpMode() {
    private lateinit var hubs: List<LynxModule>

    private val drivetrain = Mecanum()

    override fun init() {
        globalHardwareMap = hardwareMap
        globalGamepad1 = gamepad1
        globalGamepad2 = gamepad2

        hubs = org.firstinspires.ftc.teamcode.hardwareMap.getAll(LynxModule::class.java)
        for (i in hubs) i.bulkCachingMode = LynxModule.BulkCachingMode.MANUAL

        drivetrain.initialize()
    }

    override fun loop() {
        for (i in hubs) i.clearBulkCache()

        drivetrain.update()
    }
}
