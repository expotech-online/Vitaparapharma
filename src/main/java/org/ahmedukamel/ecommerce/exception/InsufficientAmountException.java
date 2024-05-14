package org.ahmedukamel.ecommerce.exception;

import lombok.Getter;

@Getter
public class InsufficientAmountException extends RuntimeException {
    private final Object data;
    private final Class theClass;

    public InsufficientAmountException(Object data, Class theClass) {
        super(theClass.getName() + " insufficient amount!");
        this.data = data;
        this.theClass = theClass;
    }

    public String getTheClassName() {
        return this.theClass.getSimpleName();
    }
}
