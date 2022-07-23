package com.greatlearning.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greatlearning.entity.Employee;
import com.greatlearning.entity.Role;
import com.greatlearning.entity.User;
import com.greatlearning.repository.RoleRepository;
import com.greatlearning.repository.UserRepository;
import com.greatlearning.service.EmployeeService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
final String order = "asc";
	
	@Autowired
	EmployeeService employeeService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

	@PostMapping("/addrole")
	public String addRole(@RequestBody Role role) {

		if (role != null) {
			Role newRole = new Role();
			newRole.setName(role.getName());
			roleRepository.save(newRole);
		} else {
			return "Role Details cannot be Null";
		}
		return "New Role Added to the repository";
	}

	@PostMapping("/newuser")
	public String createUser(@RequestBody User user) {

		if (user != null) {
			User saveUser = new User();
			saveUser.setUsername(user.getUsername());
			saveUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			saveUser.setRoles(user.getRoles());
			userRepository.save(saveUser);
		} else {
			return "User Details cannot be Null";
		}

		return "New User Added to the repository";
	}

	@PutMapping("/update/{user_id}")
	public User updateUser(@RequestBody User user, @PathVariable int user_id) {

		User existingUser = userRepository.findById(user_id).get();

		existingUser.setUsername(user.getUsername());
		existingUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		existingUser.setRoles(user.getRoles());

		userRepository.save(existingUser);

		return existingUser;
	}

	@DeleteMapping("/deleteuser/{user_id}")
	public String deleteUser(@PathVariable int user_id) {

		userRepository.deleteById(user_id);

		return "User is now Deleted";
	} 

	@RequestMapping("/listusers")
	public List<User> listUsers() {

		return userRepository.findAll();
	}

	@RequestMapping("/list")
	public List<Employee> listEmployees() {
		return employeeService.findAll();
	}

	@RequestMapping("/sort")
	public List<Employee> listEmployeesByOrderByAsc(@RequestParam("order") String order) {
		if (order != this.order) {
			return employeeService.findAllByOrderByFirstNameAsc();
		} else {
			return employeeService.findAllByOrderByFirstNameDesc();
		}
	}

	@RequestMapping("/{id}")
	public Employee findEmployeeById(@PathVariable int id) {

		Employee employee = new Employee();

		if (id != 0) {
			employee = employeeService.findById(id);
		}

		return employee;
	}

	@PostMapping("/add")
	public String addEmployee(@RequestBody Employee employee) {

		if (employee != null) {
			Employee newEmployee = new Employee(employee.getFirstName(), employee.getLastName(), employee.getEmail());
			employeeService.save(newEmployee);
		} else {
			return "Employee Details cannot be null";
		}
		return "New Employee added to the Repository";
	}

	@PutMapping("/update")
	public Employee updateEmployee(@RequestBody Employee employee) {

		Employee existingEmployee;
		
		int id = employee.getId(); 

			existingEmployee = employeeService.findById(id);

			existingEmployee.setFirstName(employee.getFirstName());
			existingEmployee.setLastName(employee.getLastName());
			existingEmployee.setEmail(employee.getEmail());

			employeeService.save(existingEmployee);

			return existingEmployee;

	}
	
	@RequestMapping("/search/{firstName}")
	public List<Employee> findEmployeesByFirstName(@PathVariable String firstName){
		return employeeService.findByFirstName(firstName);
	}

	@DeleteMapping("/{id}")
	public String deleteEmployee(@PathVariable int id) {

		if (id != 0) {
			employeeService.deleteById(id);
		} else {
			return "ID cannot be 0";
		}
		return "Deletion completed for Employee id - " + id;
	}


}
