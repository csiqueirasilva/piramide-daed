/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.on.daed.kinect.services.background;

import br.on.daed.kinect.services.j4ksdk.KinectSDK;
import br.on.daed.kinect.services.j4ksdk.KinectUserPositionReference;
import br.on.daed.kinect.services.log.LogDLO;
import edu.ufl.digitalworlds.j4k.DepthMap;
import edu.ufl.digitalworlds.j4k.Skeleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 *
 * @author caio
 */
@Service
public class KinectTasks {

    private final long noDetectionInterval = (long) (0.5 * 1000);

    private final float xDetectLimit = 0.3f;
    
    private final long captureImageInterval = (long) (6 * 1000);
    private long lastCaptureImageTimestamp = -1;
    
    private int lastCapturedUser = -1;
    
    @Autowired
    private LogDLO logDLO;

    @Autowired
    private SimpMessagingTemplate template;

    @Scheduled(fixedRate = 27)
    public void reportPlayer() {
        if (KinectSDK.isStarted()) {
            boolean detectingUser = false;
            float zIndex = Integer.MAX_VALUE;
            KinectUserPositionReference playerPosition = null;

            int currentDetectedUser = -1;
            long currentTimestamp = System.currentTimeMillis();

            if ((currentTimestamp - KinectSDK.getLastSkeletonTimestamp()) < noDetectionInterval) {
                for (int i = 0; i < 6 && !detectingUser; i++) {
                    float playerZIndex = KinectSDK.getPlayerZIndex(i);
                    float playerXIndex = KinectSDK.getPlayerXIndex(i);
                    if (KinectSDK.isTrackingPlayer(i) && playerZIndex < zIndex && Math.abs(playerXIndex) < xDetectLimit) {
                        zIndex = playerZIndex;
                        playerPosition = KinectSDK.getPlayerPosition(i);

                        // debug
                        //Gson gson = new Gson();
                        //System.out.println(gson.toJson(playerScreenInterface));
                        currentDetectedUser = i;
                        detectingUser = true;
                    }
                }
            }

            if (detectingUser) {
                if (lastCapturedUser != currentDetectedUser && (currentTimestamp - lastCaptureImageTimestamp) > captureImageInterval) {
                    lastCaptureImageTimestamp = currentTimestamp;
                    lastCapturedUser = currentDetectedUser;
                    byte[] lastColorData = KinectSDK.getLastColorData();
                    float[] lastDepthData = KinectSDK.getLastDepthData();
                    Skeleton[] lastSkeletons = KinectSDK.getLastSkeletons();
                    logDLO.add(lastColorData, lastDepthData, lastSkeletons[lastCapturedUser]);
                }
                template.convertAndSend("/topic/bodyposition", playerPosition);
            } else {
                lastCapturedUser = -1;
                template.convertAndSend("/topic/nouserdetected", true);
            }
        }
    }
}
