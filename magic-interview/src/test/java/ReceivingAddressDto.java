import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

/**
 *
 * @author Cheng Yufei
 * @create 2021-02-22 10:12 上午
 **/
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReceivingAddressDto implements Serializable {

    private static final long serialVersionUID = 4260738386882809808L;

    String username;

    String mobile;

    String address;

    boolean whetherDefault;

    Integer id;

    String plaintext;


}
