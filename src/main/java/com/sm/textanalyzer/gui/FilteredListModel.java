package com.sm.textanalyzer.gui;

import com.sm.textanalyzer.app.Token;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;

class FilteredListModel extends AbstractListModel<Token> {

	public interface Filter {
        boolean accept(Token element);
    }

    private final ListModel<Token> _source;
    private Filter _filter;
    private final ArrayList<Integer> _indices = new ArrayList<>();

    FilteredListModel(ListModel<Token> source) {
        if (source == null) {
            throw new IllegalArgumentException("Source is null");
        }
        _source = source;
        _source.addListDataListener(new ListDataListener() {
            public void intervalRemoved(ListDataEvent e) {
                doFilter();
            }

            public void intervalAdded(ListDataEvent e) {
                doFilter();
            }

            public void contentsChanged(ListDataEvent e) {
                doFilter();
            }
        });
    }
    

    void setFilter(Filter f) {
        _filter = f;
        doFilter();
    }

    private void doFilter() {
        _indices.clear();

        Filter f = _filter;
        if (f != null) {
            int count = _source.getSize();
            for (int i = 0; i < count; i++) {
                Token element = _source.getElementAt(i);
                if (f.accept(element)) {
                    _indices.add(i);
                }
            }
            fireContentsChanged(this, 0, getSize() - 1);
        }
    }

    public int getSize() {
        return (_filter != null) ? _indices.size() : _source.getSize();
    }

    public Token getElementAt(int index) {
        return (_filter != null) ? _source.getElementAt(_indices.get(index)) : _source.getElementAt(index);
    }
}
