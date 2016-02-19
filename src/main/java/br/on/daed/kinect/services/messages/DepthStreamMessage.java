/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.on.daed.kinect.services.messages;

public class DepthStreamMessage {

    public DepthStreamMessage(float[] X, float[] Y, float[] Z) {
        this.X = X;
        this.Y = Y;
        this.Z = Z;
    }

    private float[] X;
    private float[] Y;
    private float[] Z;

    public float[] getX() {
        return X;
    }

    public void setX(float[] X) {
        this.X = X;
    }

    public float[] getY() {
        return Y;
    }

    public void setY(float[] Y) {
        this.Y = Y;
    }

    public float[] getZ() {
        return Z;
    }

    public void setZ(float[] Z) {
        this.Z = Z;
    }
}
