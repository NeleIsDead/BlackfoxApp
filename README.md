/addUser post

public class User{
    @JsonProperty("FIO") 
    public String fIO;
    public String phone;
    public boolean isAdmin;
}

OK

/activate post

public class Code{
    public String code;
}

public class User{
    public String phone;
    public boolean isAdmin;
    @JsonProperty("FIO") 
    public String fIO;
}

/addTime post

public class Time {
    private String code;
    private String adress;
    private int date;
    private boolean first;
    private boolean second;
}

OK

/allPlaces get

public class Place { !!!!!!!!!!!!!!!! GET
    private String adress;
    private String start;
    private String end;
}

public class Places { !!!!!!!!!!!!!!!!
    private List<Place> places;
}

/countByTime post

public class Count {
    int time;
    String adress;
}

public class Data {
    private int first;
    private int second;
}

/nearest post

public class Code{
    public String code;
}

public class Time {
    private String adress;
    private int date;
    private boolean first;
    private boolean second;
}
