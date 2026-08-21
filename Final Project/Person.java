public abstract class Person {

    protected String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Each subclass overrides this differently - this is the hook that makes
    // runtime polymorphism visible: calling describeRole() on a Person
    // reference runs different code depending on the actual object underneath.
    public abstract String describeRole();
}
