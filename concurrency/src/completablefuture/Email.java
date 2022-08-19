package completablefuture;

public class Email {
    public Email(long id) {
        this.id = id;
    }

    public Email() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Email{" +
                "id=" + id +
                '}';
    }

    private long id;
}
