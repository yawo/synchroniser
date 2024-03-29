package synchroniser.customfields;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

public abstract class TableFieldEditor extends FieldEditor {

    /**
     * The table widget; <code>null</code> if none (before creation or after
     * disposal).
     */
    private Table table;

    /**
     * The button box containing the Add, Remove, Up, and Down buttons;
     * <code>null</code> if none (before creation or after disposal).
     */
    private Composite buttonBox;

    /**
     * The Add button.
     */
    private Button addButton;

    /**
     * The Duplicate button.
     */
    private Button duplicateButton;

    /**
     * The Remove button.
     */
    private Button removeButton;

    /**
     * The Up button.
     */
    private Button upButton;

    /**
     * The Down button.
     */
    private Button downButton;

    /**
     * The selection listener.
     */
    private SelectionListener selectionListener;

    private final String[] columnNames;

    private final int[] columnWidths;

    /**
     * Creates a new table field editor
     */
    protected TableFieldEditor() {
            columnNames = new String[0];
            columnWidths = new int[0];
    }

    /**
     * Creates a table field editor.
     *
     * @param name
     *            the name of the preference this field editor works on
     * @param labelText
     *            the label text of the field editor
     * @param columnNames
     *            the names of columns
     * @param columnWidths
     *            the widths of columns
     * @param parent
     *            the parent of the field editor's control
     *
     */
    protected TableFieldEditor(String name, String labelText,
                    String[] columnNames, int[] columnWidths, Composite parent) {
            init(name, labelText);
            this.columnNames = columnNames;
            this.columnWidths = columnWidths;
            createControl(parent);
    }

    /**
     * Combines the given list of items into a single string. This method is the
     * converse of <code>parseString</code>.
     * <p>
     * Subclasses must implement this method.
     * </p>
     *
     * @param items
     *            the list of items
     * @return the combined string
     * @see #parseString
     */
    protected abstract String createList(String[][] items);

    /**
     * Splits the given string into a array of array of value. This method is
     * the converse of <code>createList</code>.
     * <p>
     * Subclasses must implement this method.
     * </p>
     *
     * @param string
     *            the string
     * @return an array of array of <code>string</code>
     * @see #createList
     */
    protected abstract String[][] parseString(String string);

    /**
     * Creates and returns a new value row for the table.
     * <p>
     * Subclasses must implement this method.
     * </p>
     *
     * @return a new item
     */
    protected abstract String[] getNewInputObject();

    /**
     * Creates the Add, Remove, Up, and Down button in the given button box.
     *
     * @param box
     *            the box for the buttons
     */
    private void createButtons(Composite box) {
            addButton = createPushButton(box, "New");
            duplicateButton = createPushButton(box, "Duplicate");
            removeButton = createPushButton(box, "Remove");
            upButton = createPushButton(box, "Up");
            downButton = createPushButton(box, "Down");
    }

    /**
     * Return the Add button.
     *
     * @return the button
     */
    protected Button getAddButton() {
            return addButton;
    }

    /**
     * Return the Duplicate button.
     *
     * @return the button
     */
    protected Button getDuplicateButton() {
            return duplicateButton;
    }

    /**
     * Return the Remove button.
     *
     * @return the button
     */
    protected Button getRemoveButton() {
            return removeButton;
    }

    /**
     * Return the Up button.
     *
     * @return the button
     */
    protected Button getUpButton() {
            return upButton;
    }

    /**
     * Return the Down button.
     *
     * @return the button
     */
    protected Button getDownButton() {
            return downButton;
    }

    /**
     * Helper method to create a push button.
     *
     * @param parent
     *            the parent control
     * @param key
     *            the resource name used to supply the button's label text
     * @return Button
     */
    private Button createPushButton(Composite parent, String key) {
            Button button = new Button(parent, SWT.PUSH);
            button.setText(key);
            button.setFont(parent.getFont());
            GridData data = new GridData(GridData.FILL_HORIZONTAL);
            int widthHint = convertHorizontalDLUsToPixels(button,
                            IDialogConstants.BUTTON_WIDTH);
            data.widthHint = Math.max(widthHint, button.computeSize(SWT.DEFAULT,
                            SWT.DEFAULT, true).x);
            button.setLayoutData(data);
            button.addSelectionListener(getSelectionListener());
            return button;
    }

