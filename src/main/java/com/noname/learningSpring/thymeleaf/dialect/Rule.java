package com.noname.learningSpring.thymeleaf.dialect;

import java.util.List;

public class Rule {
    private  List<String> ids;
    private  String display;

    public Rule() {
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
