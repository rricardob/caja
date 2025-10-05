
package util;

public enum Roles {
    CAJERO(1,"CAJERO"), SUPERVISOR(2,"SUPERVISOR");

    private final Integer id;
    private final String name;

    Roles(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public Integer getId(){
        return id;
    }
}
