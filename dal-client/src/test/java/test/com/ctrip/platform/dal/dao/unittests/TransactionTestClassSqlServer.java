package test.com.ctrip.platform.dal.dao.unittests;

import static org.junit.Assert.*;

import java.sql.SQLException;

import test.com.ctrip.platform.dal.dao.unitbase.SqlServerDatabaseInitializer;

import com.ctrip.platform.dal.dao.DalClient;
import com.ctrip.platform.dal.dao.DalClientFactory;
import com.ctrip.platform.dal.dao.DalCommand;
import com.ctrip.platform.dal.dao.DalHints;
import com.ctrip.platform.dal.dao.annotation.Shard;
import com.ctrip.platform.dal.dao.annotation.Transactional;
import com.ctrip.platform.dal.dao.client.DalTransactionManager;

public class TransactionTestClassSqlServer {
    public static final String DB_NAME = SqlServerDatabaseInitializer.DATABASE_NAME;
    public static final String DB_NAME_SHARD = "dao_test_sqlsvr_dbShard";
    public static final String DONE = "done";
    
    public String performNormal() {
        assertTrue(!DalTransactionManager.isInTransaction());
        return DONE;
    }

    @Transactional(logicDbName = DB_NAME)
    public String perform() {
        assertTrue(DalTransactionManager.isInTransaction());
        assertEquals(DB_NAME, DalTransactionManager.getLogicDbName());
        return DONE;
    }

    @Transactional(logicDbName = DB_NAME)
    public String performFail() {
        assertTrue(DalTransactionManager.isInTransaction());
        assertEquals(DB_NAME, DalTransactionManager.getLogicDbName());
        throw new RuntimeException();
    }

    @Transactional(logicDbName = DB_NAME)
    public String performNest() {
        assertTrue(DalTransactionManager.isInTransaction());
        assertEquals(DB_NAME, DalTransactionManager.getLogicDbName());
        perform();
        return DONE;
    }

    @Transactional(logicDbName = DB_NAME)
    public String performNestDistributedTransaction() {
        assertTrue(DalTransactionManager.isInTransaction());
        assertEquals(DB_NAME, DalTransactionManager.getLogicDbName());
        perform(1);
        return DONE;
    }

    public String performNest2() {
        assertTrue(!DalTransactionManager.isInTransaction());
        perform();
        return DONE;
    }

    public String performNest3() throws InstantiationException, IllegalAccessException {
        assertTrue(!DalTransactionManager.isInTransaction());
        TransactionTestClassSqlServer target = DalTransactionManager.create(TransactionTestClassSqlServer.class);
        target.perform();
        return DONE;
    }

    @Transactional(logicDbName = DB_NAME_SHARD)
    public String perform(@Shard String id) {
        assertTrue(DalTransactionManager.isInTransaction());
        assertEquals(DB_NAME_SHARD, DalTransactionManager.getLogicDbName());
        assertEquals(id, DalTransactionManager.getCurrentDbMeta().getShardId());
        return DONE;
    }

    @Transactional(logicDbName = DB_NAME_SHARD)
    public String perform(@Shard Integer id) {
        assertTrue(DalTransactionManager.isInTransaction());
        assertEquals(DB_NAME_SHARD, DalTransactionManager.getLogicDbName());
        assertEquals(id.toString(), DalTransactionManager.getCurrentDbMeta().getShardId());
        return DONE;
    }

    @Transactional(logicDbName = DB_NAME_SHARD)
    public String perform(@Shard int id) {
        assertTrue(DalTransactionManager.isInTransaction());
        assertEquals(String.valueOf(id), DalTransactionManager.getCurrentDbMeta().getShardId());
        return DONE;
    }

    @Transactional(logicDbName = DB_NAME_SHARD)
    public String perform(String id, DalHints hints) {
        assertTrue(DalTransactionManager.isInTransaction());
        assertEquals(hints.getShardId(), DalTransactionManager.getCurrentDbMeta().getShardId());
        return DONE;
    }
    
    @Transactional(logicDbName = DB_NAME_SHARD)
    public String performFail(String id, DalHints hints) {
        assertTrue(DalTransactionManager.isInTransaction());
        assertEquals(hints.getShardId(), DalTransactionManager.getCurrentDbMeta().getShardId());
        throw new RuntimeException();
    }
    
    @Transactional(logicDbName = "dao_test_sqlsvr_dbShard")
    public String performWitShard(@Shard String id, DalHints hints) {
        assertTrue(DalTransactionManager.isInTransaction());
        assertEquals(id, DalTransactionManager.getCurrentDbMeta().getShardId());
        return DONE;
    }
    
    @Transactional(logicDbName = "dao_test_sqlsvr_dbShard")
    public String performWitShardNest(@Shard String id, DalHints hints) {
        assertTrue(DalTransactionManager.isInTransaction());
        assertEquals(id, DalTransactionManager.getCurrentDbMeta().getShardId());
        performWitShard(id, hints);
        return DONE;
    }
    
    @Transactional(logicDbName = "dao_test_sqlsvr_dbShard")
    public String performWitShardNestFail(@Shard String id, DalHints hints) {
        assertTrue(DalTransactionManager.isInTransaction());
        assertEquals(id, DalTransactionManager.getCurrentDbMeta().getShardId());
        performFail(id, hints.inShard(id));
        return DONE;
    }
    
    @Transactional(logicDbName = "dao_test_sqlsvr_dbShard")
    public String performCommandWitShardNest(final @Shard String id, DalHints hints) throws SQLException {
        DalClientFactory.getClient(DB_NAME_SHARD).execute(new DalCommand() {
            
            @Override
            public boolean execute(DalClient client) throws SQLException {
                perform(id, new DalHints().inShard(id));
                perform(id, new DalHints().inShard(id));
                performWitShard(id, new DalHints().inShard(id));
                performWitShardNest(id, new DalHints().inShard(id));
                return false;
            }
        }, new DalHints().inShard(id));
        
        return DONE;
    }
    
    @Transactional(logicDbName = "dao_test_sqlsvr_dbShard")
    public String performCommandWitShardNestFail(final @Shard String id, DalHints hints) throws SQLException {
        DalClientFactory.getClient(DB_NAME_SHARD).execute(new DalCommand() {
            
            @Override
            public boolean execute(DalClient client) throws SQLException {
                perform(id, new DalHints().inShard(id));
                perform(id, new DalHints().inShard(id));
                performWitShard(id, new DalHints().inShard(id));
                performWitShardNest(id, new DalHints().inShard(id));
                performFail(id, new DalHints().inShard(id));
                return false;
            }
        }, new DalHints().inShard(id));
        
        return DONE;
    }
    
    @Transactional(logicDbName = "dao_test_sqlsvr_dbShard")
    public String performDetectDistributedTransaction(final @Shard String id, DalHints hints) throws SQLException {
        DalClientFactory.getClient(DB_NAME_SHARD).execute(new DalCommand() {
            
            @Override
            public boolean execute(DalClient client) throws SQLException {
                perform(id, new DalHints().inShard(id));
                perform(id, new DalHints().inShard(id));
                performWitShard(id, new DalHints().inShard(id));
                performWitShardNest(id, new DalHints().inShard(id));
                performFail(id, new DalHints().inShard(id+id));
                return false;
            }
        }, new DalHints().inShard(id));
        
        return DONE;
    }
}