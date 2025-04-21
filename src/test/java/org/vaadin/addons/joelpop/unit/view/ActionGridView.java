package org.vaadin.addons.joelpop.unit.view;

import com.vaadin.flow.router.Route;
import org.vaadin.addons.joelpop.content.ActionGridViewContent;

import static org.vaadin.addons.joelpop.unit.view.ActionGridView.VIEW_ID;

@Route(value = VIEW_ID, registerAtStartup = false)
public class ActionGridView extends ActionGridViewContent {
}
