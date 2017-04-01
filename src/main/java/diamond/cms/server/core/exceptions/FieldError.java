package diamond.cms.server.core.exceptions;

import java.io.Serializable;

public class FieldError implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = -7211367675859446838L;

    private String name;
    private String message;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format("`%s` %s", name, message);
    }
}
