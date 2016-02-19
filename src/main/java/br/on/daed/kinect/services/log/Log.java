///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package br.on.daed.kinect.services.log;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import java.io.Serializable;
//import javax.persistence.Basic;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.Lob;
//import org.hibernate.annotations.Type;
//import org.joda.time.DateTime;
//
///**
// *
// * @author csiqueira
// */
//@Entity
//public class Log implements Serializable {
//    
//    @Id
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
//    private Long Id;
//    
//    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
//    private DateTime eventDate;
//
//    @JsonIgnore
//    @Lob
//    @Basic(fetch=FetchType.LAZY)
//    private byte[] image;
//
//    public DateTime getEventDate() {
//        return eventDate;
//    }
//
//    public void setEventDate(DateTime eventDate) {
//        this.eventDate = eventDate;
//    }
//
//    public byte[] getImage() {
//        return image;
//    }
//
//    public void setImage(byte[] image) {
//        this.image = image;
//    }
//    
//    public Long getId() {
//        return Id;
//    }
//
//    public void setId(Long Id) {
//        this.Id = Id;
//    }
//   
//}
