/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.on.daed.kinect.services;

import br.on.daed.kinect.services.j4ksdk.KinectSDK;
import br.on.daed.kinect.services.log.LogDLO;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAutoConfiguration
@Configuration
@EnableScheduling
@ComponentScan("br.on.daed.kinect.services")
public class SpringBootApplicationInitializer extends SpringBootServletInitializer {

    private static String url = null;

    public static void main(String[] args) throws IOException {
        KinectSDK.start();

        ConfigurableApplicationContext context = SpringApplication.run(SpringBootApplicationInitializer.class, args);
        int port = ((TomcatEmbeddedServletContainer) ((AnnotationConfigEmbeddedWebApplicationContext) context).getEmbeddedServletContainer()).getPort();
        context.registerShutdownHook();
        url = "http://localhost:" + port + "/kinect";
        
        System.out.println(url);

        Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start chrome --start-fullscreen " + url});
    }

    public static String getUrl() {
        return url;
    }

    @Override
    protected SpringApplicationBuilder
            configure(SpringApplicationBuilder application) {
        return application.sources(SpringBootApplicationInitializer.class
        );
    }
}
