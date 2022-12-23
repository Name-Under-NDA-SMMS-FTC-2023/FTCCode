package org.firstinspires.ftc.teamcode;

//importing the needed variables.
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WhiteBalanceControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WhiteBalanceControl.Mode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.FocusControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.FocusControl.Mode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.PtzControl;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
Djava.library.path=/Users/<YOUR_USERNAME>/Library/Python/3.9/lib/python/site-packages/jep
public static final String VUFORIA_LICENSE_KEY = "AV30Ctb/////AAABmRiz7bH9QEWLjtsiGkKgKIZ4N4BR7dV9S8/x48RfBEXaL3clCgsI5g8kDnWykPIHUl1yeW/uTdkbGn8fpN2PlooQcVjKkjkzFz8PaMQfEP6TEb4zbSd0sSM0qzvw0KumTdmAlrtJ8ToT8R+422OwpzaAQrCNt6VdRsglQNPw/lqqRqHM8rvdWwzn0Hql3xJNUD47m1/ZF1R/ZxZ3CWwzT2nqSzEh0i6zxWqS8XXaVBCxHOx0ud9xp+UZfD8HQiuk0XlJaklgcmGAPiBYOUEXAjTzIDuTYv43LAwq9MXzidUh63DCUounB2fo1wA4U/ZvDqfTs0nF0dsNpdl1VbFbfJ7hdn1td8enRGfLd8JlQp+Q";



//autonomous code
@Autonomous(name="autonomous phase", group="Linear Opmode")
public class AutonomousOpMode_Linear extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;
    private DcMotor ClawHeight = null;
    private Servo Claw = null;
    public void runScript(String pythonScriptFullPath) {
        try (Interpreter interp = new SharedInterpreter()) {
            interp.runScript(FTCCodr/src/main/java/org/firstinspires/ftc/teamcode/autonomous.py)
        } catch (JepException e) {
            //do something with exception
        }
    }
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
            "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.75f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 300;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);

        // Use loadModelFromFile() if you have downloaded a custom team model to the Robot Controller's FLASH.
        tfod.loadModelFromFile(best.pt, LABELS);
    }
    //Retrieves motors and servos from the robot controller.
    @Override
    public void runOpMode() {
        leftBackDrive = hardwareMap.get(DcMotor.class, "left_back_drive");
        leftFrontDrive = hardwareMap.get(DcMotor.class, "left_front_drive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        ClawHeight = hardwareMap.get(DcMotor.class, "Claw_height");
        Claw = hardwareMap.get(Servo.class, "Claw");
        leftFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        leftBackDrive.setDirection(DcMotor.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        rightBackDrive.setDirection(DcMotor.Direction.REVERSE);
        ClawHeight.setDirection(DcMotor.Direction.FORWARD);
        Claw.setDirection(Servo.Direction.FORWARD);
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();
        //actives code
        while (opModeIsActive()) {
            double max;

                double axial;
                double lateral;
                double yaw;
                double height;
                double INITposition = 0;
                double position;
                double leftFrontPower = axial+lateral+yaw;
                double rightFrontPower = axial-lateral-yaw;
                double leftBackPower = axial-lateral+yaw;
                double rightBackPower = axial+lateral-yaw;
                double ClawHeightPower;

                max = Math.max(Math.abs(leftFrontDrive), Math.abs(rightFrontDrive));
                max = Math.max(Math.abs(leftBackDrive), max);
                max = Math.max(Math.abs(rightBackDrive), max);
                max = Math.max(Math.abs(ClawHeight), max);

                //Normalizes speed of motors
                if(max>1.0) {
                    leftFrontDrive /= max;
                    rightFrontDrive /= max;
                    leftBackDrive /= max;
                    rightBackDrive /= max;
                    ClawHeight /= max;
                }
                
                //sets motor power
                leftFrontDrive.setPower(leftFrontPower);
                rightFrontDrive.setPower(rightFrontPower);
                leftBackDrive.setPower(leftBackPower);
                rightBackDrive.setPower(rightBackPower);
                ClawHeight.setPower(ClawHeightPower);

                //Shows data on the DS
                telemetry.addData("Status", "Run Time: " + runtime.toString());
                telemetry.addData("Front left/right" , "%.2f", leftFrontPower, rightFrontPower);
                telemetry.addData("Back left/right" , "%.2f", leftBackPower, rightBackPower);
                telemetry.addDada("Axial/Lateral/Yaw" , "%.2f", axial, lateral, yaw);
                telemetry.addData("Claw Height" , "%.2f", ClawHeightPower);
                telemetry.addData(isExposureSupported());
                telemetry.addData(isModeSupported(mode.Manual));
                telemetry.addData(isFocusLengthSupported());
                telemetry.addData(isFocusModeSupported(mode.Manual));
                telemetry.update();
        }
    }
}