    /*
     * (non-Javadoc) Method declared on FieldEditor.
     */
    protected void adjustForNumColumns(int numColumns) {
            Control control = getLabelControl();
            ((GridData) control.getLayoutData()).horizontalSpan = numColumns;
            ((GridData) table.getLayoutData()).horizontalSpan = numColumns - 1;
    }

    /**
     * Creates a selection listener.
     */
    public void createSelectionListener() {
            selectionListener = new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent event) {
                            Widget widget = event.widget;
                            if (widget == addButton) {
                                    addPressed();
                            } else if (widget == duplicateButton) {
                                    duplicatePressed();
                            } else if (widget == removeButton) {
                                    removePressed();
                            } else if (widget == upButton) {
                                    upPressed();
                            } else if (widget == downButton) {
                                    downPressed();
                            } else if (widget == table) {
                                    selectionChanged();
                            }
                    }
            };
    }

    /*
     * (non-Javadoc) Method declared on FieldEditor.
     */
    protected void doFillIntoGrid(Composite parent, int numColumns) {
            Control control = getLabelControl(parent);
            GridData gd = new GridData();
            gd.horizontalSpan = numColumns;
            control.setLayoutData(gd);

            Composite composite = new Composite(parent, SWT.NONE);
            GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
            gridData.horizontalSpan = 2;
            gridData.widthHint = 550;
            composite.setLayoutData(gridData);
            composite.setLayout(new GridLayout(2, false));

            table = getTableControl(composite);
            gd = new GridData(GridData.FILL_HORIZONTAL);
            gd.verticalAlignment = GridData.FILL;
            gd.horizontalSpan = numColumns - 1;
            gd.grabExcessHorizontalSpace = true;
            table.setLayoutData(gd);

            buttonBox = getButtonBoxControl(composite);
            gd = new GridData();
            gd.verticalAlignment = GridData.BEGINNING;
            buttonBox.setLayoutData(gd);
    }

    /*
     * (non-Javadoc) Method declared on FieldEditor.
     */
    protected void doLoad() {
            if (table != null) {
                    String s = getPreferenceStore().getString(getPreferenceName());
                    String[][] array = parseString(s);
                    for (int i = 0; i < array.length; i++) {
                            TableItem tableItem = new TableItem(table, SWT.NONE);
                            tableItem.setText(array[i]);
                    }
            }
    }

    /*
     * (non-Javadoc) Method declared on FieldEditor.
     */
    protected void doLoadDefault() {
            if (table != null) {
                    table.removeAll();
                    String s = getPreferenceStore().getDefaultString(
                                    getPreferenceName());
                    String[][] array = parseString(s);
                    for (int i = 0; i < array.length; i++) {
                            TableItem tableItem = new TableItem(table, SWT.NONE);
                            for (int j = 0; j < array[i].length; j++) {
                                    tableItem.setText(array[i][j]);
                            }
                    }
            }
    }

    /*
     * (non-Javadoc) Method declared on FieldEditor.
     */
    protected void doStore() {
            TableItem[] items = table.getItems();
            String[][] commands = new String[items.length][];
            for (int i = 0; i < items.length; i++) {
                    commands[i] = new String[columnNames.length];
                    TableItem item = items[i];
                    for (int j = 0; j < columnNames.length; j++) {
                            commands[i][j] = item.getText(j);
                    }
            }
            String s = createList(commands);
            if (s != null) {
                    getPreferenceStore().setValue(getPreferenceName(), s);
            }
    }

    /**
     * Returns this field editor's button box containing the Add, Remove, Up,
     * and Down button.
     *
     * @param parent
     *            the parent control
     * @return the button box
     */
    public Composite getButtonBoxControl(Composite parent) {
            if (buttonBox == null) {
                    buttonBox = new Composite(parent, SWT.NULL);
                    GridLayout layout = new GridLayout();
                    layout.marginWidth = 0;
                    buttonBox.setLayout(layout);
                    createButtons(buttonBox);
                    buttonBox.addDisposeListener(new DisposeListener() {
                            public void widgetDisposed(DisposeEvent event) {
                                    addButton = null;
                                    duplicateButton = null;
                                    removeButton = null;
                                    upButton = null;
                                    downButton = null;
                                    buttonBox = null;
                            }
                    });

            } else {
                    checkParent(buttonBox, parent);
            }

            selectionChanged();
            return buttonBox;
    }

    /**
     * Returns this field editor's table control.
     *
     * @param parent
     *            the parent control
     * @return the table control
     */
    public Table getTableControl(Composite parent) {
            if (table == null) {
                    table = new Table(parent, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL
                                    | SWT.H_SCROLL | SWT.FULL_SELECTION);
                    table.setFont(parent.getFont());
                    table.setLinesVisible(true);
                    table.setHeaderVisible(true);
                    table.addSelectionListener(getSelectionListener());
                    table.addDisposeListener(new DisposeListener() {
                            public void widgetDisposed(DisposeEvent event) {
                                    table = null;
                            }
                    });
                    for (String columnName : columnNames) {
                            TableColumn tableColumn = new TableColumn(table, SWT.LEAD);
                            tableColumn.setText(columnName);
                            tableColumn.setWidth(100);
                    }
                    if (columnNames.length > 0) {
                            TableLayout layout = new TableLayout();
                            if (columnNames.length > 1) {
                                    for (int i = 0; i < (columnNames.length - 1); i++) {
                                            layout.addColumnData(new ColumnWeightData(0,
                                                            columnWidths[i], false));

                                    }
                            }
                            layout.addColumnData(new ColumnWeightData(100,
                                            columnWidths[columnNames.length - 1], true));
                            table.setLayout(layout);
                    }
                    final TableEditor editor = new TableEditor(table);
                    editor.horizontalAlignment = SWT.LEFT;
                    editor.grabHorizontal = true;
                    table.addListener(SWT.MouseDoubleClick, new Listener() {
                            public void handleEvent(Event event) {
                                    Rectangle clientArea = table.getClientArea();
                                    Point pt = new Point(event.x, event.y);
                                    int index = table.getTopIndex();
                                    while (index < table.getItemCount()) {
                                            boolean visible = false;
                                            final TableItem item = table.getItem(index);
                                            for (int i = 0; i < table.getColumnCount(); i++) {
                                                    Rectangle rect = item.getBounds(i);
                                                    if (rect.contains(pt)) {
                                                            final int column = i;
                                                            final Text text = new Text(table, SWT.NONE);
                                                            Listener textListener = new Listener() {
                                                                    public void handleEvent(final Event e) {
                                                                            switch (e.type) {
                                                                            case SWT.FocusOut:
                                                                                    item
                                                                                                    .setText(column, text
                                                                                                                    .getText());
                                                                                    text.dispose();
                                                                                    break;
                                                                            case SWT.Traverse:
                                                                                    switch (e.detail) {
                                                                                    case SWT.TRAVERSE_RETURN:
                                                                                            item.setText(column, text
                                                                                                            .getText());
                                                                                            // FALL THROUGH
                                                                                    case SWT.TRAVERSE_ESCAPE:
                                                                                            text.dispose();
                                                                                            e.doit = false;
                                                                                    }
                                                                                    break;
                                                                            }
                                                                    }
                                                            };
                                                            text.addListener(SWT.FocusOut, textListener);
                                                            text.addListener(SWT.Traverse, textListener);
                                                            editor.setEditor(text, item, i);
                                                            text.setText(item.getText(i));
                                                            text.selectAll();
                                                            text.setFocus();
                                                            return;
                                                    }
                                                    if (!visible && rect.intersects(clientArea)) {
                                                            visible = true;
                                                    }
                                            }
                                            if (!visible)
                                                    return;
                                            index++;
                                    }
                            }
                    });
            } else {
                    checkParent(table, parent);
            }
            return table;
    }

    /*
     * (non-Javadoc) Method declared on FieldEditor.
     */
    public int getNumberOfControls() {
            return 2;
    }

    /**
     * Returns this field editor's selection listener. The listener is created
     * if necessary.
     *
     * @return the selection listener
     */
    private SelectionListener getSelectionListener() {
            if (selectionListener == null) {
                    createSelectionListener();
            }
            return selectionListener;
    }

    /**
     * Returns this field editor's shell.
     * <p>
     * This method is internal to the framework; subclassers should not call
     * this method.
     * </p>
     *
     * @return the shell
     */
    protected Shell getShell() {
            if (addButton == null) {
                    return null;
            }
            return addButton.getShell();
    }

    /**
     * Notifies that the Add button has been pressed.
     */
    private void addPressed() {
            setPresentsDefaultValue(false);
            String[] newInputObject = getNewInputObject();
            TableItem tableItem = new TableItem(table, SWT.NONE);
            tableItem.setText(newInputObject);
            selectionChanged();
    }

    /**
     * Notifies that the Add button has been pressed.
     */
    private void duplicatePressed() {
            setPresentsDefaultValue(false);
            int index = table.getSelectionIndex();
            int target = index + 1;

            if (index >= 0) {
                    TableItem[] selection = table.getSelection();
                    Assert.isTrue(selection.length == 1);
                    String[] values = new String[columnNames.length];
                    for (int j = 0; j < columnNames.length; j++) {
                            values[j] = selection[0].getText(j);
                    }
                    TableItem tableItem = new TableItem(table, SWT.NONE, target);
                    tableItem.setText(values);
                    table.setSelection(target);
            }
            selectionChanged();
    }

    /**
     * Notifies that the Remove button has been pressed.
     */
    private void removePressed() {
            setPresentsDefaultValue(false);
            int index = table.getSelectionIndex();
            if (index >= 0) {
                    table.remove(index);
                    selectionChanged();
            }
    }

    /**
     * Notifies that the Up button has been pressed.
     */
    private void upPressed() {
            swap(true);
    }

    /**
     * Notifies that the Down button has been pressed.
     */
    private void downPressed() {
            swap(false);
    }

    /**
     * Invoked when the selection in the list has changed.
     *
     * <p>
     * The default implementation of this method utilizes the selection index
     * and the size of the list to toggle the enabled state of the up, down and
     * remove buttons.
     * </p>
     *
     * <p>
     * Subclasses may override.
     * </p>
     *
     */
    protected void selectionChanged() {
            int index = table.getSelectionIndex();
            int size = table.getItemCount();

            duplicateButton.setEnabled(index >= 0);
            removeButton.setEnabled(index >= 0);
            upButton.setEnabled(size > 1 && index > 0);
            downButton.setEnabled(size > 1 && index >= 0 && index < size - 1);
    }

    /*
     * (non-Javadoc) Method declared on FieldEditor.
     */
    public void setFocus() {
            if (table != null) {
                    table.setFocus();
            }
    }

    /**
     * Moves the currently selected item up or down.
     *
     * @param up
     *            <code>true</code> if the item should move up, and
     *            <code>false</code> if it should move down
     */
    private void swap(boolean up) {
            setPresentsDefaultValue(false);
            int index = table.getSelectionIndex();
            int target = up ? index - 1 : index + 1;

            if (index >= 0) {
                    TableItem[] selection = table.getSelection();
                    Assert.isTrue(selection.length == 1);
                    String[] values = new String[columnNames.length];
                    for (int j = 0; j < columnNames.length; j++) {
                            values[j] = selection[0].getText(j);
                    }
                    table.remove(index);
                    TableItem tableItem = new TableItem(table, SWT.NONE, target);
                    tableItem.setText(values);
                    table.setSelection(target);
            }
            selectionChanged();
    }

    /*
     * @see FieldEditor.setEnabled(boolean,Composite).
     */
    public void setEnabled(boolean enabled, Composite parent) {
            super.setEnabled(enabled, parent);
            getTableControl(parent).setEnabled(enabled);
            addButton.setEnabled(enabled);
            duplicateButton.setEnabled(enabled);
            removeButton.setEnabled(enabled);
            upButton.setEnabled(enabled);
            downButton.setEnabled(enabled);
    }
}
