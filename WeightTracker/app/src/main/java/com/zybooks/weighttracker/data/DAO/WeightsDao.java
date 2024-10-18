package com.zybooks.weighttracker.data.DAO;

        import androidx.lifecycle.LiveData;
        import androidx.room.Dao;
        import androidx.room.Delete;
        import androidx.room.Insert;
        import androidx.room.OnConflictStrategy;
        import androidx.room.Query;
        import androidx.room.Update;

        import com.zybooks.weighttracker.data.model.Weights;
        import com.zybooks.weighttracker.data.DbConfig;

        import java.util.List;

        /*
Last Updated 10/6/2024, Laura Brooks
This file is a Database Access Object which sets all the queries used in the app to get
data from the SQL Server.

Only the queries to insert a new weight and get a list of weights are being used.


 */

@Dao
public abstract class WeightsDao {

    @Query("SELECT * FROM " + DbConfig.WEIGHTS_TABLE+ " WHERE Wid = :id")
    public abstract Weights getWeight(int id);

    //@Query("SELECT * FROM " + DbConfig.WEIGHTS_TABLE+ " ORDER BY Rid DESC")
    //public List<Weights> getWeights();

    @Query("SELECT * FROM " + DbConfig.WEIGHTS_TABLE+ " WHERE Rid = :id ORDER BY record_date DESC LIMIT :limit")
    public abstract List<Weights> getWeightsNewerFirst(int id, int limit);

    @Query("SELECT * FROM " + DbConfig.WEIGHTS_TABLE+ " WHERE Rid = :id ORDER BY record_date ASC LIMIT :limit")
    public abstract List<Weights> getWeightsOlderFirst(int id, int limit);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insertWeight(Weights weights);

   // @Update
    //public void updateWeight(Weights weights);

    @Query("DELETE FROM " + DbConfig.WEIGHTS_TABLE+ " WHERE Wid = :weightId")
    public abstract void deleteByWeightId(int weightId);


}