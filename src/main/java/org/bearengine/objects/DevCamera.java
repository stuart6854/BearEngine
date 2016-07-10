package main.java.org.bearengine.objects;

import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.font.Font;
import main.java.org.bearengine.graphics.Display;
import main.java.org.bearengine.graphics.types.Image;
import main.java.org.bearengine.graphics.types.Texture;
import main.java.org.bearengine.input.Keyboard;
import main.java.org.bearengine.input.Mouse;
import main.java.org.bearengine.math.MathUtils;
import main.java.org.bearengine.ui.Canvas;
import main.java.org.bearengine.ui.Label;
import main.java.org.bearengine.ui.Panel;
import main.java.org.bearengine.utils.File;
import main.java.org.bearengine.utils.ResourceLoader;
import main.java.org.joml.Matrix4f;
import main.java.org.joml.Vector3d;
import main.java.org.joml.Vector3f;

import java.text.DecimalFormat;

/**
 * Created by Stuart on 05/06/2016.
 */
public class DevCamera extends Camera {

    public static DevCamera DEV_CAMERA = new DevCamera();

    public static boolean ENABLED = false;

    private static float Velocity = 5.0f;
    private static float MouseSensitivity = 7.5f;

    Image fontImage = new Image("/main/java/resources/fonts/OpenSans_8_DF.png");
    Texture texture = new Texture().UploadTexture(fontImage);
    Font font = new Font(8, texture, ResourceLoader.Load("/main/java/resources/fonts/OpenSans_8_DF.fnt", File.class));

    private Canvas canvas = new Canvas();
    private Panel panel = new Panel();
//    private Label label = new Label("X,Y,Z:", font);

    private Vector3d lastPos = new Vector3d();

    public DevCamera() {
        super(new Matrix4f().perspective((float)Math.toRadians(60f), Display.mainDisplay.Aspect, 0.1f, 1000.0f));
        super.Name = "DevCamera";

//        panel.SetNormalisedPosition(1f, 0f);
//        panel.SetWidth(128);
//        panel.SetHeight(128);
//        panel.SetPixelOffset(-128, 0, 0);
//        panel.IsVisible = ENABLED;
//        canvas.AddChild(panel);

//        label.Name = "DevCam_Pos_Label";
//        label.SetNormalisedPosition(0f, 0f);
//        panel.Name = "DevCam_Panel";
//        panel.AddChild(label);
    }

    public static void ProcessInput(float deltaTime){
        if(Keyboard.isClicked(Keyboard.KEY_F6)){
            ENABLED = !ENABLED;

            Display.mainDisplay.LockMouse(ENABLED);

            DEV_CAMERA.panel.IsVisible = ENABLED;

            Debug.log("DevCamera -> Enabled=" + ENABLED);
        }

        if(ENABLED){
            ProcessMovement(deltaTime);
            ProcessView(deltaTime);
            DEV_CAMERA.viewIsDirty = true;

            if(!DEV_CAMERA.lastPos.equals(DEV_CAMERA.Position)){
                DEV_CAMERA.lastPos.set(DEV_CAMERA.Position);
                DecimalFormat df = new DecimalFormat("#.###");
//                DEV_CAMERA.label.SetText("X,Y,Z: " + df.format(DEV_CAMERA.Position.x) + ", " + df.format(DEV_CAMERA.Position.y) + ", " + df.format(DEV_CAMERA.Position.z));
            }

        }
    }

    private static void ProcessMovement(float deltaTime){
        float speed = Velocity * deltaTime;
        Vector3f cameraInc = new Vector3f();

        if(Keyboard.isPressed(Keyboard.KEY_W)){
            cameraInc.z -= speed;
        }
        if(Keyboard.isPressed(Keyboard.KEY_S)){
            cameraInc.z += speed;
        }
        if(Keyboard.isPressed(Keyboard.KEY_A)){
            cameraInc.x -= speed;
        }
        if(Keyboard.isPressed(Keyboard.KEY_D)){
            cameraInc.x += speed;
        }
        if(Keyboard.isPressed(Keyboard.KEY_SPACE)){
            cameraInc.y += speed;
        }
        if(Keyboard.isPressed(Keyboard.KEY_LEFT_ALT)){
            cameraInc.y -= speed;
        }

        MoveCamPosition(cameraInc.x, cameraInc.y, cameraInc.z);
    }

    private static void ProcessView(float deltaTime){
        float dx = (float)Mouse.getDX();
        float dy = (float)Mouse.getDY();

        float rotYaw = dx * (MouseSensitivity * deltaTime);
        float rotPitch = dy * (MouseSensitivity * deltaTime);

        MoveCamRotation(rotPitch, rotYaw, 0);
    }

    private static void MoveCamPosition(float offsetX, float offsetY, float offsetZ){
        if ( offsetZ != 0 ) {
            DEV_CAMERA.Position.x += (float)Math.sin(Math.toRadians(DEV_CAMERA.Rotation.y)) * -1.0f * offsetZ;
            DEV_CAMERA.Position.z += (float)Math.cos(Math.toRadians(DEV_CAMERA.Rotation.y)) * offsetZ;
        }
        if ( offsetX != 0) {
            DEV_CAMERA.Position.x += (float)Math.sin(Math.toRadians(DEV_CAMERA.Rotation.y - 90)) * -1.0f * offsetX;
            DEV_CAMERA.Position.z += (float)Math.cos(Math.toRadians(DEV_CAMERA.Rotation.y - 90)) * offsetX;
        }
        DEV_CAMERA.Position.y += offsetY;
    }

    public static void MoveCamRotation(float offsetX, float offsetY, float offsetZ) {
        DEV_CAMERA.Rotation.x += offsetX;
        DEV_CAMERA.Rotation.y += offsetY;
        DEV_CAMERA.Rotation.y += offsetZ;

        DEV_CAMERA.Rotation.x = MathUtils.Clamp(DEV_CAMERA.Rotation.x, -90, 90);
    }


}
