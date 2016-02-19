package br.on.daed.kinect.services.log;

import com.google.gson.Gson;
import edu.ufl.digitalworlds.j4k.Skeleton;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.imageio.ImageIO;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 *
 * @author csiqueira
 */
@Service
public class LogDLO {

    private BufferedImage convertImage(byte[] bytes) {
        int width = 640;
        int height = 480;

        int[] shifted = new int[width * height];

        // (byte) bgra to rgb (int)
        for (int i = 0, j = 0; i < bytes.length; i = i + 4, j++) {
            int b, g, r;

            b = bytes[i] & 0xFF;
            g = bytes[i + 1] & 0xFF;
            r = bytes[i + 2] & 0xFF;

            shifted[(width - 1 - (j % width)) + ((int) j / width) * width] = (r << 16) | (g << 8) | b;
        }

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        bufferedImage.getRaster().setDataElements(0, 0, width, height, shifted);

        return bufferedImage;
    }

//    public byte[] getImage(Long id) throws IOException {
//        Log lod = logDAO.findOne(id);
//
//        byte[] bytes = lod.getImage();
//        BufferedImage bufferedImage = convertImage(bytes);
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ImageIO.write(bufferedImage, "JPG", baos);
//        byte[] ret = baos.toByteArray();
//
//        return ret;
//    }
    
    public void add(byte[] lastColorData, float[] lastDepthData, Skeleton skeleton) {

        Gson gson = new Gson();
        String depthData = gson.toJson(lastDepthData);

        String exportDirName = "export";

        File dir = new File(exportDirName);

        if (!dir.exists()) {
            dir.mkdir();
        }

        exportDirName += File.separator + new DateTime().toString("dd-MM-yyyy-HH-mm-ss-S");

        dir = new File(exportDirName);

        if (!dir.exists()) {
            dir.mkdir();
        }

        try {

            File depthFile = new File(exportDirName + File.separator + "_depth.json");
            depthFile.createNewFile();

            FileOutputStream fileOutputStream = new FileOutputStream(depthFile);

            OutputStreamWriter osw = new OutputStreamWriter(fileOutputStream);
            osw.write(depthData);
            osw.close();
            fileOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            File skeletonFile = new File(exportDirName + File.separator + "_skeleton.json");
            skeletonFile.createNewFile();

            FileOutputStream fileOutputStream = new FileOutputStream(skeletonFile);

            HashMap<String, double[]> skeletonData = new HashMap<>();
            
            skeletonData.put("head", skeleton.get3DJoint(Skeleton.HEAD));
            skeletonData.put("elbowLeft", skeleton.get3DJoint(Skeleton.ELBOW_LEFT));
            skeletonData.put("elbowRight", skeleton.get3DJoint(Skeleton.ELBOW_RIGHT));
            skeletonData.put("neck", skeleton.get3DJoint(Skeleton.NECK));
            skeletonData.put("shoulderLeft", skeleton.get3DJoint(Skeleton.SHOULDER_LEFT));
            skeletonData.put("shoulderRight", skeleton.get3DJoint(Skeleton.SHOULDER_RIGHT));
            skeletonData.put("kneeLeft", skeleton.get3DJoint(Skeleton.KNEE_LEFT));
            skeletonData.put("kneeRight", skeleton.get3DJoint(Skeleton.KNEE_RIGHT));
            
            skeletonData.put("handLeft", skeleton.get3DJoint(Skeleton.HAND_LEFT));
            skeletonData.put("handRight", skeleton.get3DJoint(Skeleton.HAND_RIGHT));
            
            skeletonData.put("footLeft", skeleton.get3DJoint(Skeleton.FOOT_LEFT));
            skeletonData.put("footRight", skeleton.get3DJoint(Skeleton.FOOT_RIGHT));
            
            skeletonData.put("spineBase", skeleton.get3DJoint(Skeleton.SPINE_BASE));
            skeletonData.put("spineMid", skeleton.get3DJoint(Skeleton.SPINE_MID));

            skeletonData.put("hipLeft", skeleton.get3DJoint(Skeleton.HIP_LEFT));
            skeletonData.put("hipRight", skeleton.get3DJoint(Skeleton.HIP_RIGHT));
            
            OutputStreamWriter osw = new OutputStreamWriter(fileOutputStream);
            osw.write(gson.toJson(skeletonData));
            osw.close();
            fileOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        File out = new File(exportDirName + File.separator + "_color.jpg");

        try {
            BufferedImage bImageFromConvert = convertImage(lastColorData);
            ImageIO.write(bImageFromConvert, "JPG", out);
        } catch (IOException | IllegalStateException ex) {
            Logger.getLogger(LogDLO.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
