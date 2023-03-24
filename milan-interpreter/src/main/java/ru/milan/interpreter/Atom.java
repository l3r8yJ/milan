package ru.milan.interpreter;

/**
 * Atom of Milan. Smallest unit.
 */
public interface Atom {

    /**
     * Returns true if the string is a number, false otherwise.
     *
     * @return A boolean value.
     */
    boolean isNumber();

    /**
     * Returns true if the value is NaN, otherwise returns false.
     *
     * @return A boolean value.
     */
    boolean isNaN();

    /**
     * Returns the value of this OptionalInt as an Integer if it is present,
     * otherwise returns null.
     *
     * @return The return value is the integer value of the object.
     */
    Integer asInteger();

    /**
     * Returns true if the value is true, false otherwise.
     *
     * @return A boolean value.
     */
    boolean isTrue();

    /**
     * Returns true if the value is false.
     *
     * @return A boolean value.
     */
    boolean isFalse();

    /**
     * Not() returns the negation of the current atom.
     *
     * @return The result of the boolean operation.
     */
    Atom not();

    /**
     * "Returns a new atom that is true if and only if both this atom and the other
     * atom are true."
     *
     * @param other The other atom to be ANDed with this one.
     * @return A new atom that is the conjunction of the two atoms.
     */
    Atom and(Atom other);

    /**
     * "Returns a new atom that is true if either this atom or the other atom is
     * true."
     *
     * @param other The other atom to be ORed with this atom.
     * @return A new Atom object with the value of the two atoms ORed together.
     */
    Atom or(Atom other);

    /**
     * "Multiply this atom by another atom."
     *
     * @param other The other atom to multiply with.
     * @return The product of the two atoms.
     */
    Atom mul(Atom other);

    /**
     * "Divide this atom by another atom."
     *
     * @param other The other atom to divide by.
     * @return The result of the division of the two numbers.
     */
    Atom div(Atom other);

    /**
     * Adds the value of the other atom to this atom and returns the result
     *
     * @param other The other atom to add to this one.
     * @return The sum of the two atoms.
     */
    Atom add(Atom other);

    /**
     * Subtracts the value of the other atom from this atom and returns the result.
     *
     * @param other The other atom to subtract from this one.
     * @return The difference of the two atoms.
     */
    Atom sub(Atom other);

    /**
     * Returns true if this Atom is greater than the other Atom.
     *
     * @param other The other atom to compare to.
     * @return A boolean value.
     */
    Atom gt(Atom other);

    /**
     * Returns true if this Atom is greater than or equal to the other Atom.
     *
     * @param other The other atom to compare to.
     * @return A boolean value.
     */
    Atom gte(Atom other);

    /**
     * Returns true if this Atom is less than the other Atom.
     *
     * @param other The other atom to compare to.
     * @return A boolean value.
     */
    Atom lt(Atom other);

    /**
     * Returns true if this Atom is less than or equal to the other Atom.
     *
     * @param other The other atom to compare to.
     * @return A boolean value.
     */
    Atom lte(Atom other);

    /**
     * Returns true if this atom is equal to the other atom.
     *
     * @param other The other atom to compare to.
     * @return The result of the comparison.
     */
    Atom eq(Atom other);

    /**
     * Returns true if this atom is not equal to the other atom.
     *
     * @param other The other atom to compare to.
     * @return A new Atom object with the value of the comparison.
     */
    Atom neq(Atom other);

    /**
     * > Asserts that the given value is a number
     */
    void assertNumber();
}
