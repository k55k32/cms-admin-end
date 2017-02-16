package diamond.cms.server.aspect;

import java.lang.reflect.Method;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.junit.Assert;
import org.junit.Test;

import diamond.cms.server.mvc.valid.aspect.RequestParamValidAspect;
public class RequestParamVaildAdviceTest {

    @Test
    public void hibernateVaildTest() throws NoSuchMethodException, SecurityException {
        Method method = this.getClass().getMethod("vaildMethod",  String.class, String.class, String.class);
        Object [] params = new Object[]{null, "", "test"};
        Set<ConstraintViolation<RequestParamVaildAdviceTest>> validateParameters =
                RequestParamValidAspect.ValidMethodParams(this, method, params);
        Assert.assertEquals(2, validateParameters.size());
    }

    public String vaildMethod(@NotNull String name,@NotBlank String build, String test){
        return "";
    }
}
