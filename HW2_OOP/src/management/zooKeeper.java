package management;

class zooKeeper implements Supervisor {
    private final Integer id;
    private final String name;

    public zooKeeper(int id, String name) {
        this.id = (Integer) id;
        this.name = name;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
