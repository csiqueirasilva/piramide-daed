/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.on.daed.kinect.services.j4ksdk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.ufl.digitalworlds.j4k.Skeleton;
import static java.lang.Double.NaN;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;

/**
 *
 * @author csiqueira
 */
public class KinectPseudoScreenInterface {

    @JsonIgnore
    private static final double screenWidth = 0.8;

    @JsonIgnore
    private static final double screenHeight = 0.45;

    @JsonIgnore
    private static final double interactDepth = 0.35;

    @JsonIgnore
    private static final double screenYDecrement = 0.25;
    
    public static class ScreenCoords {

        public final double x;
        public final double y;
        public final boolean handDown;
        
        static ScreenCoords create(double[] center, double[] hand) {
            ScreenCoords ret = null;
            if (center[0] - screenWidth / 2 < hand[0] && center[0] + screenWidth / 2 > hand[0]
                    && center[1] - screenHeight / 2 < hand[1] && center[1] + screenHeight / 2 > hand[1]) {
                double x = 1 + (hand[0] - center[0] - screenWidth / 2) / screenWidth;
                double y = -(hand[1] - center[1] - screenHeight / 2) / screenHeight;
                ret = new ScreenCoords(x, y, center[2] > hand[2] + interactDepth);
            }
            return ret;
        }

        ScreenCoords(double pX, double pY, boolean handDown) {
            this.x = pX;
            this.y = pY;
            this.handDown = handDown;
        }
    }

    public final ScreenCoords leftHand;
    public final ScreenCoords rightHand;

    KinectPseudoScreenInterface(Skeleton skeleton) {
        double[] leftHandCoords = skeleton.get3DJoint(Skeleton.HAND_LEFT);
        double[] rightHandCoords = skeleton.get3DJoint(Skeleton.HAND_RIGHT);
        double[] head = skeleton.get3DJoint(Skeleton.HEAD);

        double[] centerScreen = new double[3];
        centerScreen[0] = head[0];
        centerScreen[1] = head[1] - screenYDecrement;
        centerScreen[2] = head[2];

//        System.out.println("centerX: " + centerScreen[0]);
//        System.out.println("centerY: " + centerScreen[1]);
//        System.out.println("centerZ: " + centerScreen[2]);
//
//        System.out.println("lHandX: " + leftHandCoords[0]);
//        System.out.println("lHandY: " + leftHandCoords[1]);
//        System.out.println("lHandZ: " + leftHandCoords[2]);
//
//        System.out.println("rHandX: " + rightHandCoords[0]);
//        System.out.println("rHandY: " + rightHandCoords[1]);
//        System.out.println("rHandZ: " + rightHandCoords[2]);

        this.leftHand = ScreenCoords.create(centerScreen, leftHandCoords);
        this.rightHand = ScreenCoords.create(centerScreen, rightHandCoords);
    }

}
