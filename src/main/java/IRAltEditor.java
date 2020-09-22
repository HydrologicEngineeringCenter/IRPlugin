package main.java;

import com.rma.client.Browser;
import com.rma.client.NewObjectDialog;
import com.rma.factories.AbstractNewObjectFactory;
import com.rma.factories.NewObjectFactory;
import com.rma.io.RmaFile;
import com.rma.model.Project;
import com.rma.ui.GenericNewObjectPanel;

import com.rma.util.I18n;

import javax.swing.*;
import javax.swing.table.TableColumn;

public class IRAltEditor extends AbstractNewObjectFactory implements NewObjectFactory{
    private IRMain _plugin;


    public IRAltEditor(IRAlt alt, IRMain plugin) {
        super(IR18n.getI18n(IRMessages.Plugin_Name));
        _plugin = plugin;
        JComponent p = createNewObjectPanel();
        JFrame frame = Browser.getBrowserFrame();
        NewObjectDialog dlg = new NewObjectDialog(frame);
        dlg.add(p);
        dlg.setVisible(true);


    }

    @Override
    public JComponent createNewObjectPanel() {
        GenericNewObjectPanel panel = new GenericNewObjectPanel();
        panel.setFileComponentsVisible(false);
        Project p = Project.getCurrentProject();
        panel.setName("");
        panel.setDescription("");
        panel.setExistingNamesList(_plugin.getAlternativeList());
        panel.setDirectory(p.getProjectDirectory() + RmaFile.separator + _plugin.getPluginDirectory());
        JComponent table = createOpenObjectPanel();
        panel.add(table);
        return panel;
    }

    @Override
    public JComponent createOpenObjectPanel() {
        JTable locationsTable = new JTable();
        TableColumn TC = new TableColumn();
        TC.setHeaderValue("Evaluation Locations");
        locationsTable.addColumn(TC);
        return locationsTable;
    }

    @Override
    public Object createObject(JComponent jComponent) {
        return null;
    }

    @Override
    public void openObject(JComponent jComponent) {

    }
}