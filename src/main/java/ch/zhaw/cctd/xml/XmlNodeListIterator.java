package ch.zhaw.cctd.xml;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * An Iterator for the W3C-<code>NodeList</code>.
 * @author Ben Felder
 */
class XmlNodeListIterator implements Iterator<Node> {
    
    /**
     * Creates a new <code>XmlNodeListIterator</code> for specified <code>NodeList</code>.
     * @param list  the <code>NodeList</code>.
     */
    public XmlNodeListIterator(final NodeList list) {
        super();
        this.list = list;
        this.searchIndex();
    }
    
    private void searchIndex() {
    	while (this.hasNext() && this.internalNext().getNodeType() != Node.ELEMENT_NODE);
    	if (this.hasNext()) this.index--;
    }
    
    /** The <code>NodeList</code>. */
    private final NodeList list;
    /** The current index. */
    private int index = 0;
    
    /**
     * @throws UnsupportedOperationException allways
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Checks if there are any more Nodes.
     * @return <code>true</code> if there are more, else <code>false</code>.
     */
    public boolean hasNext() {
        return this.index < this.list.getLength();
    }
    
    private Node internalNext() {
        Node node = this.list.item(this.index);
        if (node == null) {
            // index to high...
            throw new NoSuchElementException(""+this.index);
        }
        this.index = this.index+1;
        return node;
    }

    /**
     * Gets the next <code>Node</code>.
     * @return  the next object in the list.
     */
    public Node next() {
    	Node node = this.internalNext();
        this.searchIndex();
        return node;
    }

}