package com.example.employee.management.controllers;

import com.example.employee.management.dto.DepartmentDto;
import com.example.employee.management.model.Department;
import com.example.employee.management.repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;

    @GetMapping
    public List<DepartmentDto> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @PostMapping
    public void createDepartment(@RequestBody DepartmentDto departmentDto) {
        Department department = convertToEntity(departmentDto);
        departmentRepository.save(department);
    }

    @PutMapping("/{id}")
    public void updateDepartment(@PathVariable Long id, @RequestBody DepartmentDto departmentDto) {
        Department department = departmentRepository.findById(id).orElseThrow();
        department.setName(departmentDto.getName());
        department.setNote(departmentDto.getNote());
        if (departmentDto.getParentDepartmentId() != null) {
            Department parent = departmentRepository.findById(departmentDto.getParentDepartmentId()).orElseThrow();
            department.setParentDepartment(parent);
        } else {
            department.setParentDepartment(null);
        }
        departmentRepository.save(department);
    }

    @DeleteMapping("/{id}")
    public void deleteDepartment(@PathVariable Long id) {
        departmentRepository.deleteById(id);
    }

    private DepartmentDto convertToDto(Department department) {
        DepartmentDto dto = new DepartmentDto();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setNote(department.getNote());
        if (department.getParentDepartment() != null) {
            dto.setParentDepartmentId(department.getParentDepartment().getId());
            dto.setParentDepartmentName(department.getParentDepartment().getName());
        }
        return dto;
    }

    private Department convertToEntity(DepartmentDto dto) {
        Department department = new Department();
        department.setName(dto.getName());
        department.setNote(dto.getNote());
        if (dto.getParentDepartmentId() != null) {
            Department parent = departmentRepository.findById(dto.getParentDepartmentId()).orElseThrow();
            department.setParentDepartment(parent);
        }
        return department;
    }
}
