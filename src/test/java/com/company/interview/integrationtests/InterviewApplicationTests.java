package com.company.interview.integrationtests;

import com.company.interview.controllers.CityController;
import com.company.interview.controllers.CountryController;
import com.company.interview.controllers.EmployeeController;
import com.company.interview.controllers.OfficeController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class InterviewApplicationTests {

	@Autowired
	private EmployeeController employeeController;

	@Autowired
	private OfficeController officeController;

	@Autowired
	private CityController cityController;

	@Autowired
	private CountryController countryController;

	@Test
	void contextLoadsTest(){
		assertThat(employeeController).isNotNull();
		assertThat(officeController).isNotNull();
		assertThat(cityController).isNotNull();
		assertThat(countryController).isNotNull();
	}
}