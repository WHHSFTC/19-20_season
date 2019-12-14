package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
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

    
}

