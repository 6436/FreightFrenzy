package org.firstinspires.ftc.teamcode.opmodes.testing

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.teamcode.hardwareMap as globalHardwareMap
import org.firstinspires.ftc.teamcode.telemetry as globalTelemetry

abstract class Drivetrain : OpMode() {
    protected companion object {
        const val POWER = 0.50
    }

    private lateinit var hubs: List<LynxModule>

    lateinit var fl: DcMotorEx
    lateinit var fr: DcMotorEx
    lateinit var bl: DcMotorEx
    lateinit var br: DcMotorEx
    lateinit var motors: List<DcMotorEx>

    override fun init() {
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        globalTelemetry = telemetry
        globalHardwareMap = hardwareMap

        hubs = hardwareMap.getAll(LynxModule::class.java)
        hubs.forEach { it.bulkCachingMode = LynxModule.BulkCachingMode.MANUAL }

        fl = hardwareMap.get(DcMotorEx::class.java, ::fl.name)
        fr = hardwareMap.get(DcMotorEx::class.java, ::fr.name)
        bl = hardwareMap.get(DcMotorEx::class.java, ::bl.name)
        br = hardwareMap.get(DcMotorEx::class.java, ::br.name)
        motors = listOf(fl, fr, bl, br)

        motors.forEach {
            it.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT

            it.targetPosition = 0

            it.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

            it.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        }
        fl.direction = DcMotorSimple.Direction.FORWARD
        bl.direction = DcMotorSimple.Direction.FORWARD
        fr.direction = DcMotorSimple.Direction.REVERSE
        br.direction = DcMotorSimple.Direction.REVERSE
    }

    override fun loop() {
        hubs.forEach { it.clearBulkCache() }

        telemetry.addData("left current position", -fr.currentPosition)
        telemetry.addData("right current position", fl.currentPosition)
        telemetry.addData("back current position", -br.currentPosition)
    }
}
