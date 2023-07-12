package challenge.desafio.challenge.ShortUrl;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "url_metrics")
@ToString
public class ShortUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Getter @Setter
    @Column(name = "long_url")
    private String long_url;

    @Getter @Setter
    @Column(name = "date_of_create")
    private Date dateOfCreate;

    @Getter @Setter
    @Column(name = "is_active")
    private int is_active;

    @Getter @Setter
    @Column(name = "amount_of_clicks")
    private int amount_of_clicks;

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }
}
