package br.on.daed.kinect.services.j4ksdk;

import com.sun.javafx.iio.ImageStorage;
import edu.ufl.digitalworlds.j4k.DepthMap;
import edu.ufl.digitalworlds.j4k.J4KSDK;
import edu.ufl.digitalworlds.j4k.Skeleton;
import edu.ufl.digitalworlds.j4k.VideoFrame;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author caio
 */
public class KinectSDK extends J4KSDK {

    private static boolean started = false;
    private static KinectSDK handler = null;
    private static Skeleton[] skeletons = null;
    private static float[] lastDepthData = null;
    private static DepthMap depthMap = null;
    private static List<short[]> colorMap = null;
    private static Long lastSkeletonTimestamp = 0l;
    private static byte[] lastColorData = null;

    private KinectSDK() {
    }

    public static void start() {
        if (!started) {
            handler = new KinectSDK();
            started = handler.start(J4KSDK.COLOR | J4KSDK.DEPTH | J4KSDK.SKELETON | J4KSDK.XYZ);
            handler.setNearMode(true);

            if (!started) {
                System.out.println("CANNOT START KINECT");
                return;
            } else {
                System.out.println("KINECT STARTED");
            }

            skeletons = new Skeleton[handler.getMaxNumberOfSkeletons()];

            for (int i = 0; i < skeletons.length; i++) {
                skeletons[i] = new Skeleton();
                skeletons[i].setIsTracked(false);
            }

            depthMap = new DepthMap(handler.getDepthWidth(), handler.getDepthHeight());
            colorMap = new ArrayList();

            for (int i = 0; i < handler.getColorWidth() * handler.getColorHeight(); i++) {
                short[] color = new short[3];
                color[0] = color[1] = color[2] = 0;
                colorMap.add(color);
            }
            
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    KinectSDK.finish();
                }
            }));
        }
    }

    public static void finish() {
        if (handler != null) {
            handler.stop();
            handler = null;
        }
    }

    @Override
    public void onDepthFrameEvent(short[] depth_frame, byte[] player_index, float[] XYZ, float[] UV) {
        lastDepthData = XYZ;
    }

    @Override
    public void onColorFrameEvent(byte[] data) {
        lastColorData = data;
    }

    @Override
    public void onSkeletonFrameEvent(boolean[] skeleton_tracked, float[] joint_position, float[] joint_orientation, byte[] joint_status) {
        lastSkeletonTimestamp = System.currentTimeMillis();
        for (int i = 0; i < this.getMaxNumberOfSkeletons(); i++) {
            skeletons[i] = Skeleton.getSkeleton(i, skeleton_tracked, joint_position, joint_orientation, joint_status, this);
        }
    }

    public static byte[] getLastColorData() {
        return lastColorData;
    }
    
    public static List<short[]> getLastColorMap() {
        return colorMap;
    }

    public static float[] getLastDepthData() {
        return lastDepthData;
    }
    
    public static DepthMap getLastDepthMap() {
        return depthMap;
    }

    public static Skeleton[] getLastSkeletons() {
        return skeletons;
    }

    public static Long getLastSkeletonTimestamp() {
        return lastSkeletonTimestamp;
    }
    
    public static float getPlayerZIndex(int idx) {
        return skeletons[idx].get3DJointZ(Skeleton.HEAD);
    }

    public static float getPlayerXIndex(int idx) {
        return skeletons[idx].get3DJointX(Skeleton.HEAD);
    }    
    
    public static boolean isTrackingPlayer(int idx) {
        return skeletons[idx].isTracked();
    }

    public static KinectPseudoScreenInterface getPlayerScreenInterface(int idx) {
        return new KinectPseudoScreenInterface(skeletons[idx]);
    }

    public static KinectUserPositionReference getPlayerPosition(int idx) {
        return new KinectUserPositionReference(skeletons[idx]);
    }

    public static boolean isStarted() {
        return started;
    }
}
