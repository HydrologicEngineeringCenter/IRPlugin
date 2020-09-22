package main.java;

import com.rma.factories.AbstractNewObjectFactory;
import com.rma.factories.NewObjectFactory;
import com.rma.io.FileManagerImpl;
import com.rma.io.RmaFile;
import com.rma.model.Project;
import com.rma.ui.GenericNewObjectPanel;

import javax.swing.*;

public class IRAltFactory extends AbstractNewObjectFactory implements NewObjectFactory {
    private IRMain _plugin;
    public IRAltFactory(IRMain plugin) {
//      Call the constructor for parent, passing in I18n info
//      getI18n() just creates a new instance i18n
        super(IR18n.getI18n(IRMessages.Plugin_Name));
        _plugin = plugin;
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
        return panel;
    }

    @Override
    public Object createObject(JComponent jc) {
        GenericNewObjectPanel panel = (GenericNewObjectPanel) jc;
        IRAlt alt = new IRAlt();
        alt.setName(panel.getSelectedName());
        alt.setDescription(panel.getSelectedDescription());
        alt.setFile(FileManagerImpl.getFileManager().getFile(panel.getSelectedFile().getPath() + RmaFile.separator + alt.getName() + _plugin.getAltFileExtension()));
        alt.setProject(Project.getCurrentProject());
        _plugin.addAlternative(alt);
        _plugin.editAlternative(alt);
        alt.saveData();
        return alt;
    }
    @Override
    public void openObject(JComponent jc) {

    }
    @Override
    public JComponent createOpenObjectPanel() {
        return null;
    }
}
