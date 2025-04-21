package org.vaadin.addons.joelpop.unit.tester;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridTester;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.testbench.unit.LitRendererTestUtil;
import com.vaadin.testbench.unit.TesterWrappers;
import elemental.json.Json;
import org.vaadin.addons.joelpop.ui.component.ActionGrid;

public class ActionGridTester<T extends ActionGrid<Y>, Y> extends GridTester<T, Y>
        implements TesterWrappers {

    /**
     * Wrap action grid for testing.
     *
     * @param component target grid
     */
    public ActionGridTester(T component) {
        super(component);
    }

    public String getActionIconNameForRow(int rowIndex, String actionKey) {
        return getActionPropertyValueForRow(rowIndex, actionKey, "IconName", String.class);
    }

    public String getActionClassNameForRow(int rowIndex, String actionKey) {
        return getActionPropertyValueForRow(rowIndex, actionKey, "ClassName", String.class);
    }

    public String getActionAccessibleNameForRow(int rowIndex, String actionKey) {
        return getActionPropertyValueForRow(rowIndex, actionKey, "AriaLabel", String.class);
    }

    public String getActionTooltipForRow(int rowIndex, String actionKey) {
        return getActionPropertyValueForRow(rowIndex, actionKey, "Tooltip", String.class);
    }

    public boolean isActionVisibleForRow(int rowIndex, String actionKey) {
        return getActionPropertyValueForRow(rowIndex, actionKey, "Visible", Boolean.class);
    }

    public boolean isActionEnabledForRow(int rowIndex, String actionKey) {
        return getActionPropertyValueForRow(rowIndex, actionKey, "Enabled", Boolean.class);
    }

    public void clickActionForRow(int rowIndex, String actionKey) {
        LitRendererTestUtil.invokeFunction(getActionColumnRenderer(), this::getField, this::getRow, rowIndex, actionKey + "Click", Json.createArray());
    }

    private <V> V getActionPropertyValueForRow(int rowIndex, String actionKey, String propertyName, Class<V> valueType) {
        return LitRendererTestUtil.getPropertyValue(getActionColumnRenderer(), this::getField, this::getRow, rowIndex, actionKey + propertyName, valueType);
    }

    private LitRenderer<Y> getActionColumnRenderer() {
        ensureVisible();

        if (getActionColumn().getRenderer() instanceof LitRenderer<Y> litRenderer) {
            return litRenderer;
        } else {
            throw new IllegalArgumentException("Renderer for action column is not found.");
        }
    }

    @SuppressWarnings("unchecked")
    private Grid.Column<Y> getActionColumn() {
        try {
            // access the private actionColumn field from ActionGrid using reflection
            var actionColumnField = ActionGrid.class.getDeclaredField("actionColumn");

            // because the actionColumn field is private, make it accessible
            actionColumnField.setAccessible(true);

            // get the actionColumn field's value
            var actionColumn = (Grid.Column<Y>) actionColumnField.get(getComponent());
            if (actionColumn == null) {
                throw new IllegalStateException("action column not found. Verify ActionGrid initialization.");
            }
            return actionColumn;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException("Failed to obtain action column", e);
        }
    }
}
