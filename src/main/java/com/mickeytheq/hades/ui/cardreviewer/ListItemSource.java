package com.mickeytheq.hades.ui.cardreviewer;

// an iterator-like interface more suitable for scanning
// back and forth across a list of elements
// java.util.ListIterator has behaviour where next() + previous() returns the same element twice
// which is not ideal when trying to build, for example, a dialog that previews a list of card images where each
//
// by default this iterator starts 'just before' the first element so the first operation should be moveNext() to
// check for the existence of the first element and move to it
public interface ListItemSource<T> {
    // returns the current element being pointed to
    // repeated calls without calls to next()/previous() will return the same item
    T getCurrent();

    // move to the next element if it exists and return true
    // return false if there is no next element
    boolean next();

    // move to the previous element if it exists and return true
    // return false if there is no previous element
    boolean previous();

    // return the current index
    // the index starts at -1 and each call to next() increments this by 1 and previous() decrements by 1
    int getCurrentIndex();

    // return a 'hint' total if available
    // callers should not rely on this for bounds detection but instead rely on the results of next()/previous()
    Integer getTotal();
}
