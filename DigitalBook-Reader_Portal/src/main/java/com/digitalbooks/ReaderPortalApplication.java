package com.digitalbooks;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import com.digitalbooks.entities.BookPurchase;
import com.digitalbooks.repositories.BookPurchaseRepository;
import com.digitalbooks.services.BookService;


@SpringBootApplication
@EnableFeignClients
@EnableCaching
public class ReaderPortalApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ReaderPortalApplication.class, args);
	}
	
}
