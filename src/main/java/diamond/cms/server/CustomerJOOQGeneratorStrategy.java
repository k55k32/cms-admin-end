package diamond.cms.server;

import org.jooq.tools.StringUtils;
import org.jooq.util.DefaultGeneratorStrategy;
import org.jooq.util.Definition;
/**
 * run java application
 * main class @org.jooq.util.GenerationTool
 * arg: /jooq-code-gen.xml
 * @author Diamond
 *
 */
public class CustomerJOOQGeneratorStrategy extends DefaultGeneratorStrategy {

    @Override
    public String getJavaClassName(Definition definition, Mode mode) {
        StringBuilder result = new StringBuilder();

        result.append(StringUtils.toCamelCase(
                definition.getOutputName()
                          .replace(' ', '_')
                          .replace('-', '_')
                          .replace('.', '_')
            ));

        if (mode == Mode.RECORD) {
            result.append("Record");
        } else if (mode == Mode.DAO) {
            result.append("Dao");
        } else if (mode == Mode.INTERFACE) {
            result.insert(0, "I");
        } else if (mode == Mode.POJO) {
            result.append("Pojo");
        }

        return result.toString();
    }

}
