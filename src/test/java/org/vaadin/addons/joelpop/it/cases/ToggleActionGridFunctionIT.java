package org.vaadin.addons.joelpop.it.cases;

import com.vaadin.flow.component.icon.VaadinIcon;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.vaadin.addons.joelpop.it.AbstractViewTest;
import org.vaadin.addons.joelpop.it.element.view.actiongrid.ActionGridViewElement;
import org.vaadin.addons.joelpop.it.ui.view.actiongrid.ActionGridView;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.vaadin.addons.joelpop.content.ActionGridViewContent.TOGGLE_ACTION_KEY;

@RunWith(Parameterized.class)
public class ToggleActionGridFunctionIT extends AbstractViewTest {
    private final int rowNum;
    private final boolean initialState;
    private final boolean expectedState;

    public ToggleActionGridFunctionIT(int rowNum, boolean initialState, boolean expectedState) {
        this.rowNum = rowNum;
        this.initialState = initialState;
        this.expectedState = expectedState;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {0, false, false}, // toggleOffInvisibleDisabled
                {1, false, false}, // toggleOffInvisibleEnabled
                {2, false, false}, // toggleOffVisibleDisabled
                {3, false, true},  // toggleOffVisibleEnabled
                {4, true, true},   // toggleOnInvisibleDisabled
                {5, true, true},   // toggleOnInvisibleEnabled
                {6, true, true},   // toggleOnVisibleDisabled
                {7, true, false}   // toggleOnVisibleEnabled
        });
    }

    @Test
    public void testToggle() {
        assertEquals(initialState, isToggledOn(rowNum));
        clickToggleActionForRow(rowNum);
        assertEquals(expectedState, isToggledOn(rowNum));
    }

    private boolean isToggledOn(int rowNum) {
        var toggleActionGridElement = actionGridViewElement().toggleActionGridElement();
        var toggleActionElement = toggleActionGridElement.getActionElementForRow(rowNum, TOGGLE_ACTION_KEY);
        return Objects.equals(toggleActionElement.getIconName(), VaadinIcon.CHECK.create().getIcon());
    }

    private void clickToggleActionForRow(int rowNum) {
        var toggleActionGridElement = actionGridViewElement().toggleActionGridElement();
        var toggleActionElement = toggleActionGridElement.getActionElementForRow(rowNum, TOGGLE_ACTION_KEY);
        toggleActionElement.click();
    }

    private ActionGridViewElement actionGridViewElement() {
        return $(ActionGridViewElement.class)
                .onPage()
                .id(ActionGridView.VIEW_ID);
    }
}
