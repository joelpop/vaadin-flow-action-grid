package org.vaadin.addons.joelpop.it.cases;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.vaadin.addons.joelpop.it.AbstractViewTest;
import org.vaadin.addons.joelpop.it.element.view.actiongrid.ActionGridViewElement;
import org.vaadin.addons.joelpop.it.ui.view.actiongrid.ActionGridView;

import static org.vaadin.addons.joelpop.content.ActionGridViewContent.NOOP_ACTION_KEY;
import static org.vaadin.addons.joelpop.content.ActionGridViewContent.TOGGLE_ACTION_KEY;

public class ActionGridIT extends AbstractViewTest {

    @Before
    public void viewIsPresent() {
        Assert.assertNotNull(actionGridViewElement());
    }

    @Test
    public void columnlessActionlessActionGridIsPresent() {
        Assert.assertNotNull(actionGridViewElement().$columnlessActionlessActionGrid());
    }

    @Test
    public void actionlessActionGridIsPresent() {
        Assert.assertNotNull(actionGridViewElement().$actionlessActionGrid());
    }

    @Test
    public void toggleActionGridIsPresent() {
        Assert.assertNotNull(actionGridViewElement().toggleActionGridElement());
    }

    @Test
    public void toggleActionIsPresent() {
        var toggleActionGridElement = actionGridViewElement().toggleActionGridElement();
        Assert.assertNotNull(toggleActionGridElement.getActionElementForRow(0, TOGGLE_ACTION_KEY));
    }

    @Test
    public void noopActionIsPresent() {
        var toggleActionGridElement = actionGridViewElement().toggleActionGridElement();
        Assert.assertNotNull(toggleActionGridElement.getActionElementForRow(0, NOOP_ACTION_KEY));
    }

    @Test
    public void toggleActionGridAria() {
        var toggleActionGridElement = actionGridViewElement().toggleActionGridElement();

        var toggleActionElement = toggleActionGridElement.getActionElementForRow(0, TOGGLE_ACTION_KEY);

        Assert.assertNotNull(toggleActionElement);
        Assert.assertEquals("Toggle item state.", toggleActionElement.getAccessibleName());
        Assert.assertEquals("button", toggleActionElement.getAriaRole());
    }

    private ActionGridViewElement actionGridViewElement() {
        return $(ActionGridViewElement.class)
                .onPage()
                .id(ActionGridView.VIEW_ID);
    }
}
