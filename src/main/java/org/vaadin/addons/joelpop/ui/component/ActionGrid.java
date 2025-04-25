package org.vaadin.addons.joelpop.ui.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.function.ValueProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A {@link Grid} with a dedicated column for action icon buttons.
 * <p>
 * The action column can be frozen as either the start or end column and can be hidden.
 * The individual actions can be customized by item, including their
 * icon, aria-label, tooltip, styling, visibility, enabling, and event handling.
 *
 * @param <T> the type of the Grid row items
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })  // because this is a library
public class ActionGrid<T> extends Grid<T> {

    private static final String ACTION_COLUMN_KEY = "actions";
    private static final String ACTION_COLUMN_TEMPLATE_PREFIX = """
            <div
             style="width:100%; height:100%;"
             @click=${(event) => event.stopPropagation()}>""";
    private static final String ACTION_BUTTON_TEMPLATE_FORMAT = """
                <vaadin-button
                 name="%s"
                 role="button"
                 aria-label="${item.%<sAriaLabel}"
                 theme="small tertiary-inline icon"
                 ?disabled=${!item.%<sEnabled}
                 style="visibility:${item.%<sVisible ? "visible" : "hidden"};"
                 @click=${%<sClick}>
                    <vaadin-icon slot="prefix" icon="${item.%<sIconName}"
                     class="icon-s ${item.%<sClassName}"
                     style="padding:2px;"></vaadin-icon>
                    <vaadin-tooltip slot="tooltip" text="${item.%<sTooltip}"></vaadin-tooltip>
                </vaadin-button>
            """;
    private static final String ACTION_COLUMN_TEMPLATE_SUFFIX = "</div>";

    private final transient List<Action> actions;
    private final Column<T> actionColumn;

    /**
     * Create an ActionGrid with an empty action column frozen to the end.
     */
    public ActionGrid() {
        actions = new ArrayList<>();
        actionColumn = super.addColumn(actionColumnRenderer())
                .setKey(ACTION_COLUMN_KEY)
                .setFrozenToEnd(true)
                .setFlexGrow(0);
        actionColumn.getElement().setAttribute("name", ACTION_COLUMN_KEY);
    }

    private Renderer<T> actionColumnRenderer() {
        var renderer = LitRenderer.<T>of(
                actions.stream()
                        .map(action -> ACTION_BUTTON_TEMPLATE_FORMAT.formatted(action.getKey()))
                        .collect(Collectors.joining("", ACTION_COLUMN_TEMPLATE_PREFIX, ACTION_COLUMN_TEMPLATE_SUFFIX)));
        actions.forEach(action -> renderer
                .withProperty(action.getKey() + "IconName", action::iconNameFor)
                .withProperty(action.getKey() + "ClassName", action::classNameFor)
                .withProperty(action.getKey() + "AriaLabel", action::ariaLabelFor)
                .withProperty(action.getKey() + "Tooltip", action::tooltipFor)
                .withProperty(action.getKey() + "Visible", action::isVisibleFor)
                .withProperty(action.getKey() + "Enabled", action::isEnabledFor)
                .withFunction(action.getKey() + "Click", action::onClickFor));

        return renderer;
    }

    @Override
    @SafeVarargs
    public final void setColumnOrder(Column<T>... columns) {
        super.setColumnOrder(columns);
    }

    @Override
    public void setColumnOrder(List<Column<T>> columns) {
        // because the action column is not directly accessible,
        // yet needs to be included in the grid's column list for setting their order,
        // insert the action column into the column list
        // where it was positioned within the frozen columns
        if (actionColumn.isFrozen()) {
            var frozenToBeginningColumns = super.getColumns().stream().filter(Column::isFrozen).toList();
            var frozenIndexOfActionColumn = frozenToBeginningColumns.indexOf(actionColumn);
            var allColumns = new ArrayList<>(columns);
            allColumns.add(frozenIndexOfActionColumn, actionColumn);
            super.setColumnOrder(allColumns);
        }
        else /*if (actionColumn.isFrozenToEnd())*/ {
            var frozenToEndColumns = super.getColumns().stream().filter(Column::isFrozenToEnd).toList();
            var frozenIndexOfActionColumn = frozenToEndColumns.indexOf(actionColumn);
            var allColumns = new ArrayList<>(columns);
            allColumns.add(columns.size() - frozenToEndColumns.size() + frozenIndexOfActionColumn + 1, actionColumn);
            super.setColumnOrder(allColumns);
        }
    }

