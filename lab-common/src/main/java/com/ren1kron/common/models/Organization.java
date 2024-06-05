package com.ren1kron.common.models;



import com.ren1kron.common.models.abstractions.Validatable;

import java.io.Serializable;


public class Organization implements Validatable, Comparable<Organization>, Serializable {
//    public Organization(String fullName, Integer annualTurnover) {
//        this.fullName = fullName;
//        this.annualTurnover = annualTurnover;
//    }
public Organization(String fullName, Integer annualTurnover, int employeesCount) {
    this.fullName = fullName;
    this.annualTurnover = annualTurnover;
    this.employeesCount = employeesCount;
}
    public Organization(String string) {
        this.fullName = string.split(";")[0];
        try {
            try {
                this.annualTurnover = Integer.parseInt(string.split(";")[1]);
            } catch (NumberFormatException e) {
                annualTurnover = null;
            }
            try {
                this.employeesCount = Integer.parseInt(string.split(";")[2]);
            } catch (NumberFormatException ignored) { }
        } catch (ArrayIndexOutOfBoundsException ignored) { }
    }
    private String fullName; //Поле не может быть null
    private Integer annualTurnover; //Поле МОЖЕТ быть null, Значение поля должно быть больше 0
    private int employeesCount; //Значение поля должно быть больше 0


    public String getFullName() {
        return fullName;
    }

    public Integer getAnnualTurnover() {
        return annualTurnover;
    }
    public int getEmployeesCount() {
        return employeesCount;
    }

    public void setAnnualTurnover(Integer annualTurnover) {
        this.annualTurnover = annualTurnover;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public void setEmployeesCount(int employeesCount) {
        this.employeesCount = employeesCount;
    }



//    public void EmployeeAdded() {
//        employeesCount++;
//    }
//    public void EmployeeDeleted() {
//        employeesCount--;
//    }

    @Override
    public boolean validate() {
        if (fullName == null) return false;
        if (annualTurnover != null) if (annualTurnover <= 0) return false;
        return (employeesCount > 0);
    }
//    public boolean validateOrgName() {
//        return fullName == null;
//    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return fullName.equals(that.fullName) && annualTurnover.equals(that.annualTurnover);
    }
    @Override
    public int hashCode() {
        return fullName.hashCode() + annualTurnover.hashCode();
    }

    public String toString() {
        return fullName + ";" + annualTurnover + ";" + employeesCount;
    }

    @Override
    public int compareTo(Organization o) {
        return (this.annualTurnover - o.annualTurnover);
    }
}
