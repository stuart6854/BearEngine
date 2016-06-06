package main.java.org.bearengine.objects;

import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.graphics.Display;
import main.java.org.bearengine.input.Keyboard;
import main.java.org.bearengine.input.Mouse;
import main.java.org.bearengine.math.MathUtils;
import main.java.org.joml.Matrix4f;
import main.java.org.joml.Vector3f;

/**
 * Created by Stuart on 05/06/2016.
 */
public class DevCamera extends Camera {

    public static DevCamera DEV_CAMERA = new DevCamera();

    public static boolean ENABLED = false;

    private static float Velocity = 5.0f;
    private static float MouseSensitivity = 7.5f;

    public DevCamera() {
        super(new Matrix4f().perspective((float)Math.toRadians(60f), Display.mainDisplay.Aspect, 0.1f, 1000.0f));
        super.Name = "DevCamera";
    }

    public static void ProcessInput(float deltaTime){
        if(Keyboard.isClicked(Keyboard.KEY_F6)){
            ENABLED = !ENABLED;

            Display.mainDisplay.LockMouse(ENABLED);

            Debug.log("DevCamera -> Enabled=" + ENABLED);
        }

        if(ENABLED){
            ProcessMovement(deltaTime);
            ProcessView(deltaTime);
            DEV_CAMERA.viewIsDirty = true;
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