    @Override
    public List<Column<T>> getColumns() {
        return super.getColumns().stream()
                .filter(column -> column != actionColumn)
                .toList();
    }

    @Override
    public Column<T> getColumnByKey(String columnKey) {
        if (columnKey.equals(ACTION_COLUMN_KEY)) {
            return null;
        }
        return super.getColumnByKey(columnKey);
    }

    @Override
    public Column<T> addColumn(ValueProvider<T, ?> valueProvider) {
        var column = super.addColumn(valueProvider);
        fixFrozenColumnOrder();
        return column;
    }

    @Override
    public <V extends Comparable<? super V>> Column<T> addColumn(ValueProvider<T, V> valueProvider, String... sortingProperties) {
        var column = super.addColumn(valueProvider, sortingProperties);
        fixFrozenColumnOrder();
        return column;
    }

    @Override
    public Column<T> addColumn(Renderer<T> renderer) {
        var column = super.addColumn(renderer);
        fixFrozenColumnOrder();
        return column;
    }

    @Override
    public <V extends Component> Column<T> addComponentColumn(ValueProvider<T, V> componentProvider) {
        var column = super.addComponentColumn(componentProvider);
        fixFrozenColumnOrder();
        return column;
    }

    /**
     * Get the {@link Action}s of the grid.
     * <p>
     * The returned list is unmodifiable.
     * <p>
     * If no Actions exist, an empty list is returned.
     *
     * @return the Actions of the grid
     */
    public List<Action> getActions() {
        return List.copyOf(actions);
    }

    /**
     * Get the {@link Action} for a key.
     * <p>
     * Throws an {@link IllegalArgumentException} if the key is {@code null}.
     * <p>
     * If no Action is found for the specified key, {@code null} is returned.
     *
     * @param key the key of the Action, not null
     * @return the Action corresponding to the supplied key
     */
    public Action getActionByKey(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null.");
        }

