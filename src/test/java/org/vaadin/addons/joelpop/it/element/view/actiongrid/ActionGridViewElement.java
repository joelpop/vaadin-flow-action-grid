package org.vaadin.addons.joelpop.it.element.view.actiongrid;

import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.testbench.annotations.Attribute;
import org.vaadin.addons.joelpop.it.element.component.ActionGridElement;
import org.vaadin.addons.joelpop.it.ui.view.actiongrid.ActionGridView;

/**
 * The page model object for ActionGridView.
 */
@Attribute(name = "id", value = ActionGridView.VIEW_ID)
public class ActionGridViewElement extends DivElement {

    /**
     * Find the columnlessActionlessActionGrid element.
     *
     * @return the columnlessActionlessActionGrid element
     */
    public ActionGridElement $columnlessActionlessActionGrid() {
        return $(ActionGridElement.class)
                .id(ActionGridView.COLUMNLESS_ACTIONLESS_ACTION_GRID_ID);
    }

    /**
     * Find the actionlessActionGrid element.
     *
     * @return the actionlessActionGrid element
     */
    public ActionGridElement $actionlessActionGrid() {
        return $(ActionGridElement.class)
                .id(ActionGridView.ACTIONLESS_ACTION_GRID_ID);
    }

    /**
     * Find the toggleActionGrid element.
     *
     * @return the toggleActionGrid element
     */
    public ActionGridElement toggleActionGridElement() {
        return $(ActionGridElement.class)
                .id(ActionGridView.TOGGLE_ACTION_GRID_ID);
    }
}
