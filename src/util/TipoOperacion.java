
package util;

public enum TipoOperacion {
    
    INGRESO(1,"INGRESO"), EGRESO(2,"EGRESO");

    private final Integer id;
    private final String name;

    TipoOperacion(Integer id, String name) {
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
