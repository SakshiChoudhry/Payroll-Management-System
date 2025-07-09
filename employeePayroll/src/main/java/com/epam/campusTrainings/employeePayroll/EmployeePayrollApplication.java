package com.epam.campusTrainings.employeePayroll;

import com.epam.campusTrainings.employeePayroll.config.RsaConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.logging.Logger;


@SpringBootApplication
@EnableConfigurationProperties(RsaConfigurationProperties.class)
public class EmployeePayrollApplication{

	private static final Logger logger = Logger.getLogger(EmployeePayrollApplication.class.getName());

	public static void main(String[] args) {

		logger.info("Application Starting");
		SpringApplication.run(EmployeePayrollApplication.class, args);
	}

}
