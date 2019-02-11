package banking.model;

public class Client {
    private int id;
    private String first_name;
    private String last_name;
    private int age;
    private String sex;
    private String region;
    private long income;
    private boolean is_married;
    private int child_count;
    private boolean has_car;
    private boolean has_save_act;
    private boolean has_current_act;
    private boolean has_mortgage;
    private boolean has_pep;
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

    public String getFullName () {
        return String.format("%s %s", this.first_name, this.last_name);
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

    public long getIncome () {
        return income;
    }

    public Client setIncome (long income) {
        this.income = income;
        return this;
    }

    public boolean isMarried () {
        return is_married;
    }

    public Client setIsMarried (boolean married) {
        this.is_married = married;
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
        return has_car;
    }

    public Client setHasCar (boolean car) {
        this.has_car = car;
        return this;
    }

    public boolean hasSaveAct () {
        return has_save_act;
    }

    public Client setHasSaveAct (boolean save_act) {
        this.has_save_act = save_act;
        return this;
    }

    public boolean hasCurrentAct () {
        return has_current_act;
    }

    public Client setHasCurrentAct (boolean current_act) {
        this.has_current_act = current_act;
        return this;
    }

    public boolean hasMortgage () {
        return has_mortgage;
    }

    public Client setHasMortgage (boolean mortgage) {
        this.has_mortgage = mortgage;
        return this;
    }

    public boolean hasPep () {
        return has_pep;
    }

    public Client setHasPep (boolean pep) {
        this.has_pep = pep;
        return this;
    }

    @Override
    public String toString () {
        return String.format("client: [%d] %s %s", this.id, this.first_name, this.last_name);
    }
}