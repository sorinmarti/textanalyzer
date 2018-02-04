package com.sm.textanalyzer.gui;

import com.sm.textanalyzer.app.Corpus;
import com.sm.textanalyzer.app.CorpusCollection;
import com.sm.textanalyzer.app.CorpusFile;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.List;

public class ProjectTreeModel implements TreeModel{

    private Corpus root;
    private List<TreeModelListener> listeners;

    ProjectTreeModel(Corpus root) {
        this.root = root;
        this.listeners = new ArrayList<>();
    }

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        return ((TreeNode)parent).getChildAt(index);
    }

    @Override
    public int getChildCount(Object parent) {
        if(parent instanceof CorpusFile) {
            return 0;
        }
        return ((TreeNode)parent).getChildCount();
    }

    @Override
    public boolean isLeaf(Object node) {
        if(node instanceof CorpusFile) {
            return true;
        }
        return ((TreeNode)node).isLeaf();
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        System.out.println("Value for path");
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return ((TreeNode)parent).getIndex((TreeNode) child);
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        listeners.add(l);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        listeners.remove(l);
    }

    public void setRoot(Corpus root) {
        this.root = root;
        fireTreeStructureChanged();
    }

    public CorpusCollection addCollection(String name) {
        CorpusCollection col = root.addCollection( name );
        fireCollectionInserted(col);
        return col;
    }

    private void fireTreeStructureChanged() {
        for(TreeModelListener l : listeners) {
            l.treeStructureChanged( new TreeModelEvent( this, new Object[]{root}));
        }
    }

    private void fireCollectionInserted(CorpusCollection collection) {
        for(TreeModelListener l : listeners) {
            l.treeNodesInserted( new TreeModelEvent(this, new Object[]{root, collection}));
        }
    }
}
