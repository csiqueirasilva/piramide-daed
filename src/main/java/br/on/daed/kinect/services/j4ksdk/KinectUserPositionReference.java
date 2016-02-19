/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.on.daed.kinect.services.j4ksdk;

import edu.ufl.digitalworlds.j4k.Skeleton;

/**
 *
 * @author csiqueira
 */
public class KinectUserPositionReference {

    private final double[] head;
    private final double[] torso;
    private final double[] leftHand;
    private final double[] rightHand;

    public double[] getHead() {
        return head;
    }

    public double[] getTorso() {
        return torso;
    }

    public double[] getLeftHand() {
        return leftHand;
    }

    public double[] getRightHand() {
        return rightHand;
    }
    
    public KinectUserPositionReference(Skeleton skeleton) {
        head = skeleton.get3DJoint(Skeleton.HEAD);
        leftHand = skeleton.get3DJoint(Skeleton.HAND_LEFT);
        rightHand = skeleton.get3DJoint(Skeleton.HAND_RIGHT);
        torso = skeleton.get3DJoint(Skeleton.SPINE_BASE);
    }
    
}