        return actions.stream()
                .filter(a -> Objects.equals(a.getKey(), key))
                .findFirst()
                .orElse(null);
    }

    /**
     * Add an {@link Action} to the action column.
     * <p>
     * Throws an {@link IllegalArgumentException} if an Action with the key already exists
     * or if the key is {@code null}.
     * <p>
     * Actions appear in the order they are added.
     * <p>
     * To supply a handler for an action, use {@link Action#addClickHandler(SerializableConsumer)}.
     *
     * @param key the unique key to identify the Action, not null
     * @return the Action
     */
    public Action addAction(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null.");
        }
        else if (actions.stream().anyMatch(a -> Objects.equals(a.getKey(), key))) {
            throw new IllegalArgumentException("Action with key \"" + key + "\" already exists.");
        }

        var action = new Action(key);

        actions.add(action);
        refreshActionColumn();

        fixFrozenColumnOrder();

        return action;
    }

    /**
     * Remove all {@link Action}s from the action column.
     */
    public void removeAllActions() {
        actions.clear();
        refreshActionColumn();

        fixFrozenColumnOrder();
    }

    /**
     * Remove an {@link Action} from the action column.
     * <p>
     * Throws an {@link IllegalArgumentException} if the Action is not found.
     *
     * @param action the Action to remove, not null
     */
    public void removeAction(Action action) {
        if (action == null) {
            throw new IllegalArgumentException("Action cannot be null.");
        }
        else if (actions.stream().noneMatch(a -> a.getKey().equals(action.getKey()))) {
            throw new IllegalArgumentException("Action with key \"" + action.getKey() + "\" not found.");
        }

        actions.remove(action);
        refreshActionColumn();

        fixFrozenColumnOrder();
    }

    /**
     * Remove an {@link Action} from the action column.
     * <p>
     * Throws an {@link IllegalArgumentException} if an Action with the key is not found
     * or if the key is {@code null}.
     *
     * @param key the key of the Action, not null
     */
    public void removeActionByKey(String key) {
        var action = getActionByKey(key);
        if (action == null) {
            throw new IllegalArgumentException("Action with key \"" + key + "\" not found.");
        }

        removeAction(action);
    }

    /**
     * Return the visibility of the action column.
     *
     * @return {@code true} if the action column is visible, {@code false} otherwise
     */
    public boolean isActionColumnVisible() {
        return actionColumn.isVisible();
    }

    /**
     * Set the visibility of the action column.
     *
     * @param visible the visibility state of the action column
     */
    public void setActionColumnVisible(boolean visible) {
        actionColumn.setVisible(visible);
    }

    /**
     * Return the component of the action column header.
     *
     * @return the component of the action column header
     */
    public Component getActionColumnHeaderComponent() {
        return actionColumn.getHeaderComponent();
    }

    /**
     * Set the component to use for the action column header
     *
     * @param headerComponent the component to use for the action column header
     */
    public void setActionColumnHeader(Component headerComponent) {
        actionColumn.setHeader(headerComponent);
    }

    /**
     * Return the text of the action column header.
     *
     * @return the text of the action column header
     */
    public String getActionColumnHeaderText() {
        return actionColumn.getHeaderText();
    }

    /**
     * Set the text to use for the action column header
     *
     * @param headerText the text to use for the action column header
     */
    public void setActionColumnHeader(String headerText) {
        actionColumn.setHeader(headerText);
    }

    /**
     * Returns the position of the action column in the grid.
     * The position can either be at the beginning or the end of the grid,
     * depending on whether the action column is configured to be frozen to the end.
     *
     * @return the position of the action column;
     * either {@link FrozenColumnPosition#BEGINNING} or {@link FrozenColumnPosition#END}
     */
    public FrozenColumnPosition getActionColumnPosition() {
        return actionColumn.isFrozenToEnd() ? FrozenColumnPosition.END : FrozenColumnPosition.BEGINNING;
    }

    /**
     * Sets the position of the action column in the grid.
     * The action column can be frozen either to the beginning or the end of the grid.
     *
     * @param position the position of the action column; must be
     *                 either {@link FrozenColumnPosition#BEGINNING} or
     *                 {@link FrozenColumnPosition#END}
     */
    public void setActionColumnPosition(FrozenColumnPosition position) {
        if (position == FrozenColumnPosition.BEGINNING) {
            freezeActionColumnToBeginning();
        }
        else if (position == FrozenColumnPosition.END) {
            freezeActionColumnToEnd();
        }
    }

    /**
     * Return the frozenness of the action column to the beginning of the grid.
     *
     * @return {@code true} if the action column is frozen to the beginning of the grid, {@code false} otherwise
     */
    public boolean isActionColumnFrozenToBeginning() {
        return actionColumn.isFrozen();
    }

    /**
     * Freeze the action column to the beginning of the grid.
     */
    public void freezeActionColumnToBeginning() {
        if (isActionColumnFrozenToEnd()) {
            actionColumn.setFrozenToEnd(false);
            actionColumn.setFrozen(true);
            fixFrozenColumnOrder();
        }
    }

    /**
     * Return the frozenness of the action column to the end of the grid.
     *
     * @return {@code true} if the action column is frozen to the end of the grid, {@code false} otherwise
     */
    public boolean isActionColumnFrozenToEnd() {
        return actionColumn.isFrozenToEnd();
    }

    /**
     * Freeze the action column to the end of the grid.
     */
    public void freezeActionColumnToEnd() {
        if (isActionColumnFrozenToBeginning()) {
            actionColumn.setFrozen(false);
            actionColumn.setFrozenToEnd(true);
            fixFrozenColumnOrder();
        }
    }

    private void fixFrozenColumnOrder() {
        setColumnOrder(getColumns().stream().sorted(ActionGrid::frozenColumnOrderComparator).toList());
    }

    private static int frozenColumnOrderComparator(Column<?> a, Column<?> b) {
        return Integer.compare(frozenColumnSortValue(a), frozenColumnSortValue(b));
    }

    private static int frozenColumnSortValue(Column<?> column) {
        if (column.isFrozen()) {
            return -1;
        }
        if (column.isFrozenToEnd()) {
            return 1;
        }
        return 0;
    }

    public void refreshActionColumn() {
        actionColumn.setRenderer(actionColumnRenderer())
                .setWidth(actions.size() * 2 + Unit.REM.getSymbol());
    }


    public enum FrozenColumnPosition {
        BEGINNING,
        END
    }


    /**
     * Represents an action associated with a key,
     * configurable with dynamic properties for each row's item.
     * <p>
     * This class encapsulates an action identified by a unique {@code key}, with customizable
     * behavior for rendering icons, class names, aria-label, tooltips, visibility, enablement, and click handling.
     * Each property is defined by a function or predicate that operates on the row's item.
     * <p>
     * The action can be configured fluently via setter methods,
     * which return the instance for chaining.
     * <p>
     * Changes to action properties trigger a refresh of the action column,
     * ensuring the ActionGrid reflects the updated state.
     */
    public class Action {
        private final String key;
        private SerializableFunction<T, Icon> iconProvider;
        private SerializableFunction<T, String> classNameProvider;
        private SerializableFunction<T, String> ariaLabelProvider;
        private SerializableFunction<T, String> tooltipProvider;
        private SerializablePredicate<T> visiblePredicate;
        private SerializablePredicate<T> enabledPredicate;
        private final List<SerializableConsumer<T>> clickHandlers;

        /**
         * Construct an action with the specified key and default property providers.
         * <p>
         * The {@code key} uniquely identifies this action. All providers (icon, class name,
         * aria-label, tooltip, visibility, enablement, and click) are initialized to safe defaults.
         *
         * @param key the unique identifier for this action
         */
        Action(String key) {
            this.key = key;
            setIcon((SerializableFunction<T, Icon>) null);
            setClassName((SerializableFunction<T, String>) null);
            setAccessibleName((SerializableFunction<T, String>) null);
            setTooltip((SerializableFunction<T, String>) null);
            setVisible(null);
            setEnabled(null);
            clickHandlers = new ArrayList<>();
        }

        /**
         * Return the unique key identifying this action.
         *
         * @return the action's key
         */
        public String getKey() {
            return key;
        }

        /**
         * Return the icon name for the given item, or an empty string if none exists.
         * <p>
         * This method retrieves the icon via {@link #iconFor(Object)} and extracts its name,
         * defaulting to an empty string if the icon is null.
         *
         * @param t the item to evaluate
         * @return the icon name, or {@code ""} if no icon is provided
         */
        public String iconNameFor(T t) {
            return Optional.ofNullable(iconFor(t)).map(Icon::getIcon).orElse("");
        }

        /**
         * Retrieve the icon for the given item.
         * <p>
         * The icon is determined by the configured {@code iconProvider}, which may return
         * null if no icon is applicable.
         *
         * @param t the item to evaluate
         * @return the icon for the item, or {@code null} if none is provided
         */
        public Icon iconFor(T t) {
            return iconProvider.apply(t);
        }

        /**
         * Set the provider for determining the icon of this action.
         * <p>
         * If the provided {@code iconProvider} is null, a default provider returning
         * {@code null} is used.
         *
         * @param iconProvider the function to compute the icon, or {@code null} for none
         * @return this action, for method chaining
         */
        public Action setIcon(SerializableFunction<T, Icon> iconProvider) {
            this.iconProvider = Objects.requireNonNullElseGet(iconProvider, () -> t -> null);
            refreshActionColumn();
            return this;
        }

        /**
         * Sets the icon for this action.
         * This method assigns a specific icon to the action, replacing any existing icon configuration.
         *
         * @param icon the icon to set for this action
         * @return this action, allowing method chaining
         */
        public Action setIcon(Icon icon) {
            return setIcon(t -> icon);
        }

        /**
         * Return the CSS class name for the given item.
         * <p>
         * The class name is determined by the configured {@code classNameProvider},
         * which defaults to an empty string if not set.
         *
         * @param t the item to evaluate
         * @return the class name for the item
         */
        public String classNameFor(T t) {
            return classNameProvider.apply(t);
        }

        /**
         * Set the provider for determining the CSS class name of this action.
         * To provide multiple class names, separate them with spaces.
         * Note that this may override the default styles set for enabling/disabling.
         * <p>
         * If the provided {@code classNameProvider} is null, a default provider returning
         * an empty string is used.
         *
         * @param classNameProvider the function to compute the class name, or {@code null} for empty
         * @return this action, for method chaining
         */
        public Action setClassName(SerializableFunction<T, String> classNameProvider) {
            this.classNameProvider = Objects.requireNonNullElseGet(classNameProvider, () -> t -> "");
            refreshActionColumn();
            return this;
        }

        /**
         * Sets the CSS class name for this action.
         * This method assigns a specific class name to the action, replacing any existing class name configuration.
         * To provide multiple class names, separate them with spaces.
         * Note that this may override the default styles set for enabling/disabling.
         *
         * @param className the class name to set for this action
         * @return this action, allowing method chaining
         */
        public Action setClassName(String className) {
            return setClassName(t -> className);
        }

        /**
         * Return the aria-label text for the given item.
         * <p>
         * The aria-label is determined by the configured {@code ariaLabelProvider},
         * which defaults to an empty string if not set.
         *
         * @param t the item to evaluate
         * @return the aria-label text for the item
         */
        public String ariaLabelFor(T t) {
            return ariaLabelProvider.apply(t);
        }

        /**
         * Set the provider for determining the ariaLabel text of this action.
         * <p>
         * If the provided {@code ariaLabelProvider} is null, a default provider returning
         * an empty string is used.
         *
         * @param ariaLabelProvider the function to compute the ariaLabel, or {@code null} for empty
         * @return this action, for method chaining
         */
        public Action setAccessibleName(SerializableFunction<T, String> ariaLabelProvider) {
            this.ariaLabelProvider = Objects.requireNonNullElseGet(ariaLabelProvider, () -> t -> "");
            refreshActionColumn();
            return this;
        }

        /**
         * Sets a static aria-label text for this action.
         * This method assigns a specific aria-label to the action, replacing any existing aria-label configuration.
         *
         * @param ariaLabel the aria-label text to set for this action
         * @return this action, allowing method chaining
         */
        public Action setAccessibleName(String ariaLabel) {
            return setAccessibleName(t -> ariaLabel);
        }

        /**
         * Return the tooltip text for the given item.
         * <p>
         * The tooltip is determined by the configured {@code tooltipProvider},
         * which defaults to an empty string if not set.
         *
         * @param t the item to evaluate
         * @return the tooltip text for the item
         */
        public String tooltipFor(T t) {
            return tooltipProvider.apply(t);
        }

        /**
         * Set the provider for determining the tooltip text of this action.
         * <p>
         * If the provided {@code tooltipProvider} is null, a default provider returning
         * an empty string is used.
         *
         * @param tooltipProvider the function to compute the tooltip, or {@code null} for empty
         * @return this action, for method chaining
         */
        public Action setTooltip(SerializableFunction<T, String> tooltipProvider) {
            this.tooltipProvider = Objects.requireNonNullElseGet(tooltipProvider, () -> t -> "");
            refreshActionColumn();
            return this;
        }

        /**
         * Sets a static tooltip text for this action.
         * This method assigns a specific tooltip to the action, replacing any existing tooltip configuration.
         *
         * @param tooltip the tooltip text to set for this action
         * @return this action, allowing method chaining
         */
        public Action setTooltip(String tooltip) {
            return setTooltip(t -> tooltip);
        }

        /**
         * Return if this action is visible for the given item.
         * <p>
         * Visibility is evaluated by the configured {@code visiblePredicate},
         * which defaults to {@code true} if not set.
         *
         * @param t the item to evaluate
         * @return {@code true} if the action is visible, {@code false} otherwise
         */
        public boolean isVisibleFor(T t) {
            return visiblePredicate.test(t);
        }

        /**
         * Set the predicate for determining the visibility of this action.
         * <p>
         * If the provided {@code visiblePredicate} is null, a default predicate returning
         * {@code true} is used.
         *
         * @param visiblePredicate the predicate to evaluate visibility, or {@code null} for always visible
         * @return this action, for method chaining
         */
        public Action setVisible(SerializablePredicate<T> visiblePredicate) {
            this.visiblePredicate = Objects.requireNonNullElseGet(visiblePredicate, () -> t -> true);
            refreshActionColumn();
            return this;
        }

        /**
         * Sets the visibility for this action.
         * This method assigns a specific visibility to the action, replacing any existing visibility configuration.
         *
         * @param visible the visibility to set for this action
         * @return this action, allowing method chaining
         */
        public Action setVisible(boolean visible) {
            return setVisible(t -> visible);
        }

        /**
         * Determine if this action is enabled for the given item.
         * <p>
         * Enablement is evaluated by the configured {@code enabledPredicate},
         * which defaults to {@code true} if not set.
         *
         * @param t the item to evaluate
         * @return {@code true} if the action is enabled, {@code false} otherwise
         */
        public boolean isEnabledFor(T t) {
            return enabledPredicate.test(t);
        }

        /**
         * Set the predicate for determining the enablement of this action.
         * <p>
         * If the provided {@code enabledPredicate} is null, a default predicate returning
         * {@code true} is used.
         *
         * @param enabledPredicate the predicate to evaluate enablement, or {@code null} for always enabled
         * @return this action, for method chaining
         */
        public Action setEnabled(SerializablePredicate<T> enabledPredicate) {
            this.enabledPredicate = Objects.requireNonNullElseGet(enabledPredicate, () -> t -> true);
            refreshActionColumn();
            return this;
        }

        /**
         * Sets the enablement for this action.
         * This method assigns a specific enablement to the action, replacing any existing enablement configuration.
         *
         * @param enabled the enablement to set for this action
         * @return this action, allowing method chaining
         */
        public Action setEnabled(boolean enabled) {
            return setEnabled(t -> enabled);
        }

        /**
         * Execute the click action for the given item, but only if visible and enabled.
         * <p>
         * This method checks the visibility and enablement of the action for the specified
         * item using {@link #isVisibleFor(Object)} and {@link #isEnabledFor(Object)}. If either
         * condition fails, the method returns without action. Otherwise, it invokes the configured
         * {@code clickHandlers}. This defensive check prevents execution when the UI state might
         * have been tampered with (e.g., via browser manipulation).
         *
         * @param t the item to process on click
         */
        public void onClickFor(T t) {
            // defensively check visible & enabled state as browser state can be tampered with
            if (!isVisibleFor(t) || !isEnabledFor(t)) {
                return;
            }
            clickHandlers.forEach(clickHandler -> clickHandler.accept(t));
        }

        /**
         * Add a consumer for handling click events on this action.
         *
         * @param clickHandler a consumer to handle clicks; non-null
         * @return this action, for method chaining
         */
        public Action addClickHandler(SerializableConsumer<T> clickHandler) {
            Objects.requireNonNull(clickHandler, "clickHandler cannot be null.");
            this.clickHandlers.add(clickHandler);
            return this;
        }
    }
}
