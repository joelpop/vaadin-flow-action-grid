package org.vaadin.addons.joelpop.unit.cases;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.function.ValueProvider;
import org.junit.Test;
import org.vaadin.addons.joelpop.model.ToggleItem;
import org.vaadin.addons.joelpop.ui.component.ActionGrid;
import org.vaadin.addons.joelpop.unit.view.ActionGridView;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ActionGridTest {

    // constructor

    @Test
    public void emptyCtor() {
        var actionGrid = new ActionGrid<>();
        assertTrue(actionGrid.getColumns().isEmpty());
        assertTrue(actionGrid.getActions().isEmpty());
        assertTrue(actionGrid.isActionColumnVisible());
        assertFalse(actionGrid.isActionColumnFrozenToBeginning());
        assertTrue(actionGrid.isActionColumnFrozenToEnd());
    }

    // columns

    @Test
    public void addColumnWithValueProvider() {
        var actionGrid = new ActionGrid<ToggleItem>();

        actionGrid.addColumn(ToggleItem::getName);
        assertEquals(1, actionGrid.getColumns().size());

        actionGrid.addColumn(ToggleItem::getInfo);
        assertEquals(2, actionGrid.getColumns().size());

        assertTrue(actionGrid.getActions().isEmpty());
    }

    @Test
    public void addColumnWithValueProviderAndSorting() {
        var actionGrid = new ActionGrid<ToggleItem>();

        actionGrid.addColumn(ToggleItem::getName, "name_txt");
        assertEquals(1, actionGrid.getColumns().size());

        actionGrid.addColumn(ToggleItem::getInfo, "info_txt");
        assertEquals(2, actionGrid.getColumns().size());

        assertTrue(actionGrid.getActions().isEmpty());

        assertThrows(NullPointerException.class,
                () -> actionGrid.addColumn((ValueProvider<ToggleItem, String>) null, (String[]) null));
    }

    @Test
    public void addColumnWithRenderer() {
        var actionGrid = new ActionGrid<ToggleItem>();

        actionGrid.addColumn(LitRenderer.<ToggleItem>of("${item.name}").withProperty("name", ToggleItem::getName));
        assertEquals(1, actionGrid.getColumns().size());

        actionGrid.addColumn(LitRenderer.<ToggleItem>of("${item.info}").withProperty("info", ToggleItem::getInfo));
        assertEquals(2, actionGrid.getColumns().size());

        assertTrue(actionGrid.getActions().isEmpty());

        assertThrows(NullPointerException.class,
                () -> actionGrid.addColumn((Renderer<ToggleItem>) null));
    }

    @Test
    public void addComponentColumn() {
        var actionGrid = new ActionGrid<ToggleItem>();

        actionGrid.addComponentColumn(toggleItem -> new Div(new Text(toggleItem.getName())));
        assertEquals(1, actionGrid.getColumns().size());

        actionGrid.addComponentColumn(toggleItem -> new Div(new Text(toggleItem.getInfo())));
        assertEquals(2, actionGrid.getColumns().size());

        assertTrue(actionGrid.getActions().isEmpty());
    }

    @Test
    public void setColumnOrderWithFrozenToBeginning() {
        var actionGrid = new ActionGrid<ToggleItem>();

        var nameColumn = actionGrid.addColumn(ToggleItem::getName);
        var infoColumn = actionGrid.addColumn(ToggleItem::getInfo);
        actionGrid.freezeActionColumnToBeginning();
        assertEquals(List.of(nameColumn, infoColumn), actionGrid.getColumns());

        actionGrid.setColumnOrder(infoColumn, nameColumn);
        assertEquals(List.of(infoColumn, nameColumn), actionGrid.getColumns());
    }

    @Test
    public void setColumnOrderWithFrozenToEnd() {
        var actionGrid = new ActionGrid<ToggleItem>();

        var nameColumn = actionGrid.addColumn(ToggleItem::getName);
        var infoColumn = actionGrid.addColumn(ToggleItem::getInfo);
        actionGrid.freezeActionColumnToEnd();
        assertEquals(List.of(nameColumn, infoColumn), actionGrid.getColumns());

        actionGrid.setColumnOrder(infoColumn, nameColumn);
        assertEquals(List.of(infoColumn, nameColumn), actionGrid.getColumns());
    }

    @Test
    public void getColumnByKey() {
        var actionGrid = new ActionGrid<ToggleItem>();

        var nameColumn = actionGrid.addColumn(ToggleItem::getName)
                .setKey("name");
        var infoColumn = actionGrid.addColumn(ToggleItem::getInfo)
                .setKey("info");
        assertEquals(nameColumn, actionGrid.getColumnByKey("name"));
        assertEquals(infoColumn, actionGrid.getColumnByKey("info"));

        assertNull(actionGrid.getColumnByKey("actions"));

        assertNull(actionGrid.getColumnByKey("nonexistent"));
    }

    @Test
    public void removeAllColumns() {
        var actionGrid = new ActionGrid<ToggleItem>();

        actionGrid.addColumn(ToggleItem::getName);
        actionGrid.addColumn(ToggleItem::getInfo);
        assertEquals(2, actionGrid.getColumns().size());

        actionGrid.removeAllColumns();
        assertTrue(actionGrid.getColumns().isEmpty());

        assertThrows(NullPointerException.class,
                () -> actionGrid.removeColumn(null));
    }

    @Test
    public void removeColumn() {
        var actionGrid = new ActionGrid<ToggleItem>();

        var nameColumn = actionGrid.addColumn(ToggleItem::getName);
        var infoColumn = actionGrid.addColumn(ToggleItem::getInfo);
        assertEquals(2, actionGrid.getColumns().size());

        actionGrid.removeColumn(nameColumn);
        assertEquals(1, actionGrid.getColumns().size());

        actionGrid.removeColumn(infoColumn);
        assertTrue(actionGrid.getColumns().isEmpty());

        assertThrows(IllegalArgumentException.class,
                () -> actionGrid.removeColumn(infoColumn));
    }

    @Test
    public void removeColumnByKey() {
        var actionGrid = new ActionGrid<ToggleItem>();

        actionGrid.addColumn(ToggleItem::getName).setKey("name");
        actionGrid.addColumn(ToggleItem::getInfo).setKey("info");
        assertEquals(2, actionGrid.getColumns().size());

        actionGrid.removeColumnByKey("name");
        assertEquals(1, actionGrid.getColumns().size());

        actionGrid.removeColumnByKey("info");
        assertTrue(actionGrid.getColumns().isEmpty());

        assertThrows(IllegalArgumentException.class,
                () -> actionGrid.removeColumnByKey("actions"));

        assertThrows(IllegalArgumentException.class,
                () -> actionGrid.removeColumnByKey("nonexistent"));
    }

    // action column

    @Test
    public void addAction() {
        var actionGrid = new ActionGrid<ToggleItem>();

        actionGrid.addColumn(ToggleItem::getName);
        actionGrid.addColumn(ToggleItem::getInfo);
        assertTrue(actionGrid.getActions().isEmpty());

        actionGrid.addAction(ActionGridView.TOGGLE_ACTION_KEY);
        assertEquals(1, actionGrid.getActions().size());

        actionGrid.addAction(ActionGridView.NOOP_ACTION_KEY);
        assertEquals(2, actionGrid.getActions().size());

        assertThrows(IllegalArgumentException.class,
                () -> actionGrid.addAction(null));
    }

    @Test
    public void getActions() {
        var actionGrid = new ActionGrid<ToggleItem>();

        var toggleAction = actionGrid.addAction(ActionGridView.TOGGLE_ACTION_KEY);
        var noopAction = actionGrid.addAction(ActionGridView.NOOP_ACTION_KEY);
        assertEquals(List.of(toggleAction, noopAction), actionGrid.getActions());
    }

    @Test
    public void getActionByKey() {
        var actionGrid = new ActionGrid<ToggleItem>();

        var toggleAction = actionGrid.addAction(ActionGridView.TOGGLE_ACTION_KEY);
        assertEquals(toggleAction, actionGrid.getActionByKey(ActionGridView.TOGGLE_ACTION_KEY));

        var noopAction = actionGrid.addAction(ActionGridView.NOOP_ACTION_KEY);
        assertEquals(noopAction, actionGrid.getActionByKey(ActionGridView.NOOP_ACTION_KEY));

        assertNull(actionGrid.getActionByKey("nonexistent"));
    }

    @Test
    public void removeAllActions() {
        var actionGrid = new ActionGrid<ToggleItem>();

        actionGrid.addAction(ActionGridView.TOGGLE_ACTION_KEY);
        actionGrid.addAction(ActionGridView.NOOP_ACTION_KEY);
        assertEquals(2, actionGrid.getActions().size());

        actionGrid.removeAllActions();
        assertTrue(actionGrid.getActions().isEmpty());

        assertThrows(IllegalArgumentException.class,
                () -> actionGrid.removeAction(null));
    }

    @Test
    public void removeAction() {
        var actionGrid = new ActionGrid<ToggleItem>();

        var toggleAction = actionGrid.addAction(ActionGridView.TOGGLE_ACTION_KEY);
        var noopAction = actionGrid.addAction(ActionGridView.NOOP_ACTION_KEY);
        assertEquals(2, actionGrid.getActions().size());

        actionGrid.removeAction(toggleAction);
        assertEquals(1, actionGrid.getActions().size());

        actionGrid.removeAction(noopAction);
        assertTrue(actionGrid.getActions().isEmpty());

        assertThrows(IllegalArgumentException.class,
                () -> actionGrid.removeAction(toggleAction));
    }

    @Test
    public void removeActionByKey() {
        var actionGrid = new ActionGrid<ToggleItem>();

        actionGrid.addAction(ActionGridView.TOGGLE_ACTION_KEY);
        actionGrid.addAction(ActionGridView.NOOP_ACTION_KEY);
        assertEquals(2, actionGrid.getActions().size());

        actionGrid.removeActionByKey(ActionGridView.TOGGLE_ACTION_KEY);
        assertEquals(1, actionGrid.getActions().size());

        actionGrid.removeActionByKey(ActionGridView.NOOP_ACTION_KEY);
        assertTrue(actionGrid.getActions().isEmpty());

        assertThrows(IllegalArgumentException.class,
                () -> actionGrid.removeActionByKey("nonexistent"));
    }

    @Test
    public void actionColumnVisible() {
        var actionGrid = new ActionGrid<ToggleItem>();
        assertTrue(actionGrid.isVisible());

        actionGrid.setVisible(false);
        assertFalse(actionGrid.isVisible());

        actionGrid.setVisible(true);
        assertTrue(actionGrid.isVisible());
    }

    @Test
    public void actionColumnHeaderComponent() {
        var actionGrid = new ActionGrid<ToggleItem>();
        assertNull(actionGrid.getActionColumnHeaderComponent());

        var headerComponent = new Span("Actions");
        actionGrid.setActionColumnHeader(headerComponent);
        assertEquals(headerComponent, actionGrid.getActionColumnHeaderComponent());

        actionGrid.setActionColumnHeader((Component) null);
        assertNull(actionGrid.getActionColumnHeaderComponent());
    }

    @Test
    public void actionColumnHeaderText() {
        var actionGrid = new ActionGrid<ToggleItem>();
        assertNull(actionGrid.getActionColumnHeaderText());

        var headerText = "Actions";
        actionGrid.setActionColumnHeader(headerText);
        assertEquals(headerText, actionGrid.getActionColumnHeaderText());

        actionGrid.setActionColumnHeader("");
        assertTrue(actionGrid.getActionColumnHeaderText().isEmpty());

        actionGrid.setActionColumnHeader((String) null);
        assertNull(actionGrid.getActionColumnHeaderText());
    }

    @Test
    public void actionColumnFreezing() {
        var actionGrid = new ActionGrid<ToggleItem>();
        assertFalse(actionGrid.isActionColumnFrozenToBeginning());
        assertTrue(actionGrid.isActionColumnFrozenToEnd());

        actionGrid.freezeActionColumnToBeginning();
        assertTrue(actionGrid.isActionColumnFrozenToBeginning());
        assertFalse(actionGrid.isActionColumnFrozenToEnd());

        actionGrid.freezeActionColumnToEnd();
        assertFalse(actionGrid.isActionColumnFrozenToBeginning());
        assertTrue(actionGrid.isActionColumnFrozenToEnd());
    }
}
