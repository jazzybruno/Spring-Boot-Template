package rw.ac.rca.spring_boot_template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import rw.ac.rca.spring_boot_template.enumerations.EUserRole;
import rw.ac.rca.spring_boot_template.services.serviceImpl.RoleServiceImpl;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@EnableCaching
public class SpringBootTemplate {
	private RoleServiceImpl roleService;
	@Autowired
	public SpringBootTemplate(RoleServiceImpl roleService) {
		this.roleService = roleService;
	}
	public static void main(String[] args) {
		SpringApplication.run(SpringBootTemplate.class, args);
	}
	@Bean
	public void registerRoles(){
		Set<EUserRole> userRoleSet = new HashSet<>();
		userRoleSet.add(EUserRole.ADMIN);
		userRoleSet.add(EUserRole.USER);
		userRoleSet.add(EUserRole.DEPARTMENT_HEAD);
		for (EUserRole role : userRoleSet){
			if(!(roleService.isRolePresent(role))){
				roleService.createRole(role);
			}
		}
	}
}
