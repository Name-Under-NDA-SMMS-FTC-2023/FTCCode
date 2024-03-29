package org.firstinspires.ftc.teamcode;
import java.util.List;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

import java.util.ArrayList;

@Autonomous(name = "Autonomous")
public class autonomous extends LinearOpMode
{
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;
    private DcMotor ClawHeight = null;
    private Servo Claw = null;
    public final double radius = 9;
    public static final int DC_MOTOR_COUNTS_PER_REV = 28;
    public static final int DC_MOTOR_GEAR_RATIO = 60;
    public static final int DC_MOTOR_COUNTS = (int)((DC_MOTOR_COUNTS_PER_REV * DC_MOTOR_GEAR_RATIO) / Math.PI);
    public static final int DRIVETRAIN_WHEEL_DIAMETER = 4;
    public static final int DRIVETRAIN_COUNTS_PER_INCH = DC_MOTOR_COUNTS / DRIVETRAIN_WHEEL_DIAMETER;
    public static final String VUFORIA_LICENSE_KEY = "AV30Ctb/////AAABmRiz7bH9QEWLjtsiGkKgKIZ4N4BR7dV9S8/x48RfBEXaL3clCgsI5g8kDnWykPIHUl1yeW/uTdkbGn8fpN2PlooQcVjKkjkzFz8PaMQfEP6TEb4zbSd0sSM0qzvw0KumTdmAlrtJ8ToT8R+422OwpzaAQrCNt6VdRsglQNPw/lqqRqHM8rvdWwzn0Hql3xJNUD47m1/ZF1R/ZxZ3CWwzT2nqSzEh0i6zxWqS8XXaVBCxHOx0ud9xp+UZfD8HQiuk0XlJaklgcmGAPiBYOUEXAjTzIDuTYv43LAwq9MXzidUh63DCUounB2fo1wA4U/ZvDqfTs0nF0dsNpdl1VbFbfJ7hdn1td8enRGfLd8JlQp+Q";
    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;
    public boolean Left = false;
    public boolean Middle = false;
    public boolean Right = false;
    public boolean Terminal = false;
    
    static final double FEET_PER_METER = 3.28084;

    // Lens intrinsics
    // UNITS ARE PIXELS
    // NOTE: this calibration is for the C920 webcam at 800x448.
    // You will need to do your own calibration for other configurations!
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    // UNITS ARE METERS
    double tagsize = 0.027;

    // Tag ID 1,2,3 from the 36h11 family
    int LEFT = 1;
    int MIDDLE = 2;
    int RIGHT = 3;

    AprilTagDetection tagOfInterest = null;
    
