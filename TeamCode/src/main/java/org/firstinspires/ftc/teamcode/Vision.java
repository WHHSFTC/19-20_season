package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;



public class Vision {
    public int cameraMonitorViewId;
    public VuforiaLocalizer vuforia;
    public TFObjectDetector tfod;
    public LinearOpMode opMode;



    public static final String VUFORIA_KEY = "AePFtGr/////AAABmTqOHBpjPkiDumePTFCRXhlueWd30Y4KQGm4uFHSsP2Rdhtlt2kMXLayBPRbBrX7VJLJfwVMqOYsTUjI63iVCna9oEOLQfRkvkNnj9npDzSzaf59ccQXUBGaO2Ga/lt2nX5mr4yJinI6S9qO43TCW4qURaoXFEjeohvQthjAPDpA13up2yKez6Kr0B+7hTTrETsW6UfSeijS7/ylQORuo02fc9IonaKvCPhvdjlINpDh85+M8bHx6KPCNHE1+v4jmNmGTYCLBwbHWb36j4uHHkMSBN51B6uec7J5A34/LKPUYYKwaKOmrzThbOiAEIu9oieG3zSmUx7enMWFox0pBu0jNOLezwLO5nj+/4JV/Rxx";
    public static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    public static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    public static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    public Vision(HardwareMap hwMap, String webcam) {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = this.vuforia.getCameraName();
        int tfodMonitorViewId = hwMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hwMap.appContext.getPackageName());
        initTfod(tfodMonitorViewId);
    }
    public boolean Activeop(LinearOpMode opMode) {
        boolean active = false;
        if (opMode.opModeIsActive()) {
            active = true;
        }
        return active;
    }
    public void initializeVuforia(VuforiaLocalizer.Parameters vuforiaParameters){
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */


        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(vuforiaParameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    public void initTfod(int tfodMonitorViewId) {

        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }

    public void displayLOCATION(LinearOpMode opMode) {
        if (opMode.opModeIsActive()) {
            /** Activate Tensor Flow Object Detection. */
            if (tfod != null) {
                tfod.activate();
            }

            while (opMode.opModeIsActive()) {
                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        opMode.telemetry.addData("# Object Detected", updatedRecognitions.size());
                        if (updatedRecognitions.size() == 3) {
                            int goldMineralX = -1;
                            int silverMineral1X = -1;
                            int silverMineral2X = -1;
                            int pos = -1;
                            for (Recognition recognition : updatedRecognitions) {
                                if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                    goldMineralX = (int) recognition.getLeft();
                                } else if (silverMineral1X == -1) {
                                    silverMineral1X = (int) recognition.getLeft();
                                } else {
                                    silverMineral2X = (int) recognition.getLeft();
                                }
                            }
                            if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                                if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                                    opMode.telemetry.addData("Gold Mineral Position", 1);
                                    opMode.telemetry.addData("Left", "");
                                    pos = 1;
                                } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                                    opMode.telemetry.addData("Gold Mineral Position", 3);
                                    opMode.telemetry.addData("Right", "");
                                    pos = 3;
                                } else {
                                    opMode.telemetry.addData("Gold Mineral Position", 2);
                                    opMode.telemetry.addData("Center", "");
                                    pos = 2;
                                }
                            }
                        }
                        opMode.telemetry.update();
                    }
                }
            }
        }

        if (tfod != null) {
            tfod.shutdown();
        }
    }



    public void displayTFod(LinearOpMode opMode){
        if(opMode.opModeIsActive()){
            activateTFod();
        }
    }
    public void activateTFod(){
        if (tfod != null) {
            tfod.activate();
        }
    }

    public void deactivateTFOD(){
        if (tfod != null) {
            tfod.shutdown();
        }
    }


}


