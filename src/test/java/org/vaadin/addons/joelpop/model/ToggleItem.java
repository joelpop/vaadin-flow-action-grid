package org.vaadin.addons.joelpop.model;

public class ToggleItem {
    private String name;
    private String info;
    private boolean on;
    private boolean visible;
    private boolean enabled;

    public ToggleItem(String name, String info, boolean on, boolean visible, boolean enabled) {
        setName(name);
        setInfo(info);
        setOn(on);
        setVisible(visible);
        setEnabled(enabled);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