        public void drive(double speed, int inches, double power) {
            int target = inches * DRIVETRAIN_COUNTS_PER_INCH;
            rightFrontDrive.setTargetPosition(-target);
            leftFrontDrive.setTargetPosition(-target);
            rightBackDrive.setTargetPosition(-target);
            leftBackDrive.setTargetPosition(-target);
            rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightFrontDrive.setPower(power);
            leftFrontDrive.setPower(power);
            rightBackDrive.setPower(power);
            leftBackDrive.setPower(power);
            while (leftFrontDrive.getCurrentPosition() >= leftFrontDrive.getTargetPosition()) {
                telemetry.addData("Forward", "Running");
            }
            rightFrontDrive.setPower(0);
            leftFrontDrive.setPower(0);
            rightBackDrive.setPower(0);
            leftBackDrive.setPower(0);
            rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            
        }        
        public void turnleft(double radians, double power) {
            double distance = 2 * Math.PI * radius * (radians/(2*Math.PI));
            int target = (int) (distance * DRIVETRAIN_COUNTS_PER_INCH);
            rightFrontDrive.setTargetPosition(-target);
            rightBackDrive.setTargetPosition(-target);
            leftBackDrive.setTargetPosition(target);
            leftFrontDrive.setTargetPosition(target);
            leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightFrontDrive.setPower(power);
            rightBackDrive.setPower(power);
            leftBackDrive.setPower(power);
            leftFrontDrive.setPower(power);
            
            while (leftFrontDrive.getCurrentPosition() <= leftFrontDrive.getTargetPosition()) {
                telemetry.addData("Left", "Running");
            }
            rightFrontDrive.setPower(0);
            leftFrontDrive.setPower(0);
            rightBackDrive.setPower(0);
            leftBackDrive.setPower(0);
            rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        }                
        public void turnright(double radians, double power) {
            double distance = 2 * Math.PI * radius * (radians/(2*Math.PI));
            int target = (int) (distance * DRIVETRAIN_COUNTS_PER_INCH);
            leftFrontDrive.setTargetPosition(target);
            rightBackDrive.setTargetPosition(-target);
            leftBackDrive.setTargetPosition(target);
            rightFrontDrive.setTargetPosition(-target);
            rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftFrontDrive.setPower(power);
            rightBackDrive.setPower(power);
            leftBackDrive.setPower(power);
            rightFrontDrive.setPower(power);
            while (leftFrontDrive.getCurrentPosition() >= leftFrontDrive.getTargetPosition()) {
                telemetry.addData("Right", "Running");
            }
            rightFrontDrive.setPower(0);
            leftFrontDrive.setPower(0);
            rightBackDrive.setPower(0);
            leftBackDrive.setPower(0);
            rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
           
        }
    @Override
    public void runOpMode()
    {
        leftFrontDrive  = hardwareMap.get(DcMotor.class, "left_front_drive");
        leftBackDrive  = hardwareMap.get(DcMotor.class, "left_back_drive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive");
        ClawHeight = hardwareMap.get(DcMotor.class, "Claw_height");
        Claw = hardwareMap.get(Servo.class, "Claw");
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);
        ClawHeight.setDirection(DcMotor.Direction.FORWARD);
        ClawHeight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);
        
 

        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {

            }
        });
        





        telemetry.setMsTransmissionInterval(50);

        /*
         * The INIT-loop:
         * This REPLACES waitForStart!
         */
        while (!isStarted() && !isStopRequested())
        {
            rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();

            if(currentDetections.size() != 0)
            {
                boolean tagFound = false;

                for(AprilTagDetection tag : currentDetections)
                {
                    if(tag.id == LEFT || tag.id == MIDDLE || tag.id == RIGHT)
                    {
                        tagOfInterest = tag;
                        tagFound = true;
                        break;
                    }
                }

                if(tagFound)
                {
                    telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
                    tagToTelemetry(tagOfInterest);
                }
                else
                {
                    telemetry.addLine("Don't see tag of interest :(");

                    if(tagOfInterest == null)
                    {
                        telemetry.addLine("(The tag has never been seen)");
                    }
                    else
                    {
                        telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                        tagToTelemetry(tagOfInterest);
                    }
                }

            }
            else
            {
                telemetry.addLine("Don't see tag of interest :(");

                if(tagOfInterest == null)
                {
                    telemetry.addLine("(The tag has never been seen)");
                }
                else
                {
                    telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                    tagToTelemetry(tagOfInterest);
                }

            }

            telemetry.update();
            sleep(20);
        }

        /* Update the telemetry */
        if(tagOfInterest != null)
        {
            telemetry.addLine("Tag snapshot:\n");
            tagToTelemetry(tagOfInterest);
            telemetry.update();
        }
        else
        {
            telemetry.addLine("No tag snapshot available, it was never sighted during the init loop :(");
            telemetry.update();
        }

        int tag;

        if (tagOfInterest == null) {
            tag = 2;
        } else {
            tag = tagOfInterest.id;
        }

        /* Actually do something useful */
        switch (tag) {
            case 1:
                //while(isStarted() && !isStopRequested()){
                    drive(1000, 12, 0.7);
                    turnleft(Math.PI/2, 0.7);
                    drive(1000, 12, 0.7);
                    telemetry.addData("Front L/R", "%4.2f", "%4.2f", leftFrontDrive.getCurrentPosition(), rightFrontDrive.getCurrentPosition());
                telemetry.addData("Back L/R", "%4.2f", "%4.2f", leftBackDrive.getCurrentPosition(), rightBackDrive.getCurrentPosition());
                telemetry.addData("Front L/R target", "%4.2f", "%4.2f", leftFrontDrive.getTargetPosition(), rightFrontDrive.getTargetPosition());
                telemetry.addData("Back L/R target", "%4.2f", "%4.2f", leftBackDrive.getTargetPosition(), rightBackDrive.getTargetPosition());
                //}
                break;
                
            case 2:
                //while(isStarted() && !isStopRequested()){
                    drive(1000, 12, 0.7);
                    telemetry.addData("Front L/R", "%4.2f", "%4.2f", leftFrontDrive.getCurrentPosition(), rightFrontDrive.getCurrentPosition());
                    telemetry.addData("Back L/R", "%4.2f", "%4.2f", leftBackDrive.getCurrentPosition(), rightBackDrive.getCurrentPosition());
                    telemetry.addData("Front L/R target", "%4.2f", "%4.2f", leftFrontDrive.getTargetPosition(), rightFrontDrive.getTargetPosition());
                    telemetry.addData("Back L/R target", "%4.2f", "%4.2f", leftBackDrive.getTargetPosition(), rightBackDrive.getTargetPosition());
                //}
                break;
            case 3:
                //while(isStarted() && !isStopRequested()) {
                    drive(1000, 12, 0.7);
                    turnright(Math.PI/2, 0.7);
                    drive(1000,12,0.7);
                    telemetry.addData("Front L/R", "%4.2f", "%4.2f", leftFrontDrive.getCurrentPosition(), rightFrontDrive.getCurrentPosition());
                    telemetry.addData("Back L/R", "%4.2f", "%4.2f", leftBackDrive.getCurrentPosition(), rightBackDrive.getCurrentPosition());
                    telemetry.addData("Front L/R target", "%4.2f", "%4.2f", leftFrontDrive.getTargetPosition(), rightFrontDrive.getTargetPosition());
                    telemetry.addData("Back L/R target", "%4.2f", "%4.2f", leftBackDrive.getTargetPosition(), rightBackDrive.getTargetPosition());
                //}
                break;
            default:
                //while(isStarted() && !isStopRequested()) {
                turnleft(Math.PI/2, 0.7);
                drive(1000, 12, 0.7);
                telemetry.addData("Front L/R", "%4.2f", "%4.2f", leftFrontDrive.getCurrentPosition(), rightFrontDrive.getCurrentPosition());
                telemetry.addData("Back L/R", "%4.2f", "%4.2f", leftBackDrive.getCurrentPosition(), rightBackDrive.getCurrentPosition());
                telemetry.addData("Front L/R target", "%4.2f", "%4.2f", leftFrontDrive.getTargetPosition(), rightFrontDrive.getTargetPosition());
                telemetry.addData("Back L/R target", "%4.2f", "%4.2f", leftBackDrive.getTargetPosition(), rightBackDrive.getTargetPosition());
                //}
                break;
        }
        /*if(tagOfInterest == null){
            turnleft(Math.PI/2,0.7);
            drive(1000,24,0.7);
        }
        else if(tagOfInterest.id == LEFT){
            drive(1000,24,0.7);
            turnleft(Math.PI/2,0.7);
            drive(1000,24,0.7);
        }   else if(tagOfInterest.id == MIDDLE){
            drive(1000,24,0.7);
        }   else if(tagOfInterest.id == RIGHT){
            drive(1000,24,0.7);
            turnright(Math.PI/2,0.7);
            drive(1000,24,0.7);
        }
    }*/
   /* while(opModeIsActive()) {
        if(Left){
            drive(1000,24,0.7);
            turnleft(Math.PI/2);
            drive(1000,24,0.7);
        }
        else if(Middle){
            drive(1000,24,0.7);
        }
        else if(Right){
            drive(1000,24,0.7);
            turnright(Math.PI/2);
            drive(1000,24,0.7);
        }
        else {
            turnleft(Math.PI/2);
            drive(1000,24,0.7);
        }
    }*/
}
    void tagToTelemetry(AprilTagDetection detection)
    {
        telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
        telemetry.addLine(String.format("Translation X: %.2f feet", detection.pose.x*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Y: %.2f feet", detection.pose.y*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Z: %.2f feet", detection.pose.z*FEET_PER_METER));
        telemetry.addLine(String.format("Rotation Yaw: %.2f degrees", Math.toDegrees(detection.pose.yaw)));
        telemetry.addLine(String.format("Rotation Pitch: %.2f degrees", Math.toDegrees(detection.pose.pitch)));
        telemetry.addLine(String.format("Rotation Roll: %.2f degrees", Math.toDegrees(detection.pose.roll)));
    }
    
}
