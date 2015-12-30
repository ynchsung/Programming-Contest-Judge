package Controller;

import javafx.util.Builder;

/**
 * Created by aalexx on 12/27/15.
 *
 * OpenCode's builder class.
 */
public class OpenCodeBuilder implements Builder {
    private String code = "";

    public static OpenCodeBuilder create () {
        return new OpenCodeBuilder();
    }

    public OpenCodeBuilder setCode (String code) {
        this.code = code;
        return this;
    }

    @Override
    public OpenCode build () {
        OpenCode target = new OpenCode();
        target.setCode(code);
        return target;
    }
}
