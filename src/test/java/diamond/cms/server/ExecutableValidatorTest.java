package diamond.cms.server;

import java.lang.reflect.Method;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.executable.ExecutableValidator;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.junit.Test;

public class ExecutableValidatorTest {

    @Test
    public void hibernateVaildTest() throws NoSuchMethodException, SecurityException {
        // 需要校验的方法实例
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        ExecutableValidator validator = factory.getValidator().forExecutables();

        Method method = this.getClass().getMethod("vaildMethod",  Integer.class, String.class, String.class);
        // 校验参数，应该是有两个非法的参数
        Object [] params = new Object[]{100, "", "test"};

        // 获得校验结果 Set 集合，有多少个字段校验错误 Set 的大小就是多少
        Set<ConstraintViolation<ExecutableValidatorTest>> constraintViolationSet =
                validator.validateParameters(this, method, params);

        System.out.println("非法参数校验结果条数: " + constraintViolationSet.size());
        constraintViolationSet.forEach(cons -> {
            System.out.println("非法消息: " + cons.getMessage());
        });

        params = new Object[]{10, "build-test", "test"};
        constraintViolationSet =
                validator.validateParameters(this, method, params);

        System.out.println("合法参数校验结果条数: " + constraintViolationSet.size());
    }

    // 校验示范方法
    public void vaildMethod(@NotNull @Range(min = 0, max = 18)Integer age,@NotBlank String build, String test){}
}
