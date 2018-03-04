package org.ai.dao.utils;
import java.io.IOException;
import java.io.Writer;
import oracle.toplink.essentials.exceptions.ValidationException;
import oracle.toplink.essentials.platform.database.HSQLPlatform;
import oracle.toplink.essentials.queryframework.ValueReadQuery;


/**
 * Classe que extende HSQLPlatform para oferecer suporte a Identity para HSQL utilizando JPA
 */
public class HSQLPlatformWithNativeSequence extends HSQLPlatform {

    private static final long serialVersionUID = 1L;

    public HSQLPlatformWithNativeSequence() {
        // setUsesNativeSequencing(true);
    }

    @Override
    public boolean supportsNativeSequenceNumbers() {
        return true;
    }

    @Override
    public boolean shouldNativeSequenceAcquireValueAfterInsert() {
        return true;
    }

    @Override
    public ValueReadQuery buildSelectQueryForNativeSequence() {
        return new ValueReadQuery("CALL IDENTITY()");
    }

    @Override
    public void printFieldIdentityClause(Writer writer) throws ValidationException {
        try {
            writer.write(" IDENTITY");
        } catch (IOException ex) {
            throw ValidationException.fileError(ex);
        }
    }
}
