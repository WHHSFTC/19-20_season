package org.firstinspires.ftc.teamcode.implementations;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

public class VisionTF {
    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";

    /*
     * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
     * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
     * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
     * web site at https://developer.vuforia.com/license-manager.
     *
     * Vuforia license keys are always 380 characters long, and look as if they contain mostly
     * random data. As an example, here is a example of a fragment of a valid key:
     *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
     * Once you've obtained a license key, copy the string from the Vuforia web site
     * and paste it in to your code on the next line, between the double quotes.
     */
    private static final String VUFORIA_KEY =
            "AZjnTyD/////AAABmWbY5Kf/tUDGlNmyg0to/Ocsr2x5NKR0bN0q9InlH4shr90xC/iovUPDBu+PWzwD2+F8moAWhCpUivQDuKp/j2IHVtyjoKOQvPkTaXAb1IgPtAM6pMDltXDTkQ8Olwds22Z97Wdx+RAPK8WrC809Hj+JDZJJ3/Lx3bqAwcR1TRJ4OejxkWVSAKvFX8rOp5gE82jPNEv1bQ5S+iTgFtToZNQTj2ldtYJjoSkyUHqfODyV3JUazYSu82UEak0My2Ks/zIXYrDEY0y5MgNzRr9pzg3AiA8pbUT3SVk3SSUYmjlml+H9HovgDuiGrnJnmNMSjQGfcGpliGW6fs61ePYuAHvN4+Rwa1esR/prFgYKrTTn";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    private LinearOpMode opMode;

    private String webcam;

    public VisionTF(LinearOpMode opMode, String webcam) {
        this.opMode = opMode;
        this.webcam = webcam;
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            opMode.telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (tfod != null) {
            tfod.activate();
        }

        /** Wait for the game to begin */
        opMode.telemetry.update();
    }

    public boolean getStone() throws InterruptedException {
        boolean skystone = false;
        ElapsedTime check = new ElapsedTime();
        String label = "";
        // getUpdatedRecognitions() will return null if no new information is available since
        // the last time that call was made.
        tfod.activate();
        Thread.sleep(1000);
        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();

        opMode.telemetry.addData("# Object Detected", updatedRecognitions.size());
        // step through the list of recognitions and display boundary info.
//        int i = 0;
        for (Recognition r : updatedRecognitions) {
            skystone |= (r.getLabel().toLowerCase().equals("skystone"));
        }
//            opMode.telemetry.addData(String.format("label (%d)", i), label);
//            opMode.telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
//                    recognition.getLeft(), recognition.getTop());
//            opMode.telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
//                    recognition.getRight(), recognition.getBottom());
//            opMode.telemetry.addData("tf", "no object detected by tf");
//        opMode.telemetry.update();
        return skystone;
    }

    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = opMode.hardwareMap.get(WebcamName.class, webcam);

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = opMode.hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", opMode.hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.8;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }

    public void shutdown() {
        tfod.deactivate();
    }
}
