package org.vaadin.addons.joelpop.unit.cases;

import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.testbench.unit.ComponentTester;
import com.vaadin.testbench.unit.UIUnit4Test;
import com.vaadin.testbench.unit.ViewPackages;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.vaadin.addons.joelpop.model.ToggleItem;
import org.vaadin.addons.joelpop.ui.component.ActionGrid;
import org.vaadin.addons.joelpop.unit.tester.ActionGridTester;
import org.vaadin.addons.joelpop.unit.view.ActionGridView;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.vaadin.addons.joelpop.content.ActionGridViewContent.TOGGLE_ACTION_GRID_ID;
import static org.vaadin.addons.joelpop.unit.view.ActionGridView.TOGGLE_ACTION_KEY;

@ViewPackages(packages = { "org.vaadin.addons.joelpop.unit.view" })
@RunWith(Parameterized.class)
public class ActionGridActionsFunctionTest extends UIUnit4Test {
    private final int rowNum;
    private final boolean initialState;
    private final boolean expectedState;

    private ComponentTester<ActionGridView> actionGridViewTester;

    public ActionGridActionsFunctionTest(int rowNum, boolean initialState, boolean expectedState) {
        this.rowNum = rowNum;
        this.initialState = initialState;
        this.expectedState = expectedState;
    }

    @Before
    public void registerView() {
        RouteConfiguration.forApplicationScope()
                .setAnnotatedRoute(ActionGridView.class);
        actionGridViewTester = test(navigate(ActionGridView.class));
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


    @SuppressWarnings("unchecked")
    private ActionGridTester<ActionGrid<ToggleItem>, ToggleItem> $toggleActionGrid() {
        // find the text field and get its tester
        var toggleActionGrid = actionGridViewTester.find(ActionGrid.class)
                .id(TOGGLE_ACTION_GRID_ID);
        return test(ActionGridTester.class, toggleActionGrid);
    }

    private boolean isToggledOn(int rowNum) {
        return $toggleActionGrid().getRow(rowNum).isOn();
    }

    private void clickToggleActionForRow(int rowNum) {
        $toggleActionGrid().clickActionForRow(rowNum, TOGGLE_ACTION_KEY);
    }
}
