/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.on.daed.kinect.services.files;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 *
 * @author caio
 */
@Service
public class FileDatabase {

    public static void main(String[] args) {
        String path = "src/main/resources/static/kinect-data/";
        
        listarObservacao(path, "1");
    }

    public static HashMap<String, String> obterLocais() {
        HashMap<String, String> locais = new HashMap<>();

        locais.put("1", "Divisão de Atividades Educacionais - Observatório Nacional");
        locais.put("2", "SBPC 2015");
        locais.put("3", "SNCT 2016");

        return locais;
    }

    public static List<String> listarObservacao(String path, String idLocal) {

        HashMap<String, String> locais = obterLocais();

        final List<String> ret = new ArrayList<>();

        if (locais.containsKey(idLocal)) {
            
            path += File.separator + idLocal;
            
            File folder = new File(path);

            String baseUrl = "kinect-data/";

            folder.listFiles(new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {

                    if (name.matches("\\d{2}-\\d{2}-\\d{4}-\\d{2}-\\d{2}-\\d{2}-\\d")) {
                        ret.add(baseUrl + idLocal + "/" + name);
                    }

                    return false;
                }

            });

        }

        return ret;
    }

}