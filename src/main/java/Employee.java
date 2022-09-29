public class Employee {
    public long id;
    public String firstName;
    public String lastName;
    public String country;
    public int age;

    public Employee() {
        // Пустой конструктор
    }

    @Override
    public String toString() {
        return ("Employee{ " + "id=" + id + ", " + "firstName=" + firstName + ", "
                + "lastName=" + lastName + ", " + "country=" + country + ", " + "age=" + age + "}");
    }
    //Employee{id=1, firstName='John', lastName='Smith', country='USA', age=25}
    public Employee(long id, String firstName, String lastName, String country, int age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.age = age;
    }
}