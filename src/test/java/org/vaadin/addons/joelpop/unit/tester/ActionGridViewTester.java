package org.vaadin.addons.joelpop.unit.tester;

import com.vaadin.testbench.unit.ComponentTester;
import com.vaadin.testbench.unit.TesterWrappers;
import com.vaadin.testbench.unit.Tests;
import org.vaadin.addons.joelpop.it.ui.view.actiongrid.ActionGridView;

@Tests(ActionGridView.class)
public class ActionGridViewTester extends ComponentTester<ActionGridView>
        implements TesterWrappers {

    /**
     * Wrap given actionGridView for testing.
     *
     * @param actionGridView target actionGridView
     */
    public ActionGridViewTester(ActionGridView actionGridView) {
        super(actionGridView);
        ensureComponentIsUsable();
    }
}
