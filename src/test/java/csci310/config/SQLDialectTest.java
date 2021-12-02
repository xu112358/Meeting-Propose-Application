package csci310.config;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
class SQLDialectTest {

    SQLDialect sqlDialect;

    @BeforeEach
    void setUp() {
        sqlDialect = new SQLDialect();
    }

    @Test
    void testSupportsIdentityColumns() {
        assertTrue(sqlDialect.supportsIdentityColumns());
    }

    @Test
    void testHasDataTypeInIdentityColumn() {
        assertFalse(sqlDialect.hasDataTypeInIdentityColumn());
    }

    @Test
    void testGetIdentityColumnString() {
        assertEquals("integer", sqlDialect.getIdentityColumnString());
    }

    @Test
    void testGetIdentitySelectString() {
        assertEquals("select last_insert_rowid()", sqlDialect.getIdentitySelectString());
    }

    @Test
    void testSupportsLimit() {
        assertTrue(sqlDialect.supportsLimit());
    }

    @Test
    void testGetLimitString() {
        assertEquals("test limit ? offset ?", sqlDialect.getLimitString("test", true));
        assertEquals("test limit ?", sqlDialect.getLimitString("test", false));
    }

    @Test
    void testSupportsTemporaryTables() {
        assertTrue(sqlDialect.supportsTemporaryTables());
    }

    @Test
    void testGetCreateTemporaryTableString() {
        assertEquals("create temporary table if not exists", sqlDialect.getCreateTemporaryTableString());
    }

    @Test
    void testDropTemporaryTableAfterUse() {
        assertFalse(sqlDialect.dropTemporaryTableAfterUse());
    }

    @Test
    void testSupportsCurrentTimestampSelection() {
        assertTrue(sqlDialect.supportsCurrentTimestampSelection());
    }

    @Test
    void testIsCurrentTimestampSelectStringCallable() {
        assertFalse(sqlDialect.isCurrentTimestampSelectStringCallable());
    }

    @Test
    void testGetCurrentTimestampSelectString() {
        assertEquals("select current_timestamp", sqlDialect.getCurrentTimestampSelectString());
    }

    @Test
    void testSupportsUnionAll() {
        assertTrue(sqlDialect.supportsUnionAll());
    }

    @Test
    void testHasAlterTable() {
        assertFalse(sqlDialect.hasAlterTable());

    }

    @Test
    void testDropConstraints() {
        assertFalse(sqlDialect.dropConstraints());
    }

    @Test
    void testGetAddColumnString() {
        assertEquals("add column", sqlDialect.getAddColumnString());
    }

    @Test
    void testGetForUpdateString() {
        assertEquals("", sqlDialect.getForUpdateString());
    }

    @Test
    void testSupportsOuterJoinForUpdate() {
        assertFalse(sqlDialect.supportsOuterJoinForUpdate());
    }

    @Test
    void testGetDropForeignKeyString() {
        try{
            assertEquals("No drop foreign key syntax supported by SQLiteDialect", sqlDialect.getDropForeignKeyString().toString());
        }catch(Exception e){

        }
    }

    @Test
    void testGetAddForeignKeyConstraintString() {
        try{
        assertEquals("No add foreign key syntax supported by SQLiteDialect", sqlDialect.getAddForeignKeyConstraintString(new String(), new String[1], new String(), new String[1], true).toString());
        }catch(Exception e){

        }
    }

    @Test
    void testGetAddPrimaryKeyConstraintString() {
        try{
            assertEquals("No add primary key syntax supported by SQLiteDialect", sqlDialect.getAddPrimaryKeyConstraintString(new String()).toString());
        }catch(Exception e){

        }
    }

    @Test
    void testSupportsIfExistsBeforeTableName() {
        assertTrue(sqlDialect.supportsIfExistsBeforeTableName());
    }

    @Test
    void testSupportsCascadeDelete() {
        assertFalse(sqlDialect.supportsCascadeDelete());
    }
}