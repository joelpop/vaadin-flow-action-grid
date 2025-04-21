package org.vaadin.addons.joelpop.it.element.component;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.flow.component.icon.testbench.IconElement;
import com.vaadin.testbench.TestBenchElement;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ActionGridElement extends GridElement {

    public ActionElement getActionElementForRow(int rowIndex, String actionKey) {
        // find all <vaadin-grid-column> elements
        var columnElements = $("vaadin-grid-column").all();

        // find the column with name="actions"
        var actionColumnIndex = IntStream.range(0, columnElements.size())
                .filter(i -> "actions".equals(columnElements.get(i).getDomAttribute("name")))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Action column with name='actions' not found"));

        var actionCellElement = getCell(rowIndex, actionColumnIndex);
        var actionCellDivElement = actionCellElement.$(DivElement.class).first();
        return actionCellDivElement.$(ActionElement.class)
                .withAttribute("name", actionKey)
                .single();
    }


    public static class ActionElement extends ButtonElement {

        public String getKey() {
            return getDomAttribute("name");
        }

        public boolean isVisible() {
            return Optional.ofNullable(getDomAttribute("style"))
                    .filter(style -> style.contains("visibility:visible"))
                    .isPresent();
        }

        @Override
        public Set<String> getClassNames() {
            return getIconElement().getClassNames().stream()
                    .filter(name -> !name.equals("icon-s"))
                    .collect(Collectors.toSet());
        }

        public String getIconName() {
            return getIconElement().getDomAttribute("icon");
        }

        public String getTooltipText() {
            return getTooltipElement().getDomAttribute("text");
        }

        @Override
        public @Nullable String getAriaRole() {
            return getDomAttribute("role");
        }

        @Override
        public @Nullable String getAccessibleName() {
            return getDomAttribute("aria-label");
        }

        ///

        private IconElement getIconElement() {
            return $(IconElement.class).single();
        }

        private TestBenchElement getTooltipElement() {
            return $("vaadin-tooltip").single();
        }
    }

}
