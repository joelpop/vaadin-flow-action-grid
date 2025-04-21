package org.vaadin.addons.joelpop.it.cases;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.junit.Test;
import org.vaadin.addons.joelpop.it.AbstractViewTest;
import org.vaadin.addons.joelpop.it.element.view.actiongrid.ActionGridViewElement;
import org.vaadin.addons.joelpop.it.ui.view.actiongrid.ActionGridView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.vaadin.addons.joelpop.content.ActionGridViewContent.TOGGLE_ACTION_KEY;

public class ToggleActionGridPropertyIT extends AbstractViewTest {

    @Test
    public void verifyIcons() {
        var offIconName = VaadinIcon.CLOSE.create().getIcon();
        var onIconName = VaadinIcon.CHECK.create().getIcon();
        assertEquals(offIconName, getToggleActionIconNameForRow(0));
        assertEquals(offIconName, getToggleActionIconNameForRow(1));
        assertEquals(offIconName, getToggleActionIconNameForRow(2));
        assertEquals(offIconName, getToggleActionIconNameForRow(3));
        assertEquals(onIconName, getToggleActionIconNameForRow(4));
        assertEquals(onIconName, getToggleActionIconNameForRow(5));
        assertEquals(onIconName, getToggleActionIconNameForRow(6));
        assertEquals(onIconName, getToggleActionIconNameForRow(7));
    }

    private String getToggleActionIconNameForRow(int rowNum) {
        var toggleActionGridElement = actionGridViewElement().toggleActionGridElement();
        var toggleActionElement = toggleActionGridElement.getActionElementForRow(rowNum, TOGGLE_ACTION_KEY);
        return toggleActionElement.getIconName();
    }

    @Test
    public void verifyClasses() {
        var offClassName = LumoUtility.TextColor.ERROR;
        var onClassName = LumoUtility.TextColor.SUCCESS;
        assertEquals(offClassName, getToggleActionClassNameForRow(0));
        assertEquals(offClassName, getToggleActionClassNameForRow(1));
        assertEquals(offClassName, getToggleActionClassNameForRow(2));
        assertEquals(offClassName, getToggleActionClassNameForRow(3));
        assertEquals(onClassName, getToggleActionClassNameForRow(4));
        assertEquals(onClassName, getToggleActionClassNameForRow(5));
        assertEquals(onClassName, getToggleActionClassNameForRow(6));
        assertEquals(onClassName, getToggleActionClassNameForRow(7));
    }

    private String getToggleActionClassNameForRow(int rowNum) {
        var toggleActionGridElement = actionGridViewElement().toggleActionGridElement();
        var toggleActionElement = toggleActionGridElement.getActionElementForRow(rowNum, TOGGLE_ACTION_KEY);
        return String.join(" ", toggleActionElement.getClassNames());
    }

    @Test
    public void verifyTooltips() {
        var offTooltip = "Off";
        var onTooltip = "On";
        assertEquals(offTooltip, getToggleActionTooltipForRow(0));
        assertEquals(offTooltip, getToggleActionTooltipForRow(1));
        assertEquals(offTooltip, getToggleActionTooltipForRow(2));
        assertEquals(offTooltip, getToggleActionTooltipForRow(3));
        assertEquals(onTooltip, getToggleActionTooltipForRow(4));
        assertEquals(onTooltip, getToggleActionTooltipForRow(5));
        assertEquals(onTooltip, getToggleActionTooltipForRow(6));
        assertEquals(onTooltip, getToggleActionTooltipForRow(7));
    }

    private String getToggleActionTooltipForRow(int rowNum) {
        var toggleActionGridElement = actionGridViewElement().toggleActionGridElement();
        var toggleActionElement = toggleActionGridElement.getActionElementForRow(rowNum, TOGGLE_ACTION_KEY);
        return toggleActionElement.getTooltipText();
    }

    @Test
    public void verifyAccessibleNames() {
        var ariaLabel = "Toggle item state.";
        assertEquals(ariaLabel, getToggleActionAccessibleNameForRow(0));
        assertEquals(ariaLabel, getToggleActionAccessibleNameForRow(1));
        assertEquals(ariaLabel, getToggleActionAccessibleNameForRow(2));
        assertEquals(ariaLabel, getToggleActionAccessibleNameForRow(3));
        assertEquals(ariaLabel, getToggleActionAccessibleNameForRow(4));
        assertEquals(ariaLabel, getToggleActionAccessibleNameForRow(5));
        assertEquals(ariaLabel, getToggleActionAccessibleNameForRow(6));
        assertEquals(ariaLabel, getToggleActionAccessibleNameForRow(7));
    }

    private String getToggleActionAccessibleNameForRow(int rowNum) {
        var toggleActionGridElement = actionGridViewElement().toggleActionGridElement();
        var toggleActionElement = toggleActionGridElement.getActionElementForRow(rowNum, TOGGLE_ACTION_KEY);
        return toggleActionElement.getAccessibleName();
    }

    @Test
    public void verifyVisibilities() {
        assertFalse(isToggleActionVisibleForRow(0));
        assertFalse(isToggleActionVisibleForRow(1));
        assertTrue(isToggleActionVisibleForRow(2));
        assertTrue(isToggleActionVisibleForRow(3));
        assertFalse(isToggleActionVisibleForRow(4));
        assertFalse(isToggleActionVisibleForRow(5));
        assertTrue(isToggleActionVisibleForRow(6));
        assertTrue(isToggleActionVisibleForRow(7));
    }

    private boolean isToggleActionVisibleForRow(int rowNum) {
        var toggleActionGridElement = actionGridViewElement().toggleActionGridElement();
        var toggleActionElement = toggleActionGridElement.getActionElementForRow(rowNum, TOGGLE_ACTION_KEY);
        return toggleActionElement.isVisible();
    }

    @Test
    public void verifyEnables() {
        assertFalse(isToggleActionEnabledForRow(0));
        assertTrue(isToggleActionEnabledForRow(1));
        assertFalse(isToggleActionEnabledForRow(2));
        assertTrue(isToggleActionEnabledForRow(3));
        assertFalse(isToggleActionEnabledForRow(4));
        assertTrue(isToggleActionEnabledForRow(5));
        assertFalse(isToggleActionEnabledForRow(6));
        assertTrue(isToggleActionEnabledForRow(7));
    }

    private boolean isToggleActionEnabledForRow(int rowNum) {
        var toggleActionGridElement = actionGridViewElement().toggleActionGridElement();
        var toggleActionElement = toggleActionGridElement.getActionElementForRow(rowNum, TOGGLE_ACTION_KEY);
        return toggleActionElement.isEnabled();
    }

    private ActionGridViewElement actionGridViewElement() {
        return $(ActionGridViewElement.class)
                .onPage()
                .id(ActionGridView.VIEW_ID);
    }
}
