package org.vaadin.addons.joelpop.unit.cases;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.testbench.unit.ComponentTester;
import com.vaadin.testbench.unit.UIUnit4Test;
import com.vaadin.testbench.unit.ViewPackages;
import org.junit.Before;
import org.junit.Test;
import org.vaadin.addons.joelpop.model.ToggleItem;
import org.vaadin.addons.joelpop.ui.component.ActionGrid;
import org.vaadin.addons.joelpop.unit.tester.ActionGridTester;
import org.vaadin.addons.joelpop.unit.view.ActionGridView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.vaadin.addons.joelpop.content.ActionGridViewContent.TOGGLE_ACTION_GRID_ID;
import static org.vaadin.addons.joelpop.unit.view.ActionGridView.TOGGLE_ACTION_KEY;

@ViewPackages(packages = { "org.vaadin.addons.joelpop.unit.view" })
public class ActionGridActionsPropertyTest extends UIUnit4Test {

    private ComponentTester<ActionGridView> actionGridViewTester;

    @Before
    public void registerView() {
        RouteConfiguration.forApplicationScope()
                .setAnnotatedRoute(ActionGridView.class);
        actionGridViewTester = test(navigate(ActionGridView.class));
    }

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

    @Test
    public void verifyAriaLabels() {
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

    @SuppressWarnings("unchecked")
    private ActionGridTester<ActionGrid<ToggleItem>, ToggleItem> $toggleActionGrid() {
        // find the text field and get its tester
        var toggleActionGrid = actionGridViewTester.find(ActionGrid.class)
                .id(TOGGLE_ACTION_GRID_ID);
        return test(ActionGridTester.class, toggleActionGrid);
    }

    private String getToggleActionIconNameForRow(int rowNum) {
        return $toggleActionGrid().getActionIconNameForRow(rowNum, TOGGLE_ACTION_KEY);
    }

    private String getToggleActionClassNameForRow(int rowNum) {
        return $toggleActionGrid().getActionClassNameForRow(rowNum, TOGGLE_ACTION_KEY);
    }

    private String getToggleActionTooltipForRow(int rowNum) {
        return $toggleActionGrid().getActionTooltipForRow(rowNum, TOGGLE_ACTION_KEY);
    }

    private String getToggleActionAccessibleNameForRow(int rowNum) {
        return $toggleActionGrid().getActionAccessibleNameForRow(rowNum, TOGGLE_ACTION_KEY);
    }

    private boolean isToggleActionVisibleForRow(int rowNum) {
        return $toggleActionGrid().isActionVisibleForRow(rowNum, TOGGLE_ACTION_KEY);
    }

    private boolean isToggleActionEnabledForRow(int rowNum) {
        return $toggleActionGrid().isActionEnabledForRow(rowNum, TOGGLE_ACTION_KEY);
    }
}
