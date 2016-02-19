/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.on.daed.kinect.services.controllers;

import br.on.daed.kinect.services.files.FileDatabase;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author caio
 */
@Controller
public class KinectController {

    @RequestMapping(value = "/kinect-db", method = RequestMethod.POST)
    @ResponseBody
    public List<String> kinect(HttpServletRequest request, @RequestParam String idLocal) {
        String path = request.getServletContext().getRealPath("/WEB-INF/classes/static/kinect-data");
        return FileDatabase.listarObservacao(path, idLocal);
    }

    @RequestMapping("/usuarios")
    public String usuarios(ModelMap map) {
        map.addAttribute("listaLocais", FileDatabase.obterLocais());
        return "usuarios";
    }

    @RequestMapping("/modelos")
    public String modelos(ModelMap map) {
        //map.addAttribute("kinect", false);
        return "modelos";
    }

    @RequestMapping("/")
    public String index(ModelMap map) {
        //map.addAttribute("kinect", false);
        return "index-web";
    }

    @RequestMapping("/kinect")
    public String kinect(ModelMap map) {
        map.addAttribute("kinect", true);
        return "index";
    }

    @RequestMapping("/kinect-debug")
    public String kinectDebug(ModelMap map) {
        map.addAttribute("kinect", false);
        return "index";
    }

}
