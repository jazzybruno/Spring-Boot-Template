package rw.ac.rca.spring_boot_template.services;

import rw.ac.rca.spring_boot_template.enumerations.EUserRole;
import rw.ac.rca.spring_boot_template.models.Role;

import java.util.List;
import java.util.UUID;

public interface RoleService {
    public List<Role> getAllRoles();
    public Role getRoleById(UUID roleId);
    public  Role getRoleByName(EUserRole roleName);
    public void createRole(EUserRole roleName);
    public Role deleteRole(UUID roleId);
    public boolean isRolePresent(EUserRole roleName);
}
