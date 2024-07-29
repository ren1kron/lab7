package com.ren1kron.common.models;



import com.ren1kron.common.models.abstractions.Element;
import com.ren1kron.common.models.abstractions.Validatable;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * This class contents information about Worker – element of collection
 * @author ren1kron
 */

public class Worker extends Element implements Validatable, Serializable {
    public Worker() { }
    public Worker(Integer key, int id, String name, Organization organization, Position position, Status status, float salary,
                  Coordinates coordinates, Date creationDate, LocalDate startDate) {
        this.key = key;
        this.id = id;
        this.name = name;
        this.organization = organization;
        this.position = position;
        this.status = status;
        this.salary = salary;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.startDate = startDate;
    }



    public Worker(Integer key, int id, String name, Organization organization, Position position, Status status, float salary, Coordinates coordinates,
                  LocalDate startDate) {
        this.key = key;
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = new Date();
        this.salary = salary;
        this.startDate = startDate;
        this.position = position;
        this.status = status;
        this.organization = organization;
    }
    public static String[] toArray(Worker worker) {
        var list = new ArrayList<String>();
        list.add(String.valueOf(worker.getKey()));
        list.add(String.valueOf(worker.getId()));
        list.add(worker.getName());
        list.add(worker.getOrganization() == null ? "null" : worker.getOrganization().toString());
        list.add(worker.getPosition().toString());
        list.add(worker.getStatus() == null ? "null" : worker.getStatus().toString());
        list.add(String.valueOf(worker.getSalary()));
        list.add(worker.getCoordinates().toString());
        list.add(String.valueOf(worker.getCreationDate()));
        list.add(worker.getStartDate().format(DateTimeFormatter.ISO_DATE));
        return list.toArray(new String[0]);
    }

//    private int userId;
    private String username; // Name of user which created this worker
    private Integer key; // По ТЗ при вводе нового элемента нам необходимо ввести его ключ.
    // ID генерируется автоматически, остальные поля не уникальны. Поэтому я добавил данное поле, которое решит возникшую проблему.
    // Соответственно, key должен быть уникален и, для простоты ввода, быть больше 0

    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение
    // этого поля должно генерироваться автоматически
    private String name; //Поле НЕ может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле НЕ может быть null
    private Date creationDate; //Поле НЕ может быть null, Значение этого поля должно генерироваться автоматически
    private float salary; //Значение поля должно быть больше 0
    private LocalDate startDate; //Поле НЕ может быть null
    private Position position; //Поле НЕ может быть null
    private Status status; //Поле МОЖЕТ быть null
    private Organization organization; //Поле МОЖЕТ быть null


    public void setId(int id) {
        this.id = id;
    }

//    public
    @Override
    public int compareTo(Element element) {
        return Integer.compare(this.id, element.getId());
    }
//    public float compareTo(Worker worker) {
//        return (this.salary - worker.salary);
//    }
    @Override
    public boolean validate() {
        if (key <= 0) return false;
        if (id <= 0) return false;
        return validateWithoutId();
    }
    public boolean validateWithoutId() {
//        if (key <= 0) return false;
        if (name==null || name.isEmpty()) return false;
        if (coordinates == null) return false;
        if (creationDate == null) return false;
        if (salary <= 0) return false;
        if (startDate == null) return false;
//        if (organization == null) return false;
        if (organization != null) if (!organization.validate()) return false;
        if (!coordinates.validate()) return false;
//        if (status == null) return false;
        return (position != null);
    }

    @Override
    public int getId() {
        return id;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Worker that = (Worker) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, salary, startDate, position, status, organization);
    }
//    @Override
//    public String toString() {
//        return "key: "+ key + "; id: "+ id +"; name: "+ name + "; organization: {" + organization +"}; coordinates: {" + coordinates + "}; Date of appointment: "+ creationDate +
//                "; salary: " + salary + "; Birthday: {" + startDate + "}; position: " + position + "; status: " + status;
//    }


    @Override
    public String toString() {
        return "Worker{" +
                "username='" + username + '\'' +
                ", key=" + key +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", salary=" + salary +
                ", startDate=" + startDate +
                ", position=" + position +
                ", status=" + status +
                ", organization={" + organization +
                "}}";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }
//    public int getYear() {
//        return creationDate.getYear();
//    }
    public int getMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(creationDate);
        return calendar.get(Calendar.MONTH);
    }


    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

//    public int getUserId() {
//        return userId;
//    }
//
//    public void setUserId(int userId) {
//        this.userId = userId;
//    }
}
