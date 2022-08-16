package com.digitalbooks.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.digitalbooks.Models.EmailDetails;
import com.digitalbooks.email.EmailService;

@Service
public class KafkaConsumerListener {

    private static final String TOPIC = "Kafka-Email-topic";
    
    @Autowired
    EmailService emailService;

    @KafkaListener(topics = TOPIC, groupId="group_id", containerFactory = "userKafkaListenerFactory")
    public void consumeJson(EmailDetails emailDetails) 
    {
        System.out.println("Consumed JSON Message: " + emailDetails.toString());
        emailDetails.setSubject(emailDetails.getSubject());
        System.out.println("Email Status :"+ emailService.sendSimpleMail(emailDetails));

    }
    
}