import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.impl.db.DbIdGenerator;
import org.activiti.engine.impl.persistence.StrongUuidGenerator;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

/**
 * @author Cheng Yufei
 * @create 2024-05-04 18:48
 **/
public class TestActiviti {
    
    @Test
    public void idGenerator() {
        String areaId = "14";
        String right_areaId = StringUtils.right(areaId, 2);
        System.out.println(right_areaId);

        String center = StringUtils.substring("140103", 2, 4);
        System.out.println(center);
    }
}
