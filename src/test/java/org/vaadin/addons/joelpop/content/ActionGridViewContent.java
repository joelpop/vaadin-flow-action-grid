package org.vaadin.addons.joelpop.content;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.addons.joelpop.model.ToggleItem;
import org.vaadin.addons.joelpop.ui.component.ActionGrid;

import java.util.List;

public class ActionGridViewContent extends Composite<Div> {

    public static final String VIEW_ID = "action-grid-view";
    public static final String COLUMNLESS_ACTIONLESS_ACTION_GRID_ID = "columnless-actionless-action-grid";
    public static final String ACTIONLESS_ACTION_GRID_ID = "actionless-action-grid";
    public static final String TOGGLE_ACTION_GRID_ID = "toggle-action-grid";

    public static final String TOGGLE_ACTION_KEY = "toggle";
    public static final String NOOP_ACTION_KEY = "noop";

    public ActionGridViewContent() {
        setId(VIEW_ID);

        addColumnlessActionlessActionGrid();
        addActionlessActionGrid();
        addToggleActionGrid();
    }

    private void addColumnlessActionlessActionGrid() {
        var actionGrid = new ActionGrid<ToggleItem>();
        actionGrid.setId(COLUMNLESS_ACTIONLESS_ACTION_GRID_ID);

        getContent().add(actionGrid);
    }

    private void addActionlessActionGrid() {
        var actionGrid = new ActionGrid<ToggleItem>();
        actionGrid.setId(ACTIONLESS_ACTION_GRID_ID);

        actionGrid.addColumn(ToggleItem::getName);

        getContent().add(actionGrid);
    }

    private void addToggleActionGrid() {
        var actionGrid = new ActionGrid<ToggleItem>();
        actionGrid.setId(TOGGLE_ACTION_GRID_ID);

        actionGrid.addColumn(ToggleItem::getName);
        actionGrid.addColumn(ToggleItem::getInfo);

        actionGrid.addAction(TOGGLE_ACTION_KEY)
                .setIconProvider(item -> item.isOn() ? VaadinIcon.CHECK.create() : VaadinIcon.CLOSE.create())
                .setClassNameProvider(item -> item.isOn() ? LumoUtility.TextColor.SUCCESS : LumoUtility.TextColor.ERROR)
                .setTooltipProvider(item -> item.isOn() ? "On" : "Off")
                .setAccessibleNameProvider(item -> "Toggle item state.")
                .setVisiblePredicate(ToggleItem::isVisible)
                .setEnabledPredicate(ToggleItem::isEnabled)
                .setClickConsumer(item -> {
                    item.setOn(!item.isOn());
                    actionGrid.refreshActionColumn();
                });
        actionGrid.addAction(NOOP_ACTION_KEY)
                .setIconProvider(item -> VaadinIcon.EYE.create());

        actionGrid.setItems(List.of(
                new ToggleItem("Off/Invisible/Disabled", "", false,false, false),
                new ToggleItem("Off/Invisible/Enabled", "", false, false, true),
                new ToggleItem("Off/Visible/Disabled", "", false, true, false),
                new ToggleItem("Off/Visible/Enabled", "", false, true, true),
                new ToggleItem("On/Invisible/Disabled", "", true, false, false),
                new ToggleItem("On/Invisible/Enabled", "", true, false, true),
                new ToggleItem("On/Visible/Disabled", "", true, true, false),
                new ToggleItem("On/Visible/Enabled", "", true, true, true)));

        getContent().add(actionGrid);
    }
}
