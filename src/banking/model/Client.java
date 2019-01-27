package banking.model;

public class Client {
    private int id;
    private String first_name;
    private String last_name;
    private int age;
    private String sex;
    private String region;
    private int income;
    private boolean married;
    private int child_count;
    private boolean car;
    private boolean save_act;
    private boolean current_act;
    private boolean mortgage;
    private boolean pep;
    public Client () {}

    public int getId () {
        return id;
    }

    public Client setId (int id) {
        this.id = id;
        return this;
    }

    public String getFirstName () {
        return first_name;
    }

    public Client setFirstName (String first_name) {
        this.first_name = first_name;
        return this;
    }

    public String getLastName () {
        return last_name;
    }

    public Client setLastName (String last_name) {
        this.last_name = last_name;
        return this;
    }

    public int getAge () {
        return age;
    }

    public Client setAge (int age) {
        this.age = age;
        return this;
    }

    public String getSex () {
        return sex;
    }

    public Client setSex (String sex) {
        this.sex = sex;
        return this;
    }

    public String getRegion () {
        return region;
    }

    public Client setRegion (String region) {
        this.region = region;
        return this;
    }

    public int getIncome () {
        return income;
    }

    public Client setIncome (int income) {
        this.income = income;
        return this;
    }

    public boolean isMarried () {
        return married;
    }

    public Client setMarried (boolean married) {
        this.married = married;
        return this;
    }

    public int getChildCount () {
        return child_count;
    }

    public Client setChildCount (int child_count) {
        this.child_count = child_count;
        return this;
    }

    public boolean hasCar () {
        return car;
    }

    public Client setCar (boolean car) {
        this.car = car;
        return this;
    }

    public boolean hasSaveAct () {
        return save_act;
    }

    public Client setSaveAct (boolean save_act) {
        this.save_act = save_act;
        return this;
    }

    public boolean hasCurrentAct () {
        return current_act;
    }

    public Client setCurrentAct (boolean current_act) {
        this.current_act = current_act;
        return this;
    }

    public boolean isMortgage () {
        return mortgage;
    }

    public Client setMortgage (boolean mortgage) {
        this.mortgage = mortgage;
        return this;
    }

    public boolean hasPep () {
        return pep;
    }

    public Client setPep (boolean pep) {
        this.pep = pep;
        return this;
    }

    @Override
    public String toString () {
        return String.format("client: [%d] %s %s", this.id, this.first_name, this.last_name);
    }
}