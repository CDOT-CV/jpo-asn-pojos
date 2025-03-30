package us.dot.its.jpo.asn.runtime.types;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import us.dot.its.jpo.asn.runtime.serialization.RootSequenceOfSerializer;


/**
 * Base class for ASN.1 SEQUENCE-OF types.
 * <p>
 * In ASN.1 notation, a SEQUENCE OF represents an ordered collection of elements all the same type.
 * Unlike a basic SEQUENCE which contains named elements of potentially different types, a SEQUENCE OF
 * is more analogous to an array or list in traditional programming languages.
 * <p>
 * This implementation extends {@link AbstractList} to provide the ordered collection behavior while
 * adding ASN.1-specific constraints:
 * <ul>
 *   <li>Type safety - ensures all elements are of the specified ASN.1 type</li>
 *   <li>Size constraints - enforces minimum and maximum size bounds defined in the ASN.1 specification</li>
 *   <li>Runtime type information - maintains knowledge of the element type for encoding/decoding</li>
 * </ul>
 * <p>
 * Example ASN.1 definition:
 * <pre>
 * VehicleList ::= SEQUENCE OF Vehicle
 * </pre>
 * would be represented as a subclass of Asn1SequenceOf<Vehicle>
 *
 * @param <T> The type of items in the SEQUENCE OF, must extend {@link Asn1Type}
 *
 * @see Asn1Type The base interface for all ASN.1 types
 * @see Asn1Sequence For representing ASN.1 SEQUENCE types with named fields
 */
public abstract class Asn1SequenceOf<T extends Asn1Type> extends AbstractList<T> implements Asn1Type {

    private final Class<T> itemClass;
    /** The minimum number of elements required (inclusive) */
    private final long sizeLowerBound;
    /** The maximum number of elements allowed (inclusive), or -1 for unbounded */
    private final long sizeUpperBound;
    // The internal list that backs this AbstractList implementation
    private final ArrayList<T> elements;

    /**
     * Constructs a new ASN.1 SEQUENCE OF with the specified element type and size constraints.
     *
     * @param sizeLowerBound The minimum number of elements required (inclusive)
     * @param sizeUpperBound The maximum number of elements allowed (inclusive), or -1 for unbounded
     */
    protected Asn1SequenceOf(Class<T> itemClass, long sizeLowerBound, long sizeUpperBound) {
        this.itemClass = itemClass;
        this.sizeLowerBound = sizeLowerBound;
        this.sizeUpperBound = sizeUpperBound;
        this.elements = new ArrayList<>();
    }

    @JsonIgnore
    public Class<T> getItemClass() {
        return itemClass;
    }

    /**
     * Returns the minimum number of elements required by this SEQUENCE OF.
     *
     * @return The minimum size constraint (inclusive)
     */
    @JsonIgnore
    public long getSizeLowerBound() {
        return sizeLowerBound;
    }

    /**
     * Returns the maximum number of elements allowed by this SEQUENCE OF.
     *
     * @return The maximum size constraint (inclusive), or -1 if unbounded
     */
    @JsonIgnore
    public long getSizeUpperBound() {
        return sizeUpperBound;
    }

    /**
     * Adds an ASN.1 type element to this sequence after casting to the appropriate type.
     * This method is useful for generic ASN.1 processing frameworks.
     *
     * @param item The ASN.1 type element to add
     * @return {@code true} if the element was added successfully
     * @throws ClassCastException if the element is not of the correct type for this sequence
     */
    @SuppressWarnings("unchecked")
    public boolean add(Asn1Type item) {
        validateAddition(1);
        return elements.add((T) item);
    }

    // AbstractList implementation - only override the necessary methods

    @Override
    public T get(int index) {
        return elements.get(index);
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public T set(int index, T element) {
        return elements.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        validateAddition(1);
        elements.add(index, element);
    }

    @Override
    public T remove(int index) {
        return elements.remove(index);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        validateAddition(c.size());
        return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        validateAddition(c.size());
        return elements.addAll(index, c);
    }

    /**
     * Validates that the elements collection meets the defined size constraints.
     * Throws an exception if the size is outside the allowed range.
     *
     * @throws IllegalStateException if the collection size is below minimum or above maximum allowed
     */
    public void validate() throws IllegalStateException {
        int size = size();

        // Check lower bound constraint
        if (size < sizeLowerBound) {
            throw new IllegalStateException("Collection size " + size +
                    " is below the minimum required size of " + sizeLowerBound);
        }

        // Check upper bound constraint (only if it's bounded)
        if (sizeUpperBound != -1 && size > sizeUpperBound) {
            throw new IllegalStateException("Collection size " + size +
                    " exceeds the maximum allowed size of " + sizeUpperBound);
        }
    }

    /**
     * Validates if adding more elements would exceed the upper bound.
     *
     * @throws IllegalStateException if adding elements would exceed the maximum allowed size
     */
    private void validateAddition(int numElementsToAdd) throws IllegalStateException {
        // Only check if there's an upper bound defined
        if (sizeUpperBound != -1 && size() + numElementsToAdd > sizeUpperBound) {
            throw new IllegalStateException("Cannot add element: would exceed maximum allowed size of " + sizeUpperBound);
        }
    }
}