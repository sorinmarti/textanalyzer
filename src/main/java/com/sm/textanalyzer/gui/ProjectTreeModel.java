package com.sm.textanalyzer.gui;

import com.sm.textanalyzer.DataPool;
import com.sm.textanalyzer.app.Corpus;
import com.sm.textanalyzer.app.CorpusCollection;
import com.sm.textanalyzer.app.CorpusFile;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.List;

public class ProjectTreeModel implements TreeModel{

    private List<TreeModelListener> listeners;

    ProjectTreeModel() {
        this.listeners = new ArrayList<>();
    }

    @Override
    public Object getRoot() {
        if(DataPool.projectOpen()) {
            return DataPool.project.getCorpus();
        }
        return null;
    }

    @Override
    public Object getChild(Object parent, int index) {
        if(parent instanceof Corpus) {
            return ((Corpus)parent).getCollection(index);
        }
        if(parent instanceof CorpusCollection) {
            return ((CorpusCollection)parent).getFile(index);
        }
        return null;
    }

    @Override
    public int getChildCount(Object parent) {
        if(parent instanceof Corpus) {
            return ((Corpus)parent).getNumCollections();
        }
        if(parent instanceof CorpusCollection) {
            return ((CorpusCollection)parent).getNumFiles();
        }
        return 0;
    }

    @Override
    public boolean isLeaf(Object node) {
        if(node instanceof Corpus && ((Corpus)node).getNumCollections()>0) {
            return false;
        }
        if(node instanceof CorpusCollection && ((CorpusCollection)node).getNumFiles()>0) {
            return false;
        }
        return true;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        System.out.println("Value for path");
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if(parent instanceof Corpus) {
            return ((Corpus)parent).getCollectionIndex((CorpusCollection)child);
        }
        if(parent instanceof CorpusCollection) {
            return ((CorpusCollection)parent).getFileIndex((CorpusFile)child);
        }
        return -1;
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        listeners.add(l);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        listeners.remove(l);
    }

    public CorpusCollection addCollection(String name) {
        if(DataPool.projectOpen()) {
            CorpusCollection col = new CorpusCollection( name );
            DataPool.project.getCorpus().addCollection(col);
            fireTreeStructureChanged();
            return col;
        }
        return null;
    }

    public void deleteCollection(CorpusCollection collection) {
        if(DataPool.projectOpen()) {
            DataPool.project.getCorpus().removeCollection(collection);
            fireTreeStructureChanged();
        }
    }

    private void fireTreeStructureChanged() {
        for(TreeModelListener l : listeners) {
            l.treeStructureChanged( new TreeModelEvent( this, new Object[]{DataPool.project}));
        }
    }

    private void fireCollectionInserted(CorpusCollection collection) {
        for(TreeModelListener l : listeners) {
            l.treeNodesInserted( new TreeModelEvent(this, new Object[]{DataPool.project, collection}));
        }
    }

    public void rootChanged() {
        fireTreeStructureChanged();
    }


}
