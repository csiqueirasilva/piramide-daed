///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package br.on.daed.kinect.services.log;
//
//import java.util.List;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
///**
// *
// * @author csiqueira
// */
//@Repository
//public interface LogDAO extends CrudRepository<Log, Long> {
//    @Query(nativeQuery = true, value = "SCRIPT TO :script")
//    public List<Object> dumpDataToFile(@Param("script") String file);
//}
